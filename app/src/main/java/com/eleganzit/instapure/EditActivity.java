package com.eleganzit.instapure;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;
import me.nereo.multi_image_selector.MultiImageSelector;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Fade;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.commons.io.FileUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.eleganzit.instapure.adapter.CompaniesRecyclerAdapter;
import com.eleganzit.instapure.api.RetrofitAPI;
import com.eleganzit.instapure.api.RetrofitInterface;
import com.eleganzit.instapure.model.CompanyListData;
import com.eleganzit.instapure.model.CompanyListResponse;
import com.eleganzit.instapure.model.GetUserResponse;
import com.eleganzit.instapure.model.RegisterUserResponse;
import com.eleganzit.instapure.model.UpdateProfileResponse;
import com.eleganzit.instapure.utils.MyLinearLayoutManager;
import com.eleganzit.instapure.utils.UserLoggedInSession;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditActivity extends AppCompatActivity implements CompaniesRecyclerAdapter.ContactsAdapterListener {


    ImageView profile_pic;
    TextView done;
    ProgressDialog progressDialog;
    RelativeLayout user_add;

    final ArrayList<CompanyListData> items = new ArrayList<>();
    final ArrayList<String> sitems = new ArrayList<>();
    ProgressBar c_progress;
    boolean got_success =false;
    EditText ed_name,ed_mobile,ed_email,ed_location,ed_company,ed_language;
    ScrollView scroll_v;
    String company_id="";
    Spinner spinner;
    UserLoggedInSession userLoggedInSession;
    private static final int REQUEST_IMAGE1 = 101;
    protected static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION1 = 102;
    private ArrayList<String> mSelectPath1;
    private int REQUEST_CAMERA = 0;
    private String mediapath;
    private File file;
    CardView card_mentions;
    private RecyclerView recyclerView;
    CompaniesRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        setContentView(R.layout.activity_edit);
        profile_pic=findViewById(R.id.profile_pic);
        done=findViewById(R.id.done);
        ed_name=findViewById(R.id.ed_name);
        ed_mobile=findViewById(R.id.ed_mobile);
        ed_email=findViewById(R.id.ed_email);
        ed_location=findViewById(R.id.ed_location);
        ed_company=findViewById(R.id.ed_company);
        ed_language=findViewById(R.id.ed_language);
        spinner=findViewById(R.id.gen_spinner);
        scroll_v=findViewById(R.id.scroll_v);
        user_add=findViewById(R.id.user_add);
        userLoggedInSession=new UserLoggedInSession(this);
        card_mentions = findViewById(R.id.card_mentions);

        recyclerView = findViewById(R.id.rc_mentions);
        c_progress = findViewById(R.id.c_progress);

        MyLinearLayoutManager mLayoutManager = new MyLinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        //recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new CompaniesRecyclerAdapter(this, items, this);

        progressDialog = new ProgressDialog(EditActivity.this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        Glide
                .with(this)
                .load(R.drawable.user_pic)
                .apply(new RequestOptions().circleCrop()).into(profile_pic);

        user_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
            }
        });
        
        if(userLoggedInSession.getUserDetails().get(UserLoggedInSession.USER_NAME)!=null) {

            getUser();
            Glide
                    .with(this)
                    .load(userLoggedInSession.getUserDetails().get(UserLoggedInSession.PHOTO))
                    .apply(new RequestOptions().circleCrop()).into(profile_pic);

        }

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ed_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditActivity.this,ChangeLanguage2Activity.class));
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });

        ed_company.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b) {
                    Point childOffset = new Point();
                    getDeepChildOffset(scroll_v, view.getParent(), view, childOffset);
                    // Scroll to child.
                    scroll_v.smoothScrollTo(0, 400);
                }

            }
        });

        ed_company.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("whichccc", "onTextChanged");

                if(!got_success){
                    getCompanyList2();
                }

                Point childOffset = new Point();
                getDeepChildOffset(scroll_v, ed_company.getParent(), ed_company, childOffset);
                // Scroll to child.
                scroll_v.smoothScrollTo(0, 900);

                if (!(charSequence.toString().isEmpty())) {
                    Log.d("whereeeeeeeeee", "first if");
                    if (items.size() > 0) {
                        Log.d("whereeeeeeeeee", "second if "+charSequence);

                        card_mentions.setVisibility(View.VISIBLE);
                        mAdapter.getFilter().filter(charSequence);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        Log.d("whereeeeeeeeee", "first else");

                        card_mentions.setVisibility(View.GONE);
                    }


                } else {
                    Log.d("whereeeeeeeeee", "second else");
                    card_mentions.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for(int i=0;i<items.size();i++){

                    if(items.get(i).getTitle().equalsIgnoreCase(""+ed_company.getText().toString())){
                        company_id=items.get(i).getCompanyId();
                    }

                }

                if(isValid()){
                    if(file!=null){
                        updateProfileP();
                    }
                    else
                    {
                        updateProfile();
                    }
                }

            }
        });

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.ed_genders))
        {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView view = (TextView) super.getView(position, convertView, parent);
                Typeface typeface = ResourcesCompat.getFont(EditActivity.this, R.font.roboto_light);

                view.setTypeface(typeface);
                view.setTextColor(Color.BLACK);

                return view;
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                Typeface typeface = ResourcesCompat.getFont(EditActivity.this, R.font.roboto_light);

                TextView tv = (TextView) view;
                tv.setTypeface(typeface);
                tv.setTextColor(Color.BLACK);

                return view;
            }
        };

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // First item will be gray
                ((TextView) view).setTextColor(ContextCompat.getColor(EditActivity.this, R.color.textColor));
                Typeface typeface = ResourcesCompat.getFont(EditActivity.this, R.font.roboto_regular);

                TextView tv = (TextView) view;
                tv.setTypeface(typeface);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        /*
        ed_company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SimpleSearchDialogCompat(EditActivity.this, "Search Company",
                        "Which company are you looking for?", null, items,
                        new SearchResultListener<CompanyListData>() {
                            @Override
                            public void onSelected(BaseSearchDialogCompat dialog,
                                                   CompanyListData item, int position) {

                                ed_company.setText(item.getTitle());
                                company_id=item.getCompanyId();
                                dialog.dismiss();
                            }
                        }).show();

            }
        });*/

    }

    private void pickImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN // Permission was added in API Level 16
                && ActivityCompat.checkSelfPermission(EditActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                    getString(R.string.mis_permission_rationale),
                    REQUEST_STORAGE_READ_ACCESS_PERMISSION1);
        } else {

            MultiImageSelector selector = MultiImageSelector.create(EditActivity.this);
            selector.single();
            selector.showCamera(false);

            selector.origin(mSelectPath1);
            selector.start(EditActivity.this, REQUEST_IMAGE1);
        }
    }


    private void getDeepChildOffset(final ViewGroup mainParent, final ViewParent parent, final View child, final Point accumulatedOffset) {
        ViewGroup parentGroup = (ViewGroup) parent;
        accumulatedOffset.x += child.getLeft();
        accumulatedOffset.y += child.getTop();
        if (parentGroup.equals(mainParent)) {
            return;
        }
        getDeepChildOffset(mainParent, parentGroup.getParent(), parentGroup, accumulatedOffset);
    }


    private void requestPermission(final String permission, String rationale, final int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(EditActivity.this, permission)) {
            new AlertDialog.Builder(EditActivity.this)
                    .setTitle(R.string.mis_permission_dialog_title)
                    .setMessage(rationale)
                    .setPositiveButton(R.string.mis_permission_dialog_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(EditActivity.this, new String[]{permission}, requestCode);
                        }
                    })
                    .setNegativeButton(R.string.mis_permission_dialog_cancel, null)
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(EditActivity.this, new String[]{permission}, requestCode);
        }
    }

    private void getCompanyList() {
        RetrofitInterface myInterface = RetrofitAPI.getRetrofit().create(RetrofitInterface.class);
        Call<CompanyListResponse> call = myInterface.companyList("");
        call.enqueue(new Callback<CompanyListResponse>() {
            @Override
            public void onResponse(Call<CompanyListResponse> call, Response<CompanyListResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {

                    got_success=true;

                    if (response.body().getStatus().toString().equalsIgnoreCase("1")) {
                        if (response.body().getData() != null) {
                            String id, name;
                            items.clear();
                            for (int i = 0; i < response.body().getData().size(); i++) {
                                id = response.body().getData().get(i).getCompanyId();
                                name = response.body().getData().get(i).getTitle();

                                items.add(new CompanyListData(id,name));
                                sitems.add(name);
                            }

                            mAdapter = new CompaniesRecyclerAdapter(EditActivity.this, items, new CompaniesRecyclerAdapter.ContactsAdapterListener() {
                                @Override
                                public void onContactSelected(CompanyListData companyListData) {
                                    //Toast.makeText(EditActivity.this, ""+companyListData.getTitle(), Toast.LENGTH_SHORT).show();
                                    Log.d("rteretr",companyListData.getTitle()+"");
                                    company_id=companyListData.getCompanyId();
                                    ed_company.setText("");
                                    ed_company.append(companyListData.getTitle()+"");
                                    card_mentions.setVisibility(View.GONE);
                                }
                            });
                            recyclerView.setAdapter(mAdapter);

                        }
                    } else {

                        Toast.makeText(EditActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }
                else {

                    got_success=false;
                    Toast.makeText(EditActivity.this, "Server or Internet Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CompanyListResponse> call, Throwable t) {
                got_success=false;
                progressDialog.dismiss();
                Toast.makeText(EditActivity.this, "Server or Internet Error" , Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getCompanyList2() {
        c_progress.setVisibility(View.VISIBLE);
        RetrofitInterface myInterface = RetrofitAPI.getRetrofit().create(RetrofitInterface.class);
        Call<CompanyListResponse> call = myInterface.companyList("");
        call.enqueue(new Callback<CompanyListResponse>() {
            @Override
            public void onResponse(Call<CompanyListResponse> call, Response<CompanyListResponse> response) {
                c_progress.setVisibility(View.GONE);
                if (response.isSuccessful()) {

                    got_success=true;

                    if (response.body().getStatus().toString().equalsIgnoreCase("1")) {
                        if (response.body().getData() != null) {
                            String id, name;
                            items.clear();
                            for (int i = 0; i < response.body().getData().size(); i++) {
                                id = response.body().getData().get(i).getCompanyId();
                                name = response.body().getData().get(i).getTitle();

                                items.add(new CompanyListData(id,name));
                                sitems.add(name);
                            }

                            mAdapter = new CompaniesRecyclerAdapter(EditActivity.this, items, new CompaniesRecyclerAdapter.ContactsAdapterListener() {
                                @Override
                                public void onContactSelected(CompanyListData companyListData) {
                                    //Toast.makeText(EditActivity.this, ""+companyListData.getTitle(), Toast.LENGTH_SHORT).show();
                                    Log.d("rteretr",companyListData.getTitle()+"");
                                    company_id=companyListData.getCompanyId();
                                    ed_company.setText("");
                                    ed_company.append(companyListData.getTitle()+" ");
                                    card_mentions.setVisibility(View.GONE);
                                }
                            });
                            recyclerView.setAdapter(mAdapter);

                        }
                    } else {

                        Toast.makeText(EditActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }
                else {

                    got_success=false;
                    Toast.makeText(EditActivity.this, "Server or Internet Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CompanyListResponse> call, Throwable t) {
                got_success=false;
                c_progress.setVisibility(View.GONE);
                Toast.makeText(EditActivity.this, "Server or Internet Error" , Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getUser() {
        progressDialog.show();
        RetrofitInterface myInterface = RetrofitAPI.getRetrofit().create(RetrofitInterface.class);
        Call<GetUserResponse> call = myInterface.getUser(userLoggedInSession.getUserDetails().get(UserLoggedInSession.USER_ID));
        call.enqueue(new Callback<GetUserResponse>() {
            @Override
            public void onResponse(Call<GetUserResponse> call, Response<GetUserResponse> response) {

                if (response.isSuccessful()) {
                    if (response.body().getStatus().toString().equalsIgnoreCase("1")) {

                        getCompanyList();

                        if (response.body().getData() != null) {
                            String name="", photo="",mobile="", email="", gender="", company ="", location="";
                            for (int i = 0; i < response.body().getData().size(); i++) {
                                name = response.body().getData().get(i).getFullname();
                                photo = response.body().getData().get(i).getPhoto();
                                mobile = response.body().getData().get(i).getContact();
                                email = response.body().getData().get(i).getEmail();
                                gender = response.body().getData().get(i).getGender();
                                company = response.body().getData().get(i).getCompanyName();
                                location = response.body().getData().get(i).getAddress();
                                company_id = response.body().getData().get(i).getCompanyId();

                                Glide
                                        .with(EditActivity.this)
                                        .load(photo)
                                        .apply(new RequestOptions().circleCrop()).into(profile_pic);

                                ed_name.setText(name);
                                ed_mobile.setText(mobile);
                                ed_email.setText(email);
                                if(gender.equalsIgnoreCase("male"))
                                {
                                    spinner.setSelection(0);
                                }
                                else
                                {
                                    spinner.setSelection(1);
                                }
                                ed_company.setText(company);
                                ed_location.setText(location);

                            }
                            userLoggedInSession.updateLoginSession(email,userLoggedInSession.getUserDetails().get(UserLoggedInSession.USER_ID),name,mobile,photo, gender,location,company);

                        }
                    } else {

                        Toast.makeText(EditActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }
                else {

                    Toast.makeText(EditActivity.this, "Server or Internet Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetUserResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(EditActivity.this, "Server or Internet Error" , Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateProfileP() {
        progressDialog.show();
        RetrofitInterface myInterface = RetrofitAPI.getRetrofit().create(RetrofitInterface.class);
        RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"), userLoggedInSession.getUserDetails().get(UserLoggedInSession.USER_ID)+"");
        RequestBody fullname = RequestBody.create(MediaType.parse("text/plain"), ed_name.getText().toString().replaceFirst("\\s++$", "")+"");
        RequestBody gender = RequestBody.create(MediaType.parse("text/plain"), spinner.getSelectedItem().toString()+"");
        RequestBody company_id = RequestBody.create(MediaType.parse("text/plain"), this.company_id);
        RequestBody contact = RequestBody.create(MediaType.parse("text/plain"), ed_mobile.getText().toString()+"");
        RequestBody email = RequestBody.create(MediaType.parse("text/plain"), ed_email.getText().toString().trim()+"");
        RequestBody address = RequestBody.create(MediaType.parse("text/plain"), ed_location.getText().toString().replaceFirst("\\s++$", "")+"");
        Call<UpdateProfileResponse> call;

            RequestBody photo = RequestBody.create(MediaType.parse("*/*"), file);
            MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("photo",file.getName(),photo);
            call = myInterface.updateProfile(user_id, multipartBody,fullname,gender,company_id,contact,email,address);

        call.enqueue(new Callback<UpdateProfileResponse>() {
            @Override
            public void onResponse(Call<UpdateProfileResponse> call, Response<UpdateProfileResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().toString().equalsIgnoreCase("1")) {
                        if (response.body().getData() != null) {
                            String email, id, username,mobile,photo,genderr,location,company;
                            for (int i = 0; i < response.body().getData().size(); i++) {
                                email = response.body().getData().get(i).getEmail();
                                id = response.body().getData().get(i).getUserId();
                                username = response.body().getData().get(i).getFullname();
                                mobile = response.body().getData().get(i).getContact();
                                photo = response.body().getData().get(i).getPhoto();
                                genderr = response.body().getData().get(i).getGender();
                                location = response.body().getData().get(i).getAddress();
                                company = response.body().getData().get(i).getCompany_name();


                                userLoggedInSession.updateLoginSession(email, id, username,mobile,photo, genderr,location,company);

                            }
                            finish();
                            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

                        }
                    } else {

                        Toast.makeText(EditActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
                else {

                    Toast.makeText(EditActivity.this, "Server or Internet Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UpdateProfileResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(EditActivity.this, "Server or Internet Error" , Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateProfile() {
        progressDialog.show();
        RetrofitInterface myInterface = RetrofitAPI.getRetrofit().create(RetrofitInterface.class);
        String user_id = userLoggedInSession.getUserDetails().get(UserLoggedInSession.USER_ID)+"";
        String fullname =ed_name.getText().toString().replaceFirst("\\s++$", "")+"";
        String gender = spinner.getSelectedItem().toString()+"";
        String company_id = this.company_id;
        String contact = ed_mobile.getText().toString()+"";
        String email = ed_email.getText().toString().trim()+"";
        String address = ed_location.getText().toString().replaceFirst("\\s++$", "")+"";
        
        Log.d("ajgfdfhda",user_id+"  ,  "+ fullname+"  ,  "+gender+"  ,  "+company_id+"  ,  "+contact+"  ,  "+email+"  ,  "+address);
        
        Call<UpdateProfileResponse> call = myInterface.updateProfile(user_id, fullname,gender,company_id,contact,email,address);
        call.enqueue(new Callback<UpdateProfileResponse>() {
            @Override
            public void onResponse(Call<UpdateProfileResponse> call, Response<UpdateProfileResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().toString().equalsIgnoreCase("1")) {
                        if (response.body().getData() != null) {
                            String email, id, username,mobile,photo,genderr,location,company;
                            for (int i = 0; i < response.body().getData().size(); i++) {
                                email = response.body().getData().get(i).getEmail();
                                id = response.body().getData().get(i).getUserId();
                                username = response.body().getData().get(i).getFullname();
                                mobile = response.body().getData().get(i).getContact();
                                photo = response.body().getData().get(i).getPhoto();
                                genderr = response.body().getData().get(i).getGender();
                                location = response.body().getData().get(i).getAddress();
                                company = response.body().getData().get(i).getCompany_name();

                                userLoggedInSession.updateLoginSession(email, id, username,mobile,photo, genderr,location,company);

                            }
                            finish();
                            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

                        }
                    } else {

                        Toast.makeText(EditActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
                else {

                    Toast.makeText(EditActivity.this, "Server or Internet Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UpdateProfileResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(EditActivity.this, "Server or Internet Error" , Toast.LENGTH_SHORT).show();
            }
        });

    }


    public boolean isValid() {
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(ed_email.getText().toString());

        if (ed_name.getText().toString().equals("")) {
            ed_name.setError("Please enter name");

            ed_name.requestFocus();
            return false;
        } else if (ed_mobile.getText().toString().equals("")) {
            ed_mobile.setError("Please enter mobile number");

            ed_mobile.requestFocus();
            return false;
        }else if (spinner.getSelectedItem().toString().equalsIgnoreCase("gender*")) {

            spinner.requestFocus();
            Toast.makeText(this, "Please select gender", Toast.LENGTH_SHORT).show();

            return false;
        } else if (!sitems.contains(ed_company.getText().toString()) || company_id.equalsIgnoreCase("")) {
            ed_company.setError(getResources().getString(R.string.please_select_from_company));

            ed_company.requestFocus();
            return false;
        }
        return true;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == REQUEST_IMAGE1) {
                if (resultCode == Activity.RESULT_OK) {
                    mSelectPath1 = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                    StringBuilder sb = new StringBuilder();
                    for (String p : mSelectPath1) {
                        sb.append(p);
                        sb.append("\n");
                    }


                    mediapath = "" + sb.toString().trim();
                    file = new File(mediapath);

                    Glide
                            .with(this)
                            .load(mediapath)
                            .apply(new RequestOptions().circleCrop()).into(profile_pic);


                    Log.d("mediapathhhhhhhh", "" + mediapath);
                }
            }

            if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

    }

    @Override
    public void onContactSelected(CompanyListData pagesData) {

    }
}
