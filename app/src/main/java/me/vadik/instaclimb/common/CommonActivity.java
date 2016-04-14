package me.vadik.instaclimb.common;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import me.vadik.instaclimb.routes.VolleySingleton;

/**
 * User: vadik
 * Date: 4/13/16
 */
public abstract class CommonActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    protected ShareActionProvider shareActionProvider;

    protected int getItemId(String propertyName) {
        int objectId = getIntent().getIntExtra(propertyName, 0);
        if (objectId == 0) {
            objectId = getIdFromUriIfPresent();
        }
        return objectId;
    }

    protected String getItemName(String propertyName) {
        return getIntent().getStringExtra(propertyName);
    }

    protected void setupToolbarImage(String url, int imageViewResId) {
        NetworkImageView mNetworkImageToolbarView = (NetworkImageView) this.findViewById(imageViewResId);
        ImageLoader mImageLoader = VolleySingleton.getInstance(this).getImageLoader();
        if (mNetworkImageToolbarView != null) {
            mNetworkImageToolbarView.setImageUrl(url, mImageLoader);
        }
    }

    protected int getIdFromUriIfPresent() {
        int id = 0;
        Uri uri = getIntent().getData();
        if (uri != null) {
            List<String> segments = uri.getPathSegments();
            String lastSegment = segments.get(segments.size() - 1);
            try {
                id = Integer.valueOf(lastSegment);
            } catch (NumberFormatException e) {
                Log.e("me", "Cannot parse id from " + uri.toString());
            }
        }
        return id;
    }

    protected void setShareIntent(int objectId) {
        if (objectId != 0) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = getShareUrl() + String.valueOf(objectId);
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            if (shareActionProvider != null) {
                shareActionProvider.setShareIntent(sharingIntent);
            }
        }
    }

    protected abstract String getShareUrl();
}
