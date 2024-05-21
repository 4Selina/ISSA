package com.changshi.issa;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.idling.CountingIdlingResource;
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
    private CountingIdlingResource countingIdlingResource;

    @Before
    public void setUp() throws Exception {
        countingIdlingResource = new CountingIdlingResource("Login");
        IdlingRegistry.getInstance().register(countingIdlingResource);

    }
    @Test
    public void testInvalidlogin(){
        onView(withId(R.id.edtUserName)).perform(typeText("testings"));
        onView(withId(R.id.edtPassword)).perform(typeText("test"));
        onView(withId(R.id.btnLogin)).perform(click());
        onView(withId(R.id.btnLogin)).check(matches(isDisplayed()));
    }
    @Test
    public void testLoginWithMissingUsername(){
        onView(withId(R.id.edtUserName)).perform(typeText(""));
        onView(withId(R.id.edtPassword)).perform(typeText("admin"));
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
//        onView(withId(R.id.btnLogin)).perform(click());
//        // delay 3 seconds
//
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
////
//        onView(withId(R.id.layout_HomeActivity)).check(matches(isDisplayed()));
//
    }

    @After
    public void tearDown() throws Exception {
        loginActivity = null;
        IdlingRegistry.getInstance().unregister(countingIdlingResource);

    }






}