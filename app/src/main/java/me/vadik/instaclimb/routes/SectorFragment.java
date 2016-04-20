package me.vadik.instaclimb.routes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.vadik.instaclimb.R;
import me.vadik.instaclimb.contract.RouteContract;
import me.vadik.instaclimb.contract.ViewRouteContract;
import me.vadik.instaclimb.provider.RoutesContentProvider;

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
                new String[]{
                        RouteContract.COLOR1,
                        RouteContract.COLOR2,
                        RouteContract.COLOR3,
                        RouteContract.NAME,
                        RouteContract.IS_ACTIVE,
                        ViewRouteContract.USER_NAME,
                        RouteContract.GRADE,
                        RouteContract.CREATED_WHEN,
                        RouteContract.DONE,
                },
                new int[]{
                        R.id.marker1,
                        R.id.marker2,
                        R.id.marker3,
                        R.id.firstLine,
                        R.id.firstLine,
                        R.id.secondLine,
                        R.id.rightLabel,
                        R.id.thirdLine,
                        R.id.is_route_done,
                }, 0);
        setListAdapter(mAdapter);

        SimpleCursorAdapter.ViewBinder binder = new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                String columnName = cursor.getColumnName(columnIndex);
                if (RouteContract.DONE.equals(columnName)) {
                    int isDone = cursor.getInt(columnIndex);
                    ImageView imageView = (ImageView) view;
                    int iconResId = R.drawable.ic_done_white_24dp;
                    switch (isDone) {
                        case 2:
                            iconResId = R.drawable.ic_done_all_black_24dp; //TODO change icon
                            imageView.setVisibility(View.VISIBLE);
                            break;
                        case 1:
                            iconResId = R.drawable.edit_doneblue;
                            imageView.setVisibility(View.VISIBLE);
                            break;
                        default:
                            imageView.setVisibility(View.GONE);
                            break;
                    }
                    imageView.setImageResource(iconResId);

                    //TODO with appropriate colors
//                    LinearLayout parent = (LinearLayout) imageView.getParent();
//                    int colorRes;
//                    switch (isDone) {
//                        case 2:
//                            colorRes = android.R.color.white;
//                            break;
//                        case 1:
//                            colorRes = android.R.color.darker_gray;
//                            break;
//                        default:
//                            colorRes = android.R.color.black;
//                            break;
//                    }
//                    parent.setBackgroundResource(colorRes);

                    return true;
                } else if (RouteContract.NAME.equals(columnName)) {
                    String routeName = cursor.getString(columnIndex);
                    ((TextView) view).setText(RouteHelper.getName(getActivity(), routeName));
                    return true;
                } else if (RouteContract.IS_ACTIVE.equals(columnName)) {
                    int color;
                    if (1 == cursor.getInt(columnIndex)) {
                        int[] attrs = new int[]{android.R.attr.textColorPrimary};
                        TypedArray a = getActivity().getTheme().obtainStyledAttributes(attrs);
                        int ddd = a.getColor(0, Color.BLACK);
                        a.recycle();
                        color = ddd;
                    } else {
                        color = Color.GRAY;
                    }
                    ((TextView) view).setTextColor(color);
                    return true;
                } else if (RouteContract.COLOR1.equals(columnName)) {
                    int color = cursor.getInt(columnIndex);
                    RouteHelper.setMarkerColor(view, color, R.drawable.rect_dashed);
                    return true;
                } else if (RouteContract.COLOR2.equals(columnName)
                        || RouteContract.COLOR3.equals(columnName)) {
                    int color = cursor.getInt(columnIndex);
                    RouteHelper.setMarkerColor(view, color);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Place an action bar item for searching.
        MenuItem item = menu.add("Search");
        item.setIcon(android.R.drawable.ic_menu_search);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        SearchView sv = new SearchView(getActivity());
        //TODO: add suggestions with grades
//        final String[] from = new String[] {"cityName"};
//        final int[] to = new int[] {android.R.id.text1};
//        sv.setSuggestionsAdapter(new SimpleCursorAdapter(
//                getActivity(),
//                android.R.layout.simple_dropdown_item_1line,
//                from,
//                to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER));
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
        Context context = v.getContext();
        TextView textView = (TextView) v.findViewById(R.id.firstLine);
        Intent intent = new Intent(context, RouteActivity.class);
        intent.putExtra(RouteActivity.ARG_ROUTE_ID, (int) id);
        if (textView != null) {
            intent.putExtra(RouteActivity.ARG_ROUTE_NAME, textView.getText());
        }
        context.startActivity(intent);
    }

    // These are the Contacts rows that we will retrieve.
    static final String[] ROUTES_SUMMARY_PROJECTION = new String[]{
            ViewRouteContract._ID,
            ViewRouteContract.NAME,
            ViewRouteContract.GRADE,
            ViewRouteContract.COLOR1,
            ViewRouteContract.COLOR2,
            ViewRouteContract.COLOR3,
            ViewRouteContract.IS_ACTIVE,
            ViewRouteContract.USER_NAME,
            ViewRouteContract.CREATED_WHEN,
            ViewRouteContract.DONE,
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
            select = "(name like ? or grade like ? or user_name like ?) and ";
            String like = "%" + mCurFilter + "%";
            selectArgs.add(like);
            selectArgs.add(like);
            selectArgs.add(like);
        }
        if (mSectorId != null && mSectorId != -1) {
            select += "sector_id = ? and ";
            selectArgs.add(mSectorId.toString());
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        if (preferences.getBoolean("show_archived", false)) {
            select += "1 = 1";
        } else {
            select += "is_active = 1";
        }

//        if (mCurFilter != null) {
//            baseUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI,
//                    Uri.encode(mCurFilter));
//        } else {
        baseUri = Uri.withAppendedPath(RoutesContentProvider.CONTENT_URI, "routes_view");
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
