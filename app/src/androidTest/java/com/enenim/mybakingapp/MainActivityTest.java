package com.enenim.mybakingapp;


import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    public static final String RECIPE_NAME = "Brownies";
    private static final int RECYCLER_VIEW_ITEM_POSITION = 1;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    private SimpleIdlingResource idlingResource;

    @Before
    public void registerRestServiceIdlingResource() {
        idlingResource = new SimpleIdlingResource();
        Espresso.registerIdlingResources(idlingResource);
    }

    @After
    public void unregisterRestServiceIdlingResource() {
        Espresso.unregisterIdlingResources(idlingResource);
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    @Test
    public void mainActivityTest() {
        /*ViewInteraction textView = onView(
                allOf(withId(R.id.grid_item_text_view), withText(RECIPE_NAME),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.recycler_view_recipe),
                                        RECYCLER_VIEW_ITEM_POSITION),
                                RECYCLER_VIEW_ITEM_POSITION),
                        isDisplayed()));
        textView.check(matches(withText(RECIPE_NAME)));*/

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.recycler_view_recipe), isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(RECYCLER_VIEW_ITEM_POSITION, click()));
    }
}
