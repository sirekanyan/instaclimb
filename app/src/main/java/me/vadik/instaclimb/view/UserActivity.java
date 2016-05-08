package me.vadik.instaclimb.view;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import me.vadik.instaclimb.R;
import me.vadik.instaclimb.model.contract.RouteContract;
import me.vadik.instaclimb.model.contract.RoutesUsersViewContract;
import me.vadik.instaclimb.model.Route;
import me.vadik.instaclimb.provider.RoutesContentProvider;
import me.vadik.instaclimb.provider.UserProvider;
import me.vadik.instaclimb.view.adapter.UserRoutesAdapter;

public class UserActivity extends CommonActivity {

    public static String ARG_USER_ID = "me.vadik.instaclimb.user_id";
    public static String ARG_USER_NAME = "me.vadik.instaclimb.user_name";

    private static final int LOADER_ID = 0;
    private int userId;
    private UserRoutesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Нот импелементед йет. Сорян.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }

        userId = getItemId(ARG_USER_ID);
        String userName = getItemName(ARG_USER_NAME);

        if (userName == null && userId != 0) {
            userName = UserProvider.getUserName(this, String.valueOf(userId));
        }

        if (userName != null && getSupportActionBar() != null) {
            getSupportActionBar().setTitle(userName);
        }

        if (userId != 0 && UserProvider.hasPicture(this, userId)) {
            setupToolbarImage("https://vadik.me/userpic/" + String.valueOf(userId) + ".jpg", R.id.image_toolbar);
        }

        Bundle b = new Bundle();
        b.putInt(ARG_USER_ID, userId);

        mAdapter = setupRecyclerView();

        getSupportLoaderManager().initLoader(LOADER_ID, b, this);
    }

    private UserRoutesAdapter setupRecyclerView() {
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.user_climbed_routes);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        UserRoutesAdapter mAdapter = new UserRoutesAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        return mAdapter;
    }

    static final String[] ROUTES_USERS_PROJECTION = new String[]{
            RoutesUsersViewContract.ROUTE_ID + " as _id",
            RoutesUsersViewContract.USER_ID,
            RoutesUsersViewContract.ROUTE_ID,
            RoutesUsersViewContract.ROUTE_NAME,
            RoutesUsersViewContract.DATE,
            RoutesUsersViewContract.COLOR1,
            RoutesUsersViewContract.COLOR2,
            RoutesUsersViewContract.COLOR3,
            RoutesUsersViewContract.GRADE,
            RoutesUsersViewContract.DONE,
    };

    static final String[] ROUTES_PROJECTION = new String[]{
            RouteContract._ID,
            RouteContract.NAME,
            RouteContract.USER_ID,
            RouteContract.CREATED_WHEN + " as date",
    };

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        Uri uri;
        String[] projection;
        String select = "user_id = ?";
        String[] args = new String[]{String.valueOf(bundle.getInt(ARG_USER_ID))};
        String order = "date desc";
        switch (id) {
            case LOADER_ID:
                uri = Uri.withAppendedPath(RoutesContentProvider.CONTENT_URI, "routes_users_view");
                projection = ROUTES_USERS_PROJECTION;
                break;
            case 124:
                uri = Uri.withAppendedPath(RoutesContentProvider.CONTENT_URI, "routes");
                projection = ROUTES_PROJECTION;
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
                List<Route> climbedRoutes = new ArrayList<>();
                try {
                    //TODO: what should i do if cursor is closed?
                    if (cursor != null && !cursor.isClosed() && cursor.moveToFirst()) { //TODO remove isClosed check
                        do {
                            climbedRoutes.add(new Route.Builder(cursor).build());
                        } while (cursor.moveToNext());
                    }
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }

                mAdapter.setItems(climbedRoutes);

                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // todo ?
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share_user);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        setShareIntent(userId);
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
        setShareIntent(userId);
        Bundle b = new Bundle();
        b.putInt(ARG_USER_ID, userId);
        getSupportLoaderManager().restartLoader(LOADER_ID, b, this);
    }

    @Override
    protected String getShareUrl() {
        return "http://instaclimb.ru/climber/";
    }
}