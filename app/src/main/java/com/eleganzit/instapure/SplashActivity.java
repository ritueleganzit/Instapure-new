package com.eleganzit.instapure;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.eleganzit.instapure.utils.UserLoggedInSession;
import com.crashlytics.android.Crashlytics;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.fabric.sdk.android.Fabric;

public class SplashActivity extends AppCompatActivity {

    UserLoggedInSession userLoggedInSession;
    CallbackManager callbackManager;
    PackageInfo info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        setContentView(R.layout.activity_splash);
        userLoggedInSession=new UserLoggedInSession(SplashActivity.this);
        callbackManager = CallbackManager.Factory.create();


        try {
            info=getPackageManager().getPackageInfo("com.eleganzit.instapure", PackageManager.GET_SIGNATURES);
            for(Signature signature : info.signatures)
            {
                MessageDigest md;
                md=MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());

                String key= new String(Base64.encode(md.digest(),0  ));
                Log.e("keyyyyyy",key);
            }

        }
        catch(PackageManager.NameNotFoundException e1)
        {
            Log.e("Name not found",e1.toString());
        }
        catch (NoSuchAlgorithmException e)
        {
            Log.e("No such algorithm",e.toString());
        }
        catch (Exception e)
        {
            Log.e("Exception",e.toString());
        }

        Log.d("sdhagfsgdh",userLoggedInSession.isLoggedIn()+"");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                if (userLoggedInSession.isLoggedIn())
                {
                    startActivity(new Intent(SplashActivity.this,HomeActivity.class)
                            .putExtra("from","splash")
                    );
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                    finish();
                }
                else {
                    startActivity(new Intent(SplashActivity.this,ChangeLanguageActivity.class));
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                    finish();
                }


            }
        },3000);

    }
}
