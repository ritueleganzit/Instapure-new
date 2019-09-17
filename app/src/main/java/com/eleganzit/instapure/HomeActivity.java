package com.eleganzit.instapure;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eleganzit.instapure.fragment.HistoryFragment;
import com.eleganzit.instapure.fragment.HomeFragment;
import com.eleganzit.instapure.fragment.ScanFragment;
import com.eleganzit.instapure.utils.IOnBackPressed;
import com.eleganzit.instapure.utils.UserLoggedInSession;

public class HomeActivity extends AppCompatActivity {

    public static RelativeLayout rel_home,rel_scan,rel_history,rel_profile;
    public static  ImageView btn_home,btn_scan,btn_history,btn_profile;
    public static TextView tv_home,tv_scan,tv_history,tv_profile;
    private static final int REQUEST_CAMERA = 1;
    UserLoggedInSession userLoggedInSession;
    public static HomeActivity homeActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        setContentView(R.layout.activity_home);
        homeActivity=this;

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                REQUEST_CAMERA);

        userLoggedInSession=new UserLoggedInSession(this);

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
                //getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                HomeFragment homeFragment=new HomeFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.frame,homeFragment).commit();

            }
        });
        rel_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                if(userLoggedInSession.alreadyOrdered()){

                    new AlertDialog.Builder(HomeActivity.this).setMessage("QR code scan is already done.")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .show();

                }
                else {
                    ScanFragment homeFragment=new ScanFragment();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .addToBackStack("HomeActivity")
                            .replace(R.id.frame,homeFragment)
                            .commit();
                }

            }
        });
        rel_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                HistoryFragment homeFragment=new HistoryFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack("HomeActivity")
                        .replace(R.id.frame,homeFragment)
                        .commit();

            }
        });
        rel_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_history.setImageResource(R.mipmap.ic_history);
                tv_history.setTextColor(getResources().getColor(R.color.textColor));
                btn_scan.setImageResource(R.mipmap.ic_scan);
                tv_scan.setTextColor(getResources().getColor(R.color.textColor));
                btn_home.setImageResource(R.mipmap.ic_homegrey);
                tv_home.setTextColor(getResources().getColor(R.color.textColor));
                btn_profile.setImageResource(R.mipmap.ic_profileblue);
                tv_profile.setTextColor(getResources().getColor(R.color.colorPrimary));
                startActivity(new Intent(HomeActivity.this,ProfileActivity.class));
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        btn_home.setImageResource(R.mipmap.ic_home);
        tv_home.setTextColor(getResources().getColor(R.color.colorPrimary));
        btn_history.setImageResource(R.mipmap.ic_history);
        tv_history.setTextColor(getResources().getColor(R.color.textColor));
        btn_scan.setImageResource(R.mipmap.ic_scan);
        tv_scan.setTextColor(getResources().getColor(R.color.textColor));

        btn_profile.setImageResource(R.mipmap.ic_profile);
        tv_profile.setTextColor(getResources().getColor(R.color.textColor));

        if(getIntent()!=null || getIntent().getStringExtra("from")!=null){
            if(getIntent().getStringExtra("from").equalsIgnoreCase("filter")){
                HomeFragment homeFragment=new HomeFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.frame,homeFragment).commit();

                Bundle bundle=new Bundle();
                bundle.putString("from_limit",getIntent().getStringExtra("from_limit")+"");
                bundle.putString("to_limit",getIntent().getStringExtra("to_limit")+"");
                bundle.putString("select_months",getIntent().getStringExtra("select_months")+"");

                HistoryFragment historyFragment=new HistoryFragment();
                historyFragment.setArguments(bundle);
                getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack("HomeActivity")
                        .replace(R.id.frame,historyFragment)
                        .commit();

            }
            else {
                HomeFragment homeFragment=new HomeFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.frame,homeFragment).commit();

            }

        }
        else
        {
            HomeFragment homeFragment=new HomeFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.frame,homeFragment).commit();

        }

    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame);
        if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed) fragment).onBackPressed()) {
            super.onBackPressed();
        }
    }

}
