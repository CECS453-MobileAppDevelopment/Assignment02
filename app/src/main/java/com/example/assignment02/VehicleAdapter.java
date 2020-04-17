package com.example.assignment02;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.VehicleViewHolder> {

    private Context context;
    private ArrayList<VehicleItem> vehicleItemList;

    public VehicleAdapter(Context c, ArrayList<VehicleItem> exampleList) {
        context = c;
        vehicleItemList = exampleList;
    }

    @NonNull
    @Override
    public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.vehicle_item, parent, false);
        return new VehicleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleViewHolder holder, int position) {
        VehicleItem currentItem = vehicleItemList.get(position);

        String imageURL = currentItem.getImageURL();
        String vehicleName = currentItem.getVehicleName();
        String vehicleYear = currentItem.getVehicleYear();

        if(!imageURL.equals("")) {
            Picasso.get().load(imageURL).placeholder(R.drawable.ic_launcher_foreground).error(R.drawable.vehicle_image_na).into(holder.vehicleImage);
        }
        holder.vehicleName.setText(vehicleName);
        holder.vehicleYear.setText(vehicleYear);
    }

    @Override
    public int getItemCount() {
        return vehicleItemList.size();
    }

    public class VehicleViewHolder extends RecyclerView.ViewHolder {

        public ImageView vehicleImage;
        public TextView vehicleName;
        public TextView vehicleYear;

        public VehicleViewHolder(@NonNull View itemView) {
            super(itemView);
            vehicleImage = itemView.findViewById(R.id.vehicle_image);
            vehicleName = itemView.findViewById(R.id.vehicle_name);
            vehicleYear = itemView.findViewById(R.id.vehicle_year);
        }
    }
}
