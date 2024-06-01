package com.changshi.issa;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
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
    private FrameLayout FragmentContainer;
    SharedPreferences Pref;
    public boolean IsLoggedIn;
    private ArrayList<Functions> AllFunctions;
    private String ParentCategory;
    private static final String TAG = "HomeActivity";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize Firestore instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        AllFunctions = new ArrayList<>();

        Functions LearningSupportFunction = new Functions();
        Functions SocialActivitiesFunction = new Functions();
        Functions AccommodationFunction = new Functions();
        Functions TransportsFunction = new Functions();
        Functions JobSupportFunction = new Functions();

        FragmentContainer = findViewById(R.id.fragment_container);

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

        drawerLayout = findViewById(R.id.layout_HomeActivity);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigationDrawer);
        navigationView.setNavigationItemSelectedListener(this);

        // Initialize BottomNavigationView
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
                    intent.putExtra("IsEditMode", false);
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
                    openFragment(new  WebpageFragment(new ArrayList<>(), HomeActivity.this, FirebaseFirestore.getInstance()), "Learning Support");
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


                OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
                OnBackPressedCallback callback = new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed()
                    {
                        Log.d(TAG, "OnBackPressedCallback called");

                        if (drawerLayout.isDrawerOpen(GravityCompat.START))
                        {
                            drawerLayout.closeDrawer(GravityCompat.START);
                        }
                        else
                        {
                            // Custom back press handling
                            int backStackEntryCount = fragmentManager.getBackStackEntryCount();
                            Log.d(TAG, "Back stack entry count: " + backStackEntryCount);

                            if (backStackEntryCount > 1)
                            {
                                final Handler handler = new Handler();
                                fragmentManager.popBackStack();

                                handler.postDelayed(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        Fragment currentFragment = fragmentManager.getFragments().get(0);

                                        if(currentFragment.getClass().getSimpleName().equals("FunctionsFragment"))
                                        {
                                            getSupportActionBar().setTitle(((FunctionsFragment)currentFragment).getFragmentTitle());
                                        }
                                        else if(currentFragment.getClass().getSimpleName().equals("SupportsFragment"))
                                        {
                                            getSupportActionBar().setTitle(((SupportsFragment)currentFragment).getFragmentTitle());
                                        }
                                    }
                                }, 100);

                            }
                            else
                            {
                                finish();
                            }
                        }
                    }
                };

                onBackPressedDispatcher.addCallback(this, callback);
            }


    @Override
    protected void onResume()
    {
        super.onResume();
        // Check if the user is logged in
        Pref = getSharedPreferences("login_pref", MODE_PRIVATE);
        IsLoggedIn = Pref.getBoolean("is_logged_in", false);

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

    //Gets the name of the current fragment.
    private void openFragmentByFragmentName(String currentFragment)
    {
        // Check if there's an extra "fragment" in the intent
        String fragmentName = getIntent().getStringExtra("fragment");

        if (fragmentName != null)
        {
            switch (fragmentName)
            {
                case "learningSupport":
                    openFragment(new  WebpageFragment(new ArrayList<>(), HomeActivity.this, FirebaseFirestore.getInstance()), "Learning Support");
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

    //Gets the name of the current fragment.
    private String getCurrentFragmentName()
    {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (currentFragment != null)
        {
            return currentFragment.getClass().getSimpleName();
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_CANCELED && data != null)
        {
            String previousFragment = data.getStringExtra("from");

            if ("AddActivity".equals(previousFragment))
            {
                // Return to the previous fragment in HomeActivity
                openFragmentByFragmentName("FunctionsFragment");
            }
        }
    }

    // Method to handle navigation item selection
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        int itemId = item.getItemId();

        // Reset ParentCategory to null
        ParentCategory = null;

        // Determine the action based on the selected item
        if (itemId == R.id.nav_home)
            openFragment(new FunctionsFragment(AllFunctions), "Whitireia & WelTec");
        else if (itemId == R.id.nav_LearningSupport)
            ParentCategory = "Learning Support";
        else if (itemId == R.id.nav_Accommodation)
            ParentCategory = "Accommodation";
        else if (itemId == R.id.nav_Transport)
            ParentCategory = "Transports";
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
            // Handle the login action
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
            // Handle the logout action
            SharedPreferences sharedPref = getSharedPreferences("login_pref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("is_logged_in", false);
            editor.apply();

            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Close the current activity
        }
        drawerLayout.closeDrawer(GravityCompat.START);

        // If ParentCategory is not null, fetch and display relevant supports
        if(!Strings.isNullOrEmpty(ParentCategory))
        {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Fetch support contents based on the parent category
            String finalParentCategory = ParentCategory;
            db.collection("Support_Contents")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task)
                        {
                            // List to store fetched supports
                            ArrayList<Supports> AllSupports = new ArrayList<>();

                            // Loop through each document in the collection
                            for (DocumentSnapshot SelectedDocument : task.getResult().getDocuments())
                            {
                                // Check if the document's parent category matches the selected category
                                if(SelectedDocument.get("parentCategory").toString().equals(finalParentCategory))
                                {
                                    // Create a new Supports object and populate its fields
                                    Supports NewSupports = new Supports();

                                    NewSupports.setId((Long)SelectedDocument.get("id"));
                                    NewSupports.setDocumentID(SelectedDocument.getReference().getId());
                                    NewSupports.setTitle(SelectedDocument.get("title").toString());
                                    NewSupports.setDescription(SelectedDocument.get("description").toString());

                                    if (SelectedDocument.contains("bannerUrl") && SelectedDocument.get("bannerUrl") != null) {
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
                                                    // List to store fetched sections
                                                    ArrayList<SectionDetails> AllSections = new ArrayList<>();

                                                    // Loop through each document in the Sections collection
                                                    for (DocumentSnapshot SelectedSection : task.getResult().getDocuments())
                                                    {
                                                        boolean IDIsCorrect = false;

                                                        // Check if the section ID matches any in the document
                                                        for (Long SelectedID : SectionIDs)
                                                        {
                                                            if(SelectedID == (Long) SelectedSection.get("id"))
                                                            {
                                                                IDIsCorrect = true;
                                                            }
                                                        }

                                                        // If the ID matches, create a new SectionDetails object and populate it
                                                        if(IDIsCorrect)
                                                        {
                                                            SectionDetails NewSection = new SectionDetails();

                                                            NewSection.setID((Long)SelectedSection.get("id"));
                                                            NewSection.setDocumentID(SelectedSection.getReference().getId());
                                                            NewSection.setSectionHeading(SelectedSection.get("heading").toString());

                                                            ArrayList<Long> DetailsIDs = (ArrayList<Long>)SelectedSection.get("details");

                                                            db.collection("Details")
                                                                    .get()
                                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                                                                    {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<QuerySnapshot> task)
                                                                        {
                                                                            // List to store fetched details
                                                                            ArrayList<Details> AllDetails = new ArrayList<>();

                                                                            // Loop through each document in the Details collection
                                                                            for(DocumentSnapshot SelectedDetail : task.getResult().getDocuments())
                                                                            {
                                                                                // Check if the detail ID matches any in the document
                                                                                for(Long SelectedDetailID : DetailsIDs)
                                                                                {
                                                                                    if(SelectedDetailID.equals(SelectedDetail.getData().get("id")))
                                                                                    {
                                                                                        // Create a new Details object and populate it
                                                                                        Details NewDetail = new Details();

                                                                                        NewDetail.setID((Long)SelectedDetail.getData().get("id"));
                                                                                        NewDetail.setDocumentID(SelectedDetail.getReference().getId());
                                                                                        NewDetail.setDetail(SelectedDetail.get("detail").toString());

                                                                                        if(SelectedDetail.contains("link"))
                                                                                            NewDetail.setLink(SelectedDetail.getString("link"));

                                                                                        AllDetails.add(NewDetail);
                                                                                    }
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

    // Method to open a fragment and set its title
    public void openFragment(Fragment fragment, String title)
    {
        // Get the simple name of the fragment's class
        String ClassName = fragment.getClass().getSimpleName();

        // Set the fragment title based on the fragment's class
        if(ClassName.equals("FunctionsFragment"))
        {
            ((FunctionsFragment)fragment).setFragmentTitle(title);
        }
        else if(ClassName.equals("SupportsFragment"))
        {
            ((SupportsFragment)fragment).setFragmentTitle(title);
        }

        // Begin a transaction to replace the fragment
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Log the fragment title
        Log.d(TAG, "TITLE::"+title);

        // Replace the fragment in the container
        transaction.replace(R.id.fragment_container, fragment);

        // Add the transaction to the back stack with a tag
        transaction.addToBackStack("ccccccc"); // Use title as the back stack name

        // Commit the transaction
        transaction.commit();

        // Display the fragment title on the toolbar
        getSupportActionBar().setTitle(title);
    }

    // Method to handle back button press
    public void onBackPressed() {
        // Check if the drawer is open and close it if true
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            // If the drawer is not open, call the superclass method to execute default behavior
            super.onBackPressed();
        }
    }

    // Method to get the title for a fragment
    private String getFragmentTitle(Fragment fragment) {
        // Check the instance of the fragment and return a title accordingly
        if (fragment instanceof FunctionsFragment) {
            return "Whitireia & WelTec";
        } else if (fragment instanceof SearchSFragment) {
            return "Search";
        } else if (fragment instanceof WebpageFragment) {
            return "WelTec"; // Customize this as per the instance you create
        } else if (fragment instanceof SupportsFragment) {
            return "Support"; // Customize this as per your requirements
        }
        return "";
    }

}
