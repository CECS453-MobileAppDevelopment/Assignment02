package com.example.assignment02;

public class VehicleItem {

    private String vehicleID;
    private String imageURL;
    private String vehicleName;
    private String vehicleMiles;

    public VehicleItem(String id, String image_url, String vehicle_name, String miles) {
        vehicleID = id;
        imageURL = image_url;
        vehicleName = vehicle_name;
        vehicleMiles = miles;
    }

    public String getVehicleID() {
        return vehicleID;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public String getVehicleMiles() {
        return vehicleMiles;
    }
}
