package com.eservicebook.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.eservicebook.app.ToastDisplay;
import com.eservicebook.app.data.DataManager;

import java.util.Objects;

// TODO: cały splash screen do poprawy
public class SplashScreenActivity extends AppCompatActivity {

    private final DataManager dataManager = DataManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DataManager dataManager = DataManager.getInstance();
        dataManager.initialize(getApplicationContext());
        ToastDisplay.getInstance().initialize(getApplicationContext());

        if (isQualifiedForSilentLogIn()) {
            new Handler().postDelayed(() -> {
                startActivity(new Intent(SplashScreenActivity.this, MainScreenActivity.class));
                finish();
            }, 1000);
        } else {
            new Handler().postDelayed(() -> {
                startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                finish();
            }, 1000);
        }
    }

    private boolean isQualifiedForSilentLogIn() {
        boolean rememberMe = dataManager.readBoolean(DataManager.KEY_CHECK_BOX);
        String email = dataManager.readString(DataManager.KEY_EMAIL);
        String password = dataManager.readString(DataManager.KEY_PASSWORD);
        String authEmail = dataManager.readString(DataManager.KEY_AUTH_EMAIL);

        return rememberMe && !email.isEmpty() && !password.isEmpty() && Objects.equals(email, authEmail);
    }
}
