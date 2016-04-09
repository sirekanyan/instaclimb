package me.vadik.instaclimb.routes;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.vadik.instaclimb.R;
import me.vadik.instaclimb.routes.contract.Routes;
import me.vadik.instaclimb.routes.provider.RoutesContentProvider;

/**
 * User: vadik
 * Date: 4/8/16
 */
public class SectorFragment extends ListFragment implements
        SearchView.OnQueryTextListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    // This is the Adapter being used to display the list's data.
    SimpleCursorAdapter mAdapter;
    private String mCurFilter;

    private static final String ARG_SECTOR_ID = "sectorId";
    private Integer mSectorId;

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

    boolean mTwoPane = false;

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_sector, container, false);
//        if (view.findViewById(R.id.route_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
//            mTwoPane = true;
//        }
//        return view;
//    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Give some text to display if there is no data.  In a real
        // application this would come from a resource.
        setEmptyText("No routes found");

        // We have a menu item to show in action bar.
        setHasOptionsMenu(true);

        // Create an empty adapter we will use to display the loaded data.
        mAdapter = new SimpleCursorAdapter(getActivity(),
                R.layout.route_list_content, null,
                new String[]{Routes.COLOR, Routes.NAME, Routes.STATUS, Routes.GRADE},
                new int[]{R.id.route_list_content, R.id.route_name, R.id.route_name, R.id.route_grade}, 0);
        setListAdapter(mAdapter);


        SimpleCursorAdapter.ViewBinder binder = new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                String columnName = cursor.getColumnName(columnIndex);
                if (Routes.NAME.equals(columnName)) {
                    //TODO remove this replacement
                    String name = cursor.getString(columnIndex);
                    ((TextView) view).setText(name.replaceAll(" \\(.*?\\)$", ""));
                    return true;
                } else if (Routes.STATUS.equals(columnName)) {
                    String status = cursor.getString(columnIndex);
                    TextView textView = (TextView) view;
                    if ("Активна".equals(status)) {
                        textView.setTextColor(getResources().getColor(android.R.color.black));
                    } else {
                        textView.setText(textView.getText() + " (" + status.toLowerCase() + ")");
                        textView.setTextColor(getResources().getColor(android.R.color.darker_gray));
                    }
                    return true;
                } else if (Routes.COLOR.equals(columnName)) {

                    String color = cursor.getString(columnIndex);

                    color = color.replaceAll("^,", ""); //TODO remove replacement

                    String firstColor = null;
                    String secondColor = null;
                    String thirdColor = null;

                    if (!color.isEmpty()) {
                        String[] colors = color.split(",");
                        switch (colors.length) {
                            case 3:
                                thirdColor = colors[2];
                            case 2:
                                secondColor = colors[1];
                            case 1:
                                firstColor = colors[0];
                        }
                    }

                    setMarkerColor(view, R.id.marker1, firstColor);
                    setMarkerColor(view, R.id.marker2, secondColor);
                    setMarkerColor(view, R.id.marker3, thirdColor);

                    return true;
                }
                return false;
            }
        };

        mAdapter.setViewBinder(binder);

        // Prepare the loader.  Either re-connect with an existing one,
        // or start a new one.
        getLoaderManager().initLoader(0, null, this);
    }

    private void setMarkerColor(View view1, int resId, String colorName) {
        View view = view1.findViewById(resId);
        Map<String, Integer> colors = FilterHelper.COLORS;
        if (colors.containsKey(colorName)) {
            view.setBackgroundResource(colors.get(colorName));
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Place an action bar item for searching.
        MenuItem item = menu.add("Search");
        item.setIcon(android.R.drawable.ic_menu_search);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        SearchView sv = new SearchView(getActivity());
        sv.setOnQueryTextListener(this);
        item.setActionView(sv);
    }

    public boolean onQueryTextChange(String newText) {
        // Called when the action bar search text has changed.  Update
        // the search filter, and restart the loader to do a new query
        // with this filter.
        mCurFilter = !TextUtils.isEmpty(newText) ? newText : null;
        getLoaderManager().restartLoader(0, null, this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        // Don't care about this.
        return true;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putString(RouteFragment.ARG_ITEM_ID, String.valueOf(id));
            RouteFragment fragment = new RouteFragment();
            fragment.setArguments(arguments);
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.route_detail_container, fragment)
                    .commit();
        } else {
            Context context = v.getContext();
            Intent intent = new Intent(context, RouteActivity.class);
            intent.putExtra(RouteFragment.ARG_ITEM_ID, String.valueOf(id));

            context.startActivity(intent);
        }
    }

    // These are the Contacts rows that we will retrieve.
    static final String[] ROUTES_SUMMARY_PROJECTION = new String[]{
            Routes.ID + " as _id",
            Routes.NAME,
            Routes.GRADE,
            Routes.COLOR,
            Routes.STATUS,
    };

    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // This is called when a new Loader needs to be created.  This
        // sample only has one Loader, so we don't care about the ID.
        // First, pick the base URI to use depending on whether we are
        // currently filtering.
        Uri baseUri;
        String select = "";
        List<String> selectArgs = new ArrayList<>();
        if (mCurFilter != null) {
            select = "(name like ? or grade like ? or author like ? or color like ?) and ";
            String like = "%" + mCurFilter + "%";
            selectArgs.add(like);
            selectArgs.add(like);
            selectArgs.add(like);
            selectArgs.add(like);
        }
        if (mSectorId != null && mSectorId != -1) {
            select += "sector_id = ? and ";
            selectArgs.add(mSectorId.toString());
        }

        List<String> statusFilterArgs = FilterHelper.getStatusFilterArgs(getActivity());
        String[] statusPlaceHolders = new String[statusFilterArgs.size()];
        for (int i = 0; i < statusPlaceHolders.length; i++) {
            statusPlaceHolders[i] = "?";
        }

        select += "status in (" + TextUtils.join(",", statusPlaceHolders) + ")";
        selectArgs.addAll(statusFilterArgs);

//        if (mCurFilter != null) {
//            baseUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI,
//                    Uri.encode(mCurFilter));
//        } else {
        baseUri = Uri.withAppendedPath(RoutesContentProvider.CONTENT_URI, "routes");
//        }

        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return new CursorLoader(getActivity(), baseUri,
                ROUTES_SUMMARY_PROJECTION, select, selectArgs.toArray(new String[selectArgs.size()]), "id desc");
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Swap the new cursor in.  (The framework will take care of closing the
        // old cursor once we return.)
        mAdapter.swapCursor(data);
    }

    public void onLoaderReset(Loader<Cursor> loader) {
        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed.  We need to make sure we are no
        // longer using it.
        mAdapter.swapCursor(null);
    }
}
