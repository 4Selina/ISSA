package com.changshi.issa;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.TestCase.assertTrue;

import android.widget.EditText;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

//@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {
    @Rule
    public ActivityScenarioRule<LoginActivity> activityActivityScenarioRule= new ActivityScenarioRule<LoginActivity>(LoginActivity.class);
    LoginActivity loginActivity;
    @Before
    public void setUp() throws Exception {

    }
    @Test
    public void testInvalidlogin(){
        onView(withId(R.id.edtUserName)).perform(typeText("testings"));
        onView(withId(R.id.edtPassword)).perform(typeText("test"));
        onView(withId(R.id.btnLogin)).perform(click());
        onView(withId(R.id.btnLogin)).check(matches(isDisplayed()));
    }

    @Test
    public void testBlanklogin(){
        onView(withId(R.id.edtUserName)).perform(typeText(""));
        onView(withId(R.id.edtPassword)).perform(typeText(""));
        onView(withId(R.id.btnLogin)).perform(click());
        onView(withId(R.id.btnLogin)).check(matches(isDisplayed()));
    }

    @Test
    public void testSkipButton(){
        onView(withId(R.id.btnSkip)).perform(click());
        onView(withId(R.id.layout_HomeActivity)).check(matches(isDisplayed()));
    }

    @Test
    public void testValidlogin(){
        onView(withId(R.id.edtUserName)).perform(typeText("admin"));
        onView(withId(R.id.edtPassword)).perform(typeText("admin"));
        onView(withId(R.id.btnLogin)).perform(click());
        onView(withId(R.id.layout_HomeActivity)).check(matches(isDisplayed()));
    }

    @After
    public void tearDown() throws Exception {
        loginActivity = null;
    }


    @Test
    public void testLoginWithMissingUsername() {
        // Set missing username
        String password = "admin";

        // Start LoginActivity
        ActivityScenario<LoginActivity> scenario = ActivityScenario.launch(LoginActivity.class);

        // Perform login
        onView(withId(R.id.edtUserName)).perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.edtPassword)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.btnLogin)).perform(click());

        // Check if error message is displayed
        scenario.onActivity(activity -> {
            EditText edtUsername = activity.findViewById(R.id.edtUserName);
            assertTrue(edtUsername.getError() != null);
        });

        // Close LoginActivity
        scenario.close();
    }


}