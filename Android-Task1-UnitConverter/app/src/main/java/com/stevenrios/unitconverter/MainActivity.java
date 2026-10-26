package com.stevenrios.unitconverter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private Spinner spinnerCategory, spinnerFrom, spinnerTo;
    private EditText input;
    private TextView result;
    private UnitConverter.Category category = UnitConverter.Category.LENGTH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerFrom = findViewById(R.id.spinnerFrom);
        spinnerTo = findViewById(R.id.spinnerTo);
        input = findViewById(R.id.inputValue);
        result = findViewById(R.id.textResult);
        Button convert = findViewById(R.id.btnConvert);
        Button swap = findViewById(R.id.btnSwap);

        String[] cats = {"Length", "Weight", "Temperature", "Volume", "Speed"};
        spinnerCategory.setAdapter(adapter(cats));
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> p, View v, int pos, long id) {
                category = UnitConverter.Category.values()[pos];
                loadUnits(); // reset unit dropdowns for the new category
            }
            @Override public void onNothingSelected(AdapterView<?> p) {}
        });
        loadUnits();

        convert.setOnClickListener(v -> doConvert());
        swap.setOnClickListener(v -> {
            int f = spinnerFrom.getSelectedItemPosition();
            spinnerFrom.setSelection(spinnerTo.getSelectedItemPosition());
            spinnerTo.setSelection(f);
        });
    }

    private ArrayAdapter<String> adapter(String[] items) {
        ArrayAdapter<String> a = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return a;
    }

    private void loadUnits() {
        String[] units = UnitConverter.units(category);
        spinnerFrom.setAdapter(adapter(units));
        spinnerTo.setAdapter(adapter(units));
        spinnerTo.setSelection(Math.min(1, units.length - 1));
        result.setText("—");
    }

    private void doConvert() {
        String text = input.getText().toString();
        String err = UnitConverter.validate(text);
        if (err != null) {
            Toast.makeText(this, err.equals("empty") ? R.string.err_empty : R.string.err_nan, Toast.LENGTH_SHORT).show();
            return;
        }
        double value = Double.parseDouble(text.trim());
        String from = (String) spinnerFrom.getSelectedItem();
        String to = (String) spinnerTo.getSelectedItem();
        double out = UnitConverter.convert(category, from, to, value);
        result.setText(UnitConverter.format(value, from) + " =\n" + UnitConverter.format(out, to));
    }
}
