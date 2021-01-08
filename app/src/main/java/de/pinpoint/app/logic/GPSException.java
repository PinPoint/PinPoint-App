package de.pinpoint.app.logic;

public class GPSException extends Exception {
    public GPSException(String message){
        super(message);
    }
    public GPSException(String message, Exception cause){
        super(message, cause);
    }
}
