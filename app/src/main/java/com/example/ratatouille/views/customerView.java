package com.example.ratatouille.views;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.ratatouille.R;
import com.example.ratatouille.vars.VariablesUsed;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class customerView extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Fragment selectedFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        // Set the default view in fragment
        selectedFragment = new customerHomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

        Toast.makeText(customerView.this, "Welcome back!" + VariablesUsed.currentUser.getUsername(), Toast.LENGTH_LONG);

        bottomNavigationView = findViewById(R.id.bottom_nav);

        bottomNavigationView.setOnNavigationItemSelectedListener(
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    if (item.getItemId() == R.id.nav_profile) {
                        selectedFragment = new ViewProfileFragment();
                    }

                    if (item.getItemId() == R.id.nav_home) {
                        selectedFragment = new customerHomeFragment();
                    }

                    if (item.getItemId() == R.id.nav_favourites) {
                        selectedFragment = new customerFavourite();
                    }

                    if(selectedFragment != null) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    }

                    return true;
                }
            }
        );
    }
}
