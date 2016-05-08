package me.vadik.instaclimb.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import me.vadik.instaclimb.model.User;
import me.vadik.instaclimb.view.UserActivity;

/**
 * User: vadik
 * Date: 5/8/16
 */
public class UserItemViewModel extends ObjectViewModel<User> {
    private final User user;

    public UserItemViewModel(Context context, User user) {
        super(context, user);
        this.user = user;
    }

    public void openUser(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, UserActivity.class);
        intent.putExtra(UserActivity.ARG_USER_ID, user.id);
        intent.putExtra(UserActivity.ARG_USER_NAME, user.name);
        context.startActivity(intent);
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
