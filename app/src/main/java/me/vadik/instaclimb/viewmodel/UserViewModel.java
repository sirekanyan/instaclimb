package me.vadik.instaclimb.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import me.vadik.instaclimb.model.User;
import me.vadik.instaclimb.users.UserActivity;

/**
 * User: vadik
 * Date: 5/3/16
 */
public class UserViewModel {
    private final User user;

    public UserViewModel(User user) {
        this.user = user;
    }

    public void openUser(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, UserActivity.class);
        intent.putExtra(UserActivity.ARG_USER_ID, user.getId());
        intent.putExtra(UserActivity.ARG_USER_NAME, user.getName());
        context.startActivity(intent);
    }
}
