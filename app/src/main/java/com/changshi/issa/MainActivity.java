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

        // Initialize UI elements
        btnLogin = findViewById(R.id.btnLogin);
        bottomNavigationView = findViewById(R.id.bottomNavigationMenu);
        welcomeText = findViewById(R.id.txtWelTec);

        // Click on the welcome text to navigate to the HomeActivity
        welcomeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        // Check if the user is already logged in
        SharedPreferences Pref = getSharedPreferences("login_pref", MODE_PRIVATE);
        boolean IsLoggedIn = Pref.getBoolean("is_logged_in", false);

        // If the user is already logged in, hide the login button
        if(IsLoggedIn)
        {
            btnLogin.setVisibility(View.GONE);
        }

        // Click on the login button to navigate to the LoginActivity
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        // Set up bottom navigation bar item click listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.homeBTM:
                        // Click on the Home icon to navigate to HomeActivity
                        Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(homeIntent);
                        return true;
                    case R.id.searchBTM:
                        // Click on the search icon to navigate to SearchFragment
                        Intent searchIntent = new Intent(MainActivity.this, HomeActivity.class);
                        searchIntent.putExtra("fragment", "search");
                        startActivity(searchIntent);
                        return true;
                    case R.id.webBTM:
                        // Click on the campus icon to navigate to WebpageFragment
                        Intent webpageIntent = new Intent(MainActivity.this, HomeActivity.class);
                        webpageIntent.putExtra("fragment", "webpage");
                        startActivity(webpageIntent);
                        return true;
                }
                return false;
            }
        });

        // Hide specific bottom navigation items when users view the app without logging in
        bottomNavigationView.getMenu().findItem(R.id.logoutBTM).setVisible(false);
        bottomNavigationView.getMenu().findItem(R.id.addBTM).setVisible(false);
    }

}
