package com.example.assignment02;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VehiclePage extends AppCompatActivity {

    private TextView loggedInUsername;
    private RequestQueue mQueue;
    private Spinner mSpinner;

    private ArrayList<String> vehicles;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_page);

        loggedInUsername = findViewById(R.id.logged_in_username);

        Intent intent = getIntent();
        //username taken from log in page
        String username = intent.getStringExtra(MainActivity.EXTRA_USERNAME);
        loggedInUsername.setText(" " + username + "!");

        mSpinner = findViewById(R.id.spinner_view);

        mQueue = Volley.newRequestQueue(this);
        vehicles = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, vehicles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jsonParse();

        mSpinner.setAdapter(adapter);
    }

    private void jsonParse() {
        Log.d("JSONTest", "start");
        String url = "https://thawing-beach-68207.herokuapp.com/carmakes";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for(int i = 0; i < response.length(); i++) {
                                JSONObject car = response.getJSONObject(i);
                                String vehicle_make = car.getString("vehicle_make");
                                vehicles.add(vehicle_make);
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(request);
        Log.d("JSONTest", "end");
    }
}
