package de.pinpoint.app.preferencestorage;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.UUID;

public class PreferenceStorage {
    private SharedPreferences sharedPref;

    public PreferenceStorage(Context context) {
        sharedPref = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
    }

    public String getName() throws KeyNotFoundException {
        if (!sharedPref.contains("name"))
            throw new KeyNotFoundException("name");

        return sharedPref.getString("name", null);
    }

    public void setName(String name) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("name", name);
        editor.apply();
    }

    public String getColor() throws KeyNotFoundException {
        if (!sharedPref.contains("color"))
            throw new KeyNotFoundException("color");

        return sharedPref.getString("color", "#2196F3");
    }

    public void setColor(String color) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("color", color);
        editor.apply();
    }

    public UUID getUUID() throws KeyNotFoundException {
        if (!sharedPref.contains("uuid"))
            throw new KeyNotFoundException("uuid");

        return UUID.fromString(sharedPref.getString("uuid", null));
    }

    public void setUUID(UUID uuid) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("uuid", uuid.toString());
        editor.apply();
    }

    public boolean existsUUID() {
        return sharedPref.contains("uuid");
    }

    public int getTheme() throws KeyNotFoundException {
        if (!sharedPref.contains("theme"))
            throw new KeyNotFoundException("theme");

        return sharedPref.getInt("theme", -1);
    }

    public void setTheme(int theme) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("theme", theme);
        editor.apply();
    }

    public void clear() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();
    }
}
