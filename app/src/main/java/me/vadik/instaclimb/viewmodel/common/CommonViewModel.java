package me.vadik.instaclimb.viewmodel.common;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import me.vadik.instaclimb.BR;
import me.vadik.instaclimb.helper.VolleySingleton;
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

    @BindingAdapter({"bind:imageUrl"/*, "bind:error"*/}) // TODO bind errorDrawable
    public static void loadImage(NetworkImageView view, String url/*, Drawable error*/) {
        ImageLoader imageLoader = VolleySingleton.getInstance(view.getContext()).getImageLoader();
        view.setImageUrl(url, imageLoader);
    }
}
