package com.eleganzit.instapure.fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.eleganzit.instapure.R;
import com.eleganzit.instapure.model.PhotosData;

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
public class HistoryFragment extends Fragment {

RecyclerView history_rrc;
    public HistoryFragment() {
        // Required empty public constructor
    }
    ArrayList<PhotosData> photos=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
View v=inflater.inflate(R.layout.fragment_history, container, false);
        history_rrc=v.findViewById(R.id.history_rrc);
        @SuppressLint("WrongConstant") RecyclerView.LayoutManager layoutManager3=new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);

        history_rrc.setLayoutManager(layoutManager3);
        history_rrc.setAdapter(new MyHistoryAdapter(photos,getActivity()));
        btn_history.setImageResource(R.mipmap.ic_historyblue);
        tv_history.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        btn_scan.setImageResource(R.mipmap.ic_scan);
        tv_scan.setTextColor(getResources().getColor(R.color.textColor));
        btn_home.setImageResource(R.mipmap.ic_homegrey);
        tv_home.setTextColor(getResources().getColor(R.color.textColor));
        btn_profile.setImageResource(R.mipmap.ic_profile);
        tv_profile.setTextColor(getResources().getColor(R.color.textColor));

        return v;

    }
    public class MyHistoryAdapter extends RecyclerView.Adapter<MyHistoryAdapter.MyViewHolder> {

        ArrayList<PhotosData> photos;
        Context context;
        Activity activity;
        private int height;

        public MyHistoryAdapter(ArrayList<PhotosData> photos, Context context) {
            this.photos = photos;
            this.context = context;
            activity = (Activity) context;
        }

        @NonNull
        @Override
        public MyHistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.history_layout, viewGroup, false);
            MyHistoryAdapter.MyViewHolder myViewHolder = new MyHistoryAdapter.MyViewHolder(v);

            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull final MyHistoryAdapter.MyViewHolder holder, final int i) {


        }

        @Override
        public int getItemCount() {

                return 5;

        }

        public class MyViewHolder extends RecyclerView.ViewHolder {


            public MyViewHolder(@NonNull View itemView) {
                super(itemView);

            }
        }

    }
}
