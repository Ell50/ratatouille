package com.example.ratatouille.views;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.ratatouille.MainActivity;
import com.example.ratatouille.R;
import com.example.ratatouille.utils.Utils;
import com.example.ratatouille.vars.VariablesUsed;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import static maes.tech.intentanim.CustomIntent.customType;

public class customerView extends AppCompatActivity {

    ImageView navbar_home;
    ImageView navbar_profile;
    ImageView navbar_favourite;
    ImageView navbar_home_i;
    ImageView navbar_profile_i;
    ImageView navbar_favourite_i;
    Fragment selectedFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_customer);
        customType(customerView.this, "fadein-to-fadeout");

        // Set the default view in fragment
        selectedFragment = new customerHomeFragment();
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fadein, R.anim.fadeout).replace(R.id.fragment_container, selectedFragment).commit();

        Utils.showDialogMessage(R.drawable.verified_logo, this, "Success Log In", "Welcome back! " + VariablesUsed.currentUser.getName());

        navbar_home = findViewById(R.id.navbar_home);
        navbar_profile = findViewById(R.id.navbar_profile);
        navbar_favourite = findViewById(R.id.navbar_favourite);

        navbar_home_i = findViewById(R.id.navbar_home_i);
        navbar_profile_i = findViewById(R.id.navbar_profile_i);
        navbar_favourite_i = findViewById(R.id.navbar_favourite_i);

        navbar_home_i.setVisibility(View.VISIBLE);
        navbar_profile_i.setVisibility(View.INVISIBLE);
        navbar_favourite_i.setVisibility(View.INVISIBLE);

        navbar_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navbar_home_i.setVisibility(View.VISIBLE);
                navbar_profile_i.setVisibility(View.INVISIBLE);
                navbar_favourite_i.setVisibility(View.INVISIBLE);

                selectedFragment = new customerHomeFragment();
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fadein, R.anim.fadeout).replace(R.id.fragment_container, selectedFragment).commit();
            }
        });

        navbar_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navbar_home_i.setVisibility(View.INVISIBLE);
                navbar_profile_i.setVisibility(View.VISIBLE);
                navbar_favourite_i.setVisibility(View.INVISIBLE);

                selectedFragment = new ViewProfileFragment();
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fadein, R.anim.fadeout).replace(R.id.fragment_container, selectedFragment).commit();
            }
        });

        navbar_favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navbar_home_i.setVisibility(View.INVISIBLE);
                navbar_profile_i.setVisibility(View.INVISIBLE);
                navbar_favourite_i.setVisibility(View.VISIBLE);

                selectedFragment = new customerFavourite();
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fadein, R.anim.fadeout).replace(R.id.fragment_container, selectedFragment).commit();
            }
        });
    }


}
