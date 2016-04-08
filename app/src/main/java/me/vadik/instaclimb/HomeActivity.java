package me.vadik.instaclimb;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import me.vadik.instaclimb.routes.ContactsFragment;
import me.vadik.instaclimb.routes.FilterDialog;
import me.vadik.instaclimb.routes.GymFragment;
import me.vadik.instaclimb.routes.Sector2Fragment;
import me.vadik.instaclimb.routes.SectorActivity;
import me.vadik.instaclimb.routes.SectorFragment;
import me.vadik.instaclimb.routes.SettingsActivity;

public class HomeActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        GymFragment.OnFragmentInteractionListener,
        FilterDialog.OnFilterPickedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private static Map<Integer, Integer> GYM_IDS;

    static {
        GYM_IDS = new HashMap<>();
        GYM_IDS.put(R.id.nav_skalatoria, 1);
        GYM_IDS.put(R.id.nav_bigwall, 2);
        GYM_IDS.put(R.id.nav_rockzona, 4);
        GYM_IDS.put(R.id.nav_cherepaha, 5);
        GYM_IDS.put(R.id.nav_mgtu, 6);
        GYM_IDS.put(R.id.nav_x8, 7);
        GYM_IDS.put(R.id.nav_tramontana, 9);
        GYM_IDS.put(R.id.nav_atmosfera, 12);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Integer itemId = item.getItemId();

        Integer gymId = null;
        String gymName = null;

        if (GYM_IDS.containsKey(itemId)) {
            gymId = GYM_IDS.get(itemId);
            gymName = item.getTitle().toString();
        }

        switch (itemId) {
            case R.id.contacts_nav:
                Fragment ex111 = new ContactsFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.gym_fragment_container, ex111).commit();
                break;
            case R.id.sector_fragment_list:
                Fragment ex11 = new Sector2Fragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.gym_fragment_container, ex11).commit();
                break;
            case R.id.temprorary_route_list_activity_item:
                startActivity(new Intent(this, SectorActivity.class));
                break;
            case R.id.temprorary_route_list_fragment_item:
                Fragment example5 = new SectorFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.gym_fragment_container, example5).commit();
                break;
            case R.id.nav_all_routes:
                gymId = GymFragment.ALL_GYMS;
                gymName = item.getTitle().toString();
            case R.id.nav_skalatoria:
            case R.id.nav_bigwall:
            case R.id.nav_rockzona:
            case R.id.nav_cherepaha:
            case R.id.nav_mgtu:
            case R.id.nav_x8:
            case R.id.nav_tramontana:
            case R.id.nav_atmosfera:
                if (gymId != null) {
                    Fragment gymFragment = GymFragment.newInstance(gymId, gymName);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.gym_fragment_container, gymFragment).commit();
                }
                break;
            case R.id.nav_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.nav_send:
            case R.id.nav_share:
            default:
                Toast.makeText(this, "Sorry, not implemented yet", Toast.LENGTH_SHORT).show();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFilterPicked(int which) {
        TextView clearFilterDialog = (TextView) this.findViewById(R.id.clear_filter_dialog);
        String[] grades = getResources().getStringArray(R.array.grades);
        if (which >= 0 && which < grades.length) {
            clearFilterDialog.setText("×   Filtered on: " + grades[which]);
        }
        if (clearFilterDialog != null)
            clearFilterDialog.setVisibility(View.VISIBLE);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit().putInt("grade", which).commit();
        getSupportFragmentManager().findFragmentByTag("");
//        reloadRoutes(); //TODO
    }

    public void clearFilters(View view) {
        onFilterPicked(-1);
        view.setVisibility(View.GONE);
    }
}
