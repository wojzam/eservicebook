package com.eservicebook.app.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;
import com.eservicebook.app.Observer;
import com.eservicebook.app.Subject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;

public class DataManager implements Subject {

    public static final String FILENAME = "LocalData";
    public static final String KEY_VEHICLES = "vehicles";
    public static final String KEY_CHECK_BOX = "remember";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_AUTH_EMAIL = "auth_email";
    public static final String KEY_PASSWORD = "password";

    private static volatile DataManager instance = null;
    private final ArrayList<Observer> observers = new ArrayList<>();
    private final ArrayList<Vehicle> newVehicles = new ArrayList<>();
    private final ArrayList<Vehicle> vehicles = new ArrayList<>();
    private String sessionId;
    private Context context;
    private SharedPreferences sharedPreferences;

    private DataManager() {
        if (instance != null) {
            throw new RuntimeException("Not allowed. Please use getInstance() method");
        }
    }

    public static DataManager getInstance() {
        if (instance == null) {
            synchronized (DataManager.class) {
                if (instance == null) {
                    instance = new DataManager();
                }
            }
        }
        return instance;
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.refreshViews();
        }
        setSessionId(null);
    }

    public void initialize(Context context) {
        if (this.context == null) {
            this.context = context;
        }
        observers.clear();
        vehicles.clear();
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
        observer.refreshViews();
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public ArrayList<Vehicle> getVehicles() {
        return vehicles;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void save(String key, Object value) {
        try {
            SharedPreferences sharedPref = getSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPref.edit();
            if (value.getClass().equals(String.class)) {
                editor.putString(key, (String) value);
            } else if (value.getClass().equals(Boolean.class)) {
                editor.putBoolean(key, (Boolean) value);
            }
            editor.apply();
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    public String readString(String key) {
        String value = "";
        try {
            SharedPreferences sharedPref = getSharedPreferences(context);
            value = sharedPref.getString(key, "");
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
        return value;
    }

    public boolean readBoolean(String key) {
        boolean value = true;
        try {
            SharedPreferences sharedPref = getSharedPreferences(context);
            value = sharedPref.getBoolean(key, true);
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
        return value;
    }

    public void saveVehicles() {
        JSONArray vehiclesJson = new JSONArray();
        for (Vehicle vehicle : vehicles) {
            try {
                vehiclesJson.put(vehicle.getJson());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        save(KEY_VEHICLES, vehiclesJson.toString());
    }

    public void addVehiclesFromJson(JSONArray vehiclesJson) throws JSONException {
        if (vehiclesJson != null) {
            newVehicles.clear();
            for (int i = 0; i < vehiclesJson.length(); i++) {
                JSONObject vehicleJson = new JSONObject(vehiclesJson.get(i).toString());
                newVehicles.add(new Vehicle(vehicleJson));
            }
            if (!vehicles.equals(newVehicles)) {
                replaceVehiclesAndNotify();
                saveVehicles();
            }
        }
    }

    public void readLocalStoredVehicles() {
        try {
            JSONArray vehiclesJson = new JSONArray(readString(KEY_VEHICLES));
            addVehiclesFromJson(vehiclesJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void clearAuthorizedLocalData() {
        setSessionId(null);
        save(KEY_AUTH_EMAIL, "");
        save(KEY_VEHICLES, "");
        vehicles.clear();
    }

    private void replaceVehiclesAndNotify() {
        vehicles.clear();
        vehicles.addAll(newVehicles);
        notifyObservers();
    }

    private SharedPreferences getSharedPreferences(Context context) throws GeneralSecurityException, IOException {
        if (sharedPreferences == null) {
            KeyGenParameterSpec spec = new KeyGenParameterSpec.Builder(
                    MasterKey.DEFAULT_MASTER_KEY_ALIAS,
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .setKeySize(256)
                    .build();

            MasterKey masterKey = new MasterKey.Builder(context)
                    .setKeyGenParameterSpec(spec)
                    .build();

            sharedPreferences = EncryptedSharedPreferences.create(
                    context,
                    FILENAME,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
        }
        return sharedPreferences;
    }
}