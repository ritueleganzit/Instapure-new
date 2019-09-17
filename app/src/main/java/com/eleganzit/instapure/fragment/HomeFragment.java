package com.eleganzit.instapure.fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.eleganzit.instapure.FeedbackActivity;
import com.eleganzit.instapure.HomeActivity;
import com.eleganzit.instapure.NotificationActivity;
import com.eleganzit.instapure.ProductInstructionActivity;
import com.eleganzit.instapure.ProfileActivity;
import com.eleganzit.instapure.PromotionalActivity;
import com.eleganzit.instapure.R;
import com.eleganzit.instapure.ReorderActivity;
import com.eleganzit.instapure.RewardsActivity;
import com.eleganzit.instapure.VideoActivity;
import com.eleganzit.instapure.api.RetrofitAPI;
import com.eleganzit.instapure.api.RetrofitInterface;
import com.eleganzit.instapure.model.PhotosData;
import com.eleganzit.instapure.model.ProductInfo;
import com.eleganzit.instapure.model.VideoFeedResponse;
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
public class HomeFragment extends Fragment {

    RecyclerView rc_photos;
    TextView reorder,promotional;


    public HomeFragment() {
        // Required empty public constructor
    }

    ImageView img_user,feedback,notification,rewards;
    TextView txt_name,no_video;
    ProgressBar progress;
    UserLoggedInSession userLoggedInSession;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ADEEEEEE"));
        }
        View v=inflater.inflate(R.layout.fragment_home, container, false);

        userLoggedInSession=new UserLoggedInSession(getActivity());

        no_video=v.findViewById(R.id.no_video);
        rc_photos=v.findViewById(R.id.rc_photos);
        reorder=v.findViewById(R.id.reorder);
        progress=v.findViewById(R.id.progress);
        promotional=v.findViewById(R.id.promotional);
        img_user=v.findViewById(R.id.img_user);
        txt_name=v.findViewById(R.id.txt_name);

        if(userLoggedInSession.getUserDetails().get(UserLoggedInSession.USER_NAME)!=null)
        {

            Glide
                    .with(this)
                    .load(userLoggedInSession.getUserDetails().get(UserLoggedInSession.PHOTO))
                    .apply(new RequestOptions().centerCrop()).into(img_user);
            txt_name.setText(""+userLoggedInSession.getUserDetails().get(UserLoggedInSession.USER_NAME)+",");
        }


        @SuppressLint("WrongConstant") RecyclerView.LayoutManager layoutManager3=new GridLayoutManager(getActivity(),3, LinearLayoutManager.VERTICAL,false);
        feedback=v.findViewById(R.id.feedback);
        notification=v.findViewById(R.id.notification);
        rewards=v.findViewById(R.id.rewards);
        rc_photos.setLayoutManager(layoutManager3);
        btn_home.setImageResource(R.mipmap.ic_home);
        tv_home.setTextColor(getResources().getColor(R.color.colorPrimary));
        btn_scan.setImageResource(R.mipmap.ic_scan);
        tv_scan.setTextColor(getResources().getColor(R.color.textColor));
        btn_history.setImageResource(R.mipmap.ic_history);
        tv_history.setTextColor(getResources().getColor(R.color.textColor));
        btn_profile.setImageResource(R.mipmap.ic_profile);
        tv_profile.setTextColor(getResources().getColor(R.color.textColor));

        rewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), RewardsActivity.class));
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
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), NotificationActivity.class).putExtra("from","home"));
                getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

            }
        });
        reorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ReorderActivity.class));
                getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

            }
        });
        reorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ReorderActivity.class));
                getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

            }
        });
        promotional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), PromotionalActivity.class));
                getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

            }
        });

        videoFeedlist();

        return v;
    }

    private void videoFeedlist() {

        rc_photos.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
        RetrofitInterface myInterface = RetrofitAPI.getRetrofit().create(RetrofitInterface.class);
        Call<VideoFeedResponse> call = myInterface.videoFeedlist("1","9","2");
        call.enqueue(new Callback<VideoFeedResponse>() {
            @Override
            public void onResponse(Call<VideoFeedResponse> call, Response<VideoFeedResponse> response) {
                ArrayList<ProductInfo> arrayList=new ArrayList<>();
                progress.setVisibility(View.GONE);
                rc_photos.setVisibility(View.VISIBLE);
                if (response.isSuccessful()) {
                    if (response.body().getStatus().toString().equalsIgnoreCase("1")) {

                        no_video.setVisibility(View.GONE);
                        if (response.body().getData() != null) {

                            for (int i = 0; i < response.body().getData().size(); i++) {

                                ProductInfo productInfo=new ProductInfo(response.body().getData().get(i).getVideoId(),"image",response.body().getData().get(i).getVideoLink(),response.body().getData().get(i).getVideoTitle(),response.body().getData().get(i).getCreatedDate(),response.body().getData().get(i).getDescription());
                                arrayList.add(productInfo);

                            }

                            rc_photos.setAdapter(new MyPhotosAdapter(arrayList,getActivity()));

                        }
                    } else {
                        no_video.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }
                else {
                    if(getActivity()!=null)
                    Toast.makeText(getActivity(), "Server or Internet Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<VideoFeedResponse> call, Throwable t) {
                progress.setVisibility(View.GONE);
                rc_photos.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), "Server or Internet Error" , Toast.LENGTH_SHORT).show();
            }
        });

    }


    public class MyPhotosAdapter extends RecyclerView.Adapter<MyPhotosAdapter.MyViewHolder> {

        ArrayList<ProductInfo> photos;
        Context context;
        Activity activity;
        private int height;

        public MyPhotosAdapter(ArrayList<ProductInfo> photos, Context context) {
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

        public String convertedDate(String date){

            SimpleDateFormat spf=new SimpleDateFormat("yyyy-MM-dd");
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
        public void onBindViewHolder(@NonNull final MyViewHolder holder, final int i) {

            ProductInfo photosData = photos.get(i);
            String youtubeLink = photosData.getMedia_url();
            String GRID_YOUTUBE_ID = youtubeLink.substring(youtubeLink.lastIndexOf('=') + 1);

            holder.main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, ProductInstructionActivity.class));
                    activity.overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

                    /*context.startActivity(new Intent(context, VideoActivity.class)
                            .putExtra("youtubeLink",youtubeLink+"")
                            .putExtra("title", photosData.getTitle() + "")
                            .putExtra("time", convertedDate(photosData.getDate())+ "")
                            .putExtra("info", photosData.getDescription() + "")
                    );
                    activity.overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                    Log.d("jdsfhdfsf",youtubeLink+"");*/
/*
                    Bundle args=new Bundle();

                    args.putString("youtubeLink", photosData.getMedia_url());

                    VideoDialogFragment.display(getFragmentManager(),args);*/
                }
            });

            Glide
                    .with(context)
                    .load("http://img.youtube.com/vi/"+GRID_YOUTUBE_ID+"/0.jpg")
                    .apply(new RequestOptions().centerCrop().transforms(new CenterCrop(), new RoundedCorners(10))).into(holder.search_photo);
        }

        @Override
        public int getItemCount() {

                return photos.size();

        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView search_photo;
            FrameLayout main;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                search_photo = itemView.findViewById(R.id.search_photo);
                main = itemView.findViewById(R.id.main);

            }
        }

    }


}