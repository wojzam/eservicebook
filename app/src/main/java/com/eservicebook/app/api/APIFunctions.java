package com.eservicebook.app.api;

import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

import static com.eservicebook.app.api.APIConstants.ADD_VEHICLE;
import static com.eservicebook.app.api.APIConstants.EMAIL;
import static com.eservicebook.app.api.APIConstants.GET_VEHICLE;
import static com.eservicebook.app.api.APIConstants.LOG_IN;
import static com.eservicebook.app.api.APIConstants.LOG_OUT;
import static com.eservicebook.app.api.APIConstants.PASSWORD;
import static com.eservicebook.app.api.APIConstants.PASSWORD_REPEATED;
import static com.eservicebook.app.api.APIConstants.PATH;
import static com.eservicebook.app.api.APIConstants.RESET_PASSWORD;
import static com.eservicebook.app.api.APIConstants.RULES_ACCEPTED;
import static com.eservicebook.app.api.APIConstants.SESSION_ID;
import static com.eservicebook.app.api.APIConstants.SIGNUP;
import static com.eservicebook.app.api.APIConstants.VIN;

public class APIFunctions {

    private static final int TIMEOUT = 3000;
    private static volatile Thread thread;

    public static void logIn(Handler mainHandler, String email, String password) {
        String inputJson = String.format("{\"%s\":\"%s\",\"%s\":\"%s\"}", EMAIL, email, PASSWORD, password);
        executeAPIFunction(mainHandler, LOG_IN, inputJson);
    }

    public static void signup(Handler mainHandler, String email, String password1, String password2, Boolean accepted) {
        String inputJson = String.format("{\"%s\":\"%s\",\"%s\":\"%s\",\"%s\":\"%s\",\"%s\":\"%s\"}",
                EMAIL, email,
                PASSWORD, password1,
                PASSWORD_REPEATED, password2,
                RULES_ACCEPTED, accepted);
        executeAPIFunction(mainHandler, SIGNUP, inputJson);
    }

    public static void logOut(Handler mainHandler, String sessid) {
        String inputJson = String.format("{\"%s\":\"%s\"}", SESSION_ID, sessid);
        executeAPIFunction(mainHandler, LOG_OUT, inputJson);
    }

    public static void resetPassword(Handler mainHandler, String email) {
        String inputJson = String.format("{\"%s\":\"%s\"}", EMAIL, email);
        executeAPIFunction(mainHandler, RESET_PASSWORD, inputJson);
    }

    public static void getVehicle(Handler mainHandler, String sessid) {
        String inputJson = String.format("{\"%s\":\"%s\"}", SESSION_ID, sessid);
        executeAPIFunction(mainHandler, GET_VEHICLE, inputJson);
    }

    public static void addVehicle(Handler mainHandler, String sessid, String vin, String password) {
        String inputJson = String.format("{\"%s\":\"%s\",\"%s\":\"%s\",\"%s\":\"%s\"}", SESSION_ID, sessid, VIN, vin, PASSWORD, password);
        executeAPIFunction(mainHandler, ADD_VEHICLE, inputJson);
    }

    private static void executeAPIFunction(Handler mainHandler, String function, String jsonInput) {
        if (thread != null && thread.isAlive()) {
            return;
        }
        thread = new Thread(() -> {
            String response = null;
            try {
                URL url = new URL(PATH + function);
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.setSSLSocketFactory(new MySSLSocketFactory(connection.getSSLSocketFactory()));
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setConnectTimeout(TIMEOUT);
                connection.setReadTimeout(TIMEOUT);

                try {
                    OutputStream os = connection.getOutputStream();
                    byte[] input = jsonInput.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);

                    InputStream inputStream = connection.getErrorStream();
                    if (inputStream == null) {
                        inputStream = connection.getInputStream();
                    }
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                    response = bufferedReader.readLine();
                } finally {
                    connection.disconnect();
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                System.out.println(response);
                Message message = Message.obtain(mainHandler);
                message.obj = response;
                mainHandler.sendMessage(message);
            }
        });
        thread.start();
    }

}
