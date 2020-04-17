package com.example.assignment02;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class VehiclePage extends AppCompatActivity {

    private RequestQueue mQueue;
    private Spinner makeSpinner;
    private Spinner modelSpinner;
    private RecyclerView vehicleRecycler;
    private VehicleAdapter vehicleAdapter;
    private ArrayList<VehicleItem> vehicleItemList;

    private ArrayList<HashMap<String, String>> vehicleMakeList = new ArrayList<>();
    private ArrayList<HashMap<String, String>> vehicleModelList = new ArrayList<>();
    private ArrayList<String> vehicleMakes = new ArrayList<>();
    private ArrayList<String> vehicleModels =  new ArrayList<>();
    private ArrayAdapter<String> makeSpinnerAdapter;
    private ArrayAdapter<String> modelSpinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_page);
        mQueue = Volley.newRequestQueue(this);

        makeSpinner = findViewById(R.id.spinner_vehicle_make);
        vehicleMakes.add("- Vehicle Make -");
        makeSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, vehicleMakes);
        makeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jsonParseMakes();
        makeSpinner.setAdapter(makeSpinnerAdapter);

        modelSpinner = findViewById(R.id.spinner_vehicle_model);
        vehicleModels.add("- Vehicle Model -");
        modelSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, vehicleModels);
        modelSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modelSpinner.setAdapter(modelSpinnerAdapter);

        vehicleRecycler = findViewById(R.id.recycler_vehicle);
        vehicleRecycler.setLayoutManager(new LinearLayoutManager(this));
        vehicleItemList = new ArrayList<>();

        makeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    vehicleModelList.clear();
                    vehicleModels.clear();
                    vehicleModels.add("- Vehicle Model -");
                    modelSpinnerAdapter.notifyDataSetChanged();
                } else {
                    String make_id = vehicleMakeList.get(position-1).get("vehicle_make_id");
                    String url = "https://thawing-beach-68207.herokuapp.com/carmodelmakes/" + make_id;
                    vehicleModelList.clear();
                    vehicleModels.clear();
                    vehicleItemList.clear();
                    vehicleModels.add("- Vehicle Model -");
                    jsonParseModels(url);
                    modelSpinner.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        modelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0) {
                    String make_id = vehicleModelList.get(position-1).get("vehicle_make_id");
                    String model_id = vehicleModelList.get(position-1).get("vehicle_model_id");
                    String url = "https://thawing-beach-68207.herokuapp.com/cars/" + make_id + "/" + model_id + "/92603";
                    vehicleItemList.clear();

                    jsonParseVehicleList(url);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void jsonParseMakes() {
        String url = "https://thawing-beach-68207.herokuapp.com/carmakes";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for(int i = 0; i < response.length(); i++) {
                                JSONObject car = response.getJSONObject(i);
                                String vehicle_make_id = String.valueOf(car.getInt("id"));
                                String vehicle_make = car.getString("vehicle_make");

                                HashMap<String, String> vehicle_make_info = new HashMap<>();
                                vehicle_make_info.put("vehicle_make_id", vehicle_make_id);
                                vehicle_make_info.put("vehicle_make", vehicle_make);

                                vehicleMakeList.add(vehicle_make_info);
                            }

                            for(int i = 0; i < vehicleMakeList.size(); i++) {
                                vehicleMakes.add(vehicleMakeList.get(i).get("vehicle_make"));
                            }

                            makeSpinnerAdapter.notifyDataSetChanged();
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
    }

    private void jsonParseModels(String url) {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for(int i = 0; i < response.length(); i++) {
                                JSONObject car = response.getJSONObject(i);
                                String vehicle_make_id = String.valueOf(car.getInt("vehicle_make_id"));
                                String vehicle_model_id = String.valueOf(car.getInt("id"));
                                String vehicle_model = car.getString("model");

                                HashMap<String, String> vehicle_model_info = new HashMap<>();
                                vehicle_model_info.put("vehicle_make_id", vehicle_make_id);
                                vehicle_model_info.put("vehicle_model_id", vehicle_model_id);
                                vehicle_model_info.put("vehicle_model", vehicle_model);

                                vehicleModelList.add(vehicle_model_info);
                            }

                            for(int i = 0; i < vehicleModelList.size(); i++) {
                                vehicleModels.add(vehicleModelList.get(i).get("vehicle_model"));
                            }

                            modelSpinnerAdapter.notifyDataSetChanged();
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
    }

    private void jsonParseVehicleList(String url) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("lists");

                            for(int i = 0; i < jsonArray.length(); i++) {
                                JSONObject vehicle_info = jsonArray.getJSONObject(i);

                                String imageURL = vehicle_info.getString("image_url");
                                String make = vehicle_info.getString("vehicle_make");
                                String model = vehicle_info.getString("model");
                                String name = make + " " + model;

                                String description = vehicle_info.getString("veh_description");
                                int index = description.indexOf('.');
                                String year = "Year: " + description.substring(index+7, index+11);

                                vehicleItemList.add(new VehicleItem(imageURL, name, year));
                            }

                            vehicleAdapter = new VehicleAdapter(VehiclePage.this, vehicleItemList);
                            vehicleRecycler.setAdapter(vehicleAdapter);

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
    }
}
