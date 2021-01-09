package de.pinpoint.app.logic;

public class InternetException extends Exception {
    public InternetException(String message) {
        super(message);
    }

    public InternetException(String message, Exception cause) {
        super(message, cause);
    }
}
