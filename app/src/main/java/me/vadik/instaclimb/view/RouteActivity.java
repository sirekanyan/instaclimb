package me.vadik.instaclimb.view;

import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import me.vadik.instaclimb.R;
import me.vadik.instaclimb.databinding.RouteActivityBinding;
import me.vadik.instaclimb.model.Route;
import me.vadik.instaclimb.model.User;
import me.vadik.instaclimb.model.contract.UsersRoutesViewContract;
import me.vadik.instaclimb.provider.RoutesContentProvider;
import me.vadik.instaclimb.view.adapter.RouteUsersAdapter;
import me.vadik.instaclimb.viewmodel.RouteViewModel;

public class RouteActivity extends CommonActivity {

    public static String ARG_ROUTE_ID = "me.vadik.instaclimb.route_id";
    public static String ARG_ROUTE_NAME = "me.vadik.instaclimb.route_name";

    private static final int LOADER_ID = 0;
    private static final int LOADER_WHO_CLIMBED = 1;
    private RouteUsersAdapter mAdapter;
    private RouteActivityBinding binding;
    private RouteViewModel mRoute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.route_activity);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar)); //todo avoid findbyid

        int routeId = getItemId(ARG_ROUTE_ID);
        String routeName = getItemName(ARG_ROUTE_NAME);
        Route route = new Route.Builder(routeId, routeName).build();
        mRoute = new RouteViewModel(this, route);
        binding.setRoute(mRoute);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


//        if (routeName == null && routeId != 0) {
//            routeName = RouteProvider.getRouteName(this, String.valueOf(routeId));
//        }

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
                projection = new String[]{
                        UsersRoutesViewContract.USER_ID + " as _id",
                        UsersRoutesViewContract.USER_NAME + " as name",
                        UsersRoutesViewContract.HAS_PICTURE,
                        UsersRoutesViewContract.DATE,
                        UsersRoutesViewContract.DONE,
                };
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
                        route = new Route.Builder(cursor).build();
                        RouteViewModel routeView = new RouteViewModel(this, route); // TODO should this be here?
                        binding.setRoute(routeView); // TODO should this be here???
                        setupToolbarImage(routeView.getSmallPictureUrl(), R.id.image_toolbar);
                    }
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
                FloatingActionButton fab = binding.fab;
                if (fab != null && done > 0) {
                    if (done == 1) {
                        fab.setImageResource(R.drawable.ic_done_white_24dp);
                    } else if (done == 2) {
                        fab.setImageResource(R.drawable.ic_done_all_white_24dp);
                    }
                    fab.setBackgroundTintList(getResources().getColorStateList(R.color.colorSuccessAccent));
                }
                if (route != null) {
//                    mAdapter.setObject(route); //TODO
                }
                break;
            case LOADER_WHO_CLIMBED:
                List<User> whoClimbed = new ArrayList<>();
                try {
                    if (cursor != null && !cursor.isClosed() && cursor.moveToFirst()) { //TODO remove isClosed check
                        do {
                            whoClimbed.add(new User.Builder(cursor).build());
                        } while (cursor.moveToNext());
                    }
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
                mAdapter.setItems(whoClimbed);
                mRoute.setClimbedCount(whoClimbed.size());
                break;
        }
    }

    private RouteUsersAdapter setupRecyclerView() {
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.who_climbed_routes);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        RouteUsersAdapter mAdapter = new RouteUsersAdapter(this);
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
        setShareIntent(mRoute.getId());
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
        b.putInt(ARG_ROUTE_ID, mRoute.getId());
        getSupportLoaderManager().restartLoader(LOADER_ID, b, this);
        getSupportLoaderManager().restartLoader(LOADER_WHO_CLIMBED, b, this);
        setShareIntent(mRoute.getId());
    }

    @Override
    protected String getShareUrl() {
        return "http://instaclimb.ru/route/";
    }
}
