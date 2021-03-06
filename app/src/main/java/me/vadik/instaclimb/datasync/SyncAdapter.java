package me.vadik.instaclimb.datasync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import me.vadik.instaclimb.provider.RoutesContentProvider;
import me.vadik.instaclimb.helper.VolleySingleton;

/**
 * User: vadik
 * Date: 4/25/16
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {
    private final ContentResolver mContentResolver;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
    }

    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        update("users");
        update("routes");
        update("users_routes");
    }

    private int getMaxTs(String uriPath) {
        Uri uri = Uri.withAppendedPath(RoutesContentProvider.CONTENT_URI, uriPath);
        Cursor cursor = mContentResolver.query(
                uri,
                new String[]{"ts"},
                null,
                null,
                "ts desc");
        int maxTs = 0;
        try {
            if (cursor != null && cursor.moveToFirst()) {
                maxTs = cursor.getInt(cursor.getColumnIndex("ts"));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return maxTs;
    }

    private void update(JSONArray response, String uriPath) {
        Uri uri = Uri.withAppendedPath(RoutesContentProvider.CONTENT_URI, uriPath);
        for (int i = 0; i < response.length(); i++) {
            JSONObject jsonObject = null;
            try {
                jsonObject = response.getJSONObject(i);
            } catch (JSONException e) {
                Log.e("me", e.getMessage(), e);
            }
            if (jsonObject != null) {

                ContentValues values = new ContentValues();

                if ("users_routes".equals(uriPath)) {

                    int userId = 0;
                    int routeId = 0;

                    try {
                        userId = putValueInt(jsonObject, values, "user_id");
                        routeId = putValueInt(jsonObject, values, "route_id");
                        putValueInt(jsonObject, values, "done");
                        putValueString(jsonObject, values, "date");
                        putValueInt(jsonObject, values, "ts");
                    } catch (JSONException e) {
                        Log.e("me", e.getMessage(), e);
                    }
                    if (values.size() > 0) {
                        String userIdStr = String.valueOf(userId);
                        String routeIdStr = String.valueOf(routeId);
                        mContentResolver.delete(uri, "user_id = ? and route_id = ?",
                                new String[]{userIdStr, routeIdStr});
                        mContentResolver.insert(uri, values);
                        Log.d("me", "user:" + userIdStr + " route:" + routeIdStr + " (" + uriPath + ")");
                    }

                } else {

                    int id = 0;
                    String name = "";

                    try {
                        id = putValueInt(jsonObject, values, "_id");
                        name = putValueString(jsonObject, values, "name");
                        putValueInt(jsonObject, values, "ts");
                        if ("routes".equals(uriPath)) {
                            putValueString(jsonObject, values, "grade");
                            putValueInt(jsonObject, values, "color1");
                            putValueInt(jsonObject, values, "color2");
                            putValueInt(jsonObject, values, "color3");
                            putValueInt(jsonObject, values, "user_id");
                            putValueString(jsonObject, values, "comment");
                            putValueString(jsonObject, values, "created_when");
                            putValueInt(jsonObject, values, "is_active");
                            putValueInt(jsonObject, values, "picture_id");
                            putValueInt(jsonObject, values, "sector_id");
                        } else if ("users".equals(uriPath)) {
                            putValueInt(jsonObject, values, "has_picture");
                            putValueInt(jsonObject, values, "rating");
                            putValueInt(jsonObject, values, "height");
                            putValueInt(jsonObject, values, "weight");
                            putValueInt(jsonObject, values, "sex");
                            putValueString(jsonObject, values, "flash_bouldering");
                            putValueString(jsonObject, values, "redpoint_bouldering");
                            putValueString(jsonObject, values, "flash_lead");
                            putValueString(jsonObject, values, "redpoint_lead");
                            putValueString(jsonObject, values, "about");
                        }
                    } catch (JSONException e) {
                        Log.e("me", e.getMessage(), e);
                    }
                    if (values.size() > 0) {
                        mContentResolver.delete(uri, "_id = ?", new String[]{String.valueOf(id)});
                        mContentResolver.insert(uri, values);
                        Log.d("me", String.valueOf(id) + " " + name + " (" + uriPath + ")");
                    }

                }
            }
        }

        Log.d("me", String.valueOf(response.length()) + " rows updated");

        mContentResolver.notifyChange(uri, null); // TODO does it really work?
    }

    private String putValueString(JSONObject jsonObject, ContentValues values, String name) throws JSONException {
        String value = "";
        if (!jsonObject.isNull(name)) {
            value = jsonObject.getString(name);
            values.put(name, value);
        }
        return value;
    }

    private int putValueInt(JSONObject jsonObject, ContentValues values, String name) throws JSONException {
        int value = 0;
        if (!jsonObject.isNull(name)) {
            value = jsonObject.getInt(name);
            values.put(name, value);
        }
        return value;
    }

    public void update(final String uriPath) {
        final int maxTs = getMaxTs(uriPath);

        String apiRequest = "https://climb.vadik.me/" + uriPath + "?ts=gt." + maxTs;

        Log.d("me", apiRequest);

        JsonRequest jsonRequest;

        jsonRequest = new JsonArrayRequest
                (Request.Method.GET, apiRequest, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        update(response, uriPath);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String textError;
                        if (error == null) {
                            textError = "empty error";
                        } else if (error.networkResponse == null) {
                            textError = "empty network response";
                        } else {
                            Integer statusCode = error.networkResponse.statusCode;
                            textError = statusCode.toString() + ": " + error.getMessage();
                        }
                        Log.e("me", "ERROR: " + textError);
                    }
                });

        VolleySingleton.getInstance(this.getContext()).addToRequestQueue(jsonRequest);
    }
}
