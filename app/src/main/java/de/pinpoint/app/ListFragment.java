package de.pinpoint.app;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import de.pinpoint.app.util.Callback;
import de.pinpoint.client.locationclient.UserInfo;

public class ListFragment extends Fragment {
    private ListView listView;
    private UserInfoAdapter uAdapter;
    private Callback<UserInfo> switchFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, parent, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        listView = (ListView) view.findViewById(R.id.userinfo_list);
        uAdapter = PinPoint.getLogic().getUAdapter();
        listView.setAdapter(uAdapter);

        listView.setOnItemClickListener((parent, v, position, id) -> {
            UserInfo userInfo = uAdapter.getItem((int) id);
            switchFragment.call(userInfo);
        });
    }

    public void setSwitchFragment(Callback<UserInfo> switchFragment) {
        this.switchFragment = switchFragment;
    }
}
