package com.eleganzit.instapure.fragment;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.eleganzit.instapure.R;
import com.google.zxing.Result;

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

    public ScanFragment() {
        // Required empty public constructor
    }

    private CodeScanner mCodeScanner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_scan, container,  false);
        CodeScannerView scannerView = v.findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(getActivity(), scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {

            }
        });
        btn_scan.setImageResource(R.mipmap.ic_scanblue);
        tv_scan.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
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
