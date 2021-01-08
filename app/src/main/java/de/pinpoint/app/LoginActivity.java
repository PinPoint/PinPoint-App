package de.pinpoint.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog;
import com.github.dhaval2404.colorpicker.model.ColorShape;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    private int theme = 0;

    private String color = "#f44336";

    private EditText name = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextInputLayout nameLayout = findViewById(R.id.nameTextField);
        name = nameLayout.getEditText();
        name.setText(PinPoint.getLogic().getName());

        theme = PinPoint.getLogic().getTheme();

        color = PinPoint.getLogic().getColor();

        Spinner spinner = findViewById(R.id.themeSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.darkmode_array, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(theme);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                theme = position;
                PinPoint.getLogic().changePreferences(name.getText().toString(), color, theme);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button button = findViewById(R.id.nextButton);
        button.setOnClickListener(v -> this.goBack());

        final Button colorButton = findViewById(R.id.color);
        colorButton.setBackgroundColor(Color.parseColor(color));
        colorButton.setOnClickListener(view -> {
            String[] colorArray = new String[]{"#f44336", "#E91E63", "#9C27B0",
                    "#673AB7", "#3F51B5", "#2196F3", "#03A9F4", "#4CAF50", "#CDDC39", "#FFC107"};

            new MaterialColorPickerDialog
                    .Builder(LoginActivity.this)
                    .setTitle("Pick your color")
                    .setColorShape(ColorShape.CIRCLE)
                    .setDefaultColor(color)
                    .setColors(colorArray)
                    .setColorListener((color, colorHex) -> {
                        LoginActivity.this.color = colorHex;
                        colorButton.setBackgroundColor(Color.parseColor(colorHex));
                    })
                    .show();
        });
    }

    public void onBackPressed() {
        goBack();
    }

    private void goBack() {
        if (name.getText().length() < 3)
            name.setError("Name should be at least 3 characters");
        else {
            System.out.println(color);
            PinPoint.getLogic().changePreferences(name.getText().toString(), color, theme);
            setResult(Activity.RESULT_OK, new Intent());
            finish();
        }
    }
}