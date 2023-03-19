package com.example.calmacar.passenger.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;

import com.example.calmacar.R;
import com.example.calmacar.main.model.AuthManager;
import com.example.calmacar.common.view.ProfileFragment;
import com.example.calmacar.main.view.MainActivity;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_home);

        // Hooks
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        // Toolbar
        setSupportActionBar(toolbar);

        // Navigation Drawer Menu
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open_nav_drawer, R.string.close_nav_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        // Initialize first fragment
        getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .add(R.id.frag_container, TripsSearchFragment.newInstance("",""))
                .commit();
        navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                // show home fragment
                // if home fragment is already loaded, then return
                if (getSupportFragmentManager().findFragmentById(R.id.frag_container) instanceof TripsSearchFragment)
                    break;

                // Load home fragment
                getSupportFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .replace(R.id.frag_container, TripsSearchFragment.newInstance("", ""))
                        .commit();
                break;
            case R.id.nav_tripsHistory:
                // if History fragment is already loaded, then return
                if (getSupportFragmentManager().findFragmentById(R.id.frag_container) instanceof ReservationsHistoryFragment)
                    break;

                // Load History fragment
                getSupportFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .replace(R.id.frag_container, ReservationsHistoryFragment.newInstance("", ""))
                        .commit();
                break;
            case R.id.nav_profile:
                // show profile fragment
                // if profile fragment is already loaded, then return
                if (getSupportFragmentManager().findFragmentById(R.id.frag_container) instanceof ProfileFragment)
                    break;

                // Load profile fragment
                getSupportFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .replace(R.id.frag_container, ProfileFragment.newInstance("", ""))
                        .commit();
                break;
            case R.id.nav_logout:
                // Logout user
                AuthManager.getInstance().logout();
                Intent intentLogout = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intentLogout);
                finish();
                break;
            case R.id.nav_driver:
                // Switch to driver app
                Intent intentPassenger = new Intent(this, com.example.calmacar.driver.view.HomeActivity.class);
                startActivity(intentPassenger);
                finish();
                break;
        }

        // close the nav drawer
        drawerLayout.closeDrawer(Gravity.LEFT, true);
        return true;
    }
}