package com.eservicebook.app.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import static com.eservicebook.app.api.APIConstants.*;

public class Vehicle {

    private final String vin;
    private final String brand;
    private final String model;
    private final int year;
    private final ArrayList<HistoryRepair> historyRepairs;

    public Vehicle(JSONObject vehicleJson) throws JSONException {
        JSONArray historyRepairsJson = vehicleJson.getJSONArray(HISTORY_REPAIRS);
        ArrayList<HistoryRepair> historyRepairs = new ArrayList<>();
        for (int i = 0; i < historyRepairsJson.length(); i++) {
            JSONObject historyJson = new JSONObject(historyRepairsJson.get(i).toString());
            historyRepairs.add(new HistoryRepair(historyJson));
        }

        this.vin = vehicleJson.getString(VIN);
        this.brand = vehicleJson.getString(VEHICLE_BRAND);
        this.model = vehicleJson.getString(VEHICLE_MODEL);
        this.year = vehicleJson.getInt(VEHICLE_YEAR);
        this.historyRepairs = historyRepairs;
        Collections.sort(historyRepairs);
    }

    public String getJson() throws JSONException {
        JSONObject vehicleJson = new JSONObject();
        JSONArray historyRepairsJson = new JSONArray();

        for (HistoryRepair history : historyRepairs) {
            historyRepairsJson.put(history.getJson());
        }

        vehicleJson.put(VIN, vin);
        vehicleJson.put(VEHICLE_BRAND, brand);
        vehicleJson.put(VEHICLE_MODEL, model);
        vehicleJson.put(VEHICLE_YEAR, year);
        vehicleJson.put(HISTORY_REPAIRS, historyRepairsJson);

        return vehicleJson.toString();
    }

    public String getVin() {
        return vin;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }

    public ArrayList<HistoryRepair> getHistoryRepairs() {
        return historyRepairs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return year == vehicle.year &&
                Objects.equals(vin, vehicle.vin) &&
                Objects.equals(brand, vehicle.brand) &&
                Objects.equals(model, vehicle.model) &&
                Objects.equals(historyRepairs, vehicle.historyRepairs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vin, brand, model, year, historyRepairs);
    }
}
