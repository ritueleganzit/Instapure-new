package com.eleganzit.instapure.fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.MediaController;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.eleganzit.instapure.FeedbackActivity;
import com.eleganzit.instapure.HomeActivity;
import com.eleganzit.instapure.ProfileActivity;
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
public class HomeFragment extends Fragment {

    RecyclerView rc_photos;

    public HomeFragment() {
        // Required empty public constructor
    }
ImageView feedback;
    ArrayList<PhotosData> ar_photosData =new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_home, container, false);
        rc_photos=v.findViewById(R.id.rc_photos);

        @SuppressLint("WrongConstant") RecyclerView.LayoutManager layoutManager3=new GridLayoutManager(getActivity(),3, LinearLayoutManager.VERTICAL,false);
        feedback=v.findViewById(R.id.feedback);
        rc_photos.setLayoutManager(layoutManager3);
        PhotosData photosData1=new PhotosData("","https://images.pexels.com/photos/257360/pexels-photo-257360.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500");
        PhotosData photosData2=new PhotosData("","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSRV3S67M76jaxZTLUTtk8Wngtkfc7XC1zDE_qKZlmjClXs8AbE");
        PhotosData photosData3=new PhotosData("","https://i.ytimg.com/vi/2SAPrPZVTjs/hqdefault.jpg");
        PhotosData photosData4=new PhotosData("","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQDyu82j_yYelLzJd2tVDqHZmCXRFVyOcDt2wwr5Zfb8JvREdu1");
        PhotosData photosData5=new PhotosData("","https://i.ytimg.com/vi/2SAPrPZVTjs/hqdefault.jpg");
        PhotosData photosData6=new PhotosData("","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSRV3S67M76jaxZTLUTtk8Wngtkfc7XC1zDE_qKZlmjClXs8AbE");
        btn_home.setImageResource(R.mipmap.ic_home);
        tv_home.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        btn_scan.setImageResource(R.mipmap.ic_scan);
        tv_scan.setTextColor(getResources().getColor(R.color.textColor));
        btn_history.setImageResource(R.mipmap.ic_history);
        tv_history.setTextColor(getResources().getColor(R.color.textColor));
        btn_profile.setImageResource(R.mipmap.ic_profile);
        tv_profile.setTextColor(getResources().getColor(R.color.textColor));
        ar_photosData.add(photosData1);
        ar_photosData.add(photosData2);
        ar_photosData.add(photosData3);
        ar_photosData.add(photosData4);
        ar_photosData.add(photosData5);
        ar_photosData.add(photosData6);
        rc_photos.setAdapter(new MyPhotosAdapter(ar_photosData,getActivity()));
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), FeedbackActivity.class));

            }
        });
        return v;
    }


    public class MyPhotosAdapter extends RecyclerView.Adapter<MyPhotosAdapter.MyViewHolder> {

        ArrayList<PhotosData> photos;
        Context context;
        Activity activity;
        private int height;

        public MyPhotosAdapter(ArrayList<PhotosData> photos, Context context) {
            this.photos = photos;
            this.context = context;
            activity = (Activity) context;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.video_layout, viewGroup, false);
            MyViewHolder myViewHolder = new MyViewHolder(v);

            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder holder, final int i) {

            PhotosData photosData = photos.get(i);

            Log.d("sdadad",""+photosData.getPhoto());

            Glide
                    .with(context)
                    .load(photosData.getPhoto())
                    .apply(new RequestOptions().centerCrop().transforms(new CenterCrop(), new RoundedCorners(10))).into(holder.search_photo);
        }

        @Override
        public int getItemCount() {
            if (photos.size() > 6) {
                return 6;
            } else {
                return photos.size();
            }
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView search_photo;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                search_photo = itemView.findViewById(R.id.search_photo);

            }
        }

    }
}