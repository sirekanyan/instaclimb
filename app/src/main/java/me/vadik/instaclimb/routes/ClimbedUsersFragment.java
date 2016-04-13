package me.vadik.instaclimb.routes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import me.vadik.instaclimb.R;
import me.vadik.instaclimb.users.UserActivity;

/**
 * User: vadik
 * Date: 4/8/16
 */
public class ClimbedUsersFragment extends ListFragment {
//        implements LoaderManager.LoaderCallbacks<Cursor> {

    ArrayAdapter<CharSequence> mAdapter;

    public static final String ARG_ROUTE_ID = "me.vadik.instaclimb.route_id";
    private String mRouteId;

    public static ClimbedUsersFragment newInstance(String routeId) {
        ClimbedUsersFragment fragment = new ClimbedUsersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ROUTE_ID, routeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRouteId = getArguments().getString(ARG_ROUTE_ID);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setEmptyText(getActivity().getString(R.string.no_users_found));
        setHasOptionsMenu(true);
//        mAdapter = new SimpleCursorAdapter(getActivity(),
//                R.layout.route_users_content, null,
//                new String[]{
//                        UsersRoutesView.USER_NAME,
//                        UsersRoutesView.DATE,
//                },
//                new int[]{
//                        R.id.user_routes_name,
//                        R.id.user_routes_date,
//                }, 0);
        mAdapter = ArrayAdapter.createFromResource(getActivity(),R.array.lalalas,android.R.layout.simple_list_item_1);
        setListAdapter(mAdapter);

//        getLoaderManager().initLoader(11, null, this);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Context context = v.getContext();
        Intent intent = new Intent(context, UserActivity.class);
        intent.putExtra(RouteFragment.ARG_ITEM_ID, String.valueOf(id));
        context.startActivity(intent);
    }

//    static final String[] USERS_ROUTES_PROJECTION = new String[]{
//            UsersRoutesView.USER_ID + " as _id",
//            UsersRoutesView.USER_ID,
//            UsersRoutesView.USER_NAME,
//            UsersRoutesView.ROUTE_ID,
//            UsersRoutesView.IS_FLASH,
//            UsersRoutesView.DATE,
//    };

//    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
//        Uri baseUri;
//        String select = "route_id = ?";
//        String[] selectArgs = new String[]{mRouteId};
//        baseUri = Uri.withAppendedPath(RoutesContentProvider.CONTENT_URI, "users_routes_view");
//        return new CursorLoader(getActivity(), baseUri,
//                USERS_ROUTES_PROJECTION, select, selectArgs, "date desc");
//    }

//    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//        mAdapter.swapCursor(data);
//    }

//    public void onLoaderReset(Loader<Cursor> loader) {
//        mAdapter.swapCursor(null);
//    }
}
