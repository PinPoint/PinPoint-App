package de.pinpoint.app.preferencestorage;

public class KeyNotFoundException extends RuntimeException {
    public KeyNotFoundException(String key) {
        super("Key " + key + " not found");
    }
}
