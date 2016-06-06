package me.vadik.instaclimb.viewmodel;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import me.vadik.instaclimb.BR;
import me.vadik.instaclimb.R;
import me.vadik.instaclimb.helper.RouteHelper;
import me.vadik.instaclimb.helper.VolleySingleton;
import me.vadik.instaclimb.model.Marker;
import me.vadik.instaclimb.model.Route;
import me.vadik.instaclimb.model.User;
import me.vadik.instaclimb.viewmodel.common.CommonViewModel;

/**
 * User: vadik
 * Date: 5/3/16
 */
public class RouteViewModel extends CommonViewModel<Route> {
    private final Route route;
    private final User author;
    private final Resources resources;
    private Marker marker;

    @Bindable
    private int climbedCount;

    public RouteViewModel(Context context, Route route) {
        super(context, route);
        this.resources = context.getResources();
        this.route = route;
        this.author = new User.Builder(route.userId, route.userName).build();
        this.marker = new Marker(route.color1, route.color2, route.color3);
    }

    @Override
    public String getName() {
        return RouteHelper.getName(context, super.getName());
    }

    public String getGrade() {
        return route.grade;
    }

    public String getDate() { //TODO rename according column name
        return route.createdWhen;
    }

    public User getAuthor() {
        return author;
    }

    public Marker getMarker() {
        return marker;
    }

    public boolean isFlash() {
        return route.done == 2;
    }

    public String getThumbPictureUrl() {
        return "http://instaclimb.ru/maps/" + route.pictureId + "/thumb.jpg";
    }

    public String getSmallPictureUrl() {
        return "http://instaclimb.ru/maps/" + route.pictureId + "/small.jpg";
    }

    public String getPictureUrl() {
        return "http://instaclimb.ru/maps/" + route.pictureId + "/full.jpg";
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

    public int getClimbedCount() {
        return climbedCount;
    }

    public void onClickFAB(View view) {
        final FloatingActionButton fab = (FloatingActionButton) view;

        boolean checked = false; //TODO refactoring
        if (fab != null) {
            checked = fab.getBackgroundTintList().getDefaultColor() != resources.getColor(R.color.colorAccent)
                    && fab.getBackgroundTintList().getDefaultColor() != resources.getColor(R.color.dColorAccent);
        }

        if (checked) {
            fab.setImageResource(R.drawable.ic_add_white_24dp);
            fab.setBackgroundTintList(resources.getColorStateList(R.color.colorAccent));
        } else {
            fab.setImageResource(R.drawable.ic_done_white_24dp);
            fab.setBackgroundTintList(resources.getColorStateList(R.color.colorSuccessAccent));
            Snackbar.make(fab, R.string.route_completed, Snackbar.LENGTH_LONG)
                    .setAction(R.string.flash_button, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            fab.setImageResource(R.drawable.ic_done_all_white_24dp);
                            fab.setBackgroundTintList(resources.getColorStateList(R.color.colorSuccessAccent));
                            Snackbar.make(fab, R.string.route_completed_flash, Snackbar.LENGTH_LONG).show();
                        }
                    }).show();
        }
    }

    public void setClimbedCount(int climbedCount) {
        this.climbedCount = climbedCount;
        notifyPropertyChanged(BR.climbedCount);
    }
}
