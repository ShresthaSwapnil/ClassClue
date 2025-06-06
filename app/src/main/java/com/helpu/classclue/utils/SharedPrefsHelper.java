package com.helpu.classclue.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.helpu.classclue.models.Subject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SharedPrefsHelper {
    private static final String PREFS_NAME = "ClassCluePrefs";
    private static SharedPrefsHelper instance;
    private final SharedPreferences sharedPreferences;

    // Preference keys
    private static final String KEY_USER_TYPE = "user_type";
    private static final String KEY_ALARM_SOUND = "alarm_sound";
    private static final String KEY_LAST_EMAIL = "last_email";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_UI_STATE_PREFIX = "ui_state_";

    public SharedPrefsHelper(Context context) {
        sharedPreferences = context.getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized SharedPrefsHelper getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefsHelper(context);
        }
        return instance;
    }

    // User type methods
    public void saveUserType(String userType) {
        sharedPreferences.edit().putString(KEY_USER_TYPE, userType).apply();
    }

    public String getUserType() {
        return sharedPreferences.getString(KEY_USER_TYPE, "student");
    }

    // Alarm sound preference
    public void saveAlarmSound(String uri) {
        sharedPreferences.edit().putString("alarm_sound_uri", uri).apply();
    }

    public String getAlarmSound() {
        return sharedPreferences.getString("alarm_sound_uri", "");
    }

    // Login state management
    public void setLoggedIn(boolean isLoggedIn) {
        sharedPreferences.edit().putBoolean(KEY_IS_LOGGED_IN, isLoggedIn).apply();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    // Last email for auto-fill
    public void saveLastEmail(String email) {
        sharedPreferences.edit().putString(KEY_LAST_EMAIL, email).apply();
    }

    public String getLastEmail() {
        return sharedPreferences.getString(KEY_LAST_EMAIL, "");
    }

    // Generic UI state saving for rotation
    public void saveUIState(String screenName, String state) {
        sharedPreferences.edit().putString(KEY_UI_STATE_PREFIX + screenName, state).apply();
    }

    public String getUIState(String screenName) {
        return sharedPreferences.getString(KEY_UI_STATE_PREFIX + screenName, "");
    }

    // Clear all preferences
    public void clearAll() {
        sharedPreferences.edit().clear().apply();
    }

    // Clear specific preference
    public void clear(String key) {
        sharedPreferences.edit().remove(key).apply();
    }

    // Generic methods for flexibility
    public void put(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }

    public void put(String key, boolean value) {
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    public void put(String key, int value) {
        sharedPreferences.edit().putInt(key, value).apply();
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, "");
    }

    public boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public int getInt(String key) {
        return sharedPreferences.getInt(key, 0);
    }

    // For admin to set password
    public void saveRegistrationPassword(String password) {
        sharedPreferences.edit().putString("reg_password", password).apply();
    }

    public String getRegistrationPassword() {
        return sharedPreferences.getString("reg_password", "default_password");
    }

    // For student registration
    public void saveRegisteredSubjects(List<Subject> subjects) {
        Gson gson = new Gson();
        String json = gson.toJson(subjects);
        sharedPreferences.edit().putString("registered_subjects", json).apply();
    }

    public List<Subject> getRegisteredSubjects() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString("registered_subjects", "");
        Type type = new TypeToken<ArrayList<Subject>>(){}.getType();
        List<Subject> subjects = gson.fromJson(json, type);
        return subjects != null ? subjects : new ArrayList<>();
    }
}