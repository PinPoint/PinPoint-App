package de.pinpoint.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private boolean active = false;

    private boolean mapActive = false;

    private Fragment mapFragment;
    private Fragment listFragment;

    private Toolbar bottomAppBar;

    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.locationFab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar snackbar;
                active = !active;
                if (active) {
                    view.setBackgroundTintList(AppCompatResources.getColorStateList(getApplicationContext(), R.color.colorSuccess));
                    snackbar = Snackbar.make(view, "Location sharing activated", Snackbar.LENGTH_LONG);
                } else {
                    view.setBackgroundTintList(AppCompatResources.getColorStateList(getApplicationContext(), R.color.colorError));
                    snackbar = Snackbar.make(view, "Location sharing deactivated", Snackbar.LENGTH_LONG);
                }
                snackbar.setAnchorView(view);
                snackbar.show();
            }
        });

        bottomAppBar = findViewById(R.id.bottomAppBar);

        bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapActive = !mapActive;
                switchFragment();
            }
        });

        bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                System.out.println(PinPoint.getLogic());
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                return false;
            }
        });

        requestPermissionsIfNecessary(new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        });

        mapFragment = new MapFragment();
        listFragment = new ListFragment();

        switchFragment();

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
        if (mapActive) {
            ft.replace(R.id.fragment, mapFragment);
            bottomAppBar.setNavigationIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_arrow_back));

        }
        else {
            ft.replace(R.id.fragment, listFragment);
            bottomAppBar.setNavigationIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_map));
        }
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