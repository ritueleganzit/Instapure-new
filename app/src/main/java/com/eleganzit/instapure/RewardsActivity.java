package com.eleganzit.instapure;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eleganzit.instapure.api.RetrofitAPI;
import com.eleganzit.instapure.api.RetrofitInterface;
import com.eleganzit.instapure.model.RewardsResponse;
import com.eleganzit.instapure.model.UpdateProfileResponse;
import com.eleganzit.instapure.utils.UserLoggedInSession;

public class RewardsActivity extends AppCompatActivity {


    ProgressDialog progressDialog;
    UserLoggedInSession userLoggedInSession;
    TextView txt_points;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards);

        ImageView back=findViewById(R.id.back);
        txt_points=findViewById(R.id.txt_points);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        progressDialog = new ProgressDialog(RewardsActivity.this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        userLoggedInSession=new UserLoggedInSession(this);

        getRewards();

    }

    private void getRewards() {
        progressDialog.show();
        RetrofitInterface myInterface = RetrofitAPI.getRetrofit().create(RetrofitInterface.class);
        String user_id = userLoggedInSession.getUserDetails().get(UserLoggedInSession.USER_ID)+"";

        Call<RewardsResponse> call = myInterface.getRewards(user_id);
        call.enqueue(new Callback<RewardsResponse>() {
            @Override
            public void onResponse(Call<RewardsResponse> call, Response<RewardsResponse> response) {

                progressDialog.dismiss();

                if (response.isSuccessful()) {
                    if (response.body().getStatus().toString().equalsIgnoreCase("1")) {
                        if (response.body().getData() != null) {

                            for (int i = 0; i < response.body().getData().size(); i++) {
                                if(response.body().getData().get(i).getRewards()!=null)
                                txt_points.setText(response.body().getData().get(i).getRewards());
                                else
                                    txt_points.setText("0");
                            }

                        }
                    } else {

                        Toast.makeText(RewardsActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
                else {

                    Toast.makeText(RewardsActivity.this, "Server or Internet Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RewardsResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(RewardsActivity.this, "Server or Internet Error" , Toast.LENGTH_SHORT).show();
            }
        });

    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

    }
}
