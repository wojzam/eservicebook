package com.eservicebook.app.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.eservicebook.app.R;
import com.eservicebook.app.ToastDisplay;
import com.eservicebook.app.activities.ForgetPasswordActivity;
import com.eservicebook.app.activities.MainScreenActivity;
import com.eservicebook.app.api.APIFunctions;
import com.eservicebook.app.data.DataManager;

import org.json.JSONException;
import org.json.JSONObject;

import static com.eservicebook.app.api.APIConstants.CODE;
import static com.eservicebook.app.api.APIConstants.RESPONSES_LOG_IN;
import static com.eservicebook.app.api.APIConstants.SESSION_ID;
import static com.eservicebook.app.data.DataManager.KEY_AUTH_EMAIL;
import static com.eservicebook.app.data.DataManager.KEY_CHECK_BOX;
import static com.eservicebook.app.data.DataManager.KEY_EMAIL;
import static com.eservicebook.app.data.DataManager.KEY_PASSWORD;

public class LoginTabFragment extends Fragment {
    private final DataManager dataManager = DataManager.getInstance();
    private final ToastDisplay toastDisplay = ToastDisplay.getInstance();
    private final Context context;
    private Handler logInHandler;
    private ProgressDialog progressDialog;
    private EditText email;
    private EditText password;
    private TextView forget;
    private CheckBox rememberMe;
    private Button loginButton;

    private final TextWatcher updateButtonAfterTextChange = new TextWatcher() {
        public void afterTextChanged(Editable s) {
            updateButtonState();
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    };

    public LoginTabFragment() {
        this.context = requireContext();
    }

    public LoginTabFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_login_tab, container, false);

        email = root.findViewById(R.id.email);
        password = root.findViewById(R.id.password);
        forget = root.findViewById(R.id.text_forget);
        rememberMe = root.findViewById(R.id.remember_me);
        loginButton = root.findViewById(R.id.button_login);

        logInHandler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(Message msg) {
                String output = (String) msg.obj;
                interpretLogInOutput(output);
            }
        };

        createViews();
        animateAll();

        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        logInHandler.removeCallbacksAndMessages(null);
    }

    private void attemptToLogin() {
        try {
            progressDialog = new ProgressDialog(requireContext());
            progressDialog.setMessage("Logowanie");
            progressDialog.setCancelable(false);
            progressDialog.show();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        APIFunctions.logIn(logInHandler, email.getText().toString(), password.getText().toString());
    }

    private void interpretLogInOutput(String jsonOutput) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }

        if (jsonOutput == null) {
            toastDisplay.show("Brak połączenia");
        } else {
            try {
                JSONObject responseJson = new JSONObject(jsonOutput);
                if (responseJson.has(CODE)) {
                    int code = Integer.parseInt(responseJson.getString(CODE));
                    if (code == 0) {
                        dataManager.setSessionId(responseJson.getString(SESSION_ID));

                        //TODO : średnie rozwiązanie
                        dataManager.save(KEY_CHECK_BOX, rememberMe.isChecked());
                        dataManager.save(KEY_EMAIL, email.getText().toString());
                        dataManager.save(KEY_AUTH_EMAIL, email.getText().toString());

                        if (rememberMe.isChecked()) {
                            dataManager.save(KEY_PASSWORD, password.getText().toString());
                        } else {
                            dataManager.save(KEY_PASSWORD, "");
                        }

                        Intent intent = new Intent(context, MainScreenActivity.class);
                        startActivity(intent);
                        requireActivity().finish();
                    } else {
                        toastDisplay.show(RESPONSES_LOG_IN.get(code));
                    }
                } else {
                    //TODO : pewnie się zmieni
                    toastDisplay.show("Niepoprawny format adresu email");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean requiredFieldsFilled() {
        return !(email.getText().toString().isEmpty() || password.getText().toString().isEmpty());
    }

    private void updateButtonState() {
        if (requiredFieldsFilled()) {
            loginButton.setBackgroundResource(R.drawable.button_bg);
        } else {
            loginButton.setBackgroundResource(R.drawable.button_disabled_bg);
        }
    }

    private void createViews() {
        forget.setOnClickListener(v -> {
            Intent intent = new Intent(context, ForgetPasswordActivity.class);
            startActivity(intent);
            requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        email.addTextChangedListener(updateButtonAfterTextChange);
        password.addTextChangedListener(updateButtonAfterTextChange);
        // wczytywanie
        rememberMe.setChecked(dataManager.readBoolean(KEY_CHECK_BOX));
        email.setText(dataManager.readString(KEY_EMAIL));
        password.setText(dataManager.readString(KEY_PASSWORD));

        loginButton.setOnClickListener(v -> {
            if (requiredFieldsFilled()) {
                attemptToLogin();
            }
        });

        updateButtonState();
    }

    private void animateAll() {
        float v = 0;
        email.setTranslationX(800);
        password.setTranslationX(800);
        forget.setTranslationX(800);
        rememberMe.setTranslationX(800);
        loginButton.setTranslationX(800);

        email.setAlpha(v);
        password.setAlpha(v);
        forget.setAlpha(v);
        rememberMe.setAlpha(v);
        loginButton.setAlpha(v);

        email.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        password.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        forget.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        rememberMe.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        loginButton.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();
    }

}