package com.gruporosul.activosfijos.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Cristian Ramírez on 22-Dec-15.
 * Grupo Rosul
 * cristianramirezgt@gmail.com
 */
public class PrefManager {

    SharedPreferences preferences;

    SharedPreferences.Editor editor;

    Context _context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "AF_PREFERENCES";

    private static final String IS_LOGIN = "IsLoggedIn";

    private static final String KEY_USER = "username";

    private static final String KEY_PASSWORD = "password";

    private static final String KEY_ID_DISPOSITIVO = "idDispositivo";

    public PrefManager(Context context) {
        this._context = context;
        preferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = preferences.edit();
    }


    /**
     * Create login session
     */
    public void createLoginSession(String username, String password, String id) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        editor.putString(KEY_USER, username);

        editor.putString(KEY_PASSWORD, password);

        editor.putString(KEY_ID_DISPOSITIVO, id);

        // commit changes
        editor.commit();
    }

    public String getKeyUser() {
        return preferences.getString(KEY_USER, null);
    }

    public String getKeyPassword() {
        return preferences.getString(KEY_PASSWORD, null);
    }

    public String getKeyIdDispositivo() {
        return preferences.getString(KEY_ID_DISPOSITIVO, null);
    }

    public boolean isLoggedIn() {
        return preferences.getBoolean(IS_LOGIN, false);
    }

    public void logout() {
        editor.clear();
        editor.commit();
    }

}
