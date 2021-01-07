package de.pinpoint.app;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
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
import java.util.List;

import de.pinpoint.client.LocationClient.UserInfo;

public class UserInfoAdapter extends ArrayAdapter<UserInfo> {

    private Context mContext;
    private List<UserInfo> userInfoList = new ArrayList<>();

    public UserInfoAdapter(@NonNull Context context, ArrayList<UserInfo> list) {
        super(context, 0 , list);
        mContext = context;
        userInfoList = list;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);

        UserInfo currentUserInfo = userInfoList.get(position);

        TextView name = (TextView) listItem.findViewById(R.id.name);
        name.setText(currentUserInfo.getName());

        TextView distance = (TextView) listItem.findViewById(R.id.distance);
        distance.setText("Test");

        ImageView color = (ImageView) listItem.findViewById(R.id.color);

        Drawable unwrappedDrawable = AppCompatResources.getDrawable(mContext, R.drawable.ic_circle);
        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
        DrawableCompat.setTint(wrappedDrawable, Color.parseColor(currentUserInfo.getColor()));

        color.setImageDrawable(wrappedDrawable);

        return listItem;
    }
}
