package com.eleganzit.instapure.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.eleganzit.instapure.SignUpActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import androidx.annotation.RequiresApi;

public class MyApplication extends Application {


    private Locale locale = null;
    SharedPreferences.Editor langEditor;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (locale != null) {
            Locale.setDefault(locale);
            Configuration config = new Configuration(newConfig);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                config.setLocale(locale);
                getBaseContext().createConfigurationContext(config);
                getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());



            } else {
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        printHashKey();

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        String lang = settings.getString("language", "");
        changeLang(lang);
    }

    /**
     * Method that prints hash key.
     */
    public void printHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "tutorialwing.com.androidfacebooksharelinktutorial",
                    PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void changeLang(String lang) {
        Configuration config = getBaseContext().getResources().getConfiguration();
        if (!"".equals(lang) && !config.locale.getLanguage().equals(lang)) {

            SharedPreferences.Editor ed = PreferenceManager.getDefaultSharedPreferences(this).edit();
            ed.putString("language", lang);
            ed.commit();


            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N)
            {

                Locale locale = new Locale(lang);
                Locale.setDefault(locale);
                Configuration confi = getBaseContext().getResources().getConfiguration();
                confi.setLocale(locale);
                getBaseContext().createConfigurationContext(confi);
                getBaseContext().getResources().updateConfiguration(confi, getBaseContext().getResources().getDisplayMetrics());

               /* if (lang.equalsIgnoreCase("Ar"))
                {
                    confi.setLayoutDirection(new Locale("ar"));
                }
                else {
                    confi.setLayoutDirection(new Locale("en"));
                }*/
               /* getBaseContext().createConfigurationContext(confi);
                getBaseContext().getResources().updateConfiguration(confi, getBaseContext().getResources().getDisplayMetrics());
*/

                Log.d("yyyyy","ar");
            }
            else {
                locale = new Locale(lang);
                Locale.setDefault(locale);
                Configuration conf = new Configuration(config);
                if (lang.equalsIgnoreCase("AR"))
                {
                    conf.setLayoutDirection(new Locale("ar"));

                }
                else {
                    conf.setLayoutDirection(new Locale("en"));

                }
                conf.locale = locale;
                Log.d("yyyyy","en");
                getBaseContext().getResources().updateConfiguration(conf, getBaseContext().getResources().getDisplayMetrics());
            }

        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Toast.makeText(this, "Terminate", Toast.LENGTH_SHORT).show();

    }

    public String getLang(){
        return PreferenceManager.getDefaultSharedPreferences(this).getString("language", "");
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void changeLanguage(Context context, String language, String from) {

        Log.d("mmmmmmmmmm",language);

        SharedPreferences.Editor ed = PreferenceManager.getDefaultSharedPreferences(this).edit();
        ed.putString("language", language);
        ed.commit();

        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration configs = context.getResources().getConfiguration();
        if (language.equalsIgnoreCase("ar"))
        {
            configs.setLayoutDirection(new Locale("ar"));

        }
        else {
            configs.setLayoutDirection(new Locale("en"));

        }
        configs.setLocale(locale);
        context.createConfigurationContext(configs);
        context.getResources().updateConfiguration(configs, context.getResources().getDisplayMetrics());

        restartActivity(context,from);

    } @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void changeLanguage2(Context context, String language) {
        Log.d("mmmmmmmmmm2",language);


        SharedPreferences.Editor ed = PreferenceManager.getDefaultSharedPreferences(this).edit();
        ed.putString("lang", language);
        ed.commit();

        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration configs = context.getResources().getConfiguration();
        if (language.equalsIgnoreCase("ar"))
        {
            configs.setLayoutDirection(new Locale("ar"));

        }
        else {
            configs.setLayoutDirection(new Locale("en"));

        }
        configs.setLocale(locale);
        context.createConfigurationContext(configs);
        context.getResources().updateConfiguration(configs, context.getResources().getDisplayMetrics());



    }
    private void restartActivity(Context context, String from) {
//        Intent intent = ((Activity)context).getIntent();
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        ((Activity)context).finish();
//        startActivity(intent);
//        ((Activity)context).overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        if(from.equalsIgnoreCase("first")){
            startActivity(new Intent(context, SignUpActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            ((Activity)context).overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            ((Activity) context).finish();
        }
        if(from.equalsIgnoreCase("second")){
            ((Activity)context).finish();
            ((Activity)context).overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        }

    }

}
