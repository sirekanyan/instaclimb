package me.vadik.instaclimb.routes;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.vadik.instaclimb.R;
import me.vadik.instaclimb.routes.contract.Routes;
import me.vadik.instaclimb.routes.dummy.DummyItem;
import me.vadik.instaclimb.routes.provider.RoutesContentProvider;

/**
 * A fragment representing a single Route detail screen.
 * This fragment is either contained in a {@link SectorActivity}
 * in two-pane mode (on tablets) or a {@link RouteActivity}
 * on handsets.
 */
public class RouteFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private DummyItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RouteFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment //TODO
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.

            String itemId = getArguments().getString(ARG_ITEM_ID);
            Log.e("me", ARG_ITEM_ID + " => " + getArguments().getString(ARG_ITEM_ID));

            Uri myUri = Uri.withAppendedPath(RoutesContentProvider.CONTENT_URI, "routes");
            //TODO simplify call to content provider
            //TODO use loader here

            Cursor cursor = getActivity().getContentResolver().query(
                    myUri,
                    null,
                    "_id = ?",
                    new String[]{itemId},
                    null);

            try {
                if (cursor != null && cursor.moveToFirst()) {
                    Integer id = cursor.getInt(cursor.getColumnIndex(Routes._ID));
                    String name = cursor.getString(cursor.getColumnIndex(Routes.NAME));
                    mItem = new DummyItem(id.toString(), name, cursor);
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.name);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.route_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.route_detail)).setText(mItem.details);

            TextView picIdTextView = (TextView) rootView.findViewById(R.id.picture_id);
            picIdTextView.setText(mItem.getPictureId().toString());
        }

        return rootView;
    }
}
