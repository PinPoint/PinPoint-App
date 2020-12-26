package de.pinpoint.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private boolean active = false;

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

        Toolbar bottomAppBar = findViewById(R.id.bottomAppBar);

        bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FloatingActionButton fab = findViewById(R.id.locationFab);
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
    }
}