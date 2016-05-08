package me.vadik.instaclimb.view;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import me.vadik.instaclimb.R;
import me.vadik.instaclimb.model.Sector;
import me.vadik.instaclimb.provider.RoutesContentProvider;

public class GymFragment extends Fragment {

    private static final String ARG_GYM_ID = "gym_id";
    private static final String ARG_GYM_NAME = "gym_name";
    public static final int ALL_GYMS = -1;
    private Integer mGymId;
    private String mGymName;
    private OnFragmentInteractionListener mListener;

    public GymFragment() {
        // Required empty public constructor
    }

    public static GymFragment newInstance(Integer gymId, String gymName) {
        GymFragment fragment = new GymFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_GYM_ID, gymId);
        args.putString(ARG_GYM_NAME, gymName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mGymId = getArguments().getInt(ARG_GYM_ID);
            mGymName = getArguments().getString(ARG_GYM_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gym_fragment, container, false);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(mGymName);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager22);
        List<Sector> sectors;

        if (mGymId == ALL_GYMS) {
            sectors = new ArrayList<>();
            sectors.add(new Sector.Builder(-1, "All").build());
        } else {
            sectors = getSectors(mGymId);
        }

        setupViewPager(viewPager, sectors);

        TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.tabz);
        if (sectors.size() > 1) {
            tabLayout.setVisibility(View.VISIBLE);
        } else {
            tabLayout.setVisibility(View.GONE); //TODO: remove this
        }
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private List<Sector> getSectors(Integer gymId) {
        List<Sector> sectors = new ArrayList<>();

        Uri myUri = Uri.withAppendedPath(RoutesContentProvider.CONTENT_URI, "sectors");

        Cursor cursor = getActivity().getContentResolver().query(myUri, null,
                "gym_id = ?", new String[]{String.valueOf(gymId)}, "_id");

        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    sectors.add(new Sector.Builder(cursor).build());
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return sectors;
    }

    private void setupViewPager(ViewPager viewPager, List<Sector> sectors) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        for (Sector sector : sectors) {
            SectorFragment f = SectorFragment.newInstance(sector.id);
            adapter.addFragment(f, sector.name);
        }
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
