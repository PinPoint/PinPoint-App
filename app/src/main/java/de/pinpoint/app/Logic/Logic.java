package de.pinpoint.app.Logic;

import android.content.Context;

import androidx.appcompat.app.AppCompatDelegate;

import java.io.Serializable;

import de.pinpoint.app.PreferenceStorage.KeyNotFoundException;
import de.pinpoint.app.PreferenceStorage.PreferenceStorage;

public class Logic {
    private Context context;
    private PreferenceStorage prefStorage;

    public Logic(Context context) {
        this.context = context;
        this.prefStorage = new PreferenceStorage(context);
        try {
            setTheme(prefStorage.getTheme());
        } catch (KeyNotFoundException ignored) {
        }
    }

    public void changePreferences(String name, String color, int theme) {
        prefStorage.setName(name);
        prefStorage.setColor(color);
        prefStorage.setTheme(theme);
        setTheme(theme);
    }

    public int getTheme() {
        try {
            return prefStorage.getTheme();
        } catch (KeyNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String getName() {
        try {
            return prefStorage.getName();
        } catch (KeyNotFoundException e) {
            return "";
        }
    }

    private void setTheme(int theme) {
        if (theme == 0)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        else if (theme == 1)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

}
