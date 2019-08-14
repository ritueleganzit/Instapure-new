package com.eleganzit.instapure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Fade;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);
        profile_image=findViewById(R.id.profile_image);


        Glide
                .with(this)
                .load(R.drawable.signinimg)
                .apply(new RequestOptions().circleCrop()).into(profile_image);


findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        onBackPressed();
    }
});findViewById(R.id.edit).setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
       startActivity(new Intent(ProfileActivity.this,EditActivity.class));
       //finish();
    }
});
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

    }
}
