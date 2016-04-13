package me.vadik.instaclimb.users;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import me.vadik.instaclimb.R;
import me.vadik.instaclimb.common.CommonActivity;
import me.vadik.instaclimb.contract.RouteContract;
import me.vadik.instaclimb.contract.ViewRoutesUsersContract;
import me.vadik.instaclimb.contract.ViewUsersRoutesContract;
import me.vadik.instaclimb.provider.RoutesContentProvider;
import me.vadik.instaclimb.model.Route;
import me.vadik.instaclimb.routes.RouteHelper;

public class UserActivity extends CommonActivity {

    public static final String ARG_USER_ID = "me.vadik.instaclimb.user_id";

    private static final int LOADER_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Ты думаешь, что это действительно должно работать? Сорян.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }

        String userId = getIntent().getStringExtra(ARG_USER_ID);
        if (userId != null) {
            setupToolbarImage("https://vadik.me/userpic/" + userId + ".jpg", R.id.user_image_toolbar);
        }

        Bundle b = new Bundle();
        b.putString(ARG_USER_ID, userId);
        getSupportLoaderManager().initLoader(LOADER_ID, b, this);
    }

    private void setupRecyclerView(List<Route> myDataset) {
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.user_climbed_routes);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        RecyclerView.Adapter mAdapter = new UserRoutesAdapter(myDataset);

        mRecyclerView.setAdapter(mAdapter);
    }

    static final String[] ROUTES_USERS_PROJECTION = new String[]{
            ViewRoutesUsersContract.ROUTE_ID + " as _id",
            ViewRoutesUsersContract.USER_ID,
            ViewRoutesUsersContract.ROUTE_ID,
            ViewRoutesUsersContract.ROUTE_NAME,
            ViewRoutesUsersContract.DATE,
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
        String[] args = new String[]{bundle.getString(ARG_USER_ID)};
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
                    if (cursor != null && cursor.moveToFirst()) {
                        do {
                            Integer routeId = cursor.getInt(cursor.getColumnIndex(RouteContract._ID));
                            String routeDate = cursor.getString(cursor.getColumnIndex(ViewUsersRoutesContract.DATE));
                            String routeName = RouteHelper.getName(getResources(),
                                    cursor.getString(cursor.getColumnIndex(ViewRoutesUsersContract.ROUTE_NAME)));
                            climbedRoutes.add(new Route(routeId, routeName, routeDate));
                        } while (cursor.moveToNext());
                    }
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
                setupRecyclerView(climbedRoutes);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //TODO: do need i here something to write?
    }
}
