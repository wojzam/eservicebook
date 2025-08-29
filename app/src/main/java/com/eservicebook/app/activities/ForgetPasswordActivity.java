package com.eservicebook.app.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.eservicebook.app.R;
import com.eservicebook.app.ToastDisplay;
import com.eservicebook.app.api.APIFunctions;

import org.json.JSONException;
import org.json.JSONObject;

import static com.eservicebook.app.api.APIConstants.CODE;
import static com.eservicebook.app.api.APIConstants.RESPONSES_RESET_PASSWORD;

public class ForgetPasswordActivity extends AppCompatActivity {

    private final ToastDisplay toastDisplay = ToastDisplay.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        Handler restPasswordHandler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(Message msg) {
                String output = (String) msg.obj;
                interpretResetPasswordOutput(output);
            }
        };

        Button reset = findViewById(R.id.button_reset);
        Button cancel = findViewById(R.id.button_cancel);
        EditText email = findViewById(R.id.email);

        reset.setOnClickListener(v -> APIFunctions.resetPassword(restPasswordHandler, email.getText().toString()));
        cancel.setOnClickListener(v -> finish());
    }

    private void interpretResetPasswordOutput(String jsonOutput) {
        if (jsonOutput == null) {
            toastDisplay.show("Brak połączenia");
        } else {
            try {
                JSONObject responseJson = new JSONObject(jsonOutput);
                if (responseJson.has(CODE)) {
                    int code = Integer.parseInt(responseJson.getString(CODE));
                    toastDisplay.show(RESPONSES_RESET_PASSWORD.get(code));
                } else {
                    finish();
                }

            } catch (JSONException e) {
                System.out.println("json error");
                e.printStackTrace();
            }
        }
    }
}