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
import android.view.View;

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
import me.vadik.instaclimb.provider.RouteCheckerHttpImpl;
import me.vadik.instaclimb.provider.RoutesContentProvider;
import me.vadik.instaclimb.view.adapter.RouteRecyclerViewAdapter;
import me.vadik.instaclimb.viewmodel.RouteViewModel;

public class RouteActivity extends CommonActivity {

    public static String ARG_ROUTE_ID = "me.vadik.instaclimb.route_id";

    private static final int USERS_LOADER = 0;
    private RouteRecyclerViewAdapter mAdapter;
    private RouteActivityBinding binding;
    private RouteViewModel mRoute;
    private FloatingActionButton fab;
    private RouteStatus routeStatus;

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

        fab = binding.fab;
        fab.hide();

        if (LoginManager.isLoggedIn(this)) {
            updateFab(routeId);
        }
    }

    private void updateFab(int routeId) {
        new RouteStatusGetTask(this, routeId) {
            @Override
            protected void onSuccess(RouteStatus status) {
                int drawableResId = R.drawable.ic_add_white_24dp;
                if (status == RouteStatus.CLIMBED) {
                    drawableResId = R.drawable.ic_done_white_24dp;
                } else if (status == RouteStatus.FLASHED) {
                    drawableResId = R.drawable.ic_done_all_white_24dp;
                }
                final int bgColor;
                if (status == RouteStatus.NONE) {
                    bgColor = R.color.colorAccent;
                } else {
                    bgColor = R.color.colorSuccessAccent;
                }
                RouteActivity.this.routeStatus = status;
                fab.setBackgroundTintList(getResources().getColorStateList(bgColor));
                fab.setImageResource(drawableResId);
                fab.show();
            }

            @Override
            protected void onError(Exception exception) {
                // do nothing, fab will not be shown
            }
        }.execute();
    }

    public void onClickFab(View view) {
        final int routeId = mRoute.getId();
        new RouteStatusSetTask(this, routeId, RouteStatus.negative(routeStatus)) {
            @Override
            protected void onPostExecute(Void aVoid) {
                updateFab(routeId);
            }
        }.execute();
    }

    private static abstract class RouteStatusGetTask extends AsyncTask<Void, Void, RouteStatus> {
        private final Context context;
        private final int routeId;
        private Exception exception;

        public RouteStatusGetTask(Context context, int routeId) {
            this.context = context;
            this.routeId = routeId;
        }

        @Override
        protected RouteStatus doInBackground(Void... params) {
            try {
                RouteChecker routeChecker = new RouteCheckerHttpImpl(context);
                return routeChecker.getRouteStatus(routeId);
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

    private static class RouteStatusSetTask extends AsyncTask<Void, Void, Void> {
        private final Context context;
        private final int routeId;
        private final RouteStatus status;

        public RouteStatusSetTask(Context context, int routeId, RouteStatus status) {
            this.context = context;
            this.routeId = routeId;
            this.status = status;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                RouteChecker routeChecker = new RouteCheckerHttpImpl(context);
                routeChecker.setRouteStatus(routeId, status);
            } catch (Exception e) {
                Log.e("me", "cannot set status " + status + " for route " + routeId, e);
            }
            return null;
        }
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
