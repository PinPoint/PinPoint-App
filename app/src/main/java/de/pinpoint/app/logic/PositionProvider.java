package de.pinpoint.app.logic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;

import de.pinpoint.client.locationclient.PinPointPosition;

public class PositionProvider implements LocationListener {
    private Context context;
    private Location lastLocation;
    private boolean gpsUpdatesRequested = false;

    @SuppressLint("MissingPermission")
    public PositionProvider(Context context) {
        this.context = context;
        this.requestLocationUpdates();
    }

    private void requestLocationUpdates(){
        try {
            LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            gpsUpdatesRequested = true;
        } catch(SecurityException ex){
            System.out.println("GPS Permission not given yet....");
        }
    }

    public PinPointPosition getPosition() throws GPSException{
        if(!gpsUpdatesRequested){
            this.requestLocationUpdates();
        }
        if(lastLocation != null){
            return new PinPointPosition(lastLocation.getLongitude(), lastLocation.getLatitude());
        } else {
            throw new GPSException("No GPS data received yet");
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        System.out.println("location listener updated");
        this.lastLocation = location;
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        this.lastLocation = null;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }
}
