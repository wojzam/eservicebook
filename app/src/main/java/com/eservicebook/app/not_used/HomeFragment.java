//package com.eservicebook.app.not_used;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Looper;
//import android.os.Message;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.LinearLayout;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.fragment.app.Fragment;
//import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
//
//import com.eservicebook.app.Observer;
//import com.eservicebook.app.R;
//import com.eservicebook.app.api.APIFunctions;
//import com.eservicebook.app.data.DataManager;
//import com.eservicebook.app.data.Vehicle;
//import com.eservicebook.app.views.AddVehicleDialog;
//import com.eservicebook.app.views.VehicleSegment;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//
//import static com.eservicebook.app.api.APIConstants.CODE;
//import static com.eservicebook.app.api.APIConstants.PASSWORD;
//import static com.eservicebook.app.api.APIConstants.VIN;
//
//public class HomeFragment extends Fragment implements Observer {
//
//    private final Context context;
//    private final DataManager dataManager = DataManager.getInstance();
//    private final ArrayList<VehicleSegment> vehicleSegments = new ArrayList<>();
//    private Handler getVehicleHandler;
//    private Handler addVehicleHandler;
//    private ViewGroup root;
//    private SwipeRefreshLayout pullToRefresh;
//    private LinearLayout layout;
//
//    public HomeFragment(Context context) {
//        this.context = context;
//        dataManager.addObserver(this);
//    }
//
//    @Override
//    public void refreshViews() {
//        pullToRefresh.setRefreshing(false);
//
//        for (VehicleSegment vehicleSegment : vehicleSegments) {
//            vehicleSegment.removeFromLayout(layout);
//        }
//        vehicleSegments.clear();
//
//        for (Vehicle vehicle : dataManager.getVehicles()) {
//            VehicleSegment vehicleSegment = new VehicleSegment(context, vehicle);
//            vehicleSegments.add(vehicleSegment);
//            vehicleSegment.addToLayout(layout);
//        }
//
//        //TODO : do zmiany
//        //dataManager.saveVehicles(context);
//    }
//
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        if (root == null) {
//            root = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);
//
//            layout = root.findViewById(R.id.main_layout);
//            Button addCarButton = root.findViewById(R.id.button_add_car);
//            pullToRefresh = root.findViewById(R.id.pull_refresh);
//
////            getVehicleHandler = new Handler(Looper.myLooper()) {
////                @Override
////                public void handleMessage(Message msg) {
////                    String output = (String) msg.obj;
////                    interpretGetVehiclesOutput(output);
////                }
////            };
//
//            addVehicleHandler = new Handler(Looper.myLooper()) {
//                @Override
//                public void handleMessage(Message msg) {
//                    if (msg.what == AddVehicleDialog.ADD_VEHICLE_BUTTON_CODE) {
//                        Bundle bundle = msg.getData();
//                        APIFunctions.addVehicle(
//                                addVehicleHandler,
//                                dataManager.getSessionId(),
//                                bundle.getString(VIN),
//                                bundle.getString(PASSWORD));
//                    } else {
//                        String output = (String) msg.obj;
//                        interpretAddVehiclesOutput(output);
//                    }
//
//                }
//            };
//
//            //    pullToRefresh.setOnRefreshListener(dataManager::updateVehicles);
//
//            addCarButton.setOnClickListener(v -> {
////                old version
////                AddVehicleDialog dialog = new AddVehicleDialog();
////                dialog.showDialog(requireActivity(), addVehicleHandler);
//            });
//        }
//
//        return root;
//    }
//
//    //TODO : IllegalStateException and infinite refreshing
////    private void interpretGetVehiclesOutput(String jsonOutput) {
////        pullToRefresh.setRefreshing(false);
////
////        System.out.println(jsonOutput);
////
////        if (jsonOutput == null) {
////            Toast.makeText(context, "Brak połączenia", Toast.LENGTH_LONG).show();
////        } else {
////            try {
////                JSONObject responseJson = new JSONObject(jsonOutput);
////                if (responseJson.has(CODE_OK)) {
////                    JSONArray vehiclesJson = responseJson.getJSONArray(VEHICLES);
////                    for (int i = 0; i < vehiclesJson.length(); i++) {
////                        JSONObject vehicleJson = (JSONObject) vehiclesJson.get(i);
////                        Vehicle vehicle = new Vehicle(vehicleJson);
////                        VehicleSegment vehicleSegment = new VehicleSegment(context, vehicle);
////                        vehicleSegment.addToLayout(layout);
////                    }
////                }
////            } catch (JSONException e) {
////                System.out.println("json error");
////                e.printStackTrace();
////            }
////        }
////    }
//
//    private void interpretAddVehiclesOutput(String jsonOutput) {
//        System.out.println(jsonOutput);
//
//        if (jsonOutput == null) {
//            Toast.makeText(context, "Brak połączenia", Toast.LENGTH_LONG).show();
//        } else {
//            try {
//                JSONObject responseJson = new JSONObject(jsonOutput);
//                int code = Integer.parseInt(responseJson.getString(CODE));
//                switch (code) {
//                    case 0:
//                        // aktualizacja pojazdów
//                        //   dataManager.updateVehicles();
//                        break;
//                    case 1:
//                        Toast.makeText(context, "Podany pojazd znajduje się już na liście klienta", Toast.LENGTH_LONG).show();
//                        break;
//                    case 2:
//                        Toast.makeText(context, "VIN albo hasło niepoprawne", Toast.LENGTH_LONG).show();
//                        break;
//                    case 3:
//                        Toast.makeText(context, "Pojazd o podanym numerze VIN nie istnieje", Toast.LENGTH_LONG).show();
//                        break;
//                    default:
//                        break;
//                }
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}