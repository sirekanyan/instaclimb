package me.vadik.instaclimb.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.vadik.instaclimb.R;
import me.vadik.instaclimb.databinding.RatingsFragmentBinding;
import me.vadik.instaclimb.model.contract.RouteContract;
import me.vadik.instaclimb.model.contract.UserContract;

public class RatingsFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = RatingsFragment.class.getName();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RatingsFragment() {
        // Required empty
    }

    public static RatingsFragment newInstance(String param1, String param2) {
        RatingsFragment fragment = new RatingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static RatingsFragment newInstance() {
        return new RatingsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private static final int NUM_PAGES = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RatingsFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.ratings_fragment, container, false);
        PagerAdapter pagerAdapter = new RatingsPagerAdapter(getChildFragmentManager());
        binding.ratingsViewPager.setAdapter(pagerAdapter);
        return binding.getRoot();
    }

    private class RatingsPagerAdapter extends FragmentPagerAdapter {
        public RatingsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            final int sex;
            final int discipline;
            switch (position) {
                case 0:
                    sex = UserContract.Sex.MALE;
                    discipline = RouteContract.Discipline.BOULDER;
                    break;
                case 1:
                    sex = UserContract.Sex.FEMALE;
                    discipline = RouteContract.Discipline.BOULDER;
                    break;
                case 2:
                    sex = UserContract.Sex.MALE;
                    discipline = RouteContract.Discipline.LEAD;
                    break;
                case 3:
                    sex = UserContract.Sex.FEMALE;
                    discipline = RouteContract.Discipline.LEAD;
                    break;
                default:
                    sex = UserContract.Sex.MALE;
                    discipline = RouteContract.Discipline.BOULDER;
                    break;
            }
            return RatingFragment.newInstance(sex);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
