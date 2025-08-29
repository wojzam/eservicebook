package com.eservicebook.app.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import com.eservicebook.app.R;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;
import com.skydoves.balloon.BalloonSizeSpec;


public class AddVehicleDialog {

    private final Dialog dialog;
    private final EditText vin;
    private final EditText password;
    private final Button buttonOk;

    public AddVehicleDialog(Activity activity, View.OnClickListener addVehicle) {
        this.dialog = new Dialog(activity);
        Balloon infoBalloon = createBalloon(activity);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_car);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        vin = dialog.findViewById(R.id.number_vin);
        password = dialog.findViewById(R.id.password_vin);
        buttonOk = dialog.findViewById(R.id.button_ok);
        Button cancel = dialog.findViewById(R.id.button_cancel);
        Button info = dialog.findViewById(R.id.button_info);

        vin.setSingleLine(true);
        password.setSingleLine(true);

        TextWatcher updateButtonAfterTextChange = new TextWatcher() {
            public void afterTextChanged(Editable s) {
                updateOkButtonState();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        };
        vin.addTextChangedListener(updateButtonAfterTextChange);
        password.addTextChangedListener(updateButtonAfterTextChange);

        password.setNextFocusDownId(R.id.button_ok);

        buttonOk.setOnClickListener(addVehicle);
        cancel.setOnClickListener(v -> closeDialog());
        info.setOnClickListener(v -> infoBalloon.showAlignTop(info));

        updateOkButtonState();
    }

    public void showDialog() {
        dialog.show();
    }

    public void closeDialog() {
        vin.setText("");
        password.setText("");
        dialog.dismiss();
    }

    public String getVin() {
        return vin.getText().toString().toUpperCase();
    }

    public String getPassword() {
        return password.getText().toString();
    }

    private void updateOkButtonState() {
        if (allFieldsAreFilled()) {
            buttonOk.setBackgroundResource(R.drawable.button_bg);
            buttonOk.setClickable(true);
            buttonOk.setEnabled(true);
        } else {
            buttonOk.setBackgroundResource(R.drawable.button_disabled_bg);
            buttonOk.setClickable(false);
            buttonOk.setEnabled(false);
        }
    }

    private boolean allFieldsAreFilled() {
        return !(vin.getText().toString().isEmpty() || password.getText().toString().isEmpty());
    }

    private Balloon createBalloon(Context context) {
        TextView balloonText = new TextView(context);
        balloonText.setText(R.string.add_tooltip);
        balloonText.setTextColor(ContextCompat.getColor(context, R.color.gray_light));
        balloonText.setTypeface(ResourcesCompat.getFont(context, R.font.ubuntu));
        balloonText.setGravity(Gravity.CENTER);

        return new Balloon.Builder(context)
                .setIsAttachedInDecor(false)
                .setArrowSize(0)
                .setWidth(BalloonSizeSpec.WRAP)
                .setHeight(BalloonSizeSpec.WRAP)
                .setPadding(15)
                .setCornerRadius(4f)
                .setLayout(balloonText)
                .setBackgroundColor(ContextCompat.getColor(context, R.color.background_dark))
                .setBalloonAnimation(BalloonAnimation.FADE)
                .build();
    }
}