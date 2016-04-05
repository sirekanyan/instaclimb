package me.vadik.instaclimb.routes;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.vadik.instaclimb.routes.dummy.DummyContent;

/**
 * An activity representing a list of Routes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RouteDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RouteListActivity extends AppCompatActivity implements FilterDialog.OnFilterPickedListener {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "ОЛОЛОЛО", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        reloadRoutes();

        if (findViewById(R.id.route_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    private void reloadRoutes() {
        View recyclerView = findViewById(R.id.route_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        List<String> statusFilterArgs = new ArrayList<>();
        if (preferences.getBoolean("show_active", true)) {
            statusFilterArgs.add("Активна");
        }
        if (preferences.getBoolean("show_archived", false)) {
            statusFilterArgs.add("Архив");
        }
        if (preferences.getBoolean("show_draft", false)) {
            statusFilterArgs.add("Черновик");
        }

        List<String> gradeFilterArgs = new ArrayList<>();
        switch (preferences.getInt("grade", -1)) {
            case 0:
                gradeFilterArgs.addAll(Arrays.asList(
                        "5a", "5a+", "5b", "5b+", "5c", "5c+",
                        "6a", "6a+"
                ));
                break;
            case 1:
                gradeFilterArgs.addAll(Arrays.asList(
                        "6a", "6a+",
                        "6b", "6b+"
                ));
                break;
            case 2:
                gradeFilterArgs.addAll(Arrays.asList(
                        "6b", "6b+",
                        "6c", "6c+"
                ));
                break;
            case 3:
                gradeFilterArgs.addAll(Arrays.asList(
                        "6c", "6c+",
                        "7a", "7a+"
                ));
                break;
            case 4:
                gradeFilterArgs.addAll(Arrays.asList(
                        "7a", "7a+",
                        "7b", "7b+"
                ));
                break;
            case 5:
                gradeFilterArgs.addAll(Arrays.asList(
                        "7b", "7b+",
                        "7c", "7c+"
                ));
                break;
            case 6:
                gradeFilterArgs.addAll(Arrays.asList(
                        "7c", "7c+",
                        "8a", "8a+", "8b", "8b+", "8c", "8c+", "9a"
                ));
                break;
        }

        String[] statusFilterArgsArray = statusFilterArgs.toArray(new String[statusFilterArgs.size()]);
        String[] gradeFilterArgsArray = gradeFilterArgs.toArray(new String[gradeFilterArgs.size()]);

        List<DummyContent.DummyItem> dummyItems = getDummyItems(statusFilterArgsArray, gradeFilterArgsArray);
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(dummyItems));
    }

    private List<DummyContent.DummyItem> getDummyItems(String[] statusFilterArgs, String[] gradeFilterArgs) {
        DummyContent.clear();

        Uri myUri = Uri.withAppendedPath(RoutesContentProvider.CONTENT_URI, "routes");

        String[] statusPlaceHolders = new String[statusFilterArgs.length];
        String[] gradePlaceHolders = new String[gradeFilterArgs.length];

        for (int i = 0; i < statusPlaceHolders.length; i++) {
            statusPlaceHolders[i] = "?";
        }

        for (int i = 0; i < gradePlaceHolders.length; i++) {
            gradePlaceHolders[i] = "?";
        }

        String[] args = new String[statusFilterArgs.length + gradeFilterArgs.length];

        System.arraycopy(statusFilterArgs, 0, args, 0, statusFilterArgs.length);

        System.arraycopy(gradeFilterArgs, 0, args, statusFilterArgs.length, gradeFilterArgs.length);

        String gradeInClause = "";

        if (gradeFilterArgs.length > 0) {
            gradeInClause = "and lower(grade) in (" + TextUtils.join(",", gradePlaceHolders) + ")";
        }

        Cursor cursor = getContentResolver().query(myUri, null,
                "status in (" + TextUtils.join(",", statusPlaceHolders) + ") " + gradeInClause,
                args, "id desc");

        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Integer id = cursor.getInt(cursor.getColumnIndex("id"));
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    DummyContent.addItem(new DummyContent.DummyItem(id.toString(), name, cursor));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return DummyContent.ITEMS;
    }

    private static Map<String, Integer> colors;

    static {
        colors = new HashMap<>();
        colors.put("черный", R.drawable.rect_black);
        colors.put("синий", R.drawable.rect_blue);
        colors.put("голубой", R.drawable.rect_blue_light);
        colors.put("коричневый", R.drawable.rect_brown);
        colors.put("серый", R.drawable.rect_gray);
        colors.put("зеленый", R.drawable.rect_green);
        colors.put("оранжевый", R.drawable.rect_orange);
        colors.put("розовый", R.drawable.rect_pink);
        colors.put("фиолетовый", R.drawable.rect_purple);
        colors.put("красный", R.drawable.rect_red);
        colors.put("белый", R.drawable.rect_white);
        colors.put("желтый", R.drawable.rect_yellow);
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<DummyContent.DummyItem> mValues;

        public SimpleItemRecyclerViewAdapter(List<DummyContent.DummyItem> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.route_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            DummyContent.DummyItem item = mValues.get(position);
            holder.mItem = item;
            holder.routeNameView.setText(item.name);
            holder.routeGradeView.setText(item.grade);

            if (colors.containsKey(item.getFirstColor())) {
                holder.marker1View.setBackgroundResource(colors.get(item.getFirstColor()));
                holder.marker1View.setVisibility(View.VISIBLE);
            } else {
                holder.marker1View.setVisibility(View.INVISIBLE);
            }

            if (colors.containsKey(item.getSecondColor())) {
                holder.marker2View.setBackgroundResource(colors.get(item.getSecondColor()));
                holder.marker2View.setVisibility(View.VISIBLE);
            } else {
                holder.marker2View.setVisibility(View.INVISIBLE);
            }

            if (colors.containsKey(item.getThirdColor())) {
                holder.marker3View.setBackgroundResource(colors.get(item.getThirdColor()));
                holder.marker3View.setVisibility(View.VISIBLE);
            } else {
                holder.marker3View.setVisibility(View.INVISIBLE);
            }

            if (item.isArchived() || item.isDraft()) {
                holder.routeNameView.setTextColor(getResources().getColor(android.R.color.darker_gray));
            } else {
                holder.routeNameView.setTextColor(getResources().getColor(android.R.color.black));
            }

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(RouteDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        RouteDetailFragment fragment = new RouteDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.route_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, RouteDetailActivity.class);
                        intent.putExtra(RouteDetailFragment.ARG_ITEM_ID, holder.mItem.id);

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView routeNameView;
            public final TextView routeGradeView;
            private final View marker1View;
            private final View marker2View;
            private final View marker3View;
            public DummyContent.DummyItem mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                routeNameView = (TextView) view.findViewById(R.id.route_name);
                routeGradeView = (TextView) view.findViewById(R.id.route_grade);
                marker1View = view.findViewById(R.id.marker1);
                marker2View = view.findViewById(R.id.marker2);
                marker3View = view.findViewById(R.id.marker3);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + routeNameView.getText() + "'";
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.reload_button:
                reloadRoutes();
                break;
            case R.id.filter_button:
                DialogFragment filterDialog = new FilterDialog();
                filterDialog.show(getFragmentManager(), "Lalala");
                break;
            case R.id.settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onFilterPicked(int which) {
        TextView clearFilterDialog = (TextView) this.findViewById(R.id.clear_filter_dialog);
        if (which >= 0 && which < FilterDialog.GRADES.length) {
            clearFilterDialog.setText("×   Filtered on: " + FilterDialog.GRADES[which]);
        }
        if (clearFilterDialog != null)
            clearFilterDialog.setVisibility(View.VISIBLE);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit().putInt("grade", which).commit();
        reloadRoutes();
    }

    public void clearFilters(View view) {
        onFilterPicked(-1);
        view.setVisibility(View.GONE);
    }
}
