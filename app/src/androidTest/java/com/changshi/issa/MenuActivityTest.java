package com.changshi.issa;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class MenuActivityTest {
    @Rule
    public ActivityScenarioRule<HomeActivity> activityRule = new ActivityScenarioRule<>(HomeActivity.class);

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testHomePage() {
        // Click the Home menu item
        onView(withId(R.id.nav_home)).perform(click());
        onView(withId(R.id.fragment_container)).check(matches(isDisplayed()));
    }

    @Test
    public void testLearningSupportPage() {
        // Click the Learning Support menu item
        onView(withId(R.id.nav_LearningSupport)).perform(click());
        onView(withId(R.id.fragment_container)).check(matches(isDisplayed()));
    }

    @Test
    public void testAccommodationPage() {
        // Click the Accommodation menu item
        onView(withId(R.id.nav_Accommodation)).perform(click());
        onView(withId(R.id.fragment_container)).check(matches(isDisplayed()));
    }

    @Test
    public void testTransportPage() {
        // Click the Transport menu item
        onView(withId(R.id.nav_Transport)).perform(click());
        onView(withId(R.id.fragment_container)).check(matches(isDisplayed()));
    }

    @Test
    public void testSocialPage() {
        // Click the Social menu item
        onView(withId(R.id.nav_SocialAct)).perform(click());
        onView(withId(R.id.fragment_container)).check(matches(isDisplayed()));
    }

    @Test
    public void testJobSupportPage() {
        // Click the Job Support menu item
        onView(withId(R.id.nav_JobSupport)).perform(click());
        onView(withId(R.id.fragment_container)).check(matches(isDisplayed()));
    }

    @Test
    public void testWebPage() {
        // Click the Web menu item
        onView(withId(R.id.nav_Webpage)).perform(click());
        onView(withId(R.id.fragment_container)).check(matches(isDisplayed()));
    }

    @Test
    public void testLoginPage() {
        // Click the Login menu item
        onView(withId(R.id.nav_login)).perform(click());

        // Verify that the Login activity is displayed
        onView(withId(R.id.LoginActivity)).check(matches(isDisplayed()));
    }

    @Test
    public void testLogout() {
        // Click the Logout menu item
        onView(withId(R.id.nav_logout)).perform(click());

        // Verify that the Main activity is displayed
        onView(withId(R.id.coordinatorLayout)).check(matches(isDisplayed()));
    }

    @After
    public void tearDown() throws Exception {
        // Any necessary cleanup after each test
    }
}
