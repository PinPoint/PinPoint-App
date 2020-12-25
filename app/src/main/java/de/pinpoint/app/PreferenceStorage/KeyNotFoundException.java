package de.pinpoint.app.PreferenceStorage;

public class KeyNotFoundException extends Exception {
    public KeyNotFoundException(String key) {
        super("Key " + key + " not found" );
    }
}
