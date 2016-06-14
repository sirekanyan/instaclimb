package me.vadik.instaclimb.viewmodel.common;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import me.vadik.instaclimb.BR;
import me.vadik.instaclimb.model.common.CommonObject;

/**
 * User: vadik
 * Date: 5/7/16
 */
public abstract class CommonViewModel<T extends CommonObject> extends BaseViewModel {

    protected Context context;
    protected final T object;

    @Bindable
    private String imageUrl;

    public CommonViewModel(Context context, T object) {
        this.context = context;
        this.object = object;
    }

    public int getId() {
        return object.id;
    }

    public String getName() {
        return object.name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String toolbarImageUrl) {
        this.imageUrl = toolbarImageUrl;
        notifyPropertyChanged(BR.imageUrl);
    }

    @BindingAdapter("imageUrl")
    public static void loadImage(ImageView view, String url) {
        Picasso.with(view.getContext()).load(url).into(view);
    }

    @BindingAdapter({"imageUrl", "placeholder"})
    public static void loadImage(ImageView view, String url, Drawable placeholder) {
        Picasso.with(view.getContext()).load(url).placeholder(placeholder).into(view);
    }
}
