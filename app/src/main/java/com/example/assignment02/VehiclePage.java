/*
 * @author Juan Carlos Pasillas
 * StudentID: 017350864
 * California State University, Long Beach
 * CECS 453 Section 05
 * Professor: Arjang Fahim
 * @date April 2020
 *
 * Github Project: https://github.com/CECS453-MobileAppDevelopment/Assignment02
 */

package com.example.assignment02;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

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

public class VehiclePage extends AppCompatActivity implements VehicleAdapter.OnItemClickListener {

    private Spinner makeSpinner; //spinner to hold make list
    private Spinner modelSpinner; //spinner to hold model list
    private TextView vehicleSearch; //textview to show search parameters
    private RecyclerView vehicleRecycler; //recycler view to hold vehicle items
    private VehicleAdapter vehicleAdapter; //adapter to format and hold vehicle items
    private ArrayList<VehicleItem> vehicleItemList; //vehicle item list to populate the recycler view

    private ArrayList<HashMap<String, String>> vehicleMakeList = new ArrayList<>(); //vehicle make list data
    private ArrayList<HashMap<String, String>> vehicleModelList = new ArrayList<>(); //vehicle model list data
    private HashMap<String, String> vehicleDetails = new HashMap<>(); //vehicle detail data
    private ArrayList<String> vehicleMakes = new ArrayList<>(); //vehicle make name list for spinner
    private ArrayList<String> vehicleModels =  new ArrayList<>(); //vehicle model name list for spinner
    private ArrayAdapter<String> makeSpinnerAdapter; //adapter to format make spinner data
    private ArrayAdapter<String> modelSpinnerAdapter; //adapter to format model spinner data

    private RequestQueue mQueue; //queue to parse json using volley
    private VehicleDetailFragment fragment = null; //vehicle detail fragment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_page);
        mQueue = Volley.newRequestQueue(this);
        vehicleSearch = findViewById(R.id.vehicle_search);

        makeSpinner = findViewById(R.id.spinner_vehicle_make);
        vehicleMakes.add("- Vehicle Make -");
        makeSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, vehicleMakes);
        makeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        makeSpinner.setAdapter(makeSpinnerAdapter);
        jsonParseMakes();

        modelSpinner = findViewById(R.id.spinner_vehicle_model);
        vehicleModels.add("- Vehicle Model -");
        modelSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, vehicleModels);
        modelSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modelSpinner.setAdapter(modelSpinnerAdapter);

        vehicleRecycler = findViewById(R.id.recycler_vehicle);
        vehicleRecycler.setLayoutManager(new LinearLayoutManager(this));
        vehicleItemList = new ArrayList<>();

        vehicleAdapter = new VehicleAdapter(VehiclePage.this, vehicleItemList);
        vehicleRecycler.setAdapter(vehicleAdapter);
        vehicleAdapter.setOnItemClickListener(VehiclePage.this);

        //make spinner element has been selected
        makeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //clear model data
                vehicleModelList.clear();
                vehicleModels.clear();
                //clear recycler view data
                vehicleSearch.setText("");
                vehicleItemList.clear();
                vehicleAdapter.notifyDataSetChanged();
                //remove vehicle detail fragment
                if(fragment != null) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().remove(fragment).commit();
                    fragment = null;
                }
                //update model list
                if(position == 0) {
                    vehicleModels.add("- Vehicle Model -");
                    modelSpinnerAdapter.notifyDataSetChanged();
                } else {
                    String make_id = vehicleMakeList.get(position-1).get("vehicle_make_id");
                    String url = "https://thawing-beach-68207.herokuapp.com/carmodelmakes/" + make_id;
                    vehicleModels.add("- Vehicle Model -");
                    jsonParseModels(url);
                    modelSpinner.setSelection(0);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //required override, ignore
            }
        });

        //model spinner element has been selected
        modelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //clear recycler view data
                vehicleSearch.setText("");
                vehicleItemList.clear();
                vehicleAdapter.notifyDataSetChanged();
                //remove vehicle detail fragment
                if(fragment != null) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().remove(fragment).commit();
                    fragment = null;
                }
                //update recycler view data
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
                //required override, ignore
            }
        });

    }

    /* Get make list on create to populate the make spinner */
    private void jsonParseMakes() {
        String url = "https://thawing-beach-68207.herokuapp.com/carmakes";
        Log.d("testURL", "parseMakes: " + url);
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

    /* Get model list after selecting a make to populate the model spinner */
    private void jsonParseModels(String url) {
        Log.d("testURL", "parseModels: " + url);
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

    /* Get vehicle list after selecting a model to populate the recycler view */
    private void jsonParseVehicleList(String url) {
        Log.d("testURL", "parseVehicleList: " + url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("lists");

                            for(int i = 0; i < jsonArray.length(); i++) {
                                JSONObject vehicle_info = jsonArray.getJSONObject(i);
                                String id = vehicle_info.getString("id");
                                String imageURL = vehicle_info.getString("image_url");
                                String make = vehicle_info.getString("vehicle_make");
                                String model = vehicle_info.getString("model");
                                String miles = vehicle_info.getString("mileage") + " miles";
                                String description = vehicle_info.getString("veh_description");
                                int index = description.indexOf('.');
                                String year = description.substring(index+7, index+11);
                                String name = year + " " + make + " " + model;

                                vehicleItemList.add(new VehicleItem(id, imageURL, name, miles));
                            }

                            if(vehicleItemList.size() != 0) {
                                String search = vehicleItemList.get(0).getVehicleName().substring(5);
                                vehicleSearch.setText("Showing results for: " + search);
                                vehicleAdapter.notifyDataSetChanged();
                            }
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

    /* Item clicked on recycler view is used to inflate the detail fragment */
    @Override
    public void onItemClick(int position) {
        final VehicleItem clickedItem = vehicleItemList.get(position);
        String url = "https://thawing-beach-68207.herokuapp.com/cars/" + clickedItem.getVehicleID();

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject vehicle = response.getJSONObject(0);
                            String price = "$" + vehicle.getString("price");
                            String description = vehicle.getString("veh_description");
                            String update = vehicle.getString("updated_at");
                            vehicleDetails.put("price", price);
                            vehicleDetails.put("description", description);
                            vehicleDetails.put("update", update);

                            //start the vehicle detail fragment using the clicked vehicle
                            if(fragment != null) {
                                FragmentManager fragmentManager = getSupportFragmentManager();
                                fragmentManager.beginTransaction().remove(fragment).commit();
                            }
                            fragment = VehicleDetailFragment.newInstance(clickedItem.getVehicleName(), vehicleDetails.get("price"),
                                    clickedItem.getImageURL(), clickedItem.getVehicleMiles(), vehicleDetails.get("description"), vehicleDetails.get("update"));
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction transaction = fragmentManager.beginTransaction();
                            transaction.add(R.id.fragment_container, fragment, "VEHICLE_FRAGMENT");
                            transaction.addToBackStack(null);
                            transaction.commit();
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

    /* Used to update fragment layout on orientation change */
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            //removing old fragment with old layout
            fragmentManager.beginTransaction().remove(fragment).commit();
            //re-adding fragment to use the updated layout
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.fragment_container, fragment, "VEHICLE_FRAGMENT");
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}
