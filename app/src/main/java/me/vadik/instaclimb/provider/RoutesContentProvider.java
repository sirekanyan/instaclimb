package me.vadik.instaclimb.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class RoutesContentProvider extends ContentProvider {

    // SQLiteOpenHelper dbHelper; //TODO: uncomment
    private SQLiteDatabase sqLiteDatabase;
    public static final String AUTHORITY = "me.vadik.instaclimb.routes.provider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    private static final String DATABASE_NAME = "instaclimb.db";

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int TABLE_MATCH = 0;
    private static final int ROW_MATCH = 1;

    static {
        sUriMatcher.addURI(AUTHORITY, "*", TABLE_MATCH);
        sUriMatcher.addURI(AUTHORITY, "*/#", ROW_MATCH);
    }

    @Override
    public boolean onCreate() {
        // dbHelper = new RoutesDatabaseHelper(getContext()); //TODO: uncomment
        copyDatabaseFromAssetsToData(getContext());
        sqLiteDatabase = SQLiteDatabase.openDatabase(databasePath(), null, 0);
        return true;
    }

    //TODO: remove this method
    private String databasePath() {
        if (getContext() == null) {
            Log.e("me", "context is null, cannot get database path");
            return null;
        }
        return getContext().getFilesDir() + "/" + DATABASE_NAME;
    }

    //TODO: remove this method
    private void copyDatabaseFromAssetsToData(Context context) {
        Log.i("me", "Copying database from assets to " + databasePath());
        InputStream is = null;
        OutputStream os = null;
        try {
            is = context.getAssets().open(DATABASE_NAME);
            os = new FileOutputStream(databasePath());
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } catch (IOException e) {
            Log.e("me", "Cannot copy database from assets to data", e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    //ignored
                }
            }
            if (os != null) {
                try {
                    os.flush();
                    os.close();
                } catch (IOException e) {
                    //ignored
                }
            }
        }
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String select, String[] args, String order) {
        // SQLiteDatabase db = dbHelper.getReadableDatabase(); //TODO: uncomment
        switch (sUriMatcher.match(uri)) {
            case TABLE_MATCH:
                break;
            case ROW_MATCH:
                select = "_ID = ?";
                args = new String[]{uri.getLastPathSegment()};
                order = null;
                break;
            default:
                return null;
        }
        return sqLiteDatabase.query(getTableName(uri), projection, select, args, null, null, order);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        sqLiteDatabase.insert(getTableName(uri), null, values);
        return null; //TODO return uri
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        return sqLiteDatabase.delete(getTableName(uri), selection, selectionArgs);
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return sqLiteDatabase.update(getTableName(uri), values, selection, selectionArgs);
    }

    public static String getTableName(Uri uri) {
        return uri.getPathSegments().get(0);
    }
}