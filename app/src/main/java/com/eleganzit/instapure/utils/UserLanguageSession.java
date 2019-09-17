package com.eleganzit.instapure.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.eleganzit.instapure.HomeActivity;
import com.eleganzit.instapure.SignInActivity;

import java.util.HashMap;

public class UserLanguageSession {

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;
    Activity activity;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "user_language";

    // All Shared Preferences Keys

    // User name (make variable public to access from outside)


    public static final String SELECTED_LANGUAGE= "selected_language";

    public UserLanguageSession(Context context){
        this._context = context;

        this.activity = (Activity) context;

        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void createLanguageSession(String selected_language){
        // Storing login value as TRUE

        // Storing name in pref   ,

        editor.putString(SELECTED_LANGUAGE, selected_language);

        // commit changes
        editor.commit();

    }


    public HashMap<String, String> getUserLanguage(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name

        user.put(SELECTED_LANGUAGE, pref.getString(SELECTED_LANGUAGE, null));

        // return user
        return user;
    }


}
