package me.vadik.instaclimb.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import me.vadik.instaclimb.databinding.RowLayoutRatingBinding;
import me.vadik.instaclimb.model.User;
import me.vadik.instaclimb.viewmodel.UserViewModel;

/**
 * User: vadik
 * Date: 5/11/16
 */
public class RatingAdapter extends RecyclerViewAdapter<User> {

    public RatingAdapter(Context context) {
        super(context);
    }

    @Override
    protected void onBindRowViewHolder(BindingHolder bindingHolder, User user) {
        RowLayoutRatingBinding rowUserBinding = (RowLayoutRatingBinding) bindingHolder.getBinding();
        rowUserBinding.setUser(new UserViewModel(context, user));
    }

    @Override
    protected BindingHolder onCreateRowViewHolder(LayoutInflater inflater, ViewGroup parent) {
        RowLayoutRatingBinding rowUserBinding = RowLayoutRatingBinding.inflate(inflater, parent, false);
        return new BindingHolder(rowUserBinding.getRoot());
    }
}
