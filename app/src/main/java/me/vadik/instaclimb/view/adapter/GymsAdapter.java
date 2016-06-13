package me.vadik.instaclimb.view.adapter;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import me.vadik.instaclimb.databinding.GymCardBinding;
import me.vadik.instaclimb.model.Gym;
import me.vadik.instaclimb.view.adapter.abstr.AbstractRecyclerViewAdapter;
import me.vadik.instaclimb.viewmodel.GymViewModel;

/**
 * User: vadik
 * Date: 5/13/16
 */
public class GymsAdapter extends AbstractRecyclerViewAdapter<Gym> {

    public GymsAdapter(Context context) {
        super(context);
    }

    @Override
    protected void onBindItem(ViewDataBinding binding, Gym gym) {
        ((GymCardBinding) binding).setGym(new GymViewModel(mContext, gym));
    }

    @Override
    protected ViewDataBinding onCreateItem(LayoutInflater inflater, ViewGroup parent) {
        return GymCardBinding.inflate(inflater, parent, false);
    }
}
