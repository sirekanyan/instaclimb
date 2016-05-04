package me.vadik.instaclimb.model;

import android.content.res.TypedArray;
import android.databinding.BindingAdapter;
import android.databinding.BindingMethod;
import android.databinding.BindingMethods;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;

import me.vadik.instaclimb.R;
import me.vadik.instaclimb.android.MarkerView;
import me.vadik.instaclimb.routes.VolleySingleton;

/**
 * User: vadik
 * Date: 4/13/16
 */
public class Route {
    private final Integer id;
    private final String name;
    private final String date;
    private int color1;
    private int color2;
    private int color3;
    private final Marker marker;
    private String grade;
    private final int done;
    private final int pictureId;
    private User author;
    private int climbedCount;

    public Route(Integer id, String name, String date, int c1, int c2, int c3, String grade, int done, int pictureId) {
        this.id = id;
        this.name = name;
        this.date = date;
        color1 = c1;
        color2 = c2;
        color3 = c3;
        this.marker = new Marker(c1, c2, c3);
        this.grade = grade;
        this.done = done;
        this.pictureId = pictureId;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public Integer getId() {
        return id;
    }

    public int getColor1() {
        return color1;
    }

    public int getColor2() {
        return color2;
    }

    public int getColor3() {
        return color3;
    }

    public String getGrade() {
        return grade;
    }

    public boolean isDone() {
        return done > 0;
    }

    public boolean isFlash() {
        return done == 2;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public User getAuthor() {
        return author;
    }

    public String getThumbnailUrl() {
        return "http://instaclimb.ru/maps/" + pictureId + "/thumb.jpg";
    }

    public String getSmallPictureUrl() {
        return "http://instaclimb.ru/maps/" + pictureId + "/small.jpg";
    }

    public String getPictureUrl() {
        return "http://instaclimb.ru/maps/" + pictureId + "/full.jpg";
    }

    public void setClimbedCount(int climbedCount) {
        this.climbedCount = climbedCount;
    }

    public int getClimbedCount() {
        return climbedCount;
    }

    @BindingAdapter({"bind:markerColor", "bind:defaultResource"})
    public static void setMarkerColor(View view, int dbColor, Drawable defaultDrawable) {
        TypedArray colors = view.getContext().getResources().obtainTypedArray(R.array.colors);
        if (dbColor == 0) {
            view.setBackgroundDrawable(defaultDrawable);
            view.setVisibility(View.VISIBLE);
        } else {
            view.setBackgroundResource(colors.getResourceId(dbColor, 0));
            view.setVisibility(View.VISIBLE);
        }
        colors.recycle();
    }

    @BindingAdapter("bind:markerColor")
    public static void setMarkerColor(View view, int dbColor) {
        TypedArray colors = view.getContext().getResources().obtainTypedArray(R.array.colors);
        if (dbColor == 0) {
            view.setVisibility(View.GONE);
        } else {
            view.setBackgroundResource(colors.getResourceId(dbColor, 0));
            view.setVisibility(View.VISIBLE);
        }
        colors.recycle();
    }

    @BindingAdapter("bind:imageUrl")
    public static void loadImage(NetworkImageView view, String url) {
        ImageLoader mImageLoader = VolleySingleton.getInstance(view.getContext()).getImageLoader();
        view.setDefaultImageResId(R.drawable.blackface);
        view.setImageUrl(url, mImageLoader);
    }

    public Marker getMarker() {
        return marker;
    }
}
