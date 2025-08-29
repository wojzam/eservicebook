package com.eservicebook.app.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.UnderlineSpan;
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
import com.eservicebook.app.api.APIFunctions;

import org.json.JSONException;
import org.json.JSONObject;

import static com.eservicebook.app.api.APIConstants.CODE;
import static com.eservicebook.app.api.APIConstants.RESPONSES_SIGNUP;

public class SignupTabFragment extends Fragment {

    private final ToastDisplay toastDisplay = ToastDisplay.getInstance();
    private final String termsUrl = "https://client.e-servicebook.pl/service-terms";
    private final String provisionUrl = "https://client.e-servicebook.pl/service-provision";
    private final String privacyPolicyUrl = "https://client.e-servicebook.pl/privacy-policy";
    private ProgressDialog progressDialog;
    private Handler signupHandler;
    private EditText email;
    private EditText password;
    private EditText passwordRepeated;
    private CheckBox acceptTerms;
    private TextView additionalInfo;
    private Button signup;

    private final TextWatcher updateButtonAfterTextChange = new TextWatcher() {
        public void afterTextChanged(Editable s) {
            updateButtonState();
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    };

    public SignupTabFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_signup_tab, container, false);

        email = root.findViewById(R.id.new_email);
        password = root.findViewById(R.id.new_password);
        passwordRepeated = root.findViewById(R.id.new_password_repeated);
        acceptTerms = root.findViewById(R.id.accept_terms);
        additionalInfo = root.findViewById(R.id.additional_info);
        signup = root.findViewById(R.id.button_signup);

        signupHandler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(Message msg) {
                String output = (String) msg.obj;
                interpretSignupOutput(output);
            }
        };

        createViews();

        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        signupHandler.removeCallbacksAndMessages(null);
    }

    private void attemptToSignup() {
        try {
            progressDialog = new ProgressDialog(requireContext());
            progressDialog.setMessage("Rejestrowanie");
            progressDialog.setCancelable(false);
            progressDialog.show();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

        APIFunctions.signup(signupHandler,
                email.getText().toString(),
                password.getText().toString(),
                passwordRepeated.getText().toString(),
                acceptTerms.isChecked());
    }

    private void interpretSignupOutput(String jsonOutput) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }

        if (jsonOutput == null) {
            toastDisplay.show("Brak połączenia");
        } else {
            try {
                JSONObject responseJson = new JSONObject(jsonOutput);
                int code = Integer.parseInt(responseJson.getString(CODE));
                toastDisplay.show(RESPONSES_SIGNUP.get(code));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean requiredFieldsFilled() {
        return (isFilled(email) &&
                isFilled(password) &&
                isFilled(passwordRepeated) &&
                acceptTerms.isChecked());
    }

    private boolean isFilled(EditText editText) {
        return !editText.getText().toString().isEmpty();
    }

    private void updateButtonState() {
        if (requiredFieldsFilled()) {
            signup.setBackgroundResource(R.drawable.button_bg);
        } else {
            signup.setBackgroundResource(R.drawable.button_disabled_bg);
        }
    }

    private ClickableSpan openUrl(String url) {
        return new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        };
    }

    private void addSpannableStrings() {
        SpannableString termsSpannable = new SpannableString(getString(R.string.accept_terms));
        SpannableString additionalInfoSpannable = new SpannableString(getString(R.string.additional_info));

        int startIndexTerms = 10;
        int lastIndexTerms = startIndexTerms + 9;

        int startIndexProvision = 0;
        int lastIndexProvision = startIndexProvision + 21;

        int startIndexPrivacyPolicy = 68;
        int lastIndexPrivacyPolicy = startIndexPrivacyPolicy + 20;

        termsSpannable.setSpan(openUrl(termsUrl), startIndexTerms, lastIndexTerms, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        termsSpannable.setSpan(new UnderlineSpan(), startIndexTerms, lastIndexTerms, 0);

        additionalInfoSpannable.setSpan(openUrl(provisionUrl), startIndexProvision, lastIndexProvision, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        additionalInfoSpannable.setSpan(openUrl(privacyPolicyUrl), startIndexPrivacyPolicy, lastIndexPrivacyPolicy, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        additionalInfoSpannable.setSpan(new UnderlineSpan(), startIndexProvision, lastIndexProvision, 0);
        additionalInfoSpannable.setSpan(new UnderlineSpan(), startIndexPrivacyPolicy, lastIndexPrivacyPolicy, 0);

        acceptTerms.setText(termsSpannable);
        acceptTerms.setMovementMethod(LinkMovementMethod.getInstance());

        additionalInfo.setText(additionalInfoSpannable);
        additionalInfo.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void createViews() {
        signup.setOnClickListener(v -> {
            if (requiredFieldsFilled()) {
                attemptToSignup();
            }
        });

        email.addTextChangedListener(updateButtonAfterTextChange);
        password.addTextChangedListener(updateButtonAfterTextChange);
        passwordRepeated.addTextChangedListener(updateButtonAfterTextChange);

        acceptTerms.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    updateButtonState();
                }
        );
        addSpannableStrings();
    }
}