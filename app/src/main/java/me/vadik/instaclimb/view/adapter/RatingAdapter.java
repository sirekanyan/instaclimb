package me.vadik.instaclimb.view.adapter;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import me.vadik.instaclimb.databinding.RowLayoutRatingBinding;
import me.vadik.instaclimb.model.User;
import me.vadik.instaclimb.view.adapter.abstr.AbstractRecyclerViewAdapter;
import me.vadik.instaclimb.viewmodel.UserViewModel;

/**
 * User: vadik
 * Date: 5/11/16
 */
public class RatingAdapter extends AbstractRecyclerViewAdapter<User> {

    public RatingAdapter(Context context) {
        super(context);
    }

    @Override
    protected void onBindItem(ViewDataBinding binding, User user) {
        ((RowLayoutRatingBinding) binding).setUser(new UserViewModel(mContext, user));
    }

    @Override
    protected ViewDataBinding onCreateItem(LayoutInflater inflater, ViewGroup parent) {
        return RowLayoutRatingBinding.inflate(inflater, parent, false);
    }
}
