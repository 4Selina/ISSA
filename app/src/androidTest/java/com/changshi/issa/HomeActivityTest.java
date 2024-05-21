package com.changshi.issa;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class HomeActivityTest {
    @Rule
    public ActivityScenarioRule<HomeActivity> activityActivityScenarioRule = new ActivityScenarioRule<>(HomeActivity.class);

    @Before
    public void setUp() throws Exception {
        // 确保应用在每个测试开始前处于未登录状态
        simulateLogout();
    }

    @Test
    public void testBottomHome() {
        onView(withId(R.id.homeBTM)).perform(click());
        onView(withId(R.id.fragment_container)).check(matches(isDisplayed()));
    }

    @Test
    public void testsLoginSearch() {
        onView(withId(R.id.searchBTM)).perform(click());
        onView(withId(R.id.fragment_container)).check(matches(isDisplayed()));
    }

    @Test
    public void testLoginAdd() {
        onView(withId(R.id.addBTM)).perform(click());
        onView(withId(R.id.layout_add_activity)).check(matches(isDisplayed()));
    }

    @Test
    public void testLoginWeb() {
        onView(withId(R.id.webBTM)).perform(click());
        onView(withId(R.id.fragment_container)).check(matches(isDisplayed()));
    }

    @Test
    public void testLogout() {
        // 确保 logoutBTM 是可见的
        onView(withId(R.id.logoutBTM)).check(matches(isDisplayed()));

        // 点击 logoutBTM
        onView(withId(R.id.logoutBTM)).perform(click());

        // 等待页面跳转完成
        try {
            Thread.sleep(1000); // 等待1秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 检查页面是否跳转到 MainActivity
        onView(withId(R.id.coordinatorLayout)).check(matches(isDisplayed()));
    }

    @Test
    public void testHomeNoLogin() {
        // 模拟未登录状态
        simulateLogout();

        // 点击 Home 按钮
        onView(withId(R.id.homeBTM)).perform(click());

        // 检查是否跳转到 LoginActivity
        onView(withId(R.id.fragment_container)).check(matches(isDisplayed()));
    }

    @Test
    public void testSearchNoLogin() {
        // 模拟未登录状态
        simulateLogout();

        // 点击 Search 按钮
        onView(withId(R.id.searchBTM)).perform(click());

        // 检查是否跳转到 LoginActivity
        onView(withId(R.id.fragment_container)).check(matches(isDisplayed()));
    }

    @Test
    public void testWebpageNoLogin() {
        // 模拟未登录状态
        simulateLogout();

        // 点击 Web 按钮
        onView(withId(R.id.webBTM)).perform(click());

        // 检查是否跳转到 LoginActivity
        onView(withId(R.id.fragment_container)).check(matches(isDisplayed()));
    }

    private void simulateLogout() {
        // 清除用户登录信息，确保应用处于未登录状态
        SharedPreferences preferences = InstrumentationRegistry.getInstrumentation().getTargetContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear(); // 清除所有保存的用户数据
        editor.apply();
    }

    @After
    public void tearDown() throws Exception {
        // 任何需要在每个测试结束后重置的状态
    }
}
