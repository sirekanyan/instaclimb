package me.vadik.instaclimb.viewmodel;

import android.content.Context;
import android.databinding.Bindable;
import android.util.Log;
import android.view.View;

import me.vadik.instaclimb.BR;
import me.vadik.instaclimb.model.Gym;
import me.vadik.instaclimb.viewmodel.common.CommonViewModel;

/**
 * User: vadik
 * Date: 5/12/16
 */
public class GymViewModel extends CommonViewModel<Gym> {

    private final Gym gym;

    @Bindable
    private boolean isStarred;

    private String subtitle;

    public GymViewModel(Context context, Gym object) {
        super(context, object);
        this.gym = object;
        subtitle = "Столько трасс (столько-то новых)"; //TODO
    }

    public boolean isStarred() {
        return isStarred;
    }

    public void onClickStar(View view) {
        isStarred = !isStarred;
        notifyChange();
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getPicUrl() {
        return "http://instaclimb.ru/img/gyms/" + gym.id + "/top/1.jpg";
    }

    public void onClickRoutes(View view) {

    }

    public void onClickRatings(View view) {

    }
}
