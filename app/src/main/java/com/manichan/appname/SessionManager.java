package com.manichan.appname;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by manichan on 19/7/17.
 */

public class SessionManager {
    private static String TAG = SessionManager.class.getSimpleName();
    SharedPreferences preferences;
    Editor editor;
    Context context;
    int PRIVATE_MODE = 0;
    private static final String PREFERENCE_NAME = "mapmyenviron";
    private static final String KEY_LOGGED_IN = "loggedin";
    public SessionManager(Context context){
        this.context = context;
        preferences = context.getSharedPreferences(PREFERENCE_NAME,PRIVATE_MODE);
        editor = preferences.edit();
    }
    public void setLogin(boolean isLoggedIn){
        editor.putBoolean(KEY_LOGGED_IN,isLoggedIn);
        editor.commit();
    }
    public boolean isLoggedIn(){
        return preferences.getBoolean(KEY_LOGGED_IN,false);
    }
}