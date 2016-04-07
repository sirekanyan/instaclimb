package me.vadik.instaclimb;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import me.vadik.instaclimb.routes.GymFragment;
import me.vadik.instaclimb.routes.RouteListActivity;
import me.vadik.instaclimb.routes.RouteListFragment;
import me.vadik.instaclimb.routes.SettingsActivity;
import me.vadik.instaclimb.routes.example.MainActivity;
import me.vadik.instaclimb.routes.example.MainFragment;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GymFragment.OnFragmentInteractionListener {

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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

        if (GYM_IDS.containsKey(itemId)) {
            gymId = GYM_IDS.get(itemId);
        }

        switch (itemId) {
            case R.id.temprorary_tabs_activity_item:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.temprorary_tabs_fragment_item:
                Fragment example2 = new MainFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.gym_fragment_container, example2).commit();
                break;
            case R.id.temprorary_route_list_activity_item:
                startActivity(new Intent(this, RouteListActivity.class));
                break;
            case R.id.temprorary_route_list_fragment_item:
                Fragment example5 = new RouteListFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.gym_fragment_container, example5).commit();
                break;
            case R.id.nav_all_gyms:
            case R.id.nav_skalatoria:
            case R.id.nav_bigwall:
            case R.id.nav_rockzona:
            case R.id.nav_cherepaha:
            case R.id.nav_mgtu:
            case R.id.nav_x8:
            case R.id.nav_tramontana:
            case R.id.nav_atmosfera:
                if (gymId != null) {
                    Fragment gymFragment = GymFragment.newInstance(gymId);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.gym_fragment_container, gymFragment).commit();
                }
//                Fragment example = new RouteListFragment();
//                Bundle b = new Bundle();
//                b.putString("content", item.getTitle().toString());
//                b.putInt("menuItemId", item.getItemId());
//                example.setArguments(b);
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.gym_fragment_container, example).commit();
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
}
