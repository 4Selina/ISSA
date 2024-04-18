package com.changshi.issa;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.changshi.issa.DatabaseHandler.DBHandler;
import com.changshi.issa.DatabaseHandler.Functions;
import com.changshi.issa.Fragment.FunctionsFragment;
import com.changshi.issa.Fragment.SearchSFragment;
import com.changshi.issa.Fragment.WebpageFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

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
    private DBHandler mDbHandler;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mDbHandler = new DBHandler();

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
                    openFragment(new FunctionsFragment(AllFunctions), "Whitireia & WelTec");
                    return true;
                }
                else if(itemId == R.id.searchBTM){
                    openFragment(new SearchSFragment(), " ");
                    return true;
                }

                else if(itemId == R.id.addBTM)
                {
                    SharedPreferences sharedPref = getSharedPreferences("login_pref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean("is_logged_in", false);
                    editor.apply();

                    Intent intent = new Intent(HomeActivity.this, AddActivity.class);
                    startActivity(intent);
                    finish(); // Close the current activity
                }
                else if(itemId == R.id.webBTM){
                    openFragment(new WebpageFragment(), "WelTec");
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
        openFragment(new FunctionsFragment(AllFunctions), "Whitireia & WelTec");

        if(!IsLoggedIn)
        {
            NavView.getMenu().findItem(R.id.nav_logout).setVisible(false);
            NavView.getMenu().findItem(R.id.nav_login).setVisible(true);

            bottomNavigationView.getMenu().findItem(R.id.logoutBTM).setVisible(false);
            bottomNavigationView.getMenu().findItem(R.id.addBTM).setVisible(true);
        }
        else
        {
            NavView.getMenu().findItem(R.id.nav_logout).setVisible(true);
            NavView.getMenu().findItem(R.id.nav_login).setVisible(false);

            bottomNavigationView.getMenu().findItem(R.id.logoutBTM).setVisible(true);
            bottomNavigationView.getMenu().findItem(R.id.addBTM).setVisible(false);

        }

        // Check if there's an extra "fragment" in the intent
        String fragmentName = getIntent().getStringExtra("fragment");

        if (fragmentName != null)
        {
            switch (fragmentName)
            {
                case "learningSupport":
                    openFragment(new SearchSFragment(), "Learning Support");
                    break;
                case "social":
                    openFragment(new WebpageFragment(), "Social Activities");
                    break;
                case "accommodation":
                    openFragment(new WebpageFragment(), "Accommodation");

                case "transport":
                    openFragment(new WebpageFragment(), "Transport");
                    break;
                case "jobSupport":
                    openFragment(new WebpageFragment(), "Job Support");
                    break;
                case "search":
                    openFragment(new SearchSFragment(), " ");
                    break;
                case "webpage":
                    openFragment(new WebpageFragment(), "WelTec");
                    break;
            }
        } else {
            // If no extra "fragment", open the default fragment
            openFragment(new FunctionsFragment(AllFunctions), "Whitireia & WelTec");
        }

        /*
        String fragmentClassName = getIntent().getStringExtra("fragment_class");
        if (fragmentClassName != null) {
            try {
                Fragment fragment = (Fragment) Class.forName(fragmentClassName).newInstance();
                openFragment(fragment, fragmentClassName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            openFragment(new FunctionsFragment(AllFunctions), "Whitireia & WelTec");
        }
         */
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.nav_home) {
            openFragment(new FunctionsFragment(AllFunctions), "Whitireia & WelTec");
        }
        else if (itemId == R.id.nav_LearningSupport) {
            ArrayList<Functions> LearningSupportFunctions = new ArrayList<>();

            Functions CourseSelectionFunction = new Functions();
            CourseSelectionFunction.setNameOfFunction("Course Selection");
            CourseSelectionFunction.setfuncationImage(R.drawable.learn);
            LearningSupportFunctions.add(CourseSelectionFunction);

            Functions AcademicFunction = new Functions();
            AcademicFunction.setNameOfFunction("Academic Support");
            AcademicFunction.setfuncationImage(R.drawable.learn);
            LearningSupportFunctions.add(AcademicFunction);

            Functions StudentCouncilFunction = new Functions();
            StudentCouncilFunction.setNameOfFunction("Student Council");
            StudentCouncilFunction.setfuncationImage(R.drawable.learn);
            LearningSupportFunctions.add(StudentCouncilFunction);

            Functions HealthFunction = new Functions();
            HealthFunction.setNameOfFunction("Health & Wellbeing");
            HealthFunction.setfuncationImage(R.drawable.learn);
            LearningSupportFunctions.add(HealthFunction);

            openFragment(new FunctionsFragment(LearningSupportFunctions), "Learning Support");

        }
        else if (itemId == R.id.nav_Accommodation) {
            ArrayList<Functions> AccommodationFunctions = new ArrayList<>();

            Functions HomestayFunction = new Functions();
            HomestayFunction.setNameOfFunction("Homestay Information");
            HomestayFunction.setfuncationImage(R.drawable.accommodation);
            AccommodationFunctions.add(HomestayFunction);

            Functions RentalFunction = new Functions();
            RentalFunction.setNameOfFunction("Academic Support");
            RentalFunction.setfuncationImage(R.drawable.accommodation);
            AccommodationFunctions.add(RentalFunction);


            openFragment(new FunctionsFragment(AccommodationFunctions), "Accommodation");

        }
        else if (itemId == R.id.nav_Transport) {
            ArrayList<Functions> TransportsFunctions = new ArrayList<>();

            Functions PublicTranFunction = new Functions();
            PublicTranFunction.setNameOfFunction("Public Transport System");
            PublicTranFunction.setfuncationImage(R.drawable.transit);
            TransportsFunctions.add(PublicTranFunction);

            Functions AirportFunction = new Functions();
            AirportFunction.setNameOfFunction("Airport Express");
            AirportFunction.setfuncationImage(R.drawable.transit);
            TransportsFunctions.add(AirportFunction);

            Functions CampusFunction = new Functions();
            CampusFunction.setNameOfFunction("Campus Transfers");
            CampusFunction.setfuncationImage(R.drawable.transit);
            TransportsFunctions.add(CampusFunction);


            openFragment(new FunctionsFragment(TransportsFunctions), "Transports");

        }
        else if (itemId == R.id.nav_SocialAct) {
            ArrayList<Functions> SocialActFunctions = new ArrayList<>();

            Functions NetworkingFunction = new Functions();
            NetworkingFunction.setNameOfFunction("Networking");
            NetworkingFunction.setfuncationImage(R.drawable.social);
            SocialActFunctions.add(NetworkingFunction);

            Functions SportClubFunction = new Functions();
            SportClubFunction.setNameOfFunction("Sport Club");
            SportClubFunction.setfuncationImage(R.drawable.social);
            SocialActFunctions.add(SportClubFunction);

            Functions FoodOptionsFunction = new Functions();
            FoodOptionsFunction.setNameOfFunction("Food Options");
            FoodOptionsFunction.setfuncationImage(R.drawable.social);
            SocialActFunctions.add(FoodOptionsFunction);

            openFragment(new FunctionsFragment(SocialActFunctions), "Social Activities");

        } else if (itemId == R.id.nav_JobSupport) {
            ArrayList<Functions> JobSupportFunctions = new ArrayList<>();

            Functions PartTimeFunction = new Functions();
            PartTimeFunction.setNameOfFunction("Part-time Job");
            PartTimeFunction.setfuncationImage(R.drawable.jobsupport);
            JobSupportFunctions.add(PartTimeFunction);

            Functions InternshipFunction = new Functions();
            InternshipFunction.setNameOfFunction("Internship");
            InternshipFunction.setfuncationImage(R.drawable.jobsupport);
            JobSupportFunctions.add(InternshipFunction);

            Functions GraduateJobFunction = new Functions();
            GraduateJobFunction.setNameOfFunction("Graduate Job");
            GraduateJobFunction.setfuncationImage(R.drawable.jobsupport);
            JobSupportFunctions.add(GraduateJobFunction);

            openFragment(new FunctionsFragment(JobSupportFunctions), "Job Support");

        }

        else if (itemId == R.id.nav_Webpage) {
            openFragment(new WebpageFragment(), "WelTec");
            return true;
        }

        else if (itemId == R.id.nav_login){
            SharedPreferences sharedPref = getSharedPreferences("login_pref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("is_logged_in", false);
            editor.apply();

            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Close the current activity ;
        }
        else if (itemId == R.id.nav_logout){
            SharedPreferences sharedPref = getSharedPreferences("login_pref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("is_logged_in", false);
            editor.apply();

            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Close the current activity
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }



    private void openHomeActivityWithFragment(Fragment fragment) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("fragment", fragment.getClass().getSimpleName());
        startActivity(intent);
        finish();
    }


    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }
    public void openFragment(Fragment fragment, String title){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
        getSupportActionBar().setTitle(title); //display fragment title on the toolbar

    }

}
