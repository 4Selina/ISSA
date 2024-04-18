package com.changshi.issa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.changshi.issa.Fragment.FunctionsFragment;
import com.changshi.issa.Fragment.SearchSFragment;
import com.changshi.issa.Fragment.TransportFragment;
import com.changshi.issa.Fragment.WebpageFragment;
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