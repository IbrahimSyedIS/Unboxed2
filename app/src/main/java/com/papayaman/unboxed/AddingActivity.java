package com.papayaman.unboxed;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.*;

/* TODO
    : add more information about each sale (seller, stuff in sale, maybe a way of getting in touch with seller)
    : update layout and design
    : Add dialog box letting user know if the address was not found when they submit
 */
public class AddingActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private EditText datePicker;
    private int year, month, day;

    // Called when the user selects the date of sale
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        datePicker.setText(new StringBuilder()
                .append(String.format(Locale.getDefault(), "%02d", month + 1)).append("/").append(String.format(Locale.getDefault(), "%02d", day)).append("/").append(String.format(Locale.getDefault(), "%02d", year % 100)).append(" "));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding);

        datePicker = findViewById(R.id.textView2);
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
                Address a;
                try {
                    List<Address> addresses = g.getFromLocationName(address, 1);
                    if (addresses.size() > 0)
                        a = addresses.get(0);
                    else {
                        // TODO: Add dialog box letting user know that the address was not found
                        Log.e("submit", "No addresses found with the name: \"" + address + "\"");
                        startActivity(new Intent(AddingActivity.this, MapsActivity.class));
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("submit", "IOException getting list of addresses from name");
                    startActivity(new Intent(AddingActivity.this, MapsActivity.class));
                    return;
                }
                double lat = a.getLatitude();
                double lng = a.getLongitude();
                Client.sendNewSale(new Sale(lat,lng, month, day, year));
                Intent back = new Intent(AddingActivity.this, MapsActivity.class);
                startActivity(back);
            }
        });
    }
}
