package de.pinpoint.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.UUID;

import de.pinpoint.client.locationclient.PinPointPosition;
import de.pinpoint.client.locationclient.UserInfo;

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

        uAdapter = PinPoint.getLogic().getUAdapter();

        listView.setAdapter(uAdapter);
    }
}
