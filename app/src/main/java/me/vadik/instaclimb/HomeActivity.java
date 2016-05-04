package me.vadik.instaclimb;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.HashMap;
import java.util.Map;

import me.vadik.instaclimb.android.MyAppCompatActivity;
import me.vadik.instaclimb.provider.RoutesProvider;
import me.vadik.instaclimb.routes.GymFragment;
import me.vadik.instaclimb.routes.VolleySingleton;
import me.vadik.instaclimb.settings.SettingsActivity;
import me.vadik.instaclimb.users.UserActivity;
import me.vadik.instaclimb.users.UserHelper;

public class HomeActivity extends MyAppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        GymFragment.OnFragmentInteractionListener {

    private String gymName;

    public static final String AUTHORITY = "me.vadik.instaclimb.routes.provider";
    public static final String ACCOUNT_TYPE = "vadik.me";
    public static final String ACCOUNT = "dummyaccount";
    private Account mAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Context context = this;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, R.string.adding_route_is_under_construction, Toast.LENGTH_LONG).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            setTitle(R.string.all_routes_title);
            Fragment gymFragment = GymFragment.newInstance(GymFragment.ALL_GYMS, getTitle().toString());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.gym_fragment_container, gymFragment).commit();
        } else {
            if (savedInstanceState.getString("gymname") != null) {
                setTitle(savedInstanceState.getString("gymname"));
            }
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        View navViewHeader = navigationView.getHeaderView(0);

        NetworkImageView navHeaderImageView = (NetworkImageView) navViewHeader.findViewById(R.id.navHeaderImageView);
        if (navHeaderImageView != null) {
            ImageLoader mImageLoader = VolleySingleton.getInstance(this).getImageLoader();
            navHeaderImageView.setDefaultImageResId(R.drawable.blackface);
            String userId = preferences.getString("user_id", null);
            if (userId == null) {
                navHeaderImageView.setImageUrl(null, mImageLoader);
            } else {
                navHeaderImageView.setImageUrl("https://vadik.me/userpic/" + userId + ".jpg", mImageLoader);
            }
        } else {
            Log.e("me", "not found navHeaderImageView");
        }

        TextView caption = (TextView) navViewHeader.findViewById(R.id.caption);
        if (caption != null) {
            caption.setText(preferences.getString("user_name", "Пупкин"));
        } else {
            Log.e("me", "not found caption");
        }

        TextView email = (TextView) navViewHeader.findViewById(R.id.textView);
        if (email != null) {
            int count = preferences.getInt("user_climbed", 0);
            email.setText(getResources().getQuantityString(R.plurals.number_of_climbed_routes, count, count));
        } else {
            Log.e("me", "not found caption");
        }

        mAccount = CreateSyncAccount(this);

        int syncFrequency = Integer.valueOf(preferences.getString("sync_frequency", "3600"));

        if (syncFrequency > 0) {

            Log.e("me", "sync frequency is " + syncFrequency + " seconds");

            ContentResolver.addPeriodicSync(
                    mAccount,
                    AUTHORITY,
                    Bundle.EMPTY,
                    syncFrequency);
        }

        String userId = preferences.getString("user_id", null);

        if (userId != null) {
            // TODO don't do it in the main thread!
            RoutesProvider.clearClimbedRoutes(this);
            int c1 = RoutesProvider.prepareRoutesForUser(this, userId);
            int c2 = RoutesProvider.prepareFlashRoutesForUser(this, userId);
            String count = String.valueOf(c1 + c2);
            String flashCount = String.valueOf(c2);
            Toast.makeText(this, "You've done " + count + " routes " +
                            "(" + flashCount + " flashes)",
                    Toast.LENGTH_LONG)
                    .show();
        }
    }

    public static Account CreateSyncAccount(Context context) {
        // Create the account type and default account
        Account newAccount = new Account(ACCOUNT, ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager = (AccountManager) context.getSystemService(ACCOUNT_SERVICE);
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call context.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
        } else {
            /*
             * The account exists or some other error occurred. Log this, report it,
             * or handle it internally.
             */
            Log.e("me", "cannot add new account");
        }
        return newAccount;
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("gymname", gymName);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        gymName = savedInstanceState.getString("gymname");
        super.onRestoreInstanceState(savedInstanceState);
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
        gymName = null;

        if (GYM_IDS.containsKey(itemId)) {
            gymId = GYM_IDS.get(itemId);
            gymName = item.getTitle().toString();
        }

        switch (itemId) {
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
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:instaclimb@vadik.me"));
                intent.putExtra(Intent.EXTRA_EMAIL, "instaclimb@vadik.me");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Instaclimb for Android Feedback");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
                break;
            case R.id.nav_share:
            default:
                Toast.makeText(this, R.string.not_implemented_yet, Toast.LENGTH_SHORT).show();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                this.startActivity(new Intent(this, SettingsActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void gotoMyProfile(View view) {
        Intent intent = new Intent(this, UserActivity.class);
        String userId = UserHelper.getCurrentUserId(this);
        if (userId != null) {
            intent.putExtra(UserActivity.ARG_USER_ID, Integer.parseInt(userId));
            startActivity(intent);
        }
    }
}
