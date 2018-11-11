package com.papayaman.unboxed;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class AddingActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private EditText datePicker;

    private int month, day, year;

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        datePicker.setText(new StringBuilder()
                .append(String.format("%02d", month + 1)).append("/").append(String.format("%02d", day)).append("/").append(String.format("%02d", year % 100)).append(" "));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding);

        datePicker = (EditText) findViewById(R.id.textView2);
        datePicker.setFocusable(false);
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View l) {
                Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

                DatePickerDialog dialog = new DatePickerDialog(AddingActivity.this, AddingActivity.this,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });

        final Button submit = findViewById(R.id.submitButton);
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String date = ((EditText) findViewById(R.id.textView2)).getText().toString();
                System.out.println(date);
                String address = ((EditText) findViewById(R.id.textView4)).getText().toString();
                Geocoder g = new Geocoder(AddingActivity.this);
                Address a = null;
                try {
                    a = g.getFromLocationName(address, 1).get(0);
                } catch (IOException e) {
                    e.printStackTrace();
                    a = new Address(Locale.ENGLISH);
                }
                double lat = a.getLatitude();
                double lng = a.getLongitude();
                double m = Double.parseDouble(date.substring(0,2));
                double d = Double.parseDouble(date.substring(3,5));
                double y = Double.parseDouble(date.substring(6,8));
                MapsActivity.getClient().send(new Double[]{lat,lng,m,d,y});
                Intent back = new Intent(AddingActivity.this, MapsActivity.class);
                startActivity(back);
            }
        });
    }
}
