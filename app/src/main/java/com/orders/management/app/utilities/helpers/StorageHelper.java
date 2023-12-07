package com.orders.management.app.utilities.helpers;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orders.management.app.CustomApplication;
import com.orders.management.app.models.User2;

public class StorageHelper {
    private static SharedPreferences preferences;
    private static User2 currentUser;
    private static final String KEY_CURRENT_USER = "current-user";

    static {
        preferences = PreferenceManager.getDefaultSharedPreferences(CustomApplication.getApplication());
    }

    public static synchronized User2 getCurrentUser() {
        if (currentUser == null) {
            try {
                String json = preferences.getString(KEY_CURRENT_USER, "");
                currentUser = new Gson().fromJson(json, new TypeToken<User2>() {
                }.getType());
            } catch (Exception ex) {
            }
        }
        return currentUser;
    }
    public static synchronized void loadCurrentUser() {
        try {
            String json = preferences.getString(KEY_CURRENT_USER, "");
            currentUser = new Gson().fromJson(json, new TypeToken<User2>() {
            }.getType());
        } catch (Exception ex) {
        }
    }

    public static synchronized void clearCurrentUser() {
        try {
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove(KEY_CURRENT_USER);
            editor.apply();
            currentUser = null;
        } catch (Exception ex) {
        }
    }

    public static synchronized void setCurrentUser(User2 user) {
        if (user != null) {
            try {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(KEY_CURRENT_USER, new Gson().toJson(user));
                editor.apply();
            } catch (Exception ex) {
            }

            loadCurrentUser();
        }
    }
}
