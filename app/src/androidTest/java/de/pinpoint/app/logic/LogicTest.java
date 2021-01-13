package de.pinpoint.app.logic;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.pinpoint.app.PinPoint;
import de.pinpoint.app.preferencestorage.PreferenceStorage;

import static org.junit.Assert.*;

public class LogicTest {

    private PreferenceStorage prefStorage;

    @Before
    public void setUp() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        prefStorage = new PreferenceStorage(context);
    }

    @After
    public void tearDown() {
        prefStorage.clear();
    }

    @Test
    public void testChangePreferences() {
        String name = "Steve";
        String color = "#FF00FF";
        int theme = 1;

        PinPoint.getLogic().changePreferences(name, color, theme);
        assertEquals(name, PinPoint.getLogic().getName());
        assertEquals(color, PinPoint.getLogic().getColor());
        assertEquals(theme, PinPoint.getLogic().getTheme());
    }

    @Test
    public void testGetTheme() {
        String name = "Steve";
        String color = "#FF00FF";
        int theme = 1;

        PinPoint.getLogic().changePreferences(name, color, theme);
        assertEquals(theme, PinPoint.getLogic().getTheme());
    }

    @Test
    public void testGetName() {
        String name = "Steve";
        String color = "#FF00FF";
        int theme = 1;

        PinPoint.getLogic().changePreferences(name, color, theme);
        assertEquals(name, PinPoint.getLogic().getName());
    }

    @Test
    public void testGetColor() {
        String name = "Steve";
        String color = "#FF00FF";
        int theme = 1;

        PinPoint.getLogic().changePreferences(name, color, theme);
        assertEquals(color, PinPoint.getLogic().getColor());
    }
}