package me.vadik.instaclimb.routes;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import me.vadik.instaclimb.R;
import me.vadik.instaclimb.routes.dummy.DummyContent;
import me.vadik.instaclimb.routes.dummy.DummyItemsHelper;

public class SectorFragment extends Fragment implements FilterDialog.OnFilterPickedListener {

    private static final String ARG_SECTOR_ID = "sectorId";
    private Integer mSectorId;
    private boolean mTwoPane;

    public SectorFragment() {
        // Required empty public constructor
    }

    public static SectorFragment newInstance(Integer sectorId) {
        SectorFragment fragment = new SectorFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTOR_ID, sectorId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSectorId = getArguments().getInt(ARG_SECTOR_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sector, container, false);
        if (view.findViewById(R.id.route_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
        reloadRoutes(view);
        return view;
    }

    private void reloadRoutes(View view) {
        View recyclerView = view.findViewById(R.id.route_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        List<String> statusFilterArgs = SimpleItemRecyclerViewHelper.getStatusFilterArgs(getActivity());
        List<String> gradeFilterArgs = SimpleItemRecyclerViewHelper.getGradeFilterArgs(getActivity());

        String[] statusFilterArgsArray = statusFilterArgs.toArray(new String[statusFilterArgs.size()]);
        String[] gradeFilterArgsArray = gradeFilterArgs.toArray(new String[gradeFilterArgs.size()]);

        List<DummyContent.DummyItem> dummyItems = DummyItemsHelper.getDummyItems(getActivity(), statusFilterArgsArray, gradeFilterArgsArray, mSectorId);
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(getActivity(), getActivity().getSupportFragmentManager(), dummyItems, mTwoPane));
    }

    @Override
    public void onFilterPicked(int which) {
        TextView clearFilterDialog = (TextView) getActivity().findViewById(R.id.clear_filter_dialog);
        if (which >= 0 && which < FilterDialog.GRADES.length) {
            clearFilterDialog.setText("×   Filtered on: " + FilterDialog.GRADES[which]);
        }
        if (clearFilterDialog != null)
            clearFilterDialog.setVisibility(View.VISIBLE);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        preferences.edit().putInt("grade", which).commit();
//        reloadRoutes();
    }

    public void clearFilters(View view) {
        onFilterPicked(-1);
        view.setVisibility(View.GONE);
    }
}
