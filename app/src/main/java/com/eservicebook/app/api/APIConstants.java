package com.eservicebook.app.api;

import java.util.HashMap;
import java.util.Map;

public class APIConstants {

    public static final String PATH = "https://mobile.e-servicebook.pl:62881/";

    // API functions names
    public static final String LOG_IN = "logIn";
    public static final String LOG_OUT = "logOut";
    public static final String SIGNUP = "newClient";
    public static final String RESET_PASSWORD = "resetPassword";
    public static final String GET_VEHICLE = "getVehicle";
    public static final String ADD_VEHICLE = "addVehicle";

    // API json keys
    public static final String CODE = "code";
    public static final String VEHICLES = "vehicles";
    public static final String VIN = "vin";
    public static final String VEHICLE_BRAND = "vbrand";
    public static final String VEHICLE_MODEL = "vmodel";
    public static final String VEHICLE_YEAR = "vyear";

    public static final String HISTORY_REPAIRS = "history_repairs";
    public static final String REPAIRS = "repairs";
    public static final String MILEAGE = "mileage";
    public static final String U_DATE = "udate";
    public static final String SERVICE = "service";
    public static final String COST_PARTS = "cost_parts";
    public static final String COST_SERVICE = "cost_service";

    public static final String SESSION_ID = "sessid";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "pass";
    public static final String PASSWORD_REPEATED = "repass";
    public static final String RULES_ACCEPTED = "regulamin";

    //API response codes interpretation
    public static final Map<Integer, String> RESPONSES_LOG_IN = createResponsesLogIn();
    public static final Map<Integer, String> RESPONSES_SIGNUP = createResponsesSignUp();
    public static final Map<Integer, String> RESPONSES_RESET_PASSWORD = createResponsesResetPassword();
    public static final Map<Integer, String> RESPONSES_ADD_VEHICLE = createResponsesAddVehicle();

    // HTML codes
    public static final String CODE_OK = "200";
    public static final String CODE_UNAUTHORIZED = "401";

    private static Map<Integer, String> createResponsesLogIn() {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "Błędny email lub hasło");
        map.put(2, "Konto nieaktywne. Dokończ aktywację konta klikając w link w otrzymanej wiadomości");
        map.put(3, "Błędny email lub hasło");
        return map;
    }

    private static Map<Integer, String> createResponsesSignUp() {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "Nie wysłano wszystkich wymaganych danych");
        map.put(2, "Hasła różnią się");
        map.put(3, "Hasło musi zawierać minimum 8 znaków, dużą i małą literę oraz cyfrę");
        map.put(4, "Niepoprawny format adresu email");
        map.put(5, "Niezaakceptowany regulamin");
        map.put(6, "Konto o podanej nazwie już istnieje");
        map.put(7, "Konto zostało założone, ale nie udało się wysłać maila, prosimy skontaktować się z obsługą");
        return map;
    }

    private static Map<Integer, String> createResponsesResetPassword() {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "Nie podano adresu email");
        map.put(2, "Niepoprawny format adresu email");
        map.put(3, "Wystąpił błąd, spróbuj jeszcze raz, jeśli będzie się to powtarzać prosimy skontaktować się z obsługą");
        return map;
    }

    private static Map<Integer, String> createResponsesAddVehicle() {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "Podany pojazd znajduje się już na liście");
        map.put(2, "VIN albo hasło niepoprawne");
        map.put(3, "Pojazd o podanym numerze VIN nie istnieje");
        return map;
    }

}