package me.vadik.instaclimb.routes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import me.vadik.instaclimb.R;
import me.vadik.instaclimb.common.CommonActivity;
import me.vadik.instaclimb.contract.RouteContract;
import me.vadik.instaclimb.model.RouteDetail;
import me.vadik.instaclimb.model.User;
import me.vadik.instaclimb.provider.RouteProvider;
import me.vadik.instaclimb.provider.RoutesContentProvider;
import me.vadik.instaclimb.provider.RoutesProvider;
import me.vadik.instaclimb.provider.UserProvider;
import me.vadik.instaclimb.settings.SettingsActivity;

public class RouteActivity extends CommonActivity {

    public static String ARG_ROUTE_ID = "me.vadik.instaclimb.route_id";
    public static String ARG_ROUTE_NAME = "me.vadik.instaclimb.route_name";

    private static final int LOADER_ID = 0;
    private static final int LOADER_WHO_CLIMBED = 1;
    private int routeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {

                    boolean checked = fab.getBackgroundTintList().getDefaultColor() != getResources().getColor(R.color.colorAccent);

                    if (checked) {
                        fab.setImageResource(R.drawable.ic_add_white_24dp);
                        fab.setBackgroundTintList(getResources().getColorStateList(R.color.colorAccent));
                    } else {
                        fab.setImageResource(R.drawable.ic_done_white_24dp);
                        fab.setBackgroundTintList(getResources().getColorStateList(R.color.colorSuccessAccent));
                        Snackbar.make(view, R.string.route_completed, Snackbar.LENGTH_LONG)
                                .setAction(R.string.flash_button, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        fab.setImageResource(R.drawable.ic_done_all_white_24dp);
                                        fab.setBackgroundTintList(getResources().getColorStateList(R.color.colorSuccessAccent));
                                        Snackbar.make(view, R.string.route_completed_flash, Snackbar.LENGTH_LONG).show();
                                    }
                                }).show();
                    }

                }
            });
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        routeId = getItemId(ARG_ROUTE_ID);
        String routeName = getItemName(ARG_ROUTE_NAME);

        if (routeName == null && routeId != 0) {
            routeName = RouteProvider.getRouteName(this, String.valueOf(routeId));
        }

        if (routeName != null && getSupportActionBar() != null) {
            getSupportActionBar().setTitle(routeName);
        }

        Bundle b = new Bundle();
        b.putInt(ARG_ROUTE_ID, routeId);
        getSupportLoaderManager().initLoader(LOADER_ID, b, this);
        getSupportLoaderManager().initLoader(LOADER_WHO_CLIMBED, b, this);

        setupRecyclerView(new ArrayList<User>(), R.id.who_climbed_routes);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        Uri uri;
        String[] projection = null;
        String select;
        String[] args;
        String order = null;
        String routeId = String.valueOf(bundle.getInt(ARG_ROUTE_ID));
        switch (id) {
            case LOADER_ID:
                uri = Uri.withAppendedPath(RoutesContentProvider.CONTENT_URI, "routes_view");
                select = "_id = ?";
                args = new String[]{routeId};
                break;
            case LOADER_WHO_CLIMBED:
                uri = Uri.withAppendedPath(RoutesContentProvider.CONTENT_URI, "users_routes_view");
                select = "route_id = ?";
                args = new String[]{routeId};
                break;
            default:
                return null;
        }
        return new CursorLoader(this, uri, projection, select, args, order);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        switch (loader.getId()) {
            case LOADER_ID:
                int done = 0;
                try {
                    if (cursor != null && !cursor.isClosed() && cursor.moveToFirst()) { //TODO remove isClosed check
                        Integer id = cursor.getInt(cursor.getColumnIndex(RouteContract._ID));
                        String name = cursor.getString(cursor.getColumnIndex(RouteContract.NAME));
                        done = cursor.getInt(cursor.getColumnIndex(RouteContract.DONE));
                        RouteDetail mItem = new RouteDetail(id.toString(), name, cursor);
                        setupToolbarImage(mItem.getSmallPictureUrl(), R.id.route_image_toolbar);
                    }
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
                FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                if (fab != null && done > 0) {
                    if (done == 1) {
                        fab.setImageResource(R.drawable.ic_done_white_24dp);
                    } else if (done == 2) {
                        fab.setImageResource(R.drawable.ic_done_all_white_24dp);
                    }
                    fab.setBackgroundTintList(getResources().getColorStateList(R.color.colorSuccessAccent));
                }
                break;
            case LOADER_WHO_CLIMBED:
                List<User> whoClimbed = new ArrayList<>();
                try {
                    if (cursor != null && !cursor.isClosed() && cursor.moveToFirst()) { //TODO remove isClosed check
                        do {
                            whoClimbed.add(new User(cursor));
                        } while (cursor.moveToNext());
                    }
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
                setupRecyclerView(whoClimbed, R.id.who_climbed_routes);
                break;
        }
    }

    private void setupRecyclerView(List<User> users, int recyclerViewResId) {
        RecyclerView mRecyclerView = (RecyclerView) findViewById(recyclerViewResId);
        if (mRecyclerView != null) {
            mRecyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLayoutManager);
            RecyclerView.Adapter mAdapter = new RouteUsersAdapter(users);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
//        Bundle b = new Bundle();
//        b.putInt(ARG_ROUTE_ID, routeId);
//        getSupportLoaderManager().restartLoader(LOADER_ID, b, this);
//        getSupportLoaderManager().restartLoader(LOADER_WHO_CLIMBED, b, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_route, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share_route);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        setShareIntent(routeId);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setShareIntent(routeId);
    }

    @Override
    protected String getShareUrl() {
        return "http://instaclimb.ru/route/";
    }
}
