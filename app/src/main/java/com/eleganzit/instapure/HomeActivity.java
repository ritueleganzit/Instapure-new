package com.eleganzit.instapure;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eleganzit.instapure.fragment.HistoryFragment;
import com.eleganzit.instapure.fragment.HomeFragment;
import com.eleganzit.instapure.fragment.ScanFragment;

public class HomeActivity extends AppCompatActivity {

    public static RelativeLayout rel_home,rel_scan,rel_history,rel_profile;
    public static  ImageView btn_home,btn_scan,btn_history,btn_profile;
    public static TextView tv_home,tv_scan,tv_history,tv_profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        rel_home=findViewById(R.id.rel_home);
        btn_home=findViewById(R.id.btn_home);
        tv_home=findViewById(R.id.tv_home);
        rel_scan=findViewById(R.id.rel_scan);
        btn_scan=findViewById(R.id.btn_scan);
        tv_scan=findViewById(R.id.tv_scan);

        rel_history=findViewById(R.id.rel_history);
        btn_history=findViewById(R.id.btn_history);
        tv_history=findViewById(R.id.tv_history);
        rel_profile=findViewById(R.id.rel_profile);
        btn_profile=findViewById(R.id.btn_profile);
        tv_profile=findViewById(R.id.tv_profile);




        rel_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                HomeFragment homeFragment=new HomeFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.frame,homeFragment).commit();

            }
        });
        rel_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                ScanFragment homeFragment=new ScanFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.frame,homeFragment).commit();

            }
        });
        rel_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                HistoryFragment homeFragment=new HistoryFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.frame,homeFragment).commit();

            }
        });rel_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_history.setImageResource(R.mipmap.ic_history);
                tv_history.setTextColor(getResources().getColor(R.color.textColor));
                btn_scan.setImageResource(R.mipmap.ic_scan);
                tv_scan.setTextColor(getResources().getColor(R.color.textColor));
                btn_home.setImageResource(R.mipmap.ic_homegrey);
                tv_home.setTextColor(getResources().getColor(R.color.textColor));
                btn_profile.setImageResource(R.mipmap.ic_profileblue);
                tv_profile.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
               startActivity(new Intent(HomeActivity.this,ProfileActivity.class));
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        btn_home.setImageResource(R.mipmap.ic_home);
        tv_home.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        btn_history.setImageResource(R.mipmap.ic_history);
        tv_history.setTextColor(getResources().getColor(R.color.textColor));
        btn_scan.setImageResource(R.mipmap.ic_scan);
        tv_scan.setTextColor(getResources().getColor(R.color.textColor));

        btn_profile.setImageResource(R.mipmap.ic_profile);
        tv_profile.setTextColor(getResources().getColor(R.color.textColor));
        HomeFragment homeFragment=new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame,homeFragment).commit();


    }
}
