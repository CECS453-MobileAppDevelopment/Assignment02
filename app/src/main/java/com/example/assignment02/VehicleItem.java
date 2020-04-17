package com.example.assignment02;

public class VehicleItem {

    private String imageURL;
    private String vehicleName;
    private String vehicleYear;

    public VehicleItem(String image_url, String vehicle_name, String year) {
        imageURL = image_url;
        vehicleName = vehicle_name;
        vehicleYear = year;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public String getVehicleYear() {
        return vehicleYear;
    }
}
