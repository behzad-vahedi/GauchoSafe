package app.mma.locationlistenerapp.utils;


import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

import app.mma.locationlistenerapp.net.UserInfo;

public class SessionManager {


    private Context context;
    private SharedPreferences pref;

    private static final String KEY_ISLOGGEDIN = "is_logged_in";
    private static final String KEY_FIRSTNAME = "firstname";
    private static final String KEY_LASTNAME = "lastname";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONENUMBER = "phonenumber";



    public SessionManager(Context context){
        this.context = context;
        pref = context.getSharedPreferences("session", Context.MODE_PRIVATE);
    }

    public void setLogin(boolean login){
        pref.edit().clear().apply();
        pref.edit().putBoolean(KEY_ISLOGGEDIN, login).apply();
    }

    public void setUserInfo(String firstname, String lastname, String email, String phonenumber){
        setLogin(true);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(KEY_FIRSTNAME, firstname);
        editor.putString(KEY_LASTNAME, lastname);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PHONENUMBER, phonenumber);
        editor.apply();
    }



    public void logout(){
        setLogin(false);
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_ISLOGGEDIN, false);
    }

    public Map<String, String> getUserInfo(){
        if(!isLoggedIn()){
            return null;
        }
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put(KEY_FIRSTNAME, pref.getString(KEY_FIRSTNAME, ""));
        userInfo.put(KEY_LASTNAME, pref.getString(KEY_LASTNAME, ""));
        userInfo.put(KEY_EMAIL, pref.getString(KEY_EMAIL, ""));
        userInfo.put(KEY_PHONENUMBER, pref.getString(KEY_PHONENUMBER, ""));
        return userInfo;
    }

    public String getEmail(){
        return pref.getString(KEY_EMAIL, "");
    }

    public String getPhoneNumber(){
        return pref.getString(KEY_PHONENUMBER, "");
    }







}
