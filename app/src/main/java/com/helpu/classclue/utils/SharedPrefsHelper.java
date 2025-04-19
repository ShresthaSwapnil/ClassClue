package com.helpu.classclue.utils;

import android.content.Context;
import android.content.SharedPreferences;

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
    public void saveAlarmSound(String soundUri) {
        sharedPreferences.edit().putString(KEY_ALARM_SOUND, soundUri).apply();
    }

    public String getAlarmSound() {
        return sharedPreferences.getString(KEY_ALARM_SOUND, "default");
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
}