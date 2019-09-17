package com.eleganzit.instapure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Fade;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.eleganzit.instapure.utils.UserLoggedInSession;

import static com.eleganzit.instapure.HomeActivity.btn_history;
import static com.eleganzit.instapure.HomeActivity.btn_home;
import static com.eleganzit.instapure.HomeActivity.btn_profile;
import static com.eleganzit.instapure.HomeActivity.btn_scan;
import static com.eleganzit.instapure.HomeActivity.tv_history;
import static com.eleganzit.instapure.HomeActivity.tv_home;
import static com.eleganzit.instapure.HomeActivity.tv_profile;
import static com.eleganzit.instapure.HomeActivity.tv_scan;

public class ProfileActivity extends AppCompatActivity {

    ImageView profile_image;
    TextView txt_name, txt_email, txt_mobile, txt_gender, txt_location, txt_company;
    UserLoggedInSession userLoggedInSession;
    EditText ed_gender,ed_location,ed_company;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        setContentView(R.layout.activity_profile);

        userLoggedInSession = new UserLoggedInSession(this);
        profile_image = findViewById(R.id.profile_image);
        txt_name = findViewById(R.id.txt_name);
        txt_email = findViewById(R.id.txt_email);
        txt_mobile = findViewById(R.id.txt_mobile);

        txt_gender = findViewById(R.id.txt_gender);
        txt_location = findViewById(R.id.txt_location);
        txt_company = findViewById(R.id.txt_company);

        /*
        ed_gender=findViewById(R.id.ed_gender);
        ed_location=findViewById(R.id.ed_location);
        ed_company=findViewById(R.id.ed_company);
*/
        Glide
                .with(this)
                .load(R.drawable.user_pic)
                .apply(new RequestOptions().circleCrop()).into(profile_image);


        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        findViewById(R.id.edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, EditActivity.class));
                //finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(userLoggedInSession.getUserDetails().get(UserLoggedInSession.USER_NAME)!=null) {

            txt_name.setText(userLoggedInSession.getUserDetails().get(UserLoggedInSession.USER_NAME));
            if(userLoggedInSession.getUserDetails().get(UserLoggedInSession.EMAIL).equalsIgnoreCase(""))
            {
                txt_email.setVisibility(View.GONE);
            }
            else {
                txt_email.setText(userLoggedInSession.getUserDetails().get(UserLoggedInSession.EMAIL));
            }

            txt_mobile.setText("+91 "+userLoggedInSession.getUserDetails().get(UserLoggedInSession.MOBILE));
            txt_gender.setText(userLoggedInSession.getUserDetails().get(UserLoggedInSession.GENDER));
            if(userLoggedInSession.getUserDetails().get(UserLoggedInSession.LOCATION).equalsIgnoreCase(""))
            {
                txt_location.setText("Not provided");
            }
            else {
                txt_location.setText(userLoggedInSession.getUserDetails().get(UserLoggedInSession.LOCATION));
            }

            txt_company.setText(userLoggedInSession.getUserDetails().get(UserLoggedInSession.COMPANY));

            Glide
                    .with(this)
                    .load(userLoggedInSession.getUserDetails().get(UserLoggedInSession.PHOTO))
                    .apply(new RequestOptions().circleCrop()).into(profile_image);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }
}
