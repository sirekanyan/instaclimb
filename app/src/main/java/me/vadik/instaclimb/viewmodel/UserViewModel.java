package me.vadik.instaclimb.viewmodel;

import android.content.Context;

import me.vadik.instaclimb.model.User;
import me.vadik.instaclimb.viewmodel.common.CommonViewModel;

/**
 * User: vadik
 * Date: 5/3/16
 */
public class UserViewModel extends CommonViewModel<User> {
    private final User user;

    public UserViewModel(Context context, User user) {
        super(context, user);
        this.user = user;
    }

    public boolean hasPicture() {
        return user.hasPicture;
    }

    public String getPictureUrl() {
        return "https://vadik.me/userpic/" + user.id + ".jpg";
    }

    public String getDate() {
        return user.date;
    }
}