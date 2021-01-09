package de.pinpoint.app.util;

import org.osmdroid.util.GeoPoint;

import de.pinpoint.client.locationclient.PinPointPosition;

public class DistanceUtil {

    public static String getDistanceStr(PinPointPosition pos1, PinPointPosition pos2) {
        double meters = getDistance(pos1, pos2);
        System.out.println(meters);
        String distanceStr;
        if (meters > 999) {
            double kilometers = meters / 1000;
            distanceStr = String.format("%.2f km", kilometers);
        } else {
            distanceStr = String.valueOf((int) Math.round(meters)) + " m";
        }
        return distanceStr;
    }

    public static double getDistance(PinPointPosition pos1, PinPointPosition pos2){
        GeoPoint p1 = new GeoPoint(pos1.getLatitude(), pos2.getLongitude());
        GeoPoint p2 = new GeoPoint(pos2.getLatitude(), pos2.getLongitude());
        double meters = p1.distanceToAsDouble(p2);
        return meters;
    }
}
