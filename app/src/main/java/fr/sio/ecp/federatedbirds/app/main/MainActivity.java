package fr.sio.ecp.federatedbirds.app.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import fr.sio.ecp.federatedbirds.R;
import fr.sio.ecp.federatedbirds.app.settings.SettingsActivity;
import fr.sio.ecp.federatedbirds.app.users.UsersFollowedFragment;
import fr.sio.ecp.federatedbirds.app.home.HomeFragment;
import fr.sio.ecp.federatedbirds.app.login.LoginActivity;
import fr.sio.ecp.federatedbirds.app.users.UsersFollowersFragment;
import fr.sio.ecp.federatedbirds.auth.TokenManager;

public class MainActivity extends AppCompatActivity {

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkUserLogin();

        setContentView(R.layout.activity_main);

        final NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        Fragment fragment = new HomeFragment();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_container, fragment)
                                .commit();
                        ((DrawerLayout) findViewById(R.id.drawer)).closeDrawer(navigationView);
                        return true;
                    case R.id.followed:
                        fragment = new UsersFollowedFragment();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_container, fragment)
                                .commit();
                        ((DrawerLayout) findViewById(R.id.drawer)).closeDrawer(navigationView);
                        return true;
                    case R.id.followers:
                        fragment = new UsersFollowersFragment();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_container, fragment)
                                .commit();
                        ((DrawerLayout) findViewById(R.id.drawer)).closeDrawer(navigationView);
                        return true;
                    case R.id.settings:
                        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                        startActivity(intent);
                        ((DrawerLayout) findViewById(R.id.drawer)).closeDrawer(navigationView);
                        return true;
                }
                ((DrawerLayout) findViewById(R.id.drawer)).closeDrawer(navigationView);
                return false;
            }

        });

        if (savedInstanceState == null) {
            HomeFragment fragment = new HomeFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_container, fragment)
                    .commit();
        }

    }

    @Override
    public void setSupportActionBar(Toolbar toolbar) {
        super.setSupportActionBar(toolbar);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                (DrawerLayout) findViewById(R.id.drawer),
                toolbar,
                R.string.open_menu,
                R.string.close_menu
        );
        mDrawerToggle.syncState();
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        mDrawerToggle.syncState();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        checkUserLogin();
    }

    private void checkUserLogin() {
        if (TokenManager.getUserToken(this) == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

}
