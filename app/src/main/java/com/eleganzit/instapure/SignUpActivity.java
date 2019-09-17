package com.eleganzit.instapure;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Fade;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.eleganzit.instapure.adapter.CompaniesRecyclerAdapter;
import com.eleganzit.instapure.api.RetrofitAPI;
import com.eleganzit.instapure.api.RetrofitInterface;
import com.eleganzit.instapure.model.CompanyListData;
import com.eleganzit.instapure.model.CompanyListResponse;
import com.eleganzit.instapure.model.RegisterUserResponse;
import com.eleganzit.instapure.model.SampleSearchModel;
import com.eleganzit.instapure.utils.CustomAdapter;
import com.eleganzit.instapure.utils.MyLinearLayoutManager;
import com.eleganzit.instapure.utils.NoDefaultSpinner;
import com.eleganzit.instapure.utils.UserLanguageSession;
import com.eleganzit.instapure.utils.UserLoggedInSession;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity implements CompaniesRecyclerAdapter.ContactsAdapterListener {

    //EditText gender,company;
    EditText ed_name,ed_mobile,ed_email,ed_location,ed_company;
    String str_company="";
    String company_id="";
    Spinner spinner;
    TextView signup,spin_hint;
    TextView txt_no_match;

    ScrollView scroll_v;
    RelativeLayout spin2;
    UserLanguageSession userLanguageSession;
    UserLoggedInSession userLoggedInSession;
    final ArrayList<CompanyListData> items = new ArrayList<>();
    final ArrayList<String> sitems = new ArrayList<>();
    ProgressBar c_progress;
    ProgressDialog progressDialog;
    boolean got_success =false;
    private String Token,devicetoken;
    CardView card_mentions;
    private RecyclerView recyclerView;
    CompaniesRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        setContentView(R.layout.activity_sign_up);
        userLoggedInSession = new UserLoggedInSession(SignUpActivity.this);
        userLanguageSession = new UserLanguageSession(SignUpActivity.this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Fade fade = new Fade();
            fade.excludeTarget(android.R.id.statusBarBackground, true);
            //fade.excludeTarget(android.R.id.navigationBarBackground, true);

            getWindow().setEnterTransition(fade);
            getWindow().setExitTransition(fade);
        }

        // gender=findViewById(R.id.gender);
        progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        card_mentions = findViewById(R.id.card_mentions);

        recyclerView = findViewById(R.id.rc_mentions);
        c_progress = findViewById(R.id.c_progress);

        MyLinearLayoutManager mLayoutManager = new MyLinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        //recyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new CompaniesRecyclerAdapter(this, items, this);

        scroll_v=findViewById(R.id.scroll_v);
        spin2=findViewById(R.id.spin2);
        ed_name=findViewById(R.id.ed_name);
        ed_mobile=findViewById(R.id.ed_mobile);
        ed_email=findViewById(R.id.ed_email);
        ed_location=findViewById(R.id.ed_location);
        ed_company=findViewById(R.id.ed_company);
        spinner=findViewById(R.id.gen_spinner);
        signup=findViewById(R.id.signup);
        spin_hint=findViewById(R.id.spin_hint);

        getCompanyList();

        Log.d("ghdfssdfsd",ed_location.getBottom()+"    "+ed_name.getBottom()+"    "+ed_mobile.getBottom()+"   "+ed_email.getBottom()+"   "+ed_company.getBottom()+"   "+spinner.getBottom());

        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                Token= FirebaseInstanceId.getInstance().getToken();
                if (Token!=null)
                {

                    devicetoken=Token;

                    Log.d("Rrrrrmytokenn", Token);

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
                    Log.d("mytoken", "no token");
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });t.start();


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Token==null){
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
                                Log.d("mytoken", "no token");
                            }
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });t.start();

                }

                for(int i=0;i<items.size();i++){

                    if(items.get(i).getTitle().equalsIgnoreCase(""+ed_company.getText().toString())){
                        company_id=items.get(i).getCompanyId();
                    }

                }

                if(isValid()){

                    registerUser();
                }

            }
        });


        CustomAdapter dataAdapter = new CustomAdapter(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.genders),0)
        {
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return true;
                }
                else
                {
                    return true;
                }
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView view = (TextView) super.getView(position, convertView, parent);
                Typeface typeface = ResourcesCompat.getFont(SignUpActivity.this, R.font.roboto_light);

                view.setTypeface(typeface);
                if(position == 0){
                    // Set the hint text color gray
                    view.setTextColor(Color.parseColor("#CCCCCC"));
                }
                else {
                    view.setTextColor(Color.BLACK);
                }
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                Typeface typeface = ResourcesCompat.getFont(SignUpActivity.this, R.font.roboto_light);

                TextView tv = (TextView) view;
                tv.setTypeface(typeface);
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.BLACK);
                    tv.setTextSize(0);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }


        };

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // First item will be gray

                if (position == 0) {
                    ((TextView) view).setTextColor(Color.parseColor("#FFB8B8B8"));
                    //((TextView) view).setEnabled(false);

                    spin_hint.setVisibility(View.VISIBLE);
                } else {
                    spin_hint.setVisibility(View.GONE);

                    ((TextView) view).setTextColor(ContextCompat.getColor(SignUpActivity.this, R.color.textColor));
                    Typeface typeface = ResourcesCompat.getFont(SignUpActivity.this, R.font.roboto_regular);

                    TextView tv = (TextView) view;
                    tv.setTypeface(typeface);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ed_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                scrollToView(scroll_v,ed_mobile);

            }
        });

        ed_mobile.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if(b)
                scrollToView(scroll_v,ed_email);

            }
        });

        ed_email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if(b)
                    scrollToView3(scroll_v,spinner);

            }
        });

        spinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                scrollToView2(scroll_v,ed_company);
                return false;
            }
        });

        ed_location.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b)
                scrollToView2(scroll_v,signup);

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

        /*ed_company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new SimpleSearchDialogCompat(SignUpActivity.this, "Search",
                        "Search for company", null, items,
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
        });
*/
        findViewById(R.id.already).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignUpActivity.this, SignInActivity.class);

                startActivity(i);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

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

    }

    private final void focusOnView(){
        scroll_v.post(new Runnable() {
            @Override
            public void run() {
                scroll_v.scrollTo(0, ed_location.getBottom());
            }
        });
    }

    private void scrollToView(final ScrollView scrollViewParent, final View view) {
        // Get deepChild Offset
        Point childOffset = new Point();
        getDeepChildOffset(scrollViewParent, view.getParent(), view, childOffset);
        // Scroll to child.
        scrollViewParent.smoothScrollTo(0, 280);

        Log.d("ghdfssdfsd",childOffset.y+"");
    }

    private void scrollToView3(final ScrollView scrollViewParent, final View view) {
        // Get deepChild Offset
        Point childOffset = new Point();
        getDeepChildOffset(scrollViewParent, view.getParent(), view, childOffset);
        // Scroll to child.
        scrollViewParent.smoothScrollTo(0, 350);

        Log.d("ghdfssdfsd",childOffset.y+"");
    }

    private void scrollToView2(final ScrollView scrollViewParent, final View view) {
        // Get deepChild Offset
        Point childOffset = new Point();
        getDeepChildOffset(scrollViewParent, view.getParent(), view, childOffset);
        // Scroll to child.
        scrollViewParent.smoothScrollTo(0, 680);

        Log.d("ghdfssdfsd",childOffset.y+"");
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

    private void getCompanyList() {
        progressDialog.show();
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

                            mAdapter = new CompaniesRecyclerAdapter(SignUpActivity.this, items, new CompaniesRecyclerAdapter.ContactsAdapterListener() {
                                @Override
                                public void onContactSelected(CompanyListData companyListData) {
                                    //Toast.makeText(SignUpActivity.this, ""+companyListData.getTitle(), Toast.LENGTH_SHORT).show();
                                    Log.d("rteretr",companyListData.getTitle()+"");
                                    company_id=companyListData.getCompanyId();
                                    ed_company.setText("");
                                    ed_company.append(companyListData.getTitle()+"");
                                    Log.d("whichccc", "onContactSelected");
                                    card_mentions.setVisibility(View.GONE);

                                }
                            });
                            recyclerView.setAdapter(mAdapter);


                        }

                    } else {
                        Toast.makeText(SignUpActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
                else {
                    got_success=false;
                    Toast.makeText(SignUpActivity.this, "Server or Internet Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CompanyListResponse> call, Throwable t) {
                progressDialog.dismiss();
                got_success=false;
                Toast.makeText(SignUpActivity.this, "Server or Internet Error" , Toast.LENGTH_SHORT).show();
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

                            mAdapter = new CompaniesRecyclerAdapter(SignUpActivity.this, items, new CompaniesRecyclerAdapter.ContactsAdapterListener() {
                                @Override
                                public void onContactSelected(CompanyListData companyListData) {
                                    //Toast.makeText(SignUpActivity.this, ""+companyListData.getTitle(), Toast.LENGTH_SHORT).show();
                                    Log.d("rteretr",companyListData.getTitle()+"");
                                    company_id=companyListData.getCompanyId();
                                    ed_company.setText("");
                                    ed_company.append(companyListData.getTitle()+" ");
                                    Log.d("whichccc", "onContactSelected");
                                    card_mentions.setVisibility(View.GONE);

                                }
                            });
                            recyclerView.setAdapter(mAdapter);


                        }

                    } else {
                        Toast.makeText(SignUpActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
                else {
                    got_success=false;
                    Toast.makeText(SignUpActivity.this, "Server or Internet Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CompanyListResponse> call, Throwable t) {
                c_progress.setVisibility(View.GONE);
                got_success=false;
                Toast.makeText(SignUpActivity.this, "Server or Internet Error" , Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void registerUser() {

        progressDialog.show();
        RetrofitInterface myInterface = RetrofitAPI.getRetrofit().create(RetrofitInterface.class);
        Call<RegisterUserResponse> call = myInterface.registerUser(
                ed_name.getText().toString(),
                spinner.getSelectedItem().toString(),
                company_id,
                ed_mobile.getText().toString(),
                ed_email.getText().toString(),
                ed_location.getText().toString(),
                userLanguageSession.getUserLanguage().get(UserLanguageSession.SELECTED_LANGUAGE),
                devicetoken+""
        );
        call.enqueue(new Callback<RegisterUserResponse>() {
            @Override
            public void onResponse(Call<RegisterUserResponse> call, Response<RegisterUserResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().toString().equalsIgnoreCase("1")) {
                        if (response.body().getData() != null) {
                            String email, id = "", username,mobile,photo="", gender,location,company="";
                            for (int i = 0; i < response.body().getData().size(); i++) {
                                email = response.body().getData().get(i).getEmail();
                                id = response.body().getData().get(i).getUserId();
                                username = response.body().getData().get(i).getFullname();
                                mobile = response.body().getData().get(i).getContact();
                                photo = response.body().getData().get(i).getPhoto();
                                gender = response.body().getData().get(i).getGender();
                                location = response.body().getData().get(i).getAddress();
                                company = response.body().getData().get(i).getCompany_name();

                                //userLoggedInSession.createLoginSession(email, id, username,mobile,photo, gender,location,company);

                            }

                            Intent i = new Intent(SignUpActivity.this, SignUpOtpActivity.class);

                            i.putExtra("id",id+"");
                            i.putExtra("photo",photo+"");
                            i.putExtra("company",company+"");
                            i.putExtra("fullname",ed_name.getText().toString()+"");
                            i.putExtra("gender",spinner.getSelectedItem().toString()+"");
                            i.putExtra("company_id",company_id+"");
                            i.putExtra("contact",ed_mobile.getText().toString()+"");
                            i.putExtra("email",ed_email.getText().toString()+"");
                            i.putExtra("address",ed_location.getText().toString()+"");
                            i.putExtra("language",userLanguageSession.getUserLanguage().get(UserLanguageSession.SELECTED_LANGUAGE)+"");
                            startActivity(i);
                            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);


                        }
                    } else {

                        Toast.makeText(SignUpActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
                else {

                    Toast.makeText(SignUpActivity.this, "Server or Internet Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterUserResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(SignUpActivity.this, "Server or Internet Error" , Toast.LENGTH_SHORT).show();
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
            ed_name.setError(getResources().getString(R.string.please_enter_name));

            ed_name.requestFocus();
            return false;
        } else if (ed_mobile.getText().toString().equals("")) {
            ed_mobile.setError(getResources().getString(R.string.please_enter_mobile_number));

            ed_mobile.requestFocus();
            return false;
        }else if (spinner.getSelectedItem().toString().equalsIgnoreCase("gender*")) {

            spinner.requestFocus();
            Toast.makeText(this, getResources().getString(R.string.please_select_gender), Toast.LENGTH_SHORT).show();

            return false;
        }else if (!sitems.contains(ed_company.getText().toString()) || company_id.equalsIgnoreCase("")) {
            ed_company.setError(getResources().getString(R.string.please_select_from_company));

            ed_company.requestFocus();
            return false;
        }
        return true;
    }



    @Override
    public void onContactSelected(CompanyListData companyListData) {

    }
}
