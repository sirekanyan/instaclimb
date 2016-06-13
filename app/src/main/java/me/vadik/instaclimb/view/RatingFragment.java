package me.vadik.instaclimb.view;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import me.vadik.instaclimb.databinding.RatingFragmentBinding;
import me.vadik.instaclimb.model.User;
import me.vadik.instaclimb.provider.RoutesContentProvider;
import me.vadik.instaclimb.view.adapter.RatingAdapter;

public class RatingFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = RatingFragment.class.getName();
    private static final String ARG_DISCIPLINE = "me.vadik.instaclimb.route_discipline";
    private static final String ARG_SEX = "me.vadik.instaclimb.user_sex";

    private int mColumnCount = 1;
    private int mDiscipline;
    private int mSex;
    private RatingFragmentBinding binding;
    private RatingAdapter mAdapter;

    public RatingFragment() {
        // required empty
    }

    @Deprecated
    public static RatingFragment newInstance(int sex) {
        RatingFragment fragment = new RatingFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SEX, sex);
        fragment.setArguments(args);
        return fragment;
    }

    public static RatingFragment newInstance(int sex, int discipline) {
        RatingFragment fragment = new RatingFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SEX, sex);
        args.putInt(ARG_DISCIPLINE, discipline);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSex = getArguments().getInt(ARG_SEX);
            mDiscipline = getArguments().getInt(ARG_DISCIPLINE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = RatingFragmentBinding.inflate(inflater, container, false);
        mAdapter = setupRecyclerView();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);
    }

    public RatingAdapter setupRecyclerView() {
        RecyclerView recyclerView = binding.list;
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), mColumnCount));
        }
        RatingAdapter ratingAdapter = new RatingAdapter(getContext());
        recyclerView.setAdapter(ratingAdapter);
        return ratingAdapter;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        Uri uri = Uri.withAppendedPath(RoutesContentProvider.CONTENT_URI, "users");
        String select = "rating > 0 and sex = ?";
        String[] args = new String[]{String.valueOf(mSex)};
        return new CursorLoader(getContext(), uri, null, select, args, "rating desc");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor != null && cursor.isClosed()) {
            Log.e(TAG, "Cursor is already closed");
            return;
        }
        try {
            if (cursor != null && cursor.moveToFirst()) {
                List<User> users = new ArrayList<>();
                do {
                    users.add(new User.Builder(cursor).build());
                } while (cursor.moveToNext());
                mAdapter.swap(users);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.reset();
    }
}
