package de.pinpoint.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.content.res.AppCompatResources;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    private int theme = 0;

    private EditText name = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextInputLayout nameLayout = findViewById(R.id.nameTextField);
        name = nameLayout.getEditText();
        name.setText(PinPoint.getLogic().getName());

        theme = PinPoint.getLogic().getTheme();

        Spinner spinner = findViewById(R.id.themeSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.darkmode_array, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(theme);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                theme = position;
                PinPoint.getLogic().changePreferences(name.getText().toString(), "#324225", theme);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button button = findViewById(R.id.nextButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });
    }

    public void onBackPressed() {
        goBack();
    }

    private void goBack() {
        if (name.getText().length() < 3)
            name.setError("Name should be at least 3 characters");
        else {
            PinPoint.getLogic().changePreferences(name.getText().toString(), "#324225", theme);
            setResult(Activity.RESULT_OK, new Intent());
            finish();
        }
    }
}