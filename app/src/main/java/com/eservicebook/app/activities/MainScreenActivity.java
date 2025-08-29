package com.eservicebook.app.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.eservicebook.app.Observer;
import com.eservicebook.app.R;
import com.eservicebook.app.ToastDisplay;
import com.eservicebook.app.api.APIFunctions;
import com.eservicebook.app.data.DataManager;
import com.eservicebook.app.data.Vehicle;
import com.eservicebook.app.views.AddVehicleDialog;
import com.eservicebook.app.views.VehicleSegment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.eservicebook.app.api.APIConstants.CODE;
import static com.eservicebook.app.api.APIConstants.CODE_UNAUTHORIZED;
import static com.eservicebook.app.api.APIConstants.RESPONSES_ADD_VEHICLE;
import static com.eservicebook.app.api.APIConstants.SESSION_ID;
import static com.eservicebook.app.api.APIConstants.VEHICLES;

public class MainScreenActivity extends AppCompatActivity implements Observer {

    private final DataManager dataManager = DataManager.getInstance();
    private final ToastDisplay toastDisplay = ToastDisplay.getInstance();
    private final ArrayList<VehicleSegment> vehicleSegments = new ArrayList<>();
    private Handler addVehicleHandler;
    private Handler getVehicleHandler;
    private Handler authorizeAndGetVehicleHandler;
    private Handler authorizeAndAddVehicleHandler;
    private Handler logoutHandler;
    private AddVehicleDialog dialog;
    private SwipeRefreshLayout pullToRefresh;
    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout = findViewById(R.id.main_layout);
        Button addCarButton = findViewById(R.id.button_add_car);
        Button logoutButton = findViewById(R.id.button_logout);
        pullToRefresh = findViewById(R.id.pull_refresh);

        getVehicleHandler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(Message msg) {
                String output = (String) msg.obj;
                interpretGetVehiclesOutput(output);
                pullToRefresh.setRefreshing(false);
            }
        };

        addVehicleHandler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(Message msg) {
                String output = (String) msg.obj;
                interpretAddVehiclesOutput(output);
            }
        };

        authorizeAndGetVehicleHandler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(Message msg) {
                String output = (String) msg.obj;
                interpretLogInOutput(output);
                APIFunctions.getVehicle(getVehicleHandler, dataManager.getSessionId());
            }
        };

        authorizeAndAddVehicleHandler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(Message msg) {
                String output = (String) msg.obj;
                interpretLogInOutput(output);
                APIFunctions.addVehicle(
                        addVehicleHandler,
                        dataManager.getSessionId(),
                        dialog.getVin(),
                        dialog.getPassword());
            }
        };

        logoutHandler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(Message msg) {
                moveToLoginActivity();
            }
        };

        dialog = new AddVehicleDialog(this, view -> checkIfAuthorizedAndAddVehicles());

        pullToRefresh.setOnRefreshListener(this::checkIfAuthorizedAndGetVehicles);
        addCarButton.setOnClickListener(v -> dialog.showDialog());
        logoutButton.setOnClickListener(v -> showConfirmLogOutDialog());

        dataManager.addObserver(this);
        checkIfAuthorizedAndGetVehicles();
    }

    @Override
    public void refreshViews() {
        for (VehicleSegment vehicleSegment : vehicleSegments) {
            vehicleSegment.removeFromLayout(layout);
        }
        vehicleSegments.clear();

        for (Vehicle vehicle : dataManager.getVehicles()) {
            VehicleSegment vehicleSegment = new VehicleSegment(this, vehicle);
            vehicleSegments.add(vehicleSegment);
            vehicleSegment.addToLayout(layout);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dataManager.removeObserver(this);
        getVehicleHandler.removeCallbacksAndMessages(null);
        addVehicleHandler.removeCallbacksAndMessages(null);
        authorizeAndGetVehicleHandler.removeCallbacksAndMessages(null);
        authorizeAndAddVehicleHandler.removeCallbacksAndMessages(null);
        logoutHandler.removeCallbacksAndMessages(null);
    }

    private void showConfirmLogOutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom));
        builder.setMessage("WYLOGOWAĆ?");
        builder.setPositiveButton("TAK", (dialog, which) -> {
            logOut();
            dialog.dismiss();
        });
        builder.setNegativeButton("NIE", (dialog, which) -> dialog.dismiss());
        Dialog confirm = builder.create();
        confirm.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        confirm.show();
    }

    private void logOut() {
        APIFunctions.logOut(logoutHandler, dataManager.getSessionId());
        dataManager.clearAuthorizedLocalData();
    }

    private void checkIfAuthorizedAndGetVehicles() {
        if (dataManager.getSessionId() == null) {
            String email = dataManager.readString(DataManager.KEY_EMAIL);
            String password = dataManager.readString(DataManager.KEY_PASSWORD);
            APIFunctions.logIn(authorizeAndGetVehicleHandler, email, password);
        } else {
            APIFunctions.getVehicle(getVehicleHandler, dataManager.getSessionId());
        }
    }

    private void checkIfAuthorizedAndAddVehicles() {
        if (dataManager.getSessionId() == null) {
            String email = dataManager.readString(DataManager.KEY_EMAIL);
            String password = dataManager.readString(DataManager.KEY_PASSWORD);
            APIFunctions.logIn(authorizeAndAddVehicleHandler, email, password);
        } else {
            APIFunctions.addVehicle(
                    addVehicleHandler,
                    dataManager.getSessionId(),
                    dialog.getVin(),
                    dialog.getPassword());
        }
    }

    private void moveToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void interpretGetVehiclesOutput(String jsonOutput) {
        if (jsonOutput == null) {
            dataManager.readLocalStoredVehicles();
        } else {
            try {
                JSONObject responseJson = new JSONObject(jsonOutput);
                if (responseJson.has(CODE_UNAUTHORIZED)) {
                    toastDisplay.show("Sesja wygasła. Zaloguj się ponownie");
                    logOut();
                } else {
                    JSONArray vehiclesJson = responseJson.getJSONArray(VEHICLES);
                    dataManager.addVehiclesFromJson(vehiclesJson);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void interpretAddVehiclesOutput(String jsonOutput) {
        if (jsonOutput == null) {
            toastDisplay.show("Brak połączenia");
        } else {
            try {
                JSONObject responseJson = new JSONObject(jsonOutput);
                if (responseJson.has(CODE_UNAUTHORIZED)) {
                    toastDisplay.show("Sesja wygasła. Zaloguj się ponownie");
                    logOut();
                } else {
                    int code = Integer.parseInt(responseJson.getString(CODE));
                    if (code == 0) {
                        dialog.closeDialog();
                        checkIfAuthorizedAndGetVehicles();
                    } else {
                        toastDisplay.show(RESPONSES_ADD_VEHICLE.get(code));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void interpretLogInOutput(String jsonOutput) {
        if (jsonOutput != null) {
            try {
                JSONObject responseJson = new JSONObject(jsonOutput);
                dataManager.setSessionId(responseJson.getString(SESSION_ID));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}