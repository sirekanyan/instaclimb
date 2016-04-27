package me.vadik.instaclimb.android;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import me.vadik.instaclimb.R;

/**
 * User: vadik
 * Date: 4/13/16
 */
public class TagView extends FrameLayout {
    public TagView(Context context) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.tag_view, this);
    }
}
