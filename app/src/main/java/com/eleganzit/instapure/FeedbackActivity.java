package com.eleganzit.instapure;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.transition.Fade;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eleganzit.instapure.api.RetrofitAPI;
import com.eleganzit.instapure.api.RetrofitInterface;
import com.eleganzit.instapure.model.AddFeedbackResponse;
import com.eleganzit.instapure.model.RegisterUserResponse;
import com.eleganzit.instapure.utils.UserLoggedInSession;

public class FeedbackActivity extends AppCompatActivity {

    EditText ed_message;
    TextView submit;
    ProgressDialog progressDialog;
    UserLoggedInSession userLoggedInSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        setContentView(R.layout.activity_feedback);
        progressDialog = new ProgressDialog(FeedbackActivity.this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        userLoggedInSession = new UserLoggedInSession(this);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ed_message=findViewById(R.id.ed_message);
        submit=findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendFeedback();
            }
        });
        
    }

    private void sendFeedback() {
        progressDialog.show();
        RetrofitInterface myInterface = RetrofitAPI.getRetrofit().create(RetrofitInterface.class);
        Call<AddFeedbackResponse> call = myInterface.addFeedback(userLoggedInSession.getUserDetails().get(UserLoggedInSession.USER_ID),ed_message.getText().toString());
        call.enqueue(new Callback<AddFeedbackResponse>() {
            @Override
            public void onResponse(Call<AddFeedbackResponse> call, Response<AddFeedbackResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().toString().equalsIgnoreCase("1")) {
                        if (response.body().getData() != null) {

                            Toast.makeText(FeedbackActivity.this, "Thanks for your feedback!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    } else {

                        Toast.makeText(FeedbackActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
                else {

                    Toast.makeText(FeedbackActivity.this, "Server or Internet Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddFeedbackResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(FeedbackActivity.this, "Server or Internet Error" , Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

    }
}
