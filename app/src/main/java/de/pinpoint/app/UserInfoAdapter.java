package de.pinpoint.app;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.pinpoint.app.logic.GPSException;
import de.pinpoint.client.dataprovider.UpdateListener;
import de.pinpoint.client.locationclient.PinPointPosition;
import de.pinpoint.client.locationclient.UserInfo;

public class UserInfoAdapter extends ArrayAdapter<UserInfo> implements UpdateListener {

    private Context mContext;

    public UserInfoAdapter(@NonNull Context context, ArrayList<UserInfo> list) {
        super(context, 0, list);
        mContext = context;
    }

    public UserInfoAdapter(@NonNull Context context) {
        super(context, 0, new ArrayList<>());
        mContext = context;
    }


    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);

        UserInfo currentUserInfo = this.getItem(position);

        TextView name = (TextView) listItem.findViewById(R.id.name);
        name.setText(currentUserInfo.getName());

        TextView distance = (TextView) listItem.findViewById(R.id.distance);

        try {
            PinPointPosition ownPosition = PinPoint.getLogic().getOwnPosition();
            String distanceStr = getDistanceStr(ownPosition, currentUserInfo.getPosition());
            distance.setText(distanceStr);
        } catch (GPSException ex) {
            distance.setText("");
        }

        ImageView color = (ImageView) listItem.findViewById(R.id.color);

        Drawable unwrappedDrawable = AppCompatResources.getDrawable(mContext, R.drawable.ic_circle);
        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
        DrawableCompat.setTint(wrappedDrawable, Color.parseColor(currentUserInfo.getColor()));

        color.setImageDrawable(wrappedDrawable);

        return listItem;
    }

    @Override
    public void onUpdate(Collection<UserInfo> collection) {
        Handler mainHandler = new Handler(mContext.getMainLooper());
        mainHandler.post(() -> {
            this.clear();
            this.addAll(collection);
        });
    }

    private String getDistanceStr(PinPointPosition pos1, PinPointPosition pos2) {
        GeoPoint p1 = new GeoPoint(pos1.getLatitude(), pos2.getLongitude());
        GeoPoint p2 = new GeoPoint(pos2.getLatitude(), pos2.getLongitude());
        double meters = p1.distanceToAsDouble(p2);
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
}
