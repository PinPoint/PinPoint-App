package de.pinpoint.app;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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

import java.util.ArrayList;
import java.util.Collection;

import de.pinpoint.app.logic.GPSException;
import de.pinpoint.app.util.PositionUtil;
import de.pinpoint.app.util.UserInfoDistanceComparator;
import de.pinpoint.client.dataprovider.UpdateListener;
import de.pinpoint.client.locationclient.PinPointPosition;
import de.pinpoint.client.locationclient.UserInfo;

public class UserInfoAdapter extends ArrayAdapter<UserInfo> implements UpdateListener {

    private Context mContext;
    private Context aContext;

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
            listItem = LayoutInflater.from(aContext).inflate(R.layout.list_item, parent, false);

        UserInfo currentUserInfo = this.getItem(position);

        TextView name = (TextView) listItem.findViewById(R.id.name);
        name.setText(currentUserInfo.getName());

        TextView distance = (TextView) listItem.findViewById(R.id.distance);

        try {
            PinPointPosition ownPosition = PinPoint.getLogic().getOwnPosition();
            String distanceStr = PositionUtil.getDistanceStr(ownPosition, currentUserInfo.getPosition());
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

            try {
                PinPointPosition ownPosition = PinPoint.getLogic().getOwnPosition();
                this.sort(new UserInfoDistanceComparator(ownPosition));
            } catch (GPSException e) {
            }
        });
    }

    public void setAContext(Context aContext) {
        this.aContext = aContext;
    }
}
