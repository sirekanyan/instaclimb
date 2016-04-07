package me.vadik.instaclimb.routes;

import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import me.vadik.instaclimb.R;
import me.vadik.instaclimb.routes.dummy.DummyContent;
import me.vadik.instaclimb.routes.dummy.DummyItemsHelper;

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
        List<String> statusFilterArgs = SimpleItemRecyclerViewHelper.getStatusFilterArgs(this);
        List<String> gradeFilterArgs = SimpleItemRecyclerViewHelper.getGradeFilterArgs(this);

        String[] statusFilterArgsArray = statusFilterArgs.toArray(new String[statusFilterArgs.size()]);
        String[] gradeFilterArgsArray = gradeFilterArgs.toArray(new String[gradeFilterArgs.size()]);

        List<DummyContent.DummyItem> dummyItems = DummyItemsHelper.getDummyItems(this, statusFilterArgsArray, gradeFilterArgsArray, -1);
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, getSupportFragmentManager(), dummyItems, mTwoPane));
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
