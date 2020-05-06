package com.example.assignment02;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Locale;

public class VehicleDetailFragment extends Fragment {

    private static final String VEH_NAME = "name";
    private static final String VEH_PRICE = "price";
    private static final String VEH_IMAGE = "image";
    private static final String VEH_MILES = "miles";
    private static final String VEH_DESC = "description";
    private static final String VEH_DATE = "update";

    private String vehicle_name;
    private String vehicle_price;
    private String vehicle_image;
    private String vehicle_mileage;
    private String vehicle_description;
    private String vehicle_update;

    private TextView vehicleName;
    private TextView vehiclePrice;
    private ImageView vehicleImage;
    private TextView vehicleMileage;
    private TextView vehicleDescription;
    private TextView vehicleUpdate;

    private View view;
    private FrameLayout frameLayout;

    public VehicleDetailFragment() {
        // Required empty public constructor
    }

    public static VehicleDetailFragment newInstance(String name, String price, String image, String miles, String desc, String date) {
        VehicleDetailFragment fragment = new VehicleDetailFragment();
        Bundle args = new Bundle();
        args.putString(VEH_NAME, name);
        args.putString(VEH_PRICE, price);
        args.putString(VEH_IMAGE, image);
        args.putString(VEH_MILES, miles);
        args.putString(VEH_DESC, desc);
        args.putString(VEH_DATE, date);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            vehicle_name = getArguments().getString(VEH_NAME);
            vehicle_price = getArguments().getString(VEH_PRICE);
            vehicle_image = getArguments().getString(VEH_IMAGE);
            vehicle_mileage = getArguments().getString(VEH_MILES);
            vehicle_description = getArguments().getString(VEH_DESC);
            vehicle_update = getArguments().getString(VEH_DATE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_vehicle_detail, container, false);
        frameLayout = new FrameLayout(getActivity());

        vehicleName = view.findViewById(R.id.vehicle_name);
        vehiclePrice = view.findViewById(R.id.vehicle_price);
        vehicleImage = view.findViewById(R.id.vehicle_image);
        vehicleMileage = view.findViewById(R.id.vehicle_mileage);
        vehicleDescription = view.findViewById(R.id.vehicle_description);
        vehicleUpdate = view.findViewById(R.id.vehicle_update);

        vehicleName.setText(vehicle_name);
        vehiclePrice.setText("Price: " + vehicle_price + "0");
        Log.d("imageTest", vehicle_image);
        if(!vehicle_image.equals("")) {
            Picasso.get().load(vehicle_image).error(R.drawable.vehicle_image_na).into(vehicleImage);
        }
        vehicleMileage.setText("Mileage: " + vehicle_mileage);
        vehicleDescription.setText(vehicle_description);
        vehicleUpdate.setText("Last Updated on " + vehicle_update);

        return view;
    }
}
