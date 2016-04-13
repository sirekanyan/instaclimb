package me.vadik.instaclimb.users;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import me.vadik.instaclimb.R;
import me.vadik.instaclimb.routes.VolleySingleton;
import me.vadik.instaclimb.contract.Routes;
import me.vadik.instaclimb.contract.RoutesUsersView;
import me.vadik.instaclimb.contract.UsersRoutesView;
import me.vadik.instaclimb.provider.RoutesContentProvider;

public class UserActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Ты думаешь, что это действительно должно работать? Сорян.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        String userId = getIntent().getStringExtra("user_id");

        setupToolbarImage("https://vadik.me/userpic/" + userId + ".jpg");

        Bundle b = new Bundle();
        b.putString("user_id", userId);

        getSupportLoaderManager().initLoader(123, b, this);
        getSupportLoaderManager().initLoader(124, b, this);
    }

    private void setupToolbarImage(String url) {
        NetworkImageView mNetworkImageToolbarView = (NetworkImageView) this.findViewById(R.id.user_image_toolbar);
        ImageLoader mImageLoader = VolleySingleton.getInstance(this).getImageLoader();
        if (mNetworkImageToolbarView != null) {
            mNetworkImageToolbarView.setImageUrl(url, mImageLoader);
        }
    }

    static final String[] USER_ROUTES_PROJECTION = new String[]{
            UsersRoutesView.ROUTE_ID + " as _id",
            UsersRoutesView.USER_ID,
            UsersRoutesView.ROUTE_ID,
            RoutesUsersView.ROUTE_NAME,
            UsersRoutesView.DATE,
    };

    static final String[] ROUTES_PROJECTION = new String[]{
            Routes._ID,
            Routes.NAME,
            Routes.USER_ID,
            Routes.CREATED_WHEN,
    };

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri baseUri;
        String select = "user_id = ?";
        String[] selectArgs = new String[]{args.getString("user_id")};
        String order;
        String[] projection;
        switch (id) {
            case 123:
                baseUri = Uri.withAppendedPath(RoutesContentProvider.CONTENT_URI, "routes_users_view");
                order = "date desc";
                projection = USER_ROUTES_PROJECTION;
                break;
            case 124:
                baseUri = Uri.withAppendedPath(RoutesContentProvider.CONTENT_URI, "routes");
                order = "created_when desc";
                projection = ROUTES_PROJECTION;
                break;
            default:
                return null;
        }
        return new CursorLoader(this, baseUri,
                projection /*TODO: replace on projection*/, select, selectArgs, order);
    }

    @Override
    public void onLoadFinished(Loader loader, Cursor cursor) {

        switch (loader.getId()) {
            case 123:
                LinearLayout climbedRoutes = (LinearLayout) this.findViewById(R.id.user_climbed_routes);
                try {
                    if (cursor != null && cursor.moveToFirst()) {
                        do {
                            String id = cursor.getString(cursor.getColumnIndex(RoutesUsersView.ROUTE_NAME));
                            TextView textView = new TextView(this);
                            textView.setText(String.valueOf(id));
                            climbedRoutes.addView(textView);
                        } while (cursor.moveToNext());
                    }
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
                break;
            case 124:
                LinearLayout createdRoutes = (LinearLayout) this.findViewById(R.id.user_created_routes);
                try {
                    if (cursor != null && cursor.moveToFirst()) {
                        do {
                            String id = cursor.getString(cursor.getColumnIndex(Routes.NAME));
                            TextView textView = new TextView(this);
                            textView.setText(String.valueOf(id));
                            createdRoutes.addView(textView);
                        } while (cursor.moveToNext());
                    }
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
                break;
            default:
                break;
        }


    }

    @Override
    public void onLoaderReset(Loader loader) {
        //TODO: do need i here something to write?
    }
}
