package com.eleganzit.instapure.fragment;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.eleganzit.instapure.FeedbackActivity;
import com.eleganzit.instapure.NotificationActivity;
import com.eleganzit.instapure.OrderConfirmationActivity;
import com.eleganzit.instapure.ProductInstructionActivity;
import com.eleganzit.instapure.R;
import com.eleganzit.instapure.SignUpActivity;
import com.eleganzit.instapure.SignUpOtpActivity;
import com.eleganzit.instapure.api.RetrofitAPI;
import com.eleganzit.instapure.api.RetrofitInterface;
import com.eleganzit.instapure.model.HistoryData;
import com.eleganzit.instapure.model.ScanQRResponse;
import com.eleganzit.instapure.model.VideoFeedResponse;
import com.eleganzit.instapure.utils.UserLanguageSession;
import com.eleganzit.instapure.utils.UserLoggedInSession;
import com.google.zxing.Result;

import java.util.ArrayList;

import static com.eleganzit.instapure.HomeActivity.btn_history;
import static com.eleganzit.instapure.HomeActivity.btn_home;
import static com.eleganzit.instapure.HomeActivity.btn_profile;
import static com.eleganzit.instapure.HomeActivity.btn_scan;
import static com.eleganzit.instapure.HomeActivity.tv_history;
import static com.eleganzit.instapure.HomeActivity.tv_home;
import static com.eleganzit.instapure.HomeActivity.tv_profile;
import static com.eleganzit.instapure.HomeActivity.tv_scan;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScanFragment extends Fragment {

    private static final int REQUEST_CAMERA = 1;
    ProgressDialog progressDialog;
    UserLoggedInSession userLoggedInSession;

    public ScanFragment() {
        // Required empty public constructor
    }

    private CodeScanner mCodeScanner;
    ImageView notification,feedback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_scan, container,  false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#FFFFFF"));
        }

        ImageView back=v.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStackImmediate();

            }
        });
        Activity activity=getActivity();

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        userLoggedInSession=new UserLoggedInSession(getActivity());

        CodeScannerView scannerView = v.findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(getActivity(), scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        Toast.makeText(activity, result.getText(), Toast.LENGTH_SHORT).show();

                        Log.d("scandataaa",result.getText()+"  "+result.getNumBits()+"  ");

                        scanQRcode(result.getText());

                    }
                });

            }
        });

        notification=v.findViewById(R.id.notification);
        feedback=v.findViewById(R.id.feedback);

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), NotificationActivity.class).putExtra("from","scan"));
                getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), FeedbackActivity.class));
                getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

            }
        });

        btn_scan.setImageResource(R.mipmap.ic_scanblue);
        tv_scan.setTextColor(getResources().getColor(R.color.colorPrimary));
        btn_home.setImageResource(R.mipmap.ic_homegrey);
        tv_home.setTextColor(getResources().getColor(R.color.textColor));
        btn_history.setImageResource(R.mipmap.ic_history);
        tv_history.setTextColor(getResources().getColor(R.color.textColor));
        btn_profile.setImageResource(R.mipmap.ic_profile);
        tv_profile.setTextColor(getResources().getColor(R.color.textColor));
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.CAMERA)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CAMERA},
                        REQUEST_CAMERA);

                // MY_PERMISSIONS_REQUEST_CAMRA is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });

        return v;
    }

    private void scanQRcode(String code) {

        progressDialog.show();
        RetrofitInterface myInterface = RetrofitAPI.getRetrofit().create(RetrofitInterface.class);
        Call<ScanQRResponse> call = myInterface.scanQRcode(userLoggedInSession.getUserDetails().get(UserLoggedInSession.USER_ID), code+"");
        call.enqueue(new Callback<ScanQRResponse>() {
            @Override
            public void onResponse(Call<ScanQRResponse> call, Response<ScanQRResponse> response) {

                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().toString().equalsIgnoreCase("1")) {

                        userLoggedInSession.firstOrder();
                        Intent i = new Intent(getActivity(), OrderConfirmationActivity.class);
                        startActivity(i);
                        getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                        getActivity().finish();

                    } else {

                        new AlertDialog.Builder(getActivity())
                                .setMessage(""+response.body().getMessage())
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                })
                        .show();
                        //Toast.makeText(getActivity(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {

                    Toast.makeText(getActivity(), "Server or Internet Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ScanQRResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Server or Internet Error", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    public void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        {
            switch (requestCode) {
                case REQUEST_CAMERA: {
                    // If request is cancelled, the result arrays are empty.
                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getActivity(), "Permission Granted, Now you can access camera", Toast.LENGTH_LONG).show();

                        // permission was granted, yay! Open
                        // camera, take photo.

                    } else {
                        Toast.makeText(getActivity(), "Permission denied, Now you can access camera", Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1);
                        // permission denied, boo! Disable the
                        // functionality that depends on this permission.
                    }
                    return;
                }

                // other 'case' lines to check for other
                // permissions this app might request

            }
        }
    }
}
