package de.pinpoint.app.util;

import org.osmdroid.util.GeoPoint;

import de.pinpoint.client.locationclient.PinPointPosition;

public class PositionUtil {

    public static GeoPoint toGeoPoint(PinPointPosition position) {
        return new GeoPoint(position.getLatitude(), position.getLongitude());
    }

    public static PinPointPosition fromGeoPoint(GeoPoint geoPoint) {
        return new PinPointPosition(geoPoint.getLongitude(), geoPoint.getLatitude());
    }

    public static double getDistance(PinPointPosition pos1, PinPointPosition pos2) {
        GeoPoint p1 = toGeoPoint(pos1);
        GeoPoint p2 = toGeoPoint(pos2);
        return p1.distanceToAsDouble(p2);
    }

    public static String getDistanceStr(PinPointPosition pos1, PinPointPosition pos2) {
        double meters = getDistance(pos1, pos2);
        String distanceStr;
        if (meters > 999) {
            double kilometers = meters / 1000;
            distanceStr = String.format("%.2f km", kilometers);
        } else {
            distanceStr = String.valueOf((int) Math.round(meters)) + " m";
        }
        return distanceStr;
    }
}
