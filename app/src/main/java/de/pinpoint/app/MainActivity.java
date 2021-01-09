package de.pinpoint.app;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private boolean active = false;
    private boolean mapActive = false;
    private MapFragment mapFragment;
    private ListFragment listFragment;
    private Toolbar bottomAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.locationFab);

        PinPoint.getUiAccess().setFab(fab);

        fab.setImageTintList(ContextCompat.getColorStateList(this, R.color.icons));

        fab.setOnClickListener(view -> {
            Snackbar snackbar;
            if (PinPoint.getLogic().isUpdaterRunning()) {
                PinPoint.getLogic().stopUpdater();
                mapFragment.onUpdate(Collections.emptyList());
                PinPoint.getLogic().clearUserInfoAdapter();
                snackbar = Snackbar.make(view, "Location sharing deactivated", Snackbar.LENGTH_LONG);
            } else {
                PinPoint.getLogic().startUpdater();
                snackbar = Snackbar.make(view, "Location sharing activated", Snackbar.LENGTH_LONG);
            }
            snackbar.setAnchorView(view);
            snackbar.show();
        });

        bottomAppBar = findViewById(R.id.bottomAppBar);

        bottomAppBar.setNavigationOnClickListener(view -> {
            mapActive = !mapActive;
            switchFragment();
        });

        bottomAppBar.setOnMenuItemClickListener(item -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            return false;
        });

        requestPermissionsIfNecessary(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        });

        PinPoint.getLogic().setAContext(this);

        mapFragment = new MapFragment();

        PinPoint.getLogic().addUpdateListener(mapFragment);

        listFragment = new ListFragment();

        listFragment.setSwitchFragment(info -> {
            mapFragment.setSelectedUser(info);
            mapActive = !mapActive;
            switchFragment();
        });

        switchFragment();

        if (PinPoint.getLogic().getName().equals("")) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            permissionsToRequest.add(permissions[i]);
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void switchFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Drawable icon;
        if (mapActive) {
            ft.replace(R.id.fragment, mapFragment);
            icon = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_arrow_back);
        } else {
            ft.replace(R.id.fragment, listFragment);
            icon = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_map);
        }
        icon.setTint(getResources().getColor(R.color.icons));
        bottomAppBar.setNavigationIcon(icon);
        ft.commit();
    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }
}