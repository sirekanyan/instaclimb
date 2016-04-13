package me.vadik.instaclimb.common;

import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import me.vadik.instaclimb.routes.VolleySingleton;

/**
 * User: vadik
 * Date: 4/13/16
 */
public abstract class CommonActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    protected void setupToolbarImage(String url, int imageViewResId) {
        NetworkImageView mNetworkImageToolbarView = (NetworkImageView) this.findViewById(imageViewResId);
        ImageLoader mImageLoader = VolleySingleton.getInstance(this).getImageLoader();
        if (mNetworkImageToolbarView != null) {
            mNetworkImageToolbarView.setImageUrl(url, mImageLoader);
        }
    }
}
