package com.eservicebook.app.views;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.eservicebook.app.R;
import com.eservicebook.app.data.HistoryRepair;
import com.eservicebook.app.data.Vehicle;

import java.util.ArrayList;
import java.util.Locale;

import static com.eservicebook.app.Functions.dpToPixel;

public class VehicleSegment {

    private final static int TEXT_SIZE_BIG = 20;
    private final static int TEXT_SIZE_NORMAL = 13;

    private final Vehicle vehicle;
    private final ArrayList<HistorySegment> vehicleHistoryRepairs = new ArrayList<>();
    private LinearLayout mainLayout;
    private LinearLayout vehicleInfoLayout;
    private TextView vehicleTitle;
    private TextView vehicleVin;
    //    private TextView expand; TODO: expand button
    //    private int showedCount = 0;
    private boolean historyRolledOut = false;

    public VehicleSegment(Context context, Vehicle vehicle) {
        this.vehicle = vehicle;

        createViews(context);
        composeLayout(context);
        addHistoryRepairs(context);

//        myLayout.addView(expand);
    }

    public void addToLayout(LinearLayout mainLayout) {
        mainLayout.addView(this.mainLayout);
        animate();
    }

    public void removeFromLayout(LinearLayout mainLayout) {
        mainLayout.removeView(this.mainLayout);
    }

    private void animate() {
        float scale = 0.95f;
        mainLayout.setScaleX(scale);
        mainLayout.setScaleY(scale);
        mainLayout.setAlpha(0);
        mainLayout.animate().scaleX(1.0f).scaleY(1.0f).alpha(1).setDuration(800).start();
    }

    private String formattedTitle() {
        return String.format(new Locale("pl"), "%s %s (%d)", vehicle.getBrand(), vehicle.getModel(), vehicle.getYear());
    }

    private void createViews(Context context) {
        Typeface typefaceRegular = ResourcesCompat.getFont(context, R.font.ubuntu);
        Typeface typefaceBold = ResourcesCompat.getFont(context, R.font.ubuntu_bold);

        mainLayout = new LinearLayout(context);
        vehicleInfoLayout = new LinearLayout(context);
        vehicleTitle = new TextView(context);
        vehicleVin = new TextView(context);

//        expand = new TextView(context);

        vehicleInfoLayout.setFocusable(true);
        vehicleInfoLayout.setClickable(true);

        vehicleTitle.setText(formattedTitle());
        vehicleTitle.setTextSize(TEXT_SIZE_BIG);
        vehicleTitle.setTextColor(ContextCompat.getColor(context, R.color.teal));
        vehicleTitle.setTypeface(typefaceBold);
        vehicleTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_arrow_right, 0, 0, 0);

        vehicleVin.setText(vehicle.getVin());
        vehicleVin.setTextSize(TEXT_SIZE_NORMAL);
        vehicleVin.setTextColor(ContextCompat.getColor(context, R.color.gray_light));
        vehicleVin.setTypeface(typefaceRegular);
        vehicleVin.setPadding(dpToPixel(24, context), 0, 0, 0);

//        expand.setText("Pokaż więcej");
//        expand.setTextColor(context.getResources().getColor(R.color.gray));
//        expand.setPadding(0, 0, 0, (int) dpToPixel(15, context));
//        expand.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//        expand.setVisibility(View.GONE);
//        expand.setFocusable(true);
//        expand.setClickable(true);
//        expand.setOnClickListener(v -> {
//            int nextValue = showedCount + 1;
//            if (nextValue > entries.size()) {
//                nextValue = entries.size();
//            }
//            for (int i = showedCount; i < nextValue; i++) {
//                carTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_arrow_drop_down, 0, 0, 0);
//                entries.get(i).rollOut(i * 50);
//            }
//            showedCount = nextValue;
//        });

        vehicleInfoLayout.setOnClickListener(v -> {
            if (historyRolledOut) {
                for (int i = 0; i < vehicleHistoryRepairs.size(); i++) {
                    vehicleTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_arrow_right, 0, 0, 0);
                    vehicleHistoryRepairs.get(i).rollIn();
                }
                historyRolledOut = false;
//                expand.setVisibility(View.GONE);
            } else {
                for (int i = 0; i < vehicleHistoryRepairs.size(); i++) {
                    vehicleTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_arrow_drop_down, 0, 0, 0);
                    vehicleHistoryRepairs.get(i).rollOut(i * 100);
                }
                historyRolledOut = true;
//                expand.setVisibility(View.VISIBLE);
            }
        });
    }

    private void composeLayout(Context context) {
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, dpToPixel(10, context));

        vehicleInfoLayout.setLayoutParams(params);
        vehicleInfoLayout.setBackgroundResource(R.drawable.car_info_bg);
        vehicleInfoLayout.setOrientation(LinearLayout.VERTICAL);
        vehicleInfoLayout.setPadding(dpToPixel(10, context),
                dpToPixel(20, context),
                dpToPixel(20, context),
                dpToPixel(20, context));

        vehicleInfoLayout.addView(vehicleTitle);
        vehicleInfoLayout.addView(vehicleVin);

        mainLayout.addView(vehicleInfoLayout);
    }

    private void addHistoryRepairs(Context context) {
        for (HistoryRepair history : vehicle.getHistoryRepairs()) {
            HistorySegment entry = new HistorySegment(context, history);
            vehicleHistoryRepairs.add(entry);
            entry.addToLayout(mainLayout);
        }
    }
}
