package com.example.calmacar.driver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.calmacar.common.Auth;
import com.example.calmacar.main.MainActivity;
import com.example.calmacar.common.ProfileFragment;
import com.example.calmacar.R;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    String userType;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    public String getUserType() {
        return userType;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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
                .add(R.id.frag_home, HomeFragment.newInstance("",""))
                .commit();
        navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);

        // Intent Extras
        // TODO remove this
        userType = getIntent().getStringExtra("EXTRA_USERTYPE");

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_home:
                // show home fragment
                // if profile fragment is already loaded, then return
                if (getSupportFragmentManager().findFragmentById(R.id.frag_home) instanceof HomeFragment)
                    break;

                // Load profile fragment
                getSupportFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .replace(R.id.frag_home, HomeFragment.newInstance("",""))
                        .commit();
                break;
            case R.id.nav_trips:
                // show trips fragment
                // if profile fragment is already loaded, then return
                if (getSupportFragmentManager().findFragmentById(R.id.frag_home) instanceof TripsFragment)
                    break;

                // Load profile fragment
                getSupportFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .replace(R.id.frag_home, TripsFragment.newInstance("",""))
                        .commit();
                break;
            case R.id.nav_wallet:
                // show wallet fragment
                // if profile fragment is already loaded, then return
                if (getSupportFragmentManager().findFragmentById(R.id.frag_home) instanceof WalletFragment)
                    break;

                // Load profile fragment
                getSupportFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .replace(R.id.frag_home, WalletFragment.newInstance("",""))
                        .commit();
                break;
            case R.id.nav_profile:
                // show profile fragment
                // if profile fragment is already loaded, then return
                if (getSupportFragmentManager().findFragmentById(R.id.frag_home) instanceof ProfileFragment)
                    break;

                // Load profile fragment
                getSupportFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .replace(R.id.frag_home, ProfileFragment.newInstance("",""))
                        .commit();
                break;
            case R.id.nav_logout:
                // Logout user
                Auth.getInstance().logout();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_add_trip:
                // show add trip fragment
                if (getSupportFragmentManager().findFragmentById(R.id.frag_home) instanceof CreateTripFragment)
                    break;

                // Load profile fragment
                getSupportFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .replace(R.id.frag_home, CreateTripFragment.newInstance("",""))
                        .commit();
                break;
        }

        return true;
    }
}