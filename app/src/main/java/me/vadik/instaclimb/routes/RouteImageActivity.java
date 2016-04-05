package me.vadik.instaclimb.routes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

public class RouteImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_image);

        String pictureId = getIntent().getDataString();

        if (pictureId != null) {
            NetworkImageView mNetworkImageView = (NetworkImageView) findViewById(R.id.fullscreen_image);
            ImageLoader mImageLoader = VolleySingleton.getInstance(this).getImageLoader();

            if (mNetworkImageView != null) {
                mNetworkImageView.setImageUrl(pictureId, mImageLoader);
            }
        }
    }
}
