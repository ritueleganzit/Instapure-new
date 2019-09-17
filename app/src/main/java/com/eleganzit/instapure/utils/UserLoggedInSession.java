package com.eleganzit.instapure.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.eleganzit.instapure.HomeActivity;
import com.eleganzit.instapure.SignInActivity;

import java.util.HashMap;

/**
 * Created by eleganz on 2/11/18.
 */

public class UserLoggedInSession {
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
    private static final String PREF_NAME = "Instapure";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    private static final String ORDERED = "ordered";

    // User name (make variable public to access from outside)


    public static final String EMAIL= "email_id";
    public static final String USER_ID = "user_id";

    public static final String USER_NAME = "user_name";
    public static final String MOBILE = "mobile";
    public static final String PHOTO = "photo";
    public static final String GENDER = "gender";
    public static final String LOCATION = "location";
    public static final String COMPANY = "company";

    public UserLoggedInSession(Context context){
        this._context = context;

        this.activity = (Activity) context;

        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void createLoginSession(String email, String user_id, String user_name, String mobile, String photo, String gender, String location, String company){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref   ,
        editor.putString(USER_ID, user_id);

        editor.putString(EMAIL, email);

        editor.putString(USER_NAME, user_name);
        editor.putString(MOBILE, mobile);
        editor.putString(PHOTO, photo);
        editor.putString(GENDER, gender);
        editor.putString(LOCATION, location);
        editor.putString(COMPANY, company);

        // commit changes
        editor.commit();
        Intent i = new Intent(_context, HomeActivity.class).putExtra("from","session");

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        _context.startActivity(i);
        activity.finish();
        activity.overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

    }

    public void updateLoginSession(String email, String user_id, String user_name, String mobile, String photo, String gender, String location, String company){
        // Storing login value as TRUE

        // Storing name in pref   ,
        editor.putString(USER_ID, user_id);

        editor.putString(EMAIL, email);

        editor.putString(USER_NAME, user_name);
        editor.putString(MOBILE, mobile);
        editor.putString(PHOTO, photo);
        editor.putString(GENDER, gender);
        editor.putString(LOCATION, location);
        editor.putString(COMPANY, company);

        // commit changes
        editor.commit();

    }

    public void firstOrder(){
        // Storing login value as TRUE
        editor.putBoolean(ORDERED, true);

        // commit changes
        editor.commit();

    }


    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, SignInActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }


    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(USER_ID, pref.getString(USER_ID, null));

        user.put(EMAIL, pref.getString(EMAIL, null));
        user.put(USER_NAME, pref.getString(USER_NAME, null));
        user.put(MOBILE, pref.getString(MOBILE, null));
        user.put(PHOTO, pref.getString(PHOTO, null));
        user.put(GENDER, pref.getString(GENDER, null));
        user.put(LOCATION, pref.getString(LOCATION, null));
        user.put(COMPANY, pref.getString(COMPANY, null));

        // return user
        return user;
    }



    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, SignInActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Staring Login Activity
        _context.startActivity(i);
        activity.overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

    }

    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }

    public boolean alreadyOrdered(){
        return pref.getBoolean(ORDERED, false);
    }


}
