package com.eservicebook.app.views;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.eservicebook.app.R;
import com.eservicebook.app.data.HistoryRepair;

import static com.eservicebook.app.Functions.dpToPixel;

public class HistorySegment {

    private final static int TEXT_SIZE_BIG = 17;
    private final static int TEXT_SIZE_NORMAL = 13;

    private final Typeface typefaceRegular;
    private final HistoryRepair historyRepair;
    private LinearLayout mainLayout;
    private LinearLayout historyRepairs;
    private TextView historyService;
    private TextView historyDate;
    private TextView historyMileage;
    private TextView historyCostParts;
    private TextView historyCostService;

    public HistorySegment(Context context, HistoryRepair historyRepair) {
        this.historyRepair = historyRepair;
        this.typefaceRegular = ResourcesCompat.getFont(context, R.font.ubuntu);

        createViews(context);
        composeLayout(context);
    }

    public void addToLayout(LinearLayout mainLayout) {
        mainLayout.addView(this.mainLayout);
    }

    public void rollOut(int delay) {
        float v = 0;
        mainLayout.setVisibility(View.VISIBLE);
        mainLayout.setAlpha(v);
        mainLayout.setTranslationY(-30);
        mainLayout.animate().translationY(0).alpha(1).setDuration(600).setStartDelay(delay).start();
    }

    public void rollIn() {
        mainLayout.setVisibility(View.GONE);
    }

    private LinearLayout createHistoryRepairsList(Context context) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(dpToPixel(15, context),
                dpToPixel(15, context),
                dpToPixel(15, context),
                dpToPixel(15, context));

        for (String historyRepair : historyRepair.getRepairs()) {
            TextView repairView = new TextView(context);
            repairView.setText(String.format("• %s", historyRepair));
            repairView.setTextSize(TEXT_SIZE_NORMAL);
            repairView.setTextColor(ContextCompat.getColor(context, R.color.gray_light));
            repairView.setTypeface(typefaceRegular);
            repairView.setFocusable(true);
            repairView.setTextIsSelectable(true);
            layout.addView(repairView);
        }

        return layout;
    }

    private String getColoredSpanned(String text, String color) {
        return "<font color=" + color + ">" + text + "</font>";
    }

    private void createViews(Context context) {
        Typeface typefaceItalic = ResourcesCompat.getFont(context, R.font.ubuntu_italic);
        Typeface typefaceBold = ResourcesCompat.getFont(context, R.font.ubuntu_bold);
        mainLayout = new LinearLayout(context);
        historyService = new TextView(context);
        historyDate = new TextView(context);
        historyMileage = new TextView(context);
        historyCostParts = new TextView(context);
        historyCostService = new TextView(context);

        historyRepairs = createHistoryRepairsList(context);

        historyService.setText(historyRepair.getService());
        historyService.setTextSize(TEXT_SIZE_BIG);
        historyService.setTextColor(ContextCompat.getColor(context, R.color.teal));
        historyService.setTypeface(typefaceBold);
        historyService.setFocusable(true);
        historyService.setTextIsSelectable(true);

        historyDate.setText(historyRepair.getFormattedUpdate());
        historyDate.setTextSize(TEXT_SIZE_NORMAL);
        historyDate.setTextColor(ContextCompat.getColor(context, R.color.gray_light));
        historyDate.setTypeface(typefaceRegular);
        historyDate.setFocusable(true);
        historyDate.setTextIsSelectable(true);

        //TODO : kolorowanie tesktu
        historyMileage.setText(Html.fromHtml(getColoredSpanned("Przebieg:\t", "#29f4e1") + historyRepair.getMileage() + " KM"));
        historyMileage.setTextSize(TEXT_SIZE_NORMAL);
        historyMileage.setTextColor(ContextCompat.getColor(context, R.color.gray_light));
        historyMileage.setTypeface(typefaceRegular);
        historyMileage.setFocusable(true);
        historyMileage.setTextIsSelectable(true);

        historyCostParts.setText(Html.fromHtml(getColoredSpanned("Koszt części: ", "#2eb8b8") + historyRepair.getCostParts().toString() + " zł "));
        historyCostParts.setTextSize(TEXT_SIZE_NORMAL);
        historyCostParts.setTextColor(ContextCompat.getColor(context, R.color.gray_light));
        historyCostParts.setTypeface(typefaceItalic);
        historyCostParts.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        historyCostParts.setFocusable(true);
        historyCostParts.setTextIsSelectable(true);

        historyCostService.setText(Html.fromHtml(getColoredSpanned("Koszt usługi: ", "#2eb8b8") + historyRepair.getCostService().toString() + " zł "));
        historyCostService.setTextSize(TEXT_SIZE_NORMAL);
        historyCostService.setTextColor(ContextCompat.getColor(context, R.color.gray_light));
        historyCostService.setTypeface(typefaceItalic);
        historyCostService.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        historyCostService.setFocusable(true);
        historyCostService.setTextIsSelectable(true);
    }

    private void composeLayout(Context context) {
        mainLayout.setVisibility(View.GONE);
        mainLayout.setBackgroundResource(R.drawable.entry_bg);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setPadding(dpToPixel(20, context),
                dpToPixel(20, context),
                dpToPixel(20, context),
                dpToPixel(20, context));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(dpToPixel(14, context),
                0,
                dpToPixel(14, context),
                dpToPixel(10, context));
        mainLayout.setLayoutParams(params);

        mainLayout.addView(historyService);
        mainLayout.addView(historyDate);
        mainLayout.addView(historyRepairs);
        mainLayout.addView(historyMileage);
        mainLayout.addView(historyCostParts);
        mainLayout.addView(historyCostService);
    }
}
