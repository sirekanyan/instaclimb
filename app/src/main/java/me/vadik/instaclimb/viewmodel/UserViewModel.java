package me.vadik.instaclimb.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import me.vadik.instaclimb.model.User;
import me.vadik.instaclimb.view.UserActivity;
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
        if (!hasPicture()) {
            return null;
        }
        return "https://vadik.me/userpic/" + user.id + ".jpg";
    }

    public int getHeight() {
        return user.height;
    }

    public int getWeight() {
        return user.weight;
    }

    public int getRating() {
        return user.rating;
    }

    public String getFlashBoulder() {
        return user.flash_boulder;
    }

    public String getRedpointBoulder() {
        return user.redpoint_boulder;
    }

    public String getFlashLead() {
        return user.flash_lead;
    }

    public String getRedpointLead() {
        return user.redpoint_lead;
    }

    public int getVisibility(int property) {
        if (property == 0) {
            return View.GONE;
        }
        return View.VISIBLE;
    }

    public int getVisibility(String p1, String p2) {
        if (TextUtils.isEmpty(p1) && TextUtils.isEmpty(p2)) {
            return View.GONE;
        }
        return View.VISIBLE;
    }

    public void openUser(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, UserActivity.class);
        intent.putExtra(UserActivity.ARG_USER_ID, user.id);
        context.startActivity(intent);
    }

    @Override
    public String getImageUrl() {
        return getPictureUrl();
    }
}
