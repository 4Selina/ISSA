package com.changshi.issa;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.changshi.issa.DatabaseHandler.Functions;
import com.changshi.issa.Fragment.AccommodationFragment;
import com.changshi.issa.Fragment.FunctionsFragment;
import com.changshi.issa.Fragment.JobFragment;
import com.changshi.issa.Fragment.LearningFragment;
import com.changshi.issa.Fragment.SearchSFragment;
import com.changshi.issa.Fragment.SocialActFragment;
import com.changshi.issa.Fragment.TransportFragment;
import com.changshi.issa.Fragment.WebpageFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.function.Function;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private BottomNavigationView bottomNavigationView;
    public FragmentManager fragmentManager;
    private Toolbar toolbar;

    private NavigationView NavView;

    SharedPreferences Pref;
    SharedPreferences.Editor PrefEditor;

    public boolean IsLoggedIn;

    private ArrayList<Functions> AllFunctions;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        AllFunctions = new ArrayList<>();
        Functions LearningSupportFunction = new Functions();

        LearningSupportFunction.setNameOfFunction("Learning Support");
        LearningSupportFunction.setfuncationImage(R.drawable.learn);

        Functions SocialActivitiesFunction = new Functions();

        SocialActivitiesFunction.setNameOfFunction("Social Activities");
        SocialActivitiesFunction.setfuncationImage(R.drawable.social);

        Functions AccommodationFunction = new Functions();

        AccommodationFunction.setNameOfFunction("Accommodation");
        AccommodationFunction.setfuncationImage(R.drawable.accommodation);

        Functions TransportsFunction = new Functions();

        TransportsFunction.setNameOfFunction("Transports");
        TransportsFunction.setfuncationImage(R.drawable.transit);

        Functions JobSupportFunction = new Functions();

        JobSupportFunction.setNameOfFunction("Job Support");
        JobSupportFunction.setfuncationImage(R.drawable.jobsupport);

        AllFunctions.add(LearningSupportFunction);
        AllFunctions.add(SocialActivitiesFunction);
        AllFunctions.add(AccommodationFunction);
        AllFunctions.add(TransportsFunction);
        AllFunctions.add(JobSupportFunction);

        Pref = getSharedPreferences("login_pref", MODE_PRIVATE);
        IsLoggedIn = Pref.getBoolean("is_logged_in", false);

        NavView = findViewById(R.id.navigationDrawer);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigationDrawer);
        navigationView.setNavigationItemSelectedListener(this);

        bottomNavigationView = findViewById(R.id.bottomNavigationMenu);
        bottomNavigationView.setBackground(null);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if(itemId == R.id.homeBTM){
                    openFragment(new FunctionsFragment(AllFunctions));
                    return true;
                }
                else if(itemId == R.id.searchBTM){
                    openFragment(new SearchSFragment());
                    return true;
                }
                else if(itemId == R.id.addBTM){
                    SharedPreferences sharedPref = getSharedPreferences("login_pref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean("is_logged_in", false);
                    editor.apply();

                    Intent intent = new Intent(HomeActivity.this, AddActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if(itemId == R.id.webBTM){
                    openFragment(new WebpageFragment());
                    return true;
                }
                else if(itemId == R.id.logoutBTM)
                {
                    SharedPreferences sharedPref = getSharedPreferences("login_pref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean("is_logged_in", false);
                    editor.apply();

                    Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish(); // Close the current activity
                }
                return false;

            }
        });

        fragmentManager = getSupportFragmentManager();
        openFragment(new FunctionsFragment(AllFunctions));

        if(!IsLoggedIn)
        {
            NavView.getMenu().findItem(R.id.nav_logout).setVisible(false);
            NavView.getMenu().findItem(R.id.nav_login).setVisible(true);

            bottomNavigationView.getMenu().findItem(R.id.logoutBTM).setVisible(false);
            bottomNavigationView.getMenu().findItem(R.id.addBTM).setVisible(false);
        }
        else
        {
            NavView.getMenu().findItem(R.id.nav_logout).setVisible(true);
            NavView.getMenu().findItem(R.id.nav_login).setVisible(false);

            bottomNavigationView.getMenu().findItem(R.id.logoutBTM).setVisible(true);
            bottomNavigationView.getMenu().findItem(R.id.addBTM).setVisible(true);
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.nav_home){
            openFragment(new FunctionsFragment(AllFunctions));
        }
        else if (itemId == R.id.nav_LearningSupport){
            openFragment(new LearningFragment());
        }
        else if (itemId == R.id.nav_Accommodation){
            openFragment(new AccommodationFragment());
        }
        else if (itemId == R.id.nav_JobSupport){
            openFragment(new JobFragment());
        }
        else if (itemId == R.id.nav_SocialAct){
            openFragment(new SocialActFragment());
        }
        else if (itemId == R.id.nav_Transport){
            openFragment(new TransportFragment());
        }
        else if (itemId == R.id.nav_Webpage){
            openFragment(new WebpageFragment());
        }
        else if(itemId == R.id.nav_logout)
        {
            SharedPreferences sharedPref = getSharedPreferences("login_pref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("is_logged_in", false);
            editor.apply();

            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Close the current activity
        }
        else if(itemId == R.id.nav_login)
        {
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Close the current activity
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }
    public void openFragment(Fragment fragment){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

}
