package com.enenim.mybakingapp;


import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    public static final String RECIPE_NAME = "Brownies";
    /**
     * The ActivityTestRule is a rule provided by Android used for functional testing of a single
     * activity. The activity that will be tested will be launched before each test that's annotated
     * with @Test and before methods annotated with @before. The activity will be terminated after
     * the test and methods annotated with @After are complete. This rule allows you to directly
     * access the activity during the test.
     */

    @Rule
    public ActivityTestRule<RecipeStepDescriptionHostActivity> mActivityTestRule =
            new ActivityTestRule<>(RecipeStepDescriptionHostActivity.class);

    @Test
    public void clickRecyclerViewItem_LaunchRecipeStepDescriptionHostActivity(){
        // Click on the RecyclerView item at position 2
        onView(withId(R.id.recycler_view_recipe)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        onView(withId(R.id.testing_test_view)).check(matches(withText(RECIPE_NAME)));
    }
}
