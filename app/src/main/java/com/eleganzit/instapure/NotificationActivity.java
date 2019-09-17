package com.eleganzit.instapure;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.eleganzit.instapure.api.RetrofitAPI;
import com.eleganzit.instapure.api.RetrofitInterface;
import com.eleganzit.instapure.fragment.HistoryFragment;
import com.eleganzit.instapure.model.HistoryData;
import com.eleganzit.instapure.model.HistoryListResponse;
import com.eleganzit.instapure.model.NotificationData;
import com.eleganzit.instapure.model.NotificationsResponse;
import com.eleganzit.instapure.utils.InfiniteScrollListener;
import com.eleganzit.instapure.utils.UserLoggedInSession;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

public class NotificationActivity extends AppCompatActivity implements InfiniteScrollListener.OnLoadMoreListener {


    RecyclerView rc_notifications;
    UserLoggedInSession userLoggedInSession;
    ProgressDialog progressDialog;
    TextView no_noti;
    LinearLayoutManager layoutManager;
    ArrayList<NotificationData> arrayList=new ArrayList<>();
    NotificationsAdapter notificationsAdapter;
    InfiniteScrollListener infiniteScrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        ImageView back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        rc_notifications=findViewById(R.id.rc_notifications);
        no_noti=findViewById(R.id.no_noti);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        userLoggedInSession=new UserLoggedInSession(this);

        layoutManager=new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        infiniteScrollListener = new InfiniteScrollListener(layoutManager, this);
        infiniteScrollListener.setLoaded();

        rc_notifications.setLayoutManager(layoutManager);
        rc_notifications.addOnScrollListener(infiniteScrollListener);

        notificationsAdapter=new NotificationsAdapter(arrayList,this);

        /*ArrayList<NotificationData> arrayList=new ArrayList<>();
        NotificationData notificationData=new NotificationData("","","");
        arrayList.add(notificationData);

        rc_notifications.setAdapter(new NotificationsAdapter(arrayList,this));
*/
        notificationsList1();

    }

    private void notificationsList1() {

        progressDialog.show();
        RetrofitInterface myInterface = RetrofitAPI.getRetrofit().create(RetrofitInterface.class);
        Call<NotificationsResponse> call = myInterface.notifications( "1");
        call.enqueue(new Callback<NotificationsResponse>() {
            @Override
            public void onResponse(Call<NotificationsResponse> call, Response<NotificationsResponse> response) {

                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().toString().equalsIgnoreCase("1")) {

                        no_noti.setVisibility(View.GONE);
                        if (response.body().getData() != null) {

                            for (int i = 0; i < response.body().getData().size(); i++) {


                                NotificationData notificationData=new NotificationData(response.body().getData().get(i).getNotificationText(),response.body().getData().get(i).getImagePath(),response.body().getData().get(i).getCreated_date(),false);
                                arrayList.add(notificationData);



                            }
                            notificationsAdapter=new NotificationsAdapter(arrayList,NotificationActivity.this);
                            rc_notifications.setAdapter(notificationsAdapter);

                        }
                    } else {
                        no_noti.setVisibility(View.VISIBLE);
                        Toast.makeText(NotificationActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }


                } else {

                    Toast.makeText(NotificationActivity.this, "Server or Internet Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NotificationsResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(NotificationActivity.this, "Server or Internet Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void notificationsList2(int limit) {

        RetrofitInterface myInterface = RetrofitAPI.getRetrofit().create(RetrofitInterface.class);
        Call<NotificationsResponse> call = myInterface.notifications( limit+"");
        call.enqueue(new Callback<NotificationsResponse>() {
            @Override
            public void onResponse(Call<NotificationsResponse> call, Response<NotificationsResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().toString().equalsIgnoreCase("1")) {

                        notificationsAdapter.removeNull();

                        if (response.body().getData() != null) {

                            for (int i = 0; i < response.body().getData().size(); i++) {


                                NotificationData notificationData=new NotificationData(response.body().getData().get(i).getNotificationText(),response.body().getData().get(i).getImagePath(),response.body().getData().get(i).getCreated_date(),false);
                                arrayList.add(notificationData);


                            }

                            notificationsAdapter.addData(arrayList);
                            infiniteScrollListener.setLoaded();
                        }
                    } else {
                        notificationsAdapter.removeNull();

                    }


                } else {
                    notificationsAdapter.removeNull();
                    Toast.makeText(NotificationActivity.this, "Server or Internet Error", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<NotificationsResponse> call, Throwable t) {
                notificationsAdapter.removeNull();
            }
        });

    }

    @Override
    public void onLoadMore() {
        notificationsAdapter.addNullData();
        Log.d("asjgdashd",arrayList.size()+"");
        notificationsList2(arrayList.size());

    }


    public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.MyViewHolder> {

        ArrayList<NotificationData> photos;
        Context context;
        Activity activity;
        private int height;
        private int originalHeight = 0;
        int VIEW_TYPE_ITEM=0,VIEW_TYPE_LOADING=1;


        public NotificationsAdapter(ArrayList<NotificationData> photos, Context context) {
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
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notifications_layout,viewGroup,false);
                return new DViewHolder(v);
            } else {
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.progressbar_layout,viewGroup,false);
                return new PViewHolder(v);
            }

        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder holder, final int i) {

            if (holder instanceof DViewHolder) {
            NotificationData notificationData = photos.get(i);

                Log.d("sdadad",""+notificationData.getImg());

                ((DViewHolder)holder).txt_content.setText(notificationData.getContent());

                if(notificationData.getImg().equalsIgnoreCase("")){
                    ((DViewHolder)holder).img_layout.setVisibility(View.GONE);
                    ((DViewHolder)holder).img_layout2.setVisibility(View.GONE);
                }

                Glide
                        .with(context)
                        .load(notificationData.getImg())
                        .apply(new RequestOptions()).into(((DViewHolder)holder).img);

                Glide
                        .with(context)
                        .load(notificationData.getImg())
                        .apply(new RequestOptions()).into(((DViewHolder)holder).img2);

                ((DViewHolder)holder).main.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        if(!notificationData.getImg().equalsIgnoreCase("")){


                        // If the originalHeight is 0 then find the height of the View being used
                        // This would be the height of the cardview
                        if (originalHeight == 0) {
                            originalHeight = view.getHeight();
                        }

                        // Declare a ValueAnimator object
                        ValueAnimator valueAnimator;
                        if (!notificationData.isExpanded()) {
                            ((DViewHolder)holder).img_layout2.setVisibility(View.VISIBLE);
                            ((DViewHolder)holder).img_layout2.setEnabled(true);

                            ((DViewHolder)holder).img_layout.setVisibility(View.GONE);
                            ((DViewHolder)holder).img_layout.setEnabled(false);

                            notificationData.setExpanded(true);
                            valueAnimator = ValueAnimator.ofInt(originalHeight, originalHeight + (int) (originalHeight * 1.5)); // These values in this method can be changed to expand however much you like
                        } else {
                            notificationData.setExpanded(false);
                            valueAnimator = ValueAnimator.ofInt(originalHeight + (int) (originalHeight * 1.5), originalHeight);

                            Animation a = new AlphaAnimation(1.00f, 0.00f); // Fade out

                            a.setDuration(200);
                            // Set a listener to the animation and configure onAnimationEnd
                            a.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    ((DViewHolder)holder).img_layout2.setVisibility(View.INVISIBLE);
                                    ((DViewHolder)holder).img_layout2.setEnabled(false);

                                    ((DViewHolder)holder).img_layout.setVisibility(View.VISIBLE);
                                    ((DViewHolder)holder).img_layout.setEnabled(true);
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });

                            // Set the animation on the custom view
                            ((DViewHolder)holder).img_layout.startAnimation(a);
                            ((DViewHolder)holder).img_layout2.startAnimation(a);
                        }
                        valueAnimator.setDuration(200);
                        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            public void onAnimationUpdate(ValueAnimator animation) {
                                Integer value = (Integer) animation.getAnimatedValue();
                                view.getLayoutParams().height = value.intValue();
                                view.requestLayout();
                            }
                        });


                        valueAnimator.start();
/*
                    if(notificationData.isExpanded()){
                        notificationData.setExpanded(false);
                        ((DViewHolder)holder).img_layout.setVisibility(View.VISIBLE);
                        ((DViewHolder)holder).img_layout2.setVisibility(View.GONE);
                    }
                    else
                    {
                        notificationData.setExpanded(true);
                        ((DViewHolder)holder).img_layout.setVisibility(View.GONE);
                        ((DViewHolder)holder).img_layout2.setVisibility(View.VISIBLE);
                    }*/
                    }
                    }
                });

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

                try {
                    long time = sdf.parse(notificationData.getTime()).getTime();
                    long now = System.currentTimeMillis();

                    CharSequence ago =
                            DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);

                    ((DViewHolder)holder).txt_time.setText(ago+"");

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
            

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

            TextView txt_content,txt_time;
            ImageView img,img2;
            RelativeLayout main;
            CardView img_layout,img_layout2;
            boolean isExpanded=false;

            public DViewHolder(@NonNull View itemView) {
                super(itemView);

                main=itemView.findViewById(R.id.main);
                txt_content=itemView.findViewById(R.id.txt_content);
                txt_time=itemView.findViewById(R.id.txt_time);
                img=itemView.findViewById(R.id.img);
                img2=itemView.findViewById(R.id.img2);
                img_layout=itemView.findViewById(R.id.img_layout);
                img_layout2=itemView.findViewById(R.id.img_layout2);


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

        public void addData(ArrayList<NotificationData> arrayList) {
            photos.addAll(arrayList);
            notifyDataSetChanged();
        }

    }

    @Override
    public void onBackPressed() {
        if(getIntent()!=null || getIntent().getStringExtra("from")!=null){
            Log.d("hdgsdf","f if ");
            if(getIntent().getStringExtra("from").equalsIgnoreCase("notification")){
                Log.d("hdgsdf","s if ");
                if(userLoggedInSession.isLoggedIn()){
                    Log.d("hdgsdf","t if ");
                    startActivity(new Intent(NotificationActivity.this,HomeActivity.class).putExtra("from","notification"));
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                    finish();
                }
                else
                {
                    Log.d("hdgsdf","f else ");
                    super.onBackPressed();
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                }
            }
            else
            {
                Log.d("hdgsdf","s else ");
                super.onBackPressed();
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        }
        else
        {
            Log.d("hdgsdf","t else ");
            super.onBackPressed();
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        }

    }
}
