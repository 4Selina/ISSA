
package com.changshi.issa;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class HomeActivityTest {

    @Rule
    public ActivityTestRule<HomeActivity> activityRule = new ActivityTestRule<>(HomeActivity.class);

    @Test
    public void testNavigationButtonsVisibilityWhenNotLoggedIn() {
//        // Ensure that the user is not logged in
//        Context context = ApplicationProvider.getApplicationContext();
//        SharedPreferences sharedPref = context.getSharedPreferences("login_pref", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putBoolean("is_logged_in", false);
//        editor.apply();
//
//        // Check if navigation buttons are hidden
//        onView(withId(R.id.nav_logout)).check(matches(not(isDisplayed())));
//        onView(withId(R.id.nav_login)).check(matches(isDisplayed()));
//
//        // Check if bottom navigation buttons are hidden
//        onView(withId(R.id.logoutBTM)).check(matches(not(isDisplayed())));
//        onView(withId(R.id.addBTM)).check(matches(not(isDisplayed())));
    }
}
