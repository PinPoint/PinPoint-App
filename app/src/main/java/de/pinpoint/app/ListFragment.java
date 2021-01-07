package de.pinpoint.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.UUID;

import de.pinpoint.client.LocationClient.PinPointPosition;
import de.pinpoint.client.LocationClient.UserInfo;

public class ListFragment extends Fragment {
    private ListView listView;
    private UserInfoAdapter uAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        listView = (ListView) view.findViewById(R.id.userinfo_list);

        ArrayList<UserInfo> userInfoList = new ArrayList<>();
        userInfoList.add(new UserInfo(UUID.randomUUID(), "Steve", "#f44336", new PinPointPosition(0.0, 0.1)));
        userInfoList.add(new UserInfo(UUID.randomUUID(), "Carl", "#E91E63", new PinPointPosition(0.0, 0.1)));
        userInfoList.add(new UserInfo(UUID.randomUUID(), "Anna", "#9C27B0", new PinPointPosition(0.0, 0.1)));
        userInfoList.add(new UserInfo(UUID.randomUUID(), "Mia", "#f44336", new PinPointPosition(0.0, 0.1)));
        userInfoList.add(new UserInfo(UUID.randomUUID(), "Steve", "#E91E63", new PinPointPosition(0.0, 0.1)));
        userInfoList.add(new UserInfo(UUID.randomUUID(), "Steve", "#f44336", new PinPointPosition(0.0, 0.1)));
        userInfoList.add(new UserInfo(UUID.randomUUID(), "Steve", "#673AB7", new PinPointPosition(0.0, 0.1)));
        userInfoList.add(new UserInfo(UUID.randomUUID(), "Steve", "#673AB7", new PinPointPosition(0.0, 0.1)));
        userInfoList.add(new UserInfo(UUID.randomUUID(), "Steve", "#f44336", new PinPointPosition(0.0, 0.1)));
        userInfoList.add(new UserInfo(UUID.randomUUID(), "Steve", "#3F51B5", new PinPointPosition(0.0, 0.1)));
        userInfoList.add(new UserInfo(UUID.randomUUID(), "Steve", "#f44336", new PinPointPosition(0.0, 0.1)));
        userInfoList.add(new UserInfo(UUID.randomUUID(), "Steve", "#E91E63", new PinPointPosition(0.0, 0.1)));
        userInfoList.add(new UserInfo(UUID.randomUUID(), "Steve", "#3F51B5", new PinPointPosition(0.0, 0.1)));
        userInfoList.add(new UserInfo(UUID.randomUUID(), "Steve", "#f44336", new PinPointPosition(0.0, 0.1)));
        userInfoList.add(new UserInfo(UUID.randomUUID(), "Steve", "#E91E63", new PinPointPosition(0.0, 0.1)));

        uAdapter = new UserInfoAdapter(getActivity(), userInfoList);
        listView.setAdapter(uAdapter);
    }
}
