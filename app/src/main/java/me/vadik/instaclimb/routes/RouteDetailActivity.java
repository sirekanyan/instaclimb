package me.vadik.instaclimb.routes;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import me.vadik.instaclimb.R;
import me.vadik.instaclimb.routes.dummy.DummyContent;

/**
 * An activity representing a single Route detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link SectorActivity}.
 */
public class RouteDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Трололо", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            String argItemId = getIntent().getStringExtra(RouteDetailFragment.ARG_ITEM_ID);
            arguments.putString(RouteDetailFragment.ARG_ITEM_ID, argItemId);
            RouteDetailFragment fragment = new RouteDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.route_detail_container, fragment)
                    .commit();

            NetworkImageView mNetworkImageToolbarView = (NetworkImageView) this.findViewById(R.id.route_image_toolbar);
            ImageLoader mImageLoader = VolleySingleton.getInstance(this).getImageLoader();

            DummyContent.DummyItem mItem = DummyContent.ITEM_MAP.get(argItemId);

            if (mNetworkImageToolbarView != null) {
                if (mItem == null) {
                    Log.e("me", "mItem is null, wtf? argItemId: " + argItemId);
                } else {
                    mNetworkImageToolbarView.setImageUrl(mItem.getSmallPictureUrl(), mImageLoader);
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this, new Intent(this, SectorActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showRouteFullscreen(View view) {
        TextView pictureId = (TextView) this.findViewById(R.id.picture_id);
        Intent showRouteIntent = new Intent(this, RouteImageActivity.class);
        if (pictureId != null) {
            String pictureIdString = pictureId.getText().toString();
            if (!pictureIdString.isEmpty()) {
                String uriString = DummyContent.DummyItem.getSmallPictureUrl(pictureIdString);
                showRouteIntent.setData(Uri.parse(uriString));
            }
        }
        startActivity(showRouteIntent);
    }
}
