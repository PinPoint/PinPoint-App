package de.pinpoint.app.preferencestorage;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.MediumTest;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
@MediumTest
public class PreferenceStorageTest {

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
    public void setName() {
        String name = "Steve";
        prefStorage.setName(name);
    }

    @Test
    public void getName() throws KeyNotFoundException {
        String name = "Steve";
        prefStorage.setName(name);
        String returnedName = prefStorage.getName();
        assertEquals(name, returnedName);
    }

    @Test(expected = KeyNotFoundException.class)
    public void getEmptyName() throws KeyNotFoundException {
        String returnedName = prefStorage.getName();
    }

    @Test
    public void setColor() {
        String color = "#0000FF";
        prefStorage.setColor(color);
    }

    @Test
    public void getColor() throws KeyNotFoundException {
        String color = "#0000FF";
        prefStorage.setColor(color);
        String returnedColor = prefStorage.getColor();
        assertEquals(color, returnedColor);
    }

    @Test(expected = KeyNotFoundException.class)
    public void getEmptyColor() throws KeyNotFoundException {
        String returnedColor = prefStorage.getColor();
    }

    @Test
    public void setUUID() {
        UUID uuid = UUID.randomUUID();
        prefStorage.setUUID(uuid);
    }

    @Test
    public void getUUID() throws KeyNotFoundException {
        UUID uuid = UUID.randomUUID();
        prefStorage.setUUID(uuid);
        UUID returnedUUID = prefStorage.getUUID();
        assertEquals(uuid, returnedUUID);
    }

    @Test(expected = KeyNotFoundException.class)
    public void getEmptyUUID() throws KeyNotFoundException {
        UUID returnedUUID = prefStorage.getUUID();
    }

    @Test
    public void setTheme() {
        int theme = 1;
        prefStorage.setTheme(theme);
    }

    @Test
    public void getTheme() throws KeyNotFoundException {
        int theme = 1;
        prefStorage.setTheme(theme);
        int returnedTheme = prefStorage.getTheme();
        assertEquals(theme, returnedTheme);
    }

    @Test(expected = KeyNotFoundException.class)
    public void getEmptyTheme() throws KeyNotFoundException {
        int returnedTheme = prefStorage.getTheme();
    }
}