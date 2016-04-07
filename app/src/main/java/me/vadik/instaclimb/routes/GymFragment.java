package me.vadik.instaclimb.routes;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.vadik.instaclimb.R;
import me.vadik.instaclimb.routes.example.MyFragment;

public class GymFragment extends Fragment {

    private static final String ARG_GYM_ID = "gym_id";
    private Integer mGymId;
    private OnFragmentInteractionListener mListener;

    public GymFragment() {
        // Required empty public constructor
    }

    public static GymFragment newInstance(Integer gymId) {
        GymFragment fragment = new GymFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_GYM_ID, gymId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mGymId = getArguments().getInt(ARG_GYM_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gym, container, false);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager22);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.tabz);
        tabLayout.setVisibility(View.VISIBLE);
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

        Uri myUri = Uri.withAppendedPath(RoutesContentProvider.CONTENT_URI, "sectors_gyms");

        Cursor cursor = getActivity().getContentResolver().query(myUri, null,
                "gym_id = ?", new String[]{String.valueOf(gymId)}, null);

        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Integer id = cursor.getInt(cursor.getColumnIndex("id"));
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    sectors.add(new Sector(id, name));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return sectors;
    }

    private class Sector {
        private final Integer id;
        private final String name;

        public Sector(Integer id, String name) {
            this.id = id;
            this.name = name;
        }

        public Integer getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        for (Sector sector : getSectors(mGymId)) {
            adapter.addFragment(RouteListFragment.newInstance(sector.getId()), sector.getName());
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

    private Fragment getFragmentWithContent(String content) {
        Fragment f = new MyFragment();
        Bundle args = new Bundle();
        args.putString("content", content);
        f.setArguments(args);
        return f;
    }
}
