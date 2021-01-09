package de.pinpoint.app.util;

import java.util.Comparator;

import de.pinpoint.client.locationclient.PinPointPosition;
import de.pinpoint.client.locationclient.UserInfo;

public class UserInfoDistanceComparator implements Comparator<UserInfo> {

    private PinPointPosition fromPositon;

    public UserInfoDistanceComparator(PinPointPosition fromPosition) {
        this.fromPositon = fromPosition;
    }

    @Override
    public int compare(UserInfo u1, UserInfo u2) {
        double distToU1 = DistanceUtil.getDistance(fromPositon, u1.getPosition());
        double distToU2 = DistanceUtil.getDistance(fromPositon, u2.getPosition());
        return Double.compare(distToU1, distToU2);
    }
}
