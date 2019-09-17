package com.eleganzit.instapure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eleganzit.instapure.utils.UserLanguageSession;

public class ChangeLanguage2Activity extends AppCompatActivity {


    RelativeLayout eng_bg,lng2_bg,lng3_bg,lng4_bg;
    TextView submit,eng_txt,lng2_txt,lng3_txt,lng4_txt;
    UserLanguageSession userLanguageSession;
    String selected_language="1";
    SharedPreferences languageSharedPreference;
    SharedPreferences.Editor langEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#6D63F7"));
        }
        setContentView(R.layout.activity_change_language2);


        userLanguageSession=new UserLanguageSession(this);
        languageSharedPreference= PreferenceManager.getDefaultSharedPreferences(this);
        langEditor=languageSharedPreference.edit();
        eng_bg=findViewById(R.id.eng_bg);
        eng_txt=findViewById(R.id.eng_txt);
        lng2_bg=findViewById(R.id.lng2_bg);
        lng2_txt=findViewById(R.id.lng2_txt);
        lng3_bg=findViewById(R.id.lng3_bg);
        lng3_txt=findViewById(R.id.lng3_txt);
        lng4_bg=findViewById(R.id.lng4_bg);
        lng4_txt=findViewById(R.id.lng4_txt);
        submit=findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLanguageSession.createLanguageSession(selected_language);
                finish();
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });

        eng_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected_language="1";
                eng_bg.setBackgroundResource(R.drawable.btn_bg);
                eng_txt.setTextColor(Color.parseColor("#ffffff"));
                lng2_bg.setBackgroundResource(R.drawable.inactive_bg);
                lng2_txt.setTextColor(Color.parseColor("#838383"));
                lng3_bg.setBackgroundResource(R.drawable.inactive_bg);
                lng3_txt.setTextColor(Color.parseColor("#838383"));
                lng4_bg.setBackgroundResource(R.drawable.inactive_bg);
                lng4_txt.setTextColor(Color.parseColor("#838383"));
            }
        });

        lng2_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected_language="2";
                lng2_bg.setBackgroundResource(R.drawable.btn_bg);
                lng2_txt.setTextColor(Color.parseColor("#ffffff"));
                eng_bg.setBackgroundResource(R.drawable.inactive_bg);
                eng_txt.setTextColor(Color.parseColor("#838383"));
                lng3_bg.setBackgroundResource(R.drawable.inactive_bg);
                lng3_txt.setTextColor(Color.parseColor("#838383"));
                lng4_bg.setBackgroundResource(R.drawable.inactive_bg);
                lng4_txt.setTextColor(Color.parseColor("#838383"));
            }
        });

        lng3_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected_language="3";
                lng3_bg.setBackgroundResource(R.drawable.btn_bg);
                lng3_txt.setTextColor(Color.parseColor("#ffffff"));
                lng2_bg.setBackgroundResource(R.drawable.inactive_bg);
                lng2_txt.setTextColor(Color.parseColor("#838383"));
                eng_bg.setBackgroundResource(R.drawable.inactive_bg);
                eng_txt.setTextColor(Color.parseColor("#838383"));
                lng4_bg.setBackgroundResource(R.drawable.inactive_bg);
                lng4_txt.setTextColor(Color.parseColor("#838383"));
            }
        });

        lng4_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected_language="4";
                lng4_bg.setBackgroundResource(R.drawable.btn_bg);
                lng4_txt.setTextColor(Color.parseColor("#ffffff"));
                lng2_bg.setBackgroundResource(R.drawable.inactive_bg);
                lng2_txt.setTextColor(Color.parseColor("#838383"));
                lng3_bg.setBackgroundResource(R.drawable.inactive_bg);
                lng3_txt.setTextColor(Color.parseColor("#838383"));
                eng_bg.setBackgroundResource(R.drawable.inactive_bg);
                eng_txt.setTextColor(Color.parseColor("#838383"));
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

    }
}
