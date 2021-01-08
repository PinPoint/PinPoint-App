package de.pinpoint.app.logic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import androidx.annotation.NonNull;

import de.pinpoint.client.locationclient.PinPointPosition;

public class PositionProvider implements LocationListener {
    private Context context;
    private Location lastLocation;

    @SuppressLint("MissingPermission")
    public PositionProvider(Context context) {
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    public PinPointPosition getPosition() throws GPSException{
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
}
