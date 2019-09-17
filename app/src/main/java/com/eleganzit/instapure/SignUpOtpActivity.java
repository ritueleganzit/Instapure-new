package com.eleganzit.instapure;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import me.philio.pinentry.PinEntryView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.transition.Fade;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.eleganzit.instapure.api.RetrofitAPI;
import com.eleganzit.instapure.api.RetrofitInterface;
import com.eleganzit.instapure.model.RegisterUserResponse;
import com.eleganzit.instapure.model.ValidateLoginResponse;
import com.eleganzit.instapure.utils.UserLoggedInSession;
import com.google.firebase.iid.FirebaseInstanceId;
import com.stfalcon.smsverifycatcher.OnSmsCatchListener;
import com.stfalcon.smsverifycatcher.SmsVerifyCatcher;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpOtpActivity extends AppCompatActivity {

    TextView submit,txt_mobile;
    PinEntryView vr_code;
    ProgressDialog progressDialog;
    UserLoggedInSession userLoggedInSession;
    private String id,photo,company,fullname,gender,company_id,contact,email,address,language;
    private String Token,devicetoken;
    SmsVerifyCatcher smsVerifyCatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        setContentView(R.layout.activity_sign_up_otp);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Fade fade = new Fade();
            fade.excludeTarget(android.R.id.statusBarBackground, true);
            //fade.excludeTarget(android.R.id.navigationBarBackground, true);

            getWindow().setEnterTransition(fade);
            getWindow().setExitTransition(fade);
        }

        userLoggedInSession = new UserLoggedInSession(SignUpOtpActivity.this);

        submit=findViewById(R.id.submitotp);
        txt_mobile=findViewById(R.id.txt_mobile);
        vr_code=findViewById(R.id.vr_code);
        progressDialog = new ProgressDialog(SignUpOtpActivity.this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        id=getIntent().getStringExtra("id");
        photo=getIntent().getStringExtra("photo");
        company=getIntent().getStringExtra("company");
        fullname=getIntent().getStringExtra("fullname");
        gender=getIntent().getStringExtra("gender");
        company_id=getIntent().getStringExtra("company_id");
        contact=getIntent().getStringExtra("contact");
        email=getIntent().getStringExtra("email");
        address=getIntent().getStringExtra("address");
        language=getIntent().getStringExtra("language");

        txt_mobile.setText("+91"+contact);

        smsVerifyCatcher = new SmsVerifyCatcher(this, new OnSmsCatchListener<String>() {
            @Override
            public void onSmsCatch(String message) {
                String code = parseCode(message);//Parse verification code
                vr_code.setText(code);//set code in edit text
                //then you can send verification code to server
            }
        });

        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                Token= FirebaseInstanceId.getInstance().getToken();
                if (Token!=null)
                {

                    devicetoken=Token;

                    Log.d("Rrrrrmytokenn", Token+"  "+devicetoken);

                    StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().build();
                    StrictMode.setThreadPolicy(threadPolicy);
                    try {
                        JSONObject jsonObject=new JSONObject(Token);
                        Log.d("mytoken", jsonObject.getString("token"));
                        //devicetoken=jsonObject.getString("token");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //getLoginBoth(Token);

                }
                else
                {
                    Toast.makeText(SignUpOtpActivity.this, "No token", Toast.LENGTH_SHORT).show();
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });t.start();


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(vr_code.getText().toString().length()<4)
                {
                    Toast.makeText(SignUpOtpActivity.this, "Please enter valid otp", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    validateUser();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        smsVerifyCatcher.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        smsVerifyCatcher.onStop();
    }

    public String parseCode(String message) {
        Pattern p = Pattern.compile("\\b\\d{4}\\b");
        Matcher m = p.matcher(message);
        String code = "";
        while (m.find()) {
            code = m.group(0);
        }
        return code;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        smsVerifyCatcher.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void validateUser() {
        progressDialog.show();
        RetrofitInterface myInterface = RetrofitAPI.getRetrofit().create(RetrofitInterface.class);
        Call<ValidateLoginResponse> call = myInterface.loginUser(contact,vr_code.getText().toString(),devicetoken+"");
        call.enqueue(new Callback<ValidateLoginResponse>() {
            @Override
            public void onResponse(Call<ValidateLoginResponse> call, Response<ValidateLoginResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().toString().equalsIgnoreCase("1")) {
                        if (response.body().getData() != null) {
                            String email, id, username,mobile,photo, gender,location,company;
                            for (int i = 0; i < response.body().getData().size(); i++) {
                                email = response.body().getData().get(i).getEmail();
                                id = response.body().getData().get(i).getUserId();
                                username = response.body().getData().get(i).getFullname();
                                mobile = response.body().getData().get(i).getContact();
                                photo = response.body().getData().get(i).getPhoto();
                                gender = response.body().getData().get(i).getGender();
                                location = response.body().getData().get(i).getAddress();
                                company = response.body().getData().get(i).getCompanyName();

                                userLoggedInSession.createLoginSession(email, id, username,mobile,photo, gender,location,company);

                            }

                        }
                    } else {

                        Toast.makeText(SignUpOtpActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
                else {

                    Toast.makeText(SignUpOtpActivity.this, "Server or Internet Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ValidateLoginResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(SignUpOtpActivity.this, "Server or Internet Error" , Toast.LENGTH_SHORT).show();
            }
        });

    }

    /*private void registerUser() {
        progressDialog.show();
        RetrofitInterface myInterface = RetrofitAPI.getRetrofit().create(RetrofitInterface.class);
        Call<RegisterUserResponse> call = myInterface.registerUser(
                fullname,
                gender,
                company_id,
                contact,
                email,
                address,
                language
        );
        call.enqueue(new Callback<RegisterUserResponse>() {
            @Override
            public void onResponse(Call<RegisterUserResponse> call, Response<RegisterUserResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().toString().equalsIgnoreCase("1")) {
                        if (response.body().getData() != null) {
                            String email, id, username,mobile,photo, gender,location,company;
                            for (int i = 0; i < response.body().getData().size(); i++) {
                                email = response.body().getData().get(i).getEmail();
                                id = response.body().getData().get(i).getUserId();
                                username = response.body().getData().get(i).getFullname();
                                mobile = response.body().getData().get(i).getContact();
                                photo = response.body().getData().get(i).getPhoto();
                                gender = response.body().getData().get(i).getGender();
                                location = response.body().getData().get(i).getAddress();
                                company = response.body().getData().get(i).getCompany_name();

                                userLoggedInSession.createLoginSession(email, id, username,mobile,photo, gender,location,company);

                            }

                        }
                    } else {

                        Toast.makeText(SignUpOtpActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
                else {

                    Toast.makeText(SignUpOtpActivity.this, "Server or Internet Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterUserResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(SignUpOtpActivity.this, "Server or Internet Error" , Toast.LENGTH_SHORT).show();
            }
        });

    }
*/
}
