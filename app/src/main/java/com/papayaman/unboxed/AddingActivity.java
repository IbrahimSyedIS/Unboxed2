package com.papayaman.unboxed;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class AddingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding);

        final Button submit = findViewById(R.id.submitButton);
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent back = new Intent(AddingActivity.this, MapsActivity.class);
                startActivity(back);
                String date = ((EditText) findViewById(R.id.textView2)).getText().toString();
                String address = ((EditText) findViewById(R.id.textView4)).getText().toString();
                Geocoder g = new Geocoder(AddingActivity.this);
                try {
                    Address a = g.getFromLocationName(address, 1).get(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                
            }
        });
    }
}