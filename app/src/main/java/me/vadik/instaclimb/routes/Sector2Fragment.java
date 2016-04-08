package me.vadik.instaclimb.routes;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.widget.SimpleCursorAdapter;
import android.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.Map;

import me.vadik.instaclimb.R;
import me.vadik.instaclimb.routes.contract.Routes;
import me.vadik.instaclimb.routes.dummy.DummyItemsHelper;
import me.vadik.instaclimb.routes.provider.RoutesContentProvider;

/**
 * User: vadik
 * Date: 4/8/16
 */
public class Sector2Fragment extends ListFragment
        implements SearchView.OnQueryTextListener, LoaderManager.LoaderCallbacks<Cursor> {

    // This is the Adapter being used to display the list's data.
    SimpleCursorAdapter mAdapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Give some text to display if there is no data.  In a real
        // application this would come from a resource.
        setEmptyText("No phone numbers");

        // We have a menu item to show in action bar.
        setHasOptionsMenu(true);

        // Create an empty adapter we will use to display the loaded data.
        mAdapter = new SimpleCursorAdapter(getActivity(),
                R.layout.route_list_content, null,
                new String[]{Routes.COLOR, Routes.NAME, Routes.GRADE},
                new int[]{R.id.route_list_content, R.id.route_name, R.id.route_grade}, 0);
        setListAdapter(mAdapter);


        SimpleCursorAdapter.ViewBinder binder = new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                String name = cursor.getColumnName(columnIndex);
                if (Routes.COLOR.equals(name)) {
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
        Map<String, Integer> colors = DummyItemsHelper.COLORS;
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
//        mCurFilter = !TextUtils.isEmpty(newText) ? newText : null;
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
        // Insert desired behavior here.
        Log.i("FragmentComplexList", "Item clicked: " + id);
    }

    // These are the Contacts rows that we will retrieve.
    static final String[] ROUTES_SUMMARY_PROJECTION = new String[]{
            Routes.ID + " as _id",
            Routes.NAME,
            Routes.GRADE,
            Routes.COLOR,
    };

    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // This is called when a new Loader needs to be created.  This
        // sample only has one Loader, so we don't care about the ID.
        // First, pick the base URI to use depending on whether we are
        // currently filtering.
        Uri baseUri;
//        if (mCurFilter != null) {
//            baseUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI,
//                    Uri.encode(mCurFilter));
//        } else {
        baseUri = Uri.withAppendedPath(RoutesContentProvider.CONTENT_URI, "routes");
//        }

        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        String select = null;
        return new CursorLoader(getActivity(), baseUri,
                ROUTES_SUMMARY_PROJECTION, select, null, "id desc");
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
