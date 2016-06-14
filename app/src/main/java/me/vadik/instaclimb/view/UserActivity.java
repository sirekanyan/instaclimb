package me.vadik.instaclimb.view;

import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import me.vadik.instaclimb.R;
import me.vadik.instaclimb.databinding.UserActivityBinding;
import me.vadik.instaclimb.model.Route;
import me.vadik.instaclimb.model.User;
import me.vadik.instaclimb.model.contract.RoutesUsersViewContract;
import me.vadik.instaclimb.provider.ProviderHelper;
import me.vadik.instaclimb.provider.RoutesContentProvider;
import me.vadik.instaclimb.view.adapter.UserRecyclerViewAdapter;
import me.vadik.instaclimb.viewmodel.UserViewModel;

public class UserActivity extends CommonActivity {

    private static final String TAG = UserActivity.class.getName();
    public static final String ARG_USER_ID = "me.vadik.instaclimb.user_id";

    private static final int ROUTES_LOADER = 0;
    private static final String ARG_LIMIT = "me.vadik.instaclimb.limit";
    private static final int ITEMS_PER_PAGE = 5;
    private UserRecyclerViewAdapter mAdapter;
    private UserViewModel mUser;
    private UserActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.user_activity);
        setSupportActionBar(binding.incAppBar.toolbar);

        int userId = getItemId(ARG_USER_ID);
        User user = ProviderHelper.getUser(this, userId);

        mUser = new UserViewModel(this, user);
        binding.setUser(mUser);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Bundle b = new Bundle();
        b.putInt(ARG_USER_ID, userId);
        b.putInt(ARG_LIMIT, ITEMS_PER_PAGE);

        mAdapter = new UserRecyclerViewAdapter(this, user);
        setupRecyclerView(mAdapter);

        getSupportLoaderManager().restartLoader(ROUTES_LOADER, b, this);
    }

    private void setupRecyclerView(RecyclerView.Adapter adapter) {
        RecyclerView mRecyclerView = binding.userClimbedRoutes;
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);
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
            case ROUTES_LOADER:
                uri = Uri.withAppendedPath(RoutesContentProvider.CONTENT_URI, "routes_users_view");
                select = "user_id = ?";
                projection = ROUTES_USERS_PROJECTION;
                order = "date desc";
//                int limit = bundle.getInt(ARG_LIMIT);
//                order += " limit " + String.valueOf(limit);
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
        mAdapter.reset();
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
    }

    @Override
    protected String getShareUrl() {
        return "http://instaclimb.ru/climber/";
    }
}
