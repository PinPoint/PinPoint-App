package de.pinpoint.app.logic;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import de.pinpoint.app.PinPoint;
import de.pinpoint.client.locationclient.PinPointPosition;

public class PositionProvider implements LocationListener {
    private Context context;
    private Location lastLocation;
    private boolean gpsUpdatesRequested = false;

    public PositionProvider(Context context) {
        this.context = context;
        this.requestLocationUpdates();
    }

    private void requestLocationUpdates() {
        Handler mainHandler = new Handler(PinPoint.getmAppContext().getMainLooper());
        mainHandler.post(() -> {
            try {
                LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
                gpsUpdatesRequested = true;
            } catch (SecurityException ex) {
                Log.println(Log.WARN, "PinPoint", "GPS Permission not given yet...");
            }
        });
    }

    public PinPointPosition getPosition() throws GPSException {
        if (!gpsUpdatesRequested) {
            this.requestLocationUpdates();
        }
        if (lastLocation != null) {
            return new PinPointPosition(lastLocation.getLongitude(), lastLocation.getLatitude());
        } else {
            throw new GPSException("No GPS data received yet");
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Log.println(Log.DEBUG, "PinPoint", "location listener updated");
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
