package de.pinpoint.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import de.pinpoint.app.Logic.Logic;

public class MainActivity extends AppCompatActivity {

    private boolean active = false;

    private Logic logic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.locationFab);

        logic = new Logic(getApplicationContext());

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
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.putExtra("theme", logic.getTheme()).putExtra("name", logic.getName());
                startActivityForResult(intent, 0);
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            int theme = data.getIntExtra("theme", 0);
            String name = data.getStringExtra("name");
            logic.changePreferences(name, "#212323", theme);
        }
    }
}