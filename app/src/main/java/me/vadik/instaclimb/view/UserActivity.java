package me.vadik.instaclimb.view;

import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import me.vadik.instaclimb.R;
import me.vadik.instaclimb.databinding.UserActivityBinding;
import me.vadik.instaclimb.model.Route;
import me.vadik.instaclimb.model.User;
import me.vadik.instaclimb.model.contract.RoutesUsersViewContract;
import me.vadik.instaclimb.provider.RoutesContentProvider;
import me.vadik.instaclimb.view.adapter.UserRoutesAdapter;
import me.vadik.instaclimb.viewmodel.UserViewModel;

public class UserActivity extends CommonActivity {

    private static final String TAG = UserActivity.class.getName();
    public static final String ARG_USER_ID = "me.vadik.instaclimb.user_id";
    public static final String ARG_USER_NAME = "me.vadik.instaclimb.user_name";

    private static final int USER_LOADER = 0;
    private static final int ROUTES_LOADER = 1;
    private static final String ARG_LIMIT = "me.vadik.instaclimb.limit";
    private static final int ITEMS_PER_PAGE = 5;
    private UserRoutesAdapter mAdapter;
    private UserViewModel mUser;
    private UserActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.user_activity);
        setSupportActionBar(binding.appBar.toolbar);

        int userId = getItemId(ARG_USER_ID);
        String userName = getItemName(ARG_USER_NAME);
        if (userName == null && userId != 0) {
//            userName = UserProvider.getUserName(this, String.valueOf(userId));
        }

        User userModel = new User.Builder(userId, userName).build();
        mUser = new UserViewModel(this, userModel);
        mUser.setImageUrl("https://vadik.me/userpic/" + String.valueOf(userId) + ".jpg");
        binding.setUser(mUser);

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

        if (userName != null && getSupportActionBar() != null) {
            getSupportActionBar().setTitle(userName);
        }

//        if (userId != 0 && UserProvider.hasPicture(this, userId)) {
//            setupToolbarImage("https://vadik.me/userpic/" + String.valueOf(userId) + ".jpg", R.id.image_toolbar);
//        }

        Bundle b = new Bundle();
        b.putInt(ARG_USER_ID, userId);
        b.putInt(ARG_LIMIT, ITEMS_PER_PAGE);

        mAdapter = setupRecyclerView();

        getSupportLoaderManager().restartLoader(USER_LOADER, b, this);
        getSupportLoaderManager().restartLoader(ROUTES_LOADER, b, this);
    }

    private UserRoutesAdapter setupRecyclerView() {
        final UserActivity context = this;
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.user_climbed_routes);
        NestedScrollView nestedScrollView = (NestedScrollView) findViewById(R.id.nested_scroll_view);
        UserRoutesAdapter adapter = null;
        if (mRecyclerView != null && nestedScrollView != null) {
            mRecyclerView.setNestedScrollingEnabled(false);
            mRecyclerView.setHasFixedSize(true);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLayoutManager);
            adapter = new UserRoutesAdapter(this);
            mRecyclerView.setAdapter(adapter);
//            mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager) {
//                @Override
//                public void onLoadMore(int current_page) {
//                    Bundle bundle = new Bundle();
//                    bundle.putInt(ARG_LIMIT, ITEMS_PER_PAGE * current_page);
//                    getSupportLoaderManager().restartLoader(ROUTES_LOADER, bundle, context);
//                }
//            });
//            nestedScrollView.setOnScrollChangeListener(new EndlessOnScrollListener(mLayoutManager, mRecyclerView) {
//                @Override
//                public void onLoadMore(int current_page) {
//                    Bundle bundle = new Bundle();
//                    bundle.putInt(ARG_LIMIT, ITEMS_PER_PAGE * current_page);
//                    getSupportLoaderManager().restartLoader(ROUTES_LOADER, bundle, context);
//                }
//            });
        }
        return adapter;
    }

    static final String[] ROUTES_USERS_PROJECTION = new String[]{
            RoutesUsersViewContract.ROUTE_ID + " as _id",
            RoutesUsersViewContract.USER_ID,
            RoutesUsersViewContract.ROUTE_ID,
            RoutesUsersViewContract.ROUTE_NAME + " as name",
            RoutesUsersViewContract.DATE,
            RoutesUsersViewContract.COLOR1,
            RoutesUsersViewContract.COLOR2,
            RoutesUsersViewContract.COLOR3,
            RoutesUsersViewContract.GRADE,
            RoutesUsersViewContract.DONE,
    };

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        Uri uri;
        String[] projection;
        String select;
        String[] args = new String[]{String.valueOf(bundle.getInt(ARG_USER_ID))};
        String order = null;
        switch (id) {
            case USER_LOADER:
                uri = Uri.withAppendedPath(RoutesContentProvider.CONTENT_URI, "users");
                select = "_id = ?";
                projection = null;
                break;
            case ROUTES_LOADER:
                uri = Uri.withAppendedPath(RoutesContentProvider.CONTENT_URI, "routes_users_view");
                select = "user_id = ?";
                projection = ROUTES_USERS_PROJECTION;
                order = "date desc";
                int limit = bundle.getInt(ARG_LIMIT);
                order += " limit " + String.valueOf(limit);
                break;
            default:
                return null;
        }
        return new CursorLoader(this, uri, projection, select, args, order);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor != null && cursor.isClosed()) {
            Log.e(TAG, "Cursor is already closed");
            return;
        }
        switch (loader.getId()) {
            case USER_LOADER:
                try {
                    if (cursor != null && cursor.moveToFirst()) {
                        mUser = new UserViewModel(this, new User.Builder(cursor).build());
                        mUser.setImageUrl("https://vadik.me/userpic/" + String.valueOf(mUser.getId()) + ".jpg"); // todo remove
                        binding.setUser(mUser); // todo maybe should i avoid this style?
                    }
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
                break;
            case ROUTES_LOADER:
                try {
                    if (cursor != null && cursor.moveToFirst()) {
                        List<Route> climbedRoutes = new ArrayList<>();
                        do {
                            climbedRoutes.add(new Route.Builder(cursor).build());
                        } while (cursor.moveToNext());
                        mAdapter.swap(climbedRoutes);
                    }
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share_user);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        setShareIntent(mUser.getId());
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
        setShareIntent(mUser.getId());
//        Bundle b = new Bundle();
//        b.putInt(ARG_USER_ID, mUser.getId());
//        getSupportLoaderManager().restartLoader(USER_LOADER, b, this);
//        getSupportLoaderManager().restartLoader(ROUTES_LOADER, b, this);
    }

    @Override
    protected String getShareUrl() {
        return "http://instaclimb.ru/climber/";
    }
}
