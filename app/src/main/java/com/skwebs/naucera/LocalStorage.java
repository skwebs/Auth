package com.skwebs.naucera;

import android.content.Context;
import android.content.SharedPreferences;

public class LocalStorage {
    final SharedPreferences sharedPreferences;
    final SharedPreferences.Editor editor;
    Context context;
    String token;

    public LocalStorage() {
        Context context = this.context;
        sharedPreferences = context.getSharedPreferences("STORAGE_LOGIN_API", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public String getToken() {
        token = sharedPreferences.getString("TOKEN", "");
        return token;
    }

    public void setToken(String token) {
        editor.putLong("TOKEN", Long.parseLong(token));
        editor.commit();
        this.token = token;
    }
}
