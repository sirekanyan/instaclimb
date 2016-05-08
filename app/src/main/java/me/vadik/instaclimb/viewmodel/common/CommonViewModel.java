package me.vadik.instaclimb.viewmodel.common;

import android.content.Context;

import me.vadik.instaclimb.model.common.CommonObject;

/**
 * User: vadik
 * Date: 5/7/16
 */
public abstract class CommonViewModel<T extends CommonObject> implements BaseViewModel {

    protected Context context;
    protected final T object;

    public CommonViewModel(Context context, T object) {
        this.context = context;
        this.object = object;
    }

    public String getName() {
        return object.name;
    }
}
