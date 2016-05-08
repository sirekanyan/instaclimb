package me.vadik.instaclimb.viewmodel;

import android.content.Context;

import me.vadik.instaclimb.model.common.CommonObject;

/**
 * User: vadik
 * Date: 5/7/16
 */
abstract class ObjectViewModel<T extends CommonObject> implements ViewModel {

    protected Context context;
    protected final T object;

    public ObjectViewModel(Context context, T object) {
        this.context = context;
        this.object = object;
    }

    public String getName() {
        return object.name;
    }
}
