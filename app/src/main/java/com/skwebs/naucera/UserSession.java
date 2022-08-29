package com.skwebs.naucera;

import android.content.Context;
import android.content.SharedPreferences;

public class UserSession {
    private static UserSession userSession;
    final SharedPreferences sharedPreferences;
    final SharedPreferences.Editor editor;

    private UserSession(Context context) {
        sharedPreferences = context.getSharedPreferences("USER_SESSION", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static synchronized UserSession getInstance(Context context) {
        if (userSession == null) {
           userSession = new UserSession(context);
        }
        return userSession;
    }


    public void setUserDetails(boolean isLoggedIn, int userId, String userName, String userEmail, String userToken){
        editor.putBoolean("isLoggedIn", isLoggedIn);
        editor.putInt("id", userId);
        editor.putString("name", userName);
        editor.putString("email", userEmail);
        editor.putString("token", userToken);
        editor.apply();
    }

    public boolean isLoggedIn(){
        return sharedPreferences.getBoolean("isLoggedIn",false);
    }

    public int getUserId(){
        return sharedPreferences.getInt("id",0);
    }

    public String getUserName(){
        return sharedPreferences.getString("name", null);
    }

    public String getUserEmail(){
        return sharedPreferences.getString("email", null);
    }

    public String getUserToken(){
        return sharedPreferences.getString("token",null);
    }

    public void userLogout() {
        editor.clear();
        editor.apply();
    }
}
