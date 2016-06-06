package me.vadik.instaclimb.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import me.vadik.instaclimb.databinding.GymCardBinding;
import me.vadik.instaclimb.model.Gym;
import me.vadik.instaclimb.viewmodel.GymViewModel;

/**
 * User: vadik
 * Date: 5/13/16
 */
public class GymsAdapter extends RecyclerViewAdapter<Gym> {

    public GymsAdapter(Context context) {
        super(context);
    }

    @Override
    protected void onBindRowViewHolder(BindingHolder bindingHolder, Gym gym) {
        GymCardBinding binding = (GymCardBinding) bindingHolder.getBinding();
        binding.setGym(new GymViewModel(context, gym));
    }

    @Override
    protected BindingHolder onCreateRowViewHolder(LayoutInflater inflater, ViewGroup parent) {
        GymCardBinding binding = GymCardBinding.inflate(inflater, parent, false);
        return new BindingHolder(binding.getRoot());
    }
}
