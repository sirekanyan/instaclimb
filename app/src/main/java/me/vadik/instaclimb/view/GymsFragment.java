package me.vadik.instaclimb.view;

import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import me.vadik.instaclimb.R;
import me.vadik.instaclimb.databinding.GymsFragmentBinding;
import me.vadik.instaclimb.model.Gym;
import me.vadik.instaclimb.provider.RoutesContentProvider;
import me.vadik.instaclimb.view.adapter.GymsAdapter;

public class GymsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private GymsFragmentBinding mBinding;
    private GymsAdapter mAdapter;

    public GymsFragment() {
        // Required empty
    }

    public static GymsFragment newInstance() {
        return new GymsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.gyms_fragment, container, false);
        mAdapter = setupRecyclerView();
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);
    }

    public GymsAdapter setupRecyclerView() {
        RecyclerView recyclerView = mBinding.gymsRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        GymsAdapter gymAdapter = new GymsAdapter(getContext());
        recyclerView.setAdapter(gymAdapter);
        return gymAdapter;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = Uri.withAppendedPath(RoutesContentProvider.CONTENT_URI, "gyms");
        return new CursorLoader(getContext(), uri, null, null, null, "_id");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.isClosed()) {
            return;
        }
        try {
            if (cursor.moveToFirst()) {
                List<Gym> gyms = new ArrayList<>();
                do {
                    gyms.add(new Gym.Builder(cursor).build());
                } while (cursor.moveToNext());
                mAdapter.swap(gyms);
            }
        } finally {
            cursor.close();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.reset();
    }
}
