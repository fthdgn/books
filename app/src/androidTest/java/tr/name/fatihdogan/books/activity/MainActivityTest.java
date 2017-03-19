package tr.name.fatihdogan.books.activity;

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import tr.name.fatihdogan.books.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void bottomNavigationClickTest() {
        ViewInteraction bottomNavigationItemView = onView(
                allOf(withId(R.id.navigation_all), withContentDescription(R.string.all), isDisplayed()));
        bottomNavigationItemView.perform(click());

        bottomNavigationItemView = onView(
                allOf(withId(R.id.navigation_authors), withContentDescription(R.string.authors), isDisplayed()));
        bottomNavigationItemView.perform(click());

        bottomNavigationItemView = onView(
                allOf(withId(R.id.navigation_bookshelves), withContentDescription(R.string.title_bookshelves), isDisplayed()));
        bottomNavigationItemView.perform(click());
    }
}
