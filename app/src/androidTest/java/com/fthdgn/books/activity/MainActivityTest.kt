package com.fthdgn.books.activity

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.view.View
import com.fthdgn.books.R
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Rule
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun bottomNavigationClickTest() {
        var bottomNavigationItemView = onView(
                allOf<View>(withId(R.id.navigation_all), isDisplayed()))
        bottomNavigationItemView.perform(click())

        bottomNavigationItemView = onView(
                allOf<View>(withId(R.id.navigation_authors), isDisplayed()))
        bottomNavigationItemView.perform(click())

        bottomNavigationItemView = onView(
                allOf<View>(withId(R.id.navigation_bookshelves), isDisplayed()))
        bottomNavigationItemView.perform(click())
    }
}
