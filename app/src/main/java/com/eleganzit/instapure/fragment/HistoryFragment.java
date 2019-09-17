package com.eleganzit.instapure.fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.appyvet.materialrangebar.RangeBar;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.eleganzit.instapure.FilterDialogActivity;
import com.eleganzit.instapure.ProductInstructionActivity;
import com.eleganzit.instapure.R;
import com.eleganzit.instapure.api.RetrofitAPI;
import com.eleganzit.instapure.api.RetrofitInterface;
import com.eleganzit.instapure.model.HistoryData;
import com.eleganzit.instapure.model.HistoryListResponse;
import com.eleganzit.instapure.model.PhotosData;
import com.eleganzit.instapure.model.ProductInfo;
import com.eleganzit.instapure.model.VideoFeedResponse;
import com.eleganzit.instapure.utils.InfiniteScrollListener;
import com.eleganzit.instapure.utils.UserLoggedInSession;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
public class HistoryFragment extends Fragment implements InfiniteScrollListener.OnLoadMoreListener {

    RecyclerView history_rrc;
    ImageView filter;
    ProgressBar progress;
    UserLoggedInSession userLoggedInSession;
    TextView no_history;
    LinearLayoutManager layoutManager;
    ArrayList<HistoryData> arrayList=new ArrayList<>();
    MyHistoryAdapter myHistoryAdapter;
    InfiniteScrollListener infiniteScrollListener;
    String from_limit="1",to_limit="8",select_months="";


    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_history, container, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#FFFFFF"));
        }
        ImageView back = v.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStackImmediate();

            }
        });
        userLoggedInSession=new UserLoggedInSession(getActivity());

        progress = v.findViewById(R.id.progress);
        no_history = v.findViewById(R.id.no_history);

        history_rrc = v.findViewById(R.id.history_rrc);
        filter = v.findViewById(R.id.filter);

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //FilterDialogFragment.display(getFragmentManager());

                startActivity(new Intent(getActivity(), FilterDialogActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_up,R.anim.slide_down);

                /*final Dialog dialog=new Dialog(getActivity());
                dialog.setContentView(R.layout.filter_dialog_layout);

                ImageView close=dialog.findViewById(R.id.close);
                TextView apply=dialog.findViewById(R.id.apply);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                apply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                final RelativeLayout spin1 = dialog.findViewById(R.id.spin1);
                final RelativeLayout spin2 = dialog.findViewById(R.id.spin2);
                final Spinner spinner1 = dialog.findViewById(R.id.spinner1);
                final Spinner spinner2 = dialog.findViewById(R.id.spinner2);
                spin1.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                            spinner1.performClick();

                    }
                });

                spin2.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                            spinner2.performClick();

                    }
                });

                Window window = dialog.getWindow();
                window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                dialog.show();
*/
            }
        });

        layoutManager=new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);
        infiniteScrollListener = new InfiniteScrollListener(layoutManager, this);
        infiniteScrollListener.setLoaded();

        history_rrc.setLayoutManager(layoutManager);
        history_rrc.addOnScrollListener(infiniteScrollListener);

        myHistoryAdapter=new MyHistoryAdapter(arrayList,getActivity());

        btn_history.setImageResource(R.mipmap.ic_historyblue);
        tv_history.setTextColor(getResources().getColor(R.color.colorPrimary));
        btn_scan.setImageResource(R.mipmap.ic_scan);
        tv_scan.setTextColor(getResources().getColor(R.color.textColor));
        btn_home.setImageResource(R.mipmap.ic_homegrey);
        tv_home.setTextColor(getResources().getColor(R.color.textColor));
        btn_profile.setImageResource(R.mipmap.ic_profile);
        tv_profile.setTextColor(getResources().getColor(R.color.textColor));


        return v;

    }

    public void onUserSelectValue(String selectedValue) {
        // TODO add your implementation.

        Log.d("sgdfhsdfsd",selectedValue+"");

        //Toast.makeText(getActivity(), "" + selectedValue, Toast.LENGTH_SHORT).show();
    }

    private void historyList1() {

        if(arrayList.size()>0)
        {
            arrayList.clear();
        }

        progress.setVisibility(View.VISIBLE);

        Log.d("afsdghasd",from_limit+"   "+to_limit+"    "+select_months);

        RetrofitInterface myInterface = RetrofitAPI.getRetrofit().create(RetrofitInterface.class);
        Call<HistoryListResponse> call = myInterface.historyList(userLoggedInSession.getUserDetails().get(UserLoggedInSession.USER_ID), from_limit+"",to_limit+"",""+select_months);
        call.enqueue(new Callback<HistoryListResponse>() {
            @Override
            public void onResponse(Call<HistoryListResponse> call, Response<HistoryListResponse> response) {

                progress.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    if (response.body().getStatus().toString().equalsIgnoreCase("1")) {

                        no_history.setVisibility(View.GONE);

                        if (response.body().getData() != null) {

                            for (int i = 0; i < response.body().getData().size(); i++) {

                                HistoryData historyData = new HistoryData(response.body().getData().get(i).getUserId(), response.body().getData().get(i).getCreatedDate(), response.body().getData().get(i).getPackage());
                                arrayList.add(historyData);


                            }


                            Log.d("gwyudhd",userLoggedInSession.getUserDetails().get(UserLoggedInSession.USER_ID));

                            myHistoryAdapter=new MyHistoryAdapter(arrayList,getActivity());
                            history_rrc.setAdapter(myHistoryAdapter);

                        }
                    } else {
                        no_history.setVisibility(View.VISIBLE);
                    }


                } else {

                    Toast.makeText(getActivity(), "Server or Internet Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HistoryListResponse> call, Throwable t) {
                progress.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Server or Internet Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void historyList2(int limit) {

        RetrofitInterface myInterface = RetrofitAPI.getRetrofit().create(RetrofitInterface.class);
        Call<HistoryListResponse> call = myInterface.historyList(userLoggedInSession.getUserDetails().get(UserLoggedInSession.USER_ID), limit+"","8","");
        call.enqueue(new Callback<HistoryListResponse>() {
            @Override
            public void onResponse(Call<HistoryListResponse> call, Response<HistoryListResponse> response) {
                ArrayList<HistoryData> photos = new ArrayList<>();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().toString().equalsIgnoreCase("1")) {

                        myHistoryAdapter.removeNull();

                        if (response.body().getData() != null) {

                            for (int i = 0; i < response.body().getData().size(); i++) {

                                HistoryData historyData = new HistoryData(response.body().getData().get(i).getUserId(), response.body().getData().get(i).getCreatedDate(), response.body().getData().get(i).getPackage());
                                photos.add(historyData);

                            }

                            myHistoryAdapter.addData(arrayList);
                            infiniteScrollListener.setLoaded();
                        }
                    } else {
                        myHistoryAdapter.removeNull();

                    }


                } else {
                    myHistoryAdapter.removeNull();
                    Toast.makeText(getActivity(), "Server or Internet Error", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<HistoryListResponse> call, Throwable t) {


            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("asdgfad","on resume");

        if(getArguments()!=null){
            from_limit=getArguments().getString("from_limit");
            to_limit=getArguments().getString("to_limit");
            select_months=getArguments().getString("select_months");
        }

        historyList1();

    }

    @Override
    public void onLoadMore() {

        myHistoryAdapter.addNullData();
        Log.d("asjgdashd",arrayList.size()+"");
        historyList2(arrayList.size());

    }

    public class MyHistoryAdapter extends RecyclerView.Adapter<MyHistoryAdapter.MyViewHolder> {

        ArrayList<HistoryData> photos;
        Context context;
        Activity activity;
        private int height;
        int VIEW_TYPE_ITEM=0,VIEW_TYPE_LOADING=1;

        public MyHistoryAdapter(ArrayList<HistoryData> photos, Context context) {
            this.photos = photos;
            this.context = context;
            activity = (Activity) context;
        }


        @Override
        public int getItemViewType(int position) {
            if (photos.get(position) != null)
                return VIEW_TYPE_ITEM;
            else
                return VIEW_TYPE_LOADING;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

            View v;
            if (i == VIEW_TYPE_ITEM) {
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.history_layout,viewGroup,false);
                return new DViewHolder(v);
            } else {
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.progressbar_layout,viewGroup,false);
                return new PViewHolder(v);
            }

        }

        @Override
        public void onBindViewHolder(@NonNull final MyHistoryAdapter.MyViewHolder holder, final int i) {

            if (holder instanceof DViewHolder) {
                HistoryData historyData = photos.get(i);

                ((DViewHolder) holder).txt_date.setText(convertedDate(historyData.getDate()) + "");
                ((DViewHolder) holder).txt_qty.setText(historyData.getQuantity() + "");

                Log.d("fgdsfdfds", convertedDate(historyData.getDate()) + "");
            }
        }


        public String convertedDate(String date){

            SimpleDateFormat spf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date newDate= null;
            try {
                newDate = spf.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            spf= new SimpleDateFormat("dd MMMM yyyy");
            date = spf.format(newDate);
            System.out.println(date);

            return date;
        }


        @Override
        public int getItemCount() {

            return photos.size();

        }

        public class MyViewHolder extends RecyclerView.ViewHolder {


            public MyViewHolder(@NonNull View itemView) {
                super(itemView);

            }
        }

        public class DViewHolder extends MyViewHolder {

            TextView txt_date,txt_qty;

            public DViewHolder(@NonNull View itemView) {
                super(itemView);
                txt_date=itemView.findViewById(R.id.txt_date);
                txt_qty=itemView.findViewById(R.id.txt_qty);

            }
        }

        public class PViewHolder extends MyViewHolder {


            public PViewHolder(@NonNull View itemView) {
                super(itemView);

            }
        }


        public void addNullData() {
            photos.add(null);
            notifyItemInserted(photos.size() - 1);
        }

        public void removeNull() {
            photos.remove(photos.size() - 1);
            notifyItemRemoved(photos.size());
        }

        public void addData(ArrayList<HistoryData> arrayList) {
            photos.addAll(arrayList);
            notifyDataSetChanged();
        }

    }
}
