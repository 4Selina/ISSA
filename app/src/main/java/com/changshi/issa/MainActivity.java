package com.changshi.issa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private Button btnLogin;
    private BottomNavigationView bottomNavigationView;

    private TextView welcomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = findViewById(R.id.btnLogin);
        bottomNavigationView = findViewById(R.id.bottomNavigationMenu);
        welcomeText = findViewById(R.id.txtWelTec);
        //Click the screen to the home page
        welcomeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        SharedPreferences Pref = getSharedPreferences("login_pref", MODE_PRIVATE);
        boolean IsLoggedIn = Pref.getBoolean("is_logged_in", false);

        if(IsLoggedIn)
        {
            btnLogin.setVisibility(View.GONE);
        }

        //admin can login for managing the contents
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        // bottom navigation bar is clickable
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.homeBTM:
                        // Click Home icon to HomeActivity
                        Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(homeIntent);
                        return true;
                    case R.id.searchBTM:
                        // Click add icon to SearchFragment
                        Intent searchIntent = new Intent(MainActivity.this, HomeActivity.class);
                        searchIntent.putExtra("fragment", "search");
                        startActivity(searchIntent);
                        return true;

                    case R.id.webBTM:
                        // Click campus icon to webpageFragment
                        Intent webpageIntent = new Intent(MainActivity.this, HomeActivity.class);
                        webpageIntent.putExtra("fragment", "webpage");
                        startActivity(webpageIntent);
                        return true;
                }
                return false;
            }

//            private void displayFragment(Fragment fragment) {
//                // Hide the welcome logo and text
//                findViewById(R.id.imgWelcomeLogo).setVisibility(View.GONE);
//                findViewById(R.id.txtWelTec).setVisibility(View.GONE);
//                findViewById(R.id.txtWelcome).setVisibility(View.GONE);
//
//                // Replace the fragment
//                getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.fragmentContainer, fragment)
//                        .commit();
//            }
        });

        //hide the navigation items when users view the app without login
        bottomNavigationView.getMenu().findItem(R.id.logoutBTM).setVisible(false);
        bottomNavigationView.getMenu().findItem(R.id.addBTM).setVisible(false);
    }

}