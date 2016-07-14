package me.vadik.instaclimb.view;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import me.vadik.instaclimb.databinding.RouteActivityBinding;
import me.vadik.instaclimb.login.LoginManager;
import me.vadik.instaclimb.model.Route;
import me.vadik.instaclimb.model.RouteStatus;
import me.vadik.instaclimb.model.User;
import me.vadik.instaclimb.model.contract.UsersRoutesViewContract;
import me.vadik.instaclimb.provider.ProviderHelper;
import me.vadik.instaclimb.provider.RouteChecker;
import me.vadik.instaclimb.provider.RoutesContentProvider;
import me.vadik.instaclimb.view.adapter.RouteRecyclerViewAdapter;
import me.vadik.instaclimb.viewmodel.RouteViewModel;

public class RouteActivity extends CommonActivity {

    public static String ARG_ROUTE_ID = "me.vadik.instaclimb.route_id";

    private static final int USERS_LOADER = 0;
    private RouteRecyclerViewAdapter mAdapter;
    private RouteActivityBinding binding;
    private RouteViewModel mRoute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.route_activity);
        setSupportActionBar(binding.incAppBar.toolbar);

        final int routeId = getItemId(ARG_ROUTE_ID);
        Route route = ProviderHelper.getRoute(this, routeId);

        mRoute = new RouteViewModel(this, route);
        binding.setRoute(mRoute);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mAdapter = new RouteRecyclerViewAdapter(this, route);
        setupRecyclerView(mAdapter);

        Bundle b = new Bundle();
        b.putInt(ARG_ROUTE_ID, routeId);
        getSupportLoaderManager().restartLoader(USERS_LOADER, b, this);

        final FloatingActionButton fab = binding.fab;
        fab.hide();

        if (LoginManager.isLoggedIn(this)) {
            new RouteStatusTask(this, routeId) {
                @Override
                protected void onSuccess(RouteStatus status) {
                    if (status == RouteStatus.FLASHED) {
                        fab.setImageResource(R.drawable.ic_done_all_white_24dp);
                    } else if (status == RouteStatus.CLIMBED) {
                        fab.setImageResource(R.drawable.ic_done_white_24dp);
                    }
                    if (status != RouteStatus.NONE) {
                        fab.setBackgroundTintList(getResources().getColorStateList(R.color.colorSuccessAccent));
                    }
                    fab.show();
                }

                @Override
                protected void onError(Exception exception) {
                    // do nothing, fab will not be shown
                }
            }.execute();
        }
    }

    private static abstract class RouteStatusTask extends AsyncTask<Void, Void, RouteStatus> {
        private final Context context;
        private final int routeId;
        private Exception exception;

        public RouteStatusTask(Context context, int routeId) {
            this.context = context;
            this.routeId = routeId;
        }

        @Override
        protected RouteStatus doInBackground(Void... params) {
            try {
                return new RouteChecker(context).getRouteStatus(routeId);
            } catch (Exception e) {
                Log.e("me", "cannot get status for route " + routeId, e);
                this.exception = e;
                return null;
            }
        }

        @Override
        protected void onPostExecute(RouteStatus session) {
            if (exception == null) {
                onSuccess(session);
            } else {
                onError(exception);
            }
        }

        protected abstract void onSuccess(RouteStatus session);

        protected abstract void onError(Exception exception);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        Uri uri;
        String[] projection;
        String select;
        String[] args;
        String order = null;
        String routeId = String.valueOf(bundle.getInt(ARG_ROUTE_ID));
        switch (id) {
            case USERS_LOADER:
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
            case USERS_LOADER:
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
                mAdapter.swap(whoClimbed);
                mRoute.setClimbedCount(whoClimbed.size());
                break;
        }
    }

    private void setupRecyclerView(RecyclerView.Adapter mAdapter) {
        RecyclerView recyclerView = binding.whoClimbedRoutes;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.reset();
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
        setShareIntent(mRoute.getId());
    }

    @Override
    protected String getShareUrl() {
        return "http://instaclimb.ru/route/";
    }
}
