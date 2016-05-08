package me.vadik.instaclimb.view.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import me.vadik.instaclimb.R;
import me.vadik.instaclimb.model.Marker;

/**
 * User: vadik
 * Date: 4/13/16
 */
public class MarkerView extends LinearLayout {
    public MarkerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.marker_view, this);
    }

    public void setMarkerColor(View view, int dbColor) {
        setMarkerColor(view, dbColor, 0);
    }

    public void setMarkerColor(View view, int dbColor, int defaultResId) {
        TypedArray colors = view.getContext().getResources().obtainTypedArray(R.array.colors);
        if (dbColor == 0) {
            if (defaultResId == 0) {
                view.setVisibility(View.GONE);
            } else {
                view.setBackgroundResource(defaultResId);
                view.setVisibility(View.VISIBLE);
            }
        } else {
            view.setBackgroundResource(colors.getResourceId(dbColor, 0));
            view.setVisibility(View.VISIBLE);
        }
        colors.recycle();
    }

    public void setMarker(Marker marker) {
        if (marker == null) {
            Log.e("me","marker is null, why?");
        } else {
            setMarkerColor(findViewById(R.id.marker1), marker.getColor1(), R.drawable.rect_dashed);
            setMarkerColor(findViewById(R.id.marker2), marker.getColor2());
            setMarkerColor(findViewById(R.id.marker3), marker.getColor3());
        }
    }
}
