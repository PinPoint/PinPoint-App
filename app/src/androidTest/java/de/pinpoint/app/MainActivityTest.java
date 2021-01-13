package de.pinpoint.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule activityTestRule = new ActivityTestRule(MainActivity.class, false, false);

    @Before
    public void setup() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        SharedPreferences sharedPrefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.clear();
        editor.apply();
        activityTestRule.launchActivity(new Intent());
    }

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.ACCESS_FINE_LOCATION",
                    "android.permission.WRITE_EXTERNAL_STORAGE");

    @Test
    public void testSetSettings() {
        String name = "Steve";
        String color = "#E91E63";
        onView(withId(R.id.nameText)).perform(replaceText(name));
        onView(withId(R.id.color)).perform(click());
        onView(allOf(withId(R.id.materialColorRV))).perform(actionOnItemAtPosition(1, click()));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.nextButton)).perform(click());

        Assert.assertEquals(name, PinPoint.getLogic().getName());
        Assert.assertEquals(color, PinPoint.getLogic().getColor());
    }

    @Test
    public void testSetEmptyName() {
        String name = "";
        ViewInteraction editText = onView(withId(R.id.nameText)).perform(replaceText(name));
        onView(withId(R.id.nextButton)).perform(click());
        editText.check(ViewAssertions.matches(hasErrorText("Name should be at least 3 characters")));
    }

    @Test
    public void testSetMinName() {
        String name = "Tom";
        onView(withId(R.id.nameText)).perform(replaceText(name));
        onView(withId(R.id.nextButton)).perform(click());

        Assert.assertEquals(name, PinPoint.getLogic().getName());
    }

    @Test
    public void testSetMaxName() {
        String name = "Braidonalexander";
        onView(withId(R.id.nameText)).perform(replaceText(name));
        onView(withId(R.id.nextButton)).perform(click());

        Assert.assertEquals(name, PinPoint.getLogic().getName());
    }

    @Test
    public void testSetLongName() {
        String name = "Morningstarwalker";
        onView(withId(R.id.nameText)).perform(replaceText(name));
        onView(withId(R.id.nextButton)).perform(click());

        Assert.assertEquals(name.substring(0, 16), PinPoint.getLogic().getName());
    }
}
