package com.eservicebook.app.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import static com.eservicebook.app.api.APIConstants.*;

public class HistoryRepair implements Comparable<HistoryRepair> {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final ArrayList<String> repairs;
    private final int mileage;
    private final Date update;
    private final String service;
    private final BigDecimal costParts;
    private final BigDecimal costService;

    public HistoryRepair(JSONObject historyJson) throws JSONException {
        JSONArray repairsJson = historyJson.getJSONArray(REPAIRS);
        ArrayList<String> repairs = new ArrayList<>();
        for (int i = 0; i < repairsJson.length(); i++) {
            repairs.add(repairsJson.getString(i));
        }

        this.repairs = repairs;
        this.mileage = historyJson.getInt(MILEAGE);
        this.update = parseToDate(historyJson.getString(U_DATE));
        this.service = historyJson.getString(SERVICE);
        this.costParts = new BigDecimal(historyJson.getString(COST_PARTS));
        this.costService = new BigDecimal(historyJson.getString(COST_SERVICE));
    }

    @Override
    public int compareTo(HistoryRepair other) {
        return -getUpdate().compareTo(other.getUpdate());
    }

    public String getJson() throws JSONException {
        JSONObject historyJson = new JSONObject();
        JSONArray repairsJson = new JSONArray();

        for (String repair : repairs) {
            repairsJson.put(repair);
        }

        historyJson.put(REPAIRS, repairsJson);
        historyJson.put(MILEAGE, mileage);
        historyJson.put(U_DATE, getFormattedUpdate());
        historyJson.put(SERVICE, service);
        historyJson.put(COST_PARTS, costParts);
        historyJson.put(COST_SERVICE, costService);

        return historyJson.toString();
    }

    public ArrayList<String> getRepairs() {
        return repairs;
    }

    public int getMileage() {
        return mileage;
    }

    public Date getUpdate() {
        return update;
    }

    public String getFormattedUpdate() {
        return dateFormat.format(update);
    }

    public String getService() {
        return service;
    }

    public BigDecimal getCostParts() {
        return costParts;
    }

    public BigDecimal getCostService() {
        return costService;
    }

    private Date parseToDate(String string) {
        Date date;
        try {
            date = dateFormat.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
            date = new Date();
        }
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HistoryRepair that = (HistoryRepair) o;
        return mileage == that.mileage &&
                Objects.equals(dateFormat, that.dateFormat) &&
                Objects.equals(repairs, that.repairs) &&
                Objects.equals(update, that.update) &&
                Objects.equals(service, that.service) &&
                Objects.equals(costParts, that.costParts) &&
                Objects.equals(costService, that.costService);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateFormat, repairs, mileage, update, service, costParts, costService);
    }
}
