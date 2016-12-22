package me.vadik.instaclimb.view;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import me.vadik.instaclimb.R;
import me.vadik.instaclimb.databinding.HomeActivityBinding;
import me.vadik.instaclimb.databinding.NavHeaderHomeBinding;
import me.vadik.instaclimb.login.InstaclimbLogin;
import me.vadik.instaclimb.login.LoginManager;
import me.vadik.instaclimb.login.UserSession;
import me.vadik.instaclimb.provider.UserProvider;
import me.vadik.instaclimb.view.custom.MyAppCompatActivity;

public class HomeActivity extends MyAppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        GymFragment.OnFragmentInteractionListener,
        InstaclimbLogin.OnPostExecuteListener {

    private static final int GET_SESSION_ID_REQUEST = 0;
    private String gymName;

    public static final String AUTHORITY = "me.vadik.instaclimb.routes.provider";
    public static final String ACCOUNT_TYPE = "vadik.me";
    public static final String ACCOUNT = "default account";
    private HomeActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.home_activity);
        Toolbar toolbar = binding.incAppBarHome.toolbar;
        setSupportActionBar(toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = binding.navView;
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateNavHeader();
    }

    private void updateNavHeader() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        NavHeaderHomeBinding headerBinding = DataBindingUtil.bind(binding.navView.getHeaderView(0));

        if (!LoginManager.isLoggedIn(this)) {
            TextView userCaption = headerBinding.caption;
            userCaption.setVisibility(View.VISIBLE);
            userCaption.setText("Sign in");
        } else {
            ImageView navHeaderImageView = headerBinding.navHeaderImageView;
            int userId = LoginManager.getUserId(this);
            Picasso.with(this).load("https://vadik.me/userpic/" + userId + ".jpg").placeholder(R.drawable.blackface).into(navHeaderImageView);
            // TODO remove hardcoded url

            String userName = UserProvider.getUserName(this, userId);

            if (userName == null || userName.isEmpty()) {
                userName = "Unknown user (" + userId + ")";
            }

            TextView userCaption = headerBinding.caption;
            userCaption.setVisibility(View.VISIBLE);
            userCaption.setText(userName);

            TextView emailCaption = headerBinding.textView;
            emailCaption.setVisibility(View.VISIBLE);
            int count = preferences.getInt("user_climbed", 0); // TODO this property is not exist anymore: always will return 0
            emailCaption.setText(getResources().getQuantityString(R.plurals.number_of_climbed_routes, count, count));

            Account mAccount = createSyncAccount(this);

            int syncFrequency = Integer.valueOf(preferences.getString("sync_frequency", "3600"));

            if (syncFrequency > 0) {

                Log.e("me", "sync frequency is " + syncFrequency + " seconds");

                ContentResolver.addPeriodicSync(
                        mAccount,
                        AUTHORITY,
                        Bundle.EMPTY,
                        syncFrequency);
            }

        /*
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
        */
        }
    }

    public static Account createSyncAccount(Context context) {
        // Create the account type and default account
        Account newAccount = new Account(ACCOUNT, ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager = (AccountManager) context.getSystemService(ACCOUNT_SERVICE);
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        //noinspection StatementWithEmptyBody
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

//        String userId = UserHelper.getCurrentUserId(this);
//        boolean loggedIn = !TextUtils.isEmpty(userId);

        switch (itemId) {
//            case R.id.nav_news:
//                Fragment newsFragment = NewsFragment.newInstance(loggedIn);
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.gym_fragment_container, newsFragment).commit();
//                break;
            case R.id.nav_gyms:
                Fragment gymsFragment = GymsFragment.newInstance();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.gym_fragment_container, gymsFragment).commit();
                break;
//            case R.id.nav_climbers:
//                Fragment usersFragment = UsersFragment.newInstance();
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.gym_fragment_container, usersFragment).commit();
//                break;
            case R.id.nav_rating:
                Fragment ratingsFragment = RatingsFragment.newInstance();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.gym_fragment_container, ratingsFragment).commit();
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
        MenuItem nightMode = menu.findItem(R.id.night_mode);
        nightMode.setChecked(preferences.isDark());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                this.startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.night_mode:
                preferences.applyDark();
                this.refreshActivity();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void refreshActivity() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
                HomeActivity.this.finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                startActivity(intent);
            }
        });
    }

    public void gotoMyProfile(View view) {
        if (LoginManager.isLoggedIn(this)) {
            if (preferences.hasSavedCredentials()) {
                new InstaclimbLogin(this).execute(preferences.getCredentials());
            }
            UserSession session = LoginManager.getSession(this);
            Intent intent = new Intent(this, UserActivity.class);
            intent.putExtra(UserActivity.ARG_USER_ID, session.getUserId());
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, GET_SESSION_ID_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GET_SESSION_ID_REQUEST) {
            if (resultCode != RESULT_OK) {
                Toast.makeText(this, R.string.cannot_log_in, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onPostExecute() {

    }

    @Override
    public void onSuccessLogin(UserSession session) {

    }

    @Override
    public void onErrorLogin(Exception ex) {

    }
}
