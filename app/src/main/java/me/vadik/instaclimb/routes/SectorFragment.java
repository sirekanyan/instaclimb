package me.vadik.instaclimb.routes;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.vadik.instaclimb.R;
import me.vadik.instaclimb.routes.contract.Routes;
import me.vadik.instaclimb.routes.contract.StatusValues;
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
        setEmptyText(getActivity().getString(R.string.no_routes_found));

        // We have a menu item to show in action bar.
        setHasOptionsMenu(true);

        // Create an empty adapter we will use to display the loaded data.
        mAdapter = new SimpleCursorAdapter(getActivity(),
                R.layout.route_list_content, null,
                new String[]{Routes.COLOR1, Routes.COLOR2, Routes.COLOR3, Routes.NAME, Routes.STATUS, Routes.GRADE},
                new int[]{R.id.marker1, R.id.marker2, R.id.marker3, R.id.route_name, R.id.route_name, R.id.route_grade}, 0);
        setListAdapter(mAdapter);


        SimpleCursorAdapter.ViewBinder binder = new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                String columnName = cursor.getColumnName(columnIndex);
                if (Routes.STATUS.equals(columnName)) {
                    Integer status = cursor.getInt(columnIndex);
                    TextView textView = (TextView) view;
                    if (StatusValues.ACTIVE.equals(status)) {
                        textView.setTextColor(getResources().getColor(android.R.color.black));
                    } else {
                        textView.setText(textView.getText() + " (" + status.toString() + ")");
                        textView.setTextColor(getResources().getColor(android.R.color.darker_gray));
                    }
                    return true;
                } else if (Routes.COLOR1.equals(columnName)
                        || Routes.COLOR2.equals(columnName)
                        || Routes.COLOR3.equals(columnName)) {
                    int color = cursor.getInt(columnIndex);
                    setMarkerColor(view, color, Routes.COLOR1.equals(columnName));
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

    private void setMarkerColor(View view, int color, boolean isFirst) {
        TypedArray colors = getResources().obtainTypedArray(R.array.colors);
        if (color == 0 && isFirst) {
            view.setBackgroundResource(R.drawable.rect_dashed);
        } else {
            view.setBackgroundResource(colors.getResourceId(color, 0));
        }
        colors.recycle();
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
            Routes._ID,
            Routes.NAME,
            Routes.GRADE,
            Routes.COLOR1,
            Routes.COLOR2,
            Routes.COLOR3,
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
                ROUTES_SUMMARY_PROJECTION, select, selectArgs.toArray(new String[selectArgs.size()]), "_id desc");
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
