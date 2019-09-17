package com.eleganzit.instapure;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.eleganzit.instapure.api.RetrofitAPI;
import com.eleganzit.instapure.api.RetrofitInterface;
import com.eleganzit.instapure.model.ReorderBannerResponse;
import com.eleganzit.instapure.model.ReorderResponse;
import com.eleganzit.instapure.model.RewardsResponse;
import com.eleganzit.instapure.utils.UserLoggedInSession;

public class ReorderActivity extends AppCompatActivity {

    TextView submit;
    ProgressDialog progressDialog;
    UserLoggedInSession userLoggedInSession;
    EditText ed_message;
    RadioGroup rg1,rg2,rg3;
    ImageView reorder_banner;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reorder);

        ImageView back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        progressDialog = new ProgressDialog(ReorderActivity.this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        userLoggedInSession=new UserLoggedInSession(this);

        reorder_banner=findViewById(R.id.reorder_banner);
        submit=findViewById(R.id.submit);
        rg1=findViewById(R.id.rg1);
        rg2=findViewById(R.id.rg2);
        rg3=findViewById(R.id.rg3);
        ed_message=findViewById(R.id.ed_message);
        progress=findViewById(R.id.progress);

        Log.d("aytsdftasdvg",rg1.getCheckedRadioButtonId()+"  <>"+
                rg2.getCheckedRadioButtonId()+" <> "+
                rg3.getCheckedRadioButtonId());

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rg1.getCheckedRadioButtonId()==-1)
                {
                    Toast.makeText(ReorderActivity.this, "Please answer all the questions", Toast.LENGTH_SHORT).show();
                }
                else if(rg2.getCheckedRadioButtonId()==-1)
                {
                    Toast.makeText(ReorderActivity.this, "Please answer all the questions", Toast.LENGTH_SHORT).show();
                }
                else if(rg3.getCheckedRadioButtonId()==-1)
                {
                    Toast.makeText(ReorderActivity.this, "Please answer all the questions", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    userReorder();
                }
            }
        });

        getReorderBanner();
    }


    private void getReorderBanner() {
        progress.setVisibility(View.VISIBLE);
        RetrofitInterface myInterface = RetrofitAPI.getRetrofit().create(RetrofitInterface.class);

        Call<ReorderBannerResponse> call = myInterface.reorderBanner("");
        call.enqueue(new Callback<ReorderBannerResponse>() {
            @Override
            public void onResponse(Call<ReorderBannerResponse> call, Response<ReorderBannerResponse> response) {
                progress.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    if (response.body().getStatus().toString().equalsIgnoreCase("1")) {
                        if (response.body().getData() != null) {

                            Glide
                                    .with(ReorderActivity.this)
                                    .load(response.body().getData().get(0).getImagePath())
                                    .apply(new RequestOptions().centerCrop()).into(reorder_banner);


                        }
                    } else {

                        Toast.makeText(ReorderActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
                else {

                    Toast.makeText(ReorderActivity.this, "Server or Internet Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReorderBannerResponse> call, Throwable t) {
                progress.setVisibility(View.GONE);
                Toast.makeText(ReorderActivity.this, "Server or Internet Error" , Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void userReorder() {
        progressDialog.show();
        RetrofitInterface myInterface = RetrofitAPI.getRetrofit().create(RetrofitInterface.class);
        String user_id = userLoggedInSession.getUserDetails().get(UserLoggedInSession.USER_ID)+"";

        int selectedId1 = rg1.getCheckedRadioButtonId();
        int selectedId2 = rg2.getCheckedRadioButtonId();
        int selectedId3 = rg3.getCheckedRadioButtonId();

        RadioButton radioButton1 =  findViewById(selectedId1);
        RadioButton radioButton2 =  findViewById(selectedId2);
        RadioButton radioButton3 =  findViewById(selectedId3);


        Call<ReorderResponse> call = myInterface.userReorder(user_id, radioButton1.getText().toString(),radioButton2.getText().toString(),radioButton3.getText().toString(),ed_message.getText().toString());
        call.enqueue(new Callback<ReorderResponse>() {
            @Override
            public void onResponse(Call<ReorderResponse> call, Response<ReorderResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().toString().equalsIgnoreCase("1")) {
                        if (response.body().getData() != null) {

                            startActivity(new Intent(ReorderActivity.this, OrderConfirmationActivity2.class));
                            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                            finish();

                        }
                    } else {

                        Toast.makeText(ReorderActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
                else {

                    Toast.makeText(ReorderActivity.this, "Server or Internet Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReorderResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ReorderActivity.this, "Server or Internet Error" , Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }
}
