package me.vadik.instaclimb.routes;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import me.vadik.instaclimb.R;
import me.vadik.instaclimb.common.CommonActivity;
import me.vadik.instaclimb.contract.RouteContract;
import me.vadik.instaclimb.contract.ViewRouteContract;
import me.vadik.instaclimb.model.Route;
import me.vadik.instaclimb.model.RouteDetail;
import me.vadik.instaclimb.model.User;
import me.vadik.instaclimb.provider.RouteProvider;
import me.vadik.instaclimb.provider.RoutesContentProvider;
import me.vadik.instaclimb.settings.SettingsActivity;

public class RouteActivity extends CommonActivity {

    public static String ARG_ROUTE_ID = "me.vadik.instaclimb.route_id";
    public static String ARG_ROUTE_NAME = "me.vadik.instaclimb.route_name";

    private static final int LOADER_ID = 0;
    private static final int LOADER_WHO_CLIMBED = 1;
    private int routeId;
    private RouteUsersAdapter mAdapter;

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

                    boolean checked //TODO refactoring
                            = fab.getBackgroundTintList().getDefaultColor() != getResources().getColor(R.color.colorAccent)
                            && fab.getBackgroundTintList().getDefaultColor() != getResources().getColor(R.color.dColorAccent);

                    Log.e("me", "fab checked: " + checked);
                    Log.e("me", "fab .getDefaultColor(): " + Integer.toHexString(fab.getBackgroundTintList().getDefaultColor()));

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

        mAdapter = setupRecyclerView();

        Bundle b = new Bundle();
        b.putInt(ARG_ROUTE_ID, routeId);
        getSupportLoaderManager().initLoader(LOADER_ID, b, this);
        getSupportLoaderManager().initLoader(LOADER_WHO_CLIMBED, b, this);
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
                Route route = null;
                int done = 0;
                try {
                    if (cursor != null && !cursor.isClosed() && cursor.moveToFirst()) { //TODO remove isClosed check
                        Integer id = cursor.getInt(cursor.getColumnIndex(ViewRouteContract._ID));
                        String name = cursor.getString(cursor.getColumnIndex(ViewRouteContract.NAME));
                        String date = cursor.getString(cursor.getColumnIndex(ViewRouteContract.CREATED_WHEN));
                        int c1 = cursor.getInt(cursor.getColumnIndex(ViewRouteContract.COLOR1));
                        int c2 = cursor.getInt(cursor.getColumnIndex(ViewRouteContract.COLOR2));
                        int c3 = cursor.getInt(cursor.getColumnIndex(ViewRouteContract.COLOR3));
                        String grade = cursor.getString(cursor.getColumnIndex(ViewRouteContract.GRADE));
                        done = cursor.getInt(cursor.getColumnIndex(ViewRouteContract.DONE));
                        int userId = cursor.getInt(cursor.getColumnIndex(ViewRouteContract.USER_ID));
                        String userName = cursor.getString(cursor.getColumnIndex(ViewRouteContract.USER_NAME));
                        RouteDetail mItem = new RouteDetail(id.toString(), name, cursor);
                        setupToolbarImage(mItem.getSmallPictureUrl(), R.id.route_image_toolbar);
                        route = new Route(id, name, date, c1, c2, c3, grade, done);
                        route.setAuthor(new User(userId, userName));
                    }
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
                FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                Log.e("me", "fab: " + fab);
                Log.e("me", "done: " + done);
                if (fab != null && done > 0) {
                    if (done == 1) {
                        fab.setImageResource(R.drawable.ic_done_white_24dp);
                    } else if (done == 2) {
                        fab.setImageResource(R.drawable.ic_done_all_white_24dp);
                    }
                    fab.setBackgroundTintList(getResources().getColorStateList(R.color.colorSuccessAccent));
                }
                if (route != null) {
                    mAdapter.setRoute(route);
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
                mAdapter.addAll(whoClimbed);
                break;
        }
    }

    private RouteUsersAdapter setupRecyclerView() {
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.who_climbed_routes);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        RouteUsersAdapter mAdapter = new RouteUsersAdapter();
        mRecyclerView.setAdapter(mAdapter);
        return mAdapter;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // todo ?
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
        Bundle b = new Bundle();
        b.putInt(ARG_ROUTE_ID, routeId);
        getSupportLoaderManager().restartLoader(LOADER_ID, b, this);
        getSupportLoaderManager().restartLoader(LOADER_WHO_CLIMBED, b, this);
        setShareIntent(routeId);
    }

    @Override
    protected String getShareUrl() {
        return "http://instaclimb.ru/route/";
    }
}
