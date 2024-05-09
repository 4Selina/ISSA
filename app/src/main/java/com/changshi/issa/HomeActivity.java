package com.changshi.issa;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.changshi.issa.DatabaseHandler.DBHandler;
import com.changshi.issa.DatabaseHandler.Details;
import com.changshi.issa.DatabaseHandler.Functions;
import com.changshi.issa.DatabaseHandler.SectionDetails;
import com.changshi.issa.DatabaseHandler.Supports;
import com.changshi.issa.Fragment.FunctionsFragment;
import com.changshi.issa.Fragment.SearchSFragment;
import com.changshi.issa.Fragment.SupportsFragment;
import com.changshi.issa.Fragment.WebpageFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.common.base.Strings;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

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

    private String ParentCategory;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        mDbHandler = new DBHandler(this);

        AllFunctions = new ArrayList<>();

        Functions LearningSupportFunction = new Functions();
        Functions SocialActivitiesFunction = new Functions();
        Functions AccommodationFunction = new Functions();
        Functions TransportsFunction = new Functions();
        Functions JobSupportFunction = new Functions();

        db.collection("Settings")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        for(DocumentSnapshot SelectedSnapshot : task.getResult().getDocuments())
                        {
                            if(SelectedSnapshot.contains("AccommodationUrl"))
                            {
                                if(!Strings.isNullOrEmpty(SelectedSnapshot.get("LearningSupportUrl").toString()))
                                    LearningSupportFunction.setFunctionURL(SelectedSnapshot.get("LearningSupportUrl").toString());

                                if(!Strings.isNullOrEmpty(SelectedSnapshot.get("SocialActivitiesUrl").toString()))
                                    SocialActivitiesFunction.setFunctionURL(SelectedSnapshot.get("SocialActivitiesUrl").toString());

                                if(!Strings.isNullOrEmpty(SelectedSnapshot.get("AccommodationUrl").toString()))
                                    AccommodationFunction.setFunctionURL(SelectedSnapshot.get("AccommodationUrl").toString());

                                if(!Strings.isNullOrEmpty(SelectedSnapshot.get("TransportUrl").toString()))
                                    TransportsFunction.setFunctionURL(SelectedSnapshot.get("TransportUrl").toString());

                                if(!Strings.isNullOrEmpty(SelectedSnapshot.get("JobSupportUrl").toString()))
                                    JobSupportFunction.setFunctionURL(SelectedSnapshot.get("JobSupportUrl").toString());
                            }
                        }
                    }
                });

        LearningSupportFunction.setNameOfFunction("Learning Support");
        LearningSupportFunction.setFunctionImage(R.drawable.learn);

        SocialActivitiesFunction.setNameOfFunction("Social Activities");
        SocialActivitiesFunction.setFunctionImage(R.drawable.social);

        AccommodationFunction.setNameOfFunction("Accommodation");
        AccommodationFunction.setFunctionImage(R.drawable.accommodation);

        TransportsFunction.setNameOfFunction("Transports");
        TransportsFunction.setFunctionImage(R.drawable.transit);

        JobSupportFunction.setNameOfFunction("Job Support");
        JobSupportFunction.setFunctionImage(R.drawable.jobsupport);

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

        //Bottom navigation
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
                    Intent intent = new Intent(HomeActivity.this, AddActivity.class);
                    startActivity(intent);
                    return true;
                }
                else if(itemId == R.id.webBTM)
                {
                    openFragment(new WebpageFragment(new ArrayList<>(), HomeActivity.this, FirebaseFirestore.getInstance()), "WelTec");
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

        //hide the navigation items when users view the app without login
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
                    openFragment(new WebpageFragment(new ArrayList<>(), HomeActivity.this, FirebaseFirestore.getInstance()), "Social Activities");
                    break;
                case "accommodation":
                    openFragment(new WebpageFragment(new ArrayList<>(), HomeActivity.this, FirebaseFirestore.getInstance()), "Accommodation");

                case "transport":
                    openFragment(new WebpageFragment(new ArrayList<>(), HomeActivity.this, FirebaseFirestore.getInstance()), "Transport");
                    break;
                case "jobSupport":
                    openFragment(new WebpageFragment(new ArrayList<>(), HomeActivity.this, FirebaseFirestore.getInstance()), "Job Support");
                    break;
                case "search":
                    openFragment(new SearchSFragment(), " ");
                    break;
                case "webpage":
                    openFragment(new WebpageFragment(new ArrayList<>(), HomeActivity.this, FirebaseFirestore.getInstance()), "WelTec");
                    break;
            }
        } else {
            // If no extra "fragment", open the default fragment
            openFragment(new FunctionsFragment(AllFunctions), "Whitireia & WelTec");
        }

        db.collection("Settings")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        for(DocumentSnapshot SelectedSnapshot : task.getResult().getDocuments())
                        {
                            if(SelectedSnapshot.contains("AccommodationUrl"))
                            {
                                if(!Strings.isNullOrEmpty(SelectedSnapshot.get("LearningSupportUrl").toString()))
                                {
                                    LearningSupportFunction.setFunctionURL(SelectedSnapshot.get("LearningSupportUrl").toString());
                                }
                                if(!Strings.isNullOrEmpty(SelectedSnapshot.get("SocialActivitiesUrl").toString()))
                                    SocialActivitiesFunction.setFunctionURL(SelectedSnapshot.get("SocialActivitiesUrl").toString());

                                if(!Strings.isNullOrEmpty(SelectedSnapshot.get("AccommodationUrl").toString()))
                                    AccommodationFunction.setFunctionURL(SelectedSnapshot.get("AccommodationUrl").toString());

                                if(!Strings.isNullOrEmpty(SelectedSnapshot.get("TransportUrl").toString()))
                                    TransportsFunction.setFunctionURL(SelectedSnapshot.get("TransportUrl").toString());

                                if(!Strings.isNullOrEmpty(SelectedSnapshot.get("JobSupportUrl").toString()))
                                    JobSupportFunction.setFunctionURL(SelectedSnapshot.get("JobSupportUrl").toString());
                            }
                        }
                    }
                });


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
    protected void onResume() {
        super.onResume();
        // Check if the user is logged in
        Pref = getSharedPreferences("login_pref", MODE_PRIVATE);
        IsLoggedIn = Pref.getBoolean("is_logged_in", false);

        // Determine which fragment to display based on login status
        //String currentFragment = getIntent().getStringExtra("currentFragment");
        //openFragmentByFragmentName(currentFragment);

        //hide the navigation items when users view the app without login
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

    private void openFragmentByFragmentName(String currentFragment)
    {
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
                    openFragment(new WebpageFragment(new ArrayList<>(), HomeActivity.this, FirebaseFirestore.getInstance()), "Social Activities");
                    break;
                case "accommodation":
                    openFragment(new WebpageFragment(new ArrayList<>(), HomeActivity.this, FirebaseFirestore.getInstance()), "Accommodation");

                case "transport":
                    openFragment(new WebpageFragment(new ArrayList<>(), HomeActivity.this, FirebaseFirestore.getInstance()), "Transport");
                    break;
                case "jobSupport":
                    openFragment(new WebpageFragment(new ArrayList<>(), HomeActivity.this, FirebaseFirestore.getInstance()), "Job Support");
                    break;
                case "search":
                    openFragment(new SearchSFragment(), " ");
                    break;
                case "webpage":
                    openFragment(new WebpageFragment(new ArrayList<>(), HomeActivity.this, FirebaseFirestore.getInstance()), "WelTec");
                    break;
            }
        } else {
            // If no extra "fragment", open the default fragment
            openFragment(new FunctionsFragment(AllFunctions), "Whitireia & WelTec");
        }

    }

    private String getCurrentFragmentName() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (currentFragment != null) {
            return currentFragment.getClass().getSimpleName();
        }
        return null;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_CANCELED && data != null) {
            String previousFragment = data.getStringExtra("from");
            if ("AddActivity".equals(previousFragment)) {
                // Return to the previous fragment in HomeActivity
                openFragmentByFragmentName("FunctionsFragment");
            }
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        int itemId = item.getItemId();

        if (itemId == R.id.nav_home)
        {
            openFragment(new FunctionsFragment(AllFunctions), "Whitireia & WelTec");
        }
        else if (itemId == R.id.nav_LearningSupport)
            ParentCategory = "Learning Support";
        else if (itemId == R.id.nav_Accommodation)
            ParentCategory = "Accommodation";
        else if (itemId == R.id.nav_Transport)
            ParentCategory = "Transport";
        else if (itemId == R.id.nav_SocialAct)
            ParentCategory = "Social Activities";
        else if (itemId == R.id.nav_JobSupport)
            ParentCategory = "Job Support";
        else if (itemId == R.id.nav_Webpage)
        {
            openFragment(new WebpageFragment(new ArrayList<>(), HomeActivity.this, FirebaseFirestore.getInstance()), "WelTec");
        }
        else if (itemId == R.id.nav_login)
        {
            SharedPreferences sharedPref = getSharedPreferences("login_pref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("is_logged_in", false);
            editor.apply();

            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Close the current activity ;
        }
        else if (itemId == R.id.nav_logout)
        {
            SharedPreferences sharedPref = getSharedPreferences("login_pref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("is_logged_in", false);
            editor.apply();

            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Close the current activity
        }
        drawerLayout.closeDrawer(GravityCompat.START);

        if(!Strings.isNullOrEmpty(ParentCategory))
        {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            String finalParentCategory = ParentCategory;
            db.collection("Support_Contents")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task)
                        {
                            ArrayList<Supports> AllSupports = new ArrayList<>();

                            for (DocumentSnapshot SelectedDocument : task.getResult().getDocuments())
                            {
                                if(SelectedDocument.get("parentCategory").toString().equals(finalParentCategory))
                                {
                                    Supports NewSupports = new Supports();

                                    NewSupports.setTitle(SelectedDocument.get("title").toString());
                                    NewSupports.setDescription(SelectedDocument.get("description").toString());

                                    if(SelectedDocument.contains("bannerUrl"))
                                    {
                                        NewSupports.setBannerUrl(SelectedDocument.get("bannerUrl").toString());
                                    }

                                    NewSupports.setParentCategory(SelectedDocument.get("parentCategory").toString());

                                    // Get the Sections.
                                    ArrayList<Long> SectionIDs= (ArrayList<Long>)SelectedDocument.get("sections");

                                    db.collection("Sections")
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                                            {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task)
                                                {
                                                    ArrayList<SectionDetails> AllSections = new ArrayList<>();

                                                    for (DocumentSnapshot SelectedSection : task.getResult().getDocuments())
                                                    {
                                                        boolean IDIsCorrect = false;

                                                        for (Long SelectedID : SectionIDs)
                                                        {
                                                            if(SelectedID == (Long) SelectedSection.get("id"))
                                                            {
                                                                IDIsCorrect = true;
                                                            }
                                                        }

                                                        if(IDIsCorrect)
                                                        {
                                                            SectionDetails NewSection = new SectionDetails();

                                                            NewSection.setSectionHeading(SelectedSection.get("heading").toString());

                                                            ArrayList<Long> DetailsIDs = (ArrayList<Long>)SelectedSection.get("details");

                                                            db.collection("Details")
                                                                    .get()
                                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                                                                    {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<QuerySnapshot> task)
                                                                        {
                                                                            ArrayList<Details> AllDetails = new ArrayList<>();

                                                                            for(DocumentSnapshot SelectedDetail : task.getResult().getDocuments())
                                                                            {
                                                                                boolean IsCorrectID = false;

                                                                                for(Long SelectedDetailID : DetailsIDs)
                                                                                {
                                                                                    if(SelectedDetailID == (Long) SelectedDetail.get("id"))
                                                                                    {
                                                                                        IsCorrectID = true;
                                                                                    }
                                                                                }

                                                                                if(IsCorrectID)
                                                                                {
                                                                                    Details NewDetail = new Details();
                                                                                    NewDetail.setDetail(SelectedDetail.get("detail").toString());

                                                                                    AllDetails.add(NewDetail);
                                                                                }
                                                                            }

                                                                            NewSection.setSectionDetails(AllDetails);
                                                                        }
                                                                    });

                                                            AllSections.add(NewSection);
                                                        }
                                                    }

                                                    NewSupports.setSections(AllSections);
                                                }
                                            });


                                    NewSupports.setConclusion(SelectedDocument.get("conclusion").toString());
                                    AllSupports.add(NewSupports);
                                }
                            }

                            openFragment(new SupportsFragment(AllSupports), finalParentCategory);
                        }
                    });
        }

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
    public void openFragment(Fragment fragment, String title){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
        getSupportActionBar().setTitle(title); //display fragment title on the toolbar

    }


}
