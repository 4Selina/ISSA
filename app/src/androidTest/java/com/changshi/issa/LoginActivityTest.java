package com.changshi.issa;

import static junit.framework.TestCase.assertTrue;

import android.widget.EditText;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

//    private SharedPreferences sharedPref;

//    @Before
//    public void setUp() {
//        Context context = ApplicationProvider.getApplicationContext();
//        sharedPref = context.getSharedPreferences("login_pref", Context.MODE_PRIVATE);
//    }
//
//    @After
//    public void tearDown() {
//        sharedPref.edit().clear().apply();
//    }
//    @Test
//    public void testLoginWithValidCredentials() {
//        // Set valid input credentials
//        String username = "admin";
//        String password = "admin";
//
//
//        // Perform login
//        onView(withId(R.id.edtUserName)).perform(typeText(username), closeSoftKeyboard());
//        onView(withId(R.id.edtPassword)).perform(typeText(password), closeSoftKeyboard());
//        onView(withId(R.id.btnLogin)).perform(click());
//
//        // Check if login is successful
//        SharedPreferences sharedPref = ApplicationProvider.getApplicationContext().getSharedPreferences("login_pref", Context.MODE_PRIVATE);
//        assertEquals(username, sharedPref.getString("username", null));
//    }

//    @Test
//    public void testSkipButton() {
//        // Perform click on skip button
//        onView(withId(R.id.btnSkip)).perform(click());
//
//        // Check if HomeActivity is launched
//        Context context = ApplicationProvider.getApplicationContext();
//        Intent expectedIntent = new Intent(context, HomeActivity.class);
//
//        Pools.Pool<Object> Intents = null;
//        Intents.release();
//    }


    @Test
    public void testLoginWithMissingUsername() {
        // Set missing username
        String password = "admin";

        // Start LoginActivity
        ActivityScenario<LoginActivity> scenario = ActivityScenario.launch(LoginActivity.class);

        // Perform login
        scenario.onActivity(activity -> {
            EditText edtUsername = activity.findViewById(R.id.edtUserName);
            edtUsername.setText("");
            EditText edtPassword = activity.findViewById(R.id.edtPassword);
            edtPassword.setText(password);
            activity.findViewById(R.id.btnLogin).performClick();
        });

        // Check if error message is displayed
        scenario.onActivity(activity -> {
            EditText edtUsername = activity.findViewById(R.id.edtUserName);
            assertTrue(edtUsername.getError() != null);
        });

        // Close LoginActivity
        scenario.close();
    }
    @Test
    public void testLoginWithMissingPassword() {
        // Set missing password
        String username = "admin";

        // Start LoginActivity
        ActivityScenario<LoginActivity> scenario = ActivityScenario.launch(LoginActivity.class);

        // Perform login
        scenario.onActivity(activity -> {
            EditText edtPassword = activity.findViewById(R.id.edtUserName);
            edtPassword.setText(username);
            EditText edtUsername = activity.findViewById(R.id.edtPassword);
            edtUsername.setText("");
            activity.findViewById(R.id.btnLogin).performClick();
        });

        // Check if error message is displayed
        scenario.onActivity(activity -> {
            EditText edtUsername = activity.findViewById(R.id.edtUserName);
            assertTrue(edtUsername.getError() != null);
        });

        // Close LoginActivity
        scenario.close();
    }
    @Test
    public void testSuccessfulLogin() {
        // Set valid input credentials
        String username = "admin";
        String password = "admin";

        // Start LoginActivity
        ActivityScenario<LoginActivity> scenario = ActivityScenario.launch(LoginActivity.class);


        // Perform login
        scenario.onActivity(activity -> {
            EditText edtUsername = activity.findViewById(R.id.edtPassword);
            edtUsername.setText(password);
            EditText edtPassword = activity.findViewById(R.id.edtUserName);
            edtPassword.setText(username);
            activity.findViewById(R.id.btnLogin).performClick();
        });


        // Check if logged in
        scenario.onActivity(activity -> {
            SharedPreferences sharedPref = activity.getSharedPreferences("login_pref", Context.MODE_PRIVATE);
            assertTrue(sharedPref.getBoolean("is_logged_in", false));
            assertEquals(username, sharedPref.getString("username", ""));
        });

        // Close LoginActivity
        scenario.close();

    }
}
