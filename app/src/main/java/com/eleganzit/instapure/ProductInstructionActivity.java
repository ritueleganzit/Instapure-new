package com.eleganzit.instapure;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;
import me.nereo.multi_image_selector.bean.Image;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.allattentionhere.autoplayvideos.AAH_CustomRecyclerView;
import com.allattentionhere.autoplayvideos.AAH_CustomViewHolder;
import com.allattentionhere.autoplayvideos.AAH_VideosAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.eleganzit.instapure.api.RetrofitAPI;
import com.eleganzit.instapure.api.RetrofitInterface;
import com.eleganzit.instapure.fragment.HomeFragment;
import com.eleganzit.instapure.model.AddPromotionResponse;
import com.eleganzit.instapure.model.GetUserResponse;
import com.eleganzit.instapure.model.ImageFeedResponse;
import com.eleganzit.instapure.model.PhotosData;
import com.eleganzit.instapure.model.ProductInfo;
import com.eleganzit.instapure.model.ReorderResponse;
import com.eleganzit.instapure.model.VideoFeedResponse;
import com.eleganzit.instapure.utils.Exoplayer;
import com.eleganzit.instapure.utils.InfiniteScrollListener;
import com.eleganzit.instapure.utils.UserLoggedInSession;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.provider.PicassoProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductInstructionActivity extends AppCompatActivity {

    RecyclerView rc_productInfo;
    ProgressDialog progressDialog;

    ArrayList<ProductInfo> arrayList=new ArrayList<>();
    ArrayList<ProductInfo> arrayList2=new ArrayList<>();

    CallbackManager callbackManager;
    UserLoggedInSession userLoggedInSession;
    ShareDialog shareDialog;

    int vid_list_size=0,img_list_size=0;
    boolean vid_end=false,img_end=false;

    LinearLayoutManager layoutManager;
    ProductInfoAdapter productInfoAdapter;

    String promoType,feed_id;
    private int INSTA_REQUEST_CODE=0;
    private int WHATS_REQUEST_CODE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_instruction);

        FacebookSdk.sdkInitialize(getApplicationContext());

        ImageView back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        userLoggedInSession=new UserLoggedInSession(this);
        progressDialog = new ProgressDialog(ProductInstructionActivity.this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        rc_productInfo=findViewById(R.id.rc_productInfo);

        shareDialog = new ShareDialog(this);

        callbackManager = CallbackManager.Factory.create();

        layoutManager=new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        /*infiniteScrollListener = new InfiniteScrollListener(layoutManager, this);
        infiniteScrollListener.setLoaded();
*/
        rc_productInfo.setLayoutManager(layoutManager);
        //rc_productInfo.addOnScrollListener(infiniteScrollListener);

        productInfoAdapter=new ProductInfoAdapter(arrayList,ProductInstructionActivity.this);

        videoFeedlist();

        shareDialog.registerCallback(callbackManager, callback);
    }

    private FacebookCallback<Sharer.Result> callback = new FacebookCallback<Sharer.Result>() {
        @Override
        public void onSuccess(Sharer.Result result) {
            Log.v("fbstatus", "Successfully posted");
            addPromotion();
            // Write some code to do some operations when you shared content successfully.
        }

        @Override
        public void onCancel() {
            Log.v("fbstatus", "Sharing cancelled");
            // Write some code to do some operations when you cancel sharing content.
        }

        @Override
        public void onError(FacebookException error) {
            Log.v("fbstatus", error.getMessage());
            // Write some code to do some operations when some error occurs while sharing content.
        }
    };

    private void addPromotion() {

        progressDialog.show();
        RetrofitInterface myInterface = RetrofitAPI.getRetrofit().create(RetrofitInterface.class);

        Call<AddPromotionResponse> call = myInterface.addPromotion(userLoggedInSession.getUserDetails().get(UserLoggedInSession.USER_ID), promoType,feed_id);
        call.enqueue(new Callback<AddPromotionResponse>() {
            @Override
            public void onResponse(Call<AddPromotionResponse> call, Response<AddPromotionResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().toString().equalsIgnoreCase("1")) {
                        if (response.body().getData() != null) {

                            Toast.makeText(ProductInstructionActivity.this, "Promoted successfully", Toast.LENGTH_SHORT).show();

                        }
                    } else {

                        Toast.makeText(ProductInstructionActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
                else {

                    Toast.makeText(ProductInstructionActivity.this, "Server or Internet Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddPromotionResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ProductInstructionActivity.this, "Server or Internet Error" , Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void videoFeedlist() {
        progressDialog.show();
        RetrofitInterface myInterface = RetrofitAPI.getRetrofit().create(RetrofitInterface.class);
        Call<VideoFeedResponse> call = myInterface.videoFeedlist("1","3","2");
        call.enqueue(new Callback<VideoFeedResponse>() {
            @Override
            public void onResponse(Call<VideoFeedResponse> call, Response<VideoFeedResponse> response) {
                //  progressDialog.dismiss();
                if (response.isSuccessful()) {

                    if (response.body().getStatus().toString().equalsIgnoreCase("1")) {

                        if (response.body().getData() != null) {

                            for (int i = 0; i < response.body().getData().size(); i++) {

                                ProductInfo productInfo=new ProductInfo(response.body().getData().get(i).getVideoId(),"video",response.body().getData().get(i).getVideoLink(),response.body().getData().get(i).getVideoTitle(),response.body().getData().get(i).getCreatedDate(),response.body().getData().get(i).getDescription());
                                arrayList.add(productInfo);

                            }
                            Log.d("aysdfastd",response.body().getMessage()+" vids "+arrayList.size());

                            vid_list_size=vid_list_size+response.body().getData().size();
                            //rc_productInfo.setAdapter(new ProductInfoAdapter(arrayList,ProductInstructionActivity.this));

                        }
                    } else {
                        Log.d("aysdfastd","videos end");
                        vid_end=true;
                        Toast.makeText(ProductInstructionActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    if(!img_end){
                        imageFeedlist();
                    }


                }
                else {

                    Toast.makeText(ProductInstructionActivity.this, "Server or Internet Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<VideoFeedResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ProductInstructionActivity.this, "Server or Internet Error" , Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void imageFeedlist() {
        //progressDialog.show();
        RetrofitInterface myInterface = RetrofitAPI.getRetrofit().create(RetrofitInterface.class);
        Call<ImageFeedResponse> call = myInterface.imageFeedlist("1","2");
        call.enqueue(new Callback<ImageFeedResponse>() {
            @Override
            public void onResponse(Call<ImageFeedResponse> call, Response<ImageFeedResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {


                    if (response.body().getStatus().toString().equalsIgnoreCase("1")) {


                        if (response.body().getData() != null) {

                            for (int i = 0; i < response.body().getData().size(); i++) {

                                ProductInfo productInfo=new ProductInfo(response.body().getData().get(i).getImageId(),"image",response.body().getData().get(i).getImagePath(),response.body().getData().get(i).getImageTitle(),response.body().getData().get(i).getCreatedDate(),response.body().getData().get(i).getDescription());
                                arrayList.add(productInfo);

                            }
                            Log.d("aysdfastd",response.body().getMessage()+" imgs "+arrayList.size());
                            img_list_size=img_list_size+response.body().getData().size();

                            productInfoAdapter=new ProductInfoAdapter(arrayList,ProductInstructionActivity.this);

                            rc_productInfo.setAdapter(productInfoAdapter);

                        }
                    } else {
                        Log.d("aysdfastd","images end");
                        img_end=true;
                        Toast.makeText(ProductInstructionActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    if(!vid_end){
                        productInfoAdapter.addNullData();
                        Log.d("asjgdashd",arrayList.size()+"");
                        videoFeedlist2((vid_list_size+1));
                    }

                }
                else {

                    Toast.makeText(ProductInstructionActivity.this, "Server or Internet Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ImageFeedResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ProductInstructionActivity.this, "Server or Internet Error" , Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void videoFeedlist2(int limit) {

        RetrofitInterface myInterface = RetrofitAPI.getRetrofit().create(RetrofitInterface.class);
        Call<VideoFeedResponse> call = myInterface.videoFeedlist(limit+"","3","2");
        call.enqueue(new Callback<VideoFeedResponse>() {
            @Override
            public void onResponse(Call<VideoFeedResponse> call, Response<VideoFeedResponse> response) {

                if (response.isSuccessful()) {

                    if (response.body().getStatus().toString().equalsIgnoreCase("1")) {

                        Log.d("aysdfastd"," vids found");

                        if (response.body().getData() != null) {

                            for (int i = 0; i < response.body().getData().size(); i++) {

                                ProductInfo productInfo=new ProductInfo(response.body().getData().get(i).getVideoId(),"video",response.body().getData().get(i).getVideoLink(),response.body().getData().get(i).getVideoTitle(),response.body().getData().get(i).getCreatedDate(),response.body().getData().get(i).getDescription());
                                arrayList2.add(productInfo);

                            }
                            Log.d("aysdfastd",response.body().getMessage()+" vids "+arrayList.size());

                            if(img_end){
                                Log.d("where"," imgs end, data added");

                                productInfoAdapter.removeNull();
                                productInfoAdapter.addData(arrayList2);
                                //infiniteScrollListener.setLoaded();
                            }
                            else
                            {
                                Log.d("where"," imgs not end, data not added");

                            }

                            vid_list_size=vid_list_size+response.body().getData().size();
                            //rc_productInfo.setAdapter(new ProductInfoAdapter(arrayList,ProductInstructionActivity.this));

                        }
                    } else {

                        Log.d("aysdfastd","videos end");
                        vid_end=true;
                    }

                    if(vid_end){
                        productInfoAdapter.removeNull();
                    }

                    if(!img_end){
                        //productInfoAdapter.addNullData();
                        Log.d("asjgdashd",arrayList.size()+"");
                        Log.d("where"," imgs not end, called 2nd img api");

                        imageFeedlist2((img_list_size+1));
                    }
                    else if(!vid_end)
                    {
                        Log.d("where"," vids not end, called 2nd vids api");

                        productInfoAdapter.addNullData();
                        Log.d("asjgdashd",arrayList.size()+"");
                        videoFeedlist2((vid_list_size+1));
                    }

                    if(img_end && vid_end)
                    {
                        //productInfoAdapter.removeNull();
                    }

                }
                else {

                    Toast.makeText(ProductInstructionActivity.this, "Server or Internet Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<VideoFeedResponse> call, Throwable t) {
                Toast.makeText(ProductInstructionActivity.this, "Server or Internet Error" , Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void imageFeedlist2(int limit) {

        RetrofitInterface myInterface = RetrofitAPI.getRetrofit().create(RetrofitInterface.class);
        Call<ImageFeedResponse> call = myInterface.imageFeedlist(limit+"","2");
        call.enqueue(new Callback<ImageFeedResponse>() {
            @Override
            public void onResponse(Call<ImageFeedResponse> call, Response<ImageFeedResponse> response) {

                if (response.isSuccessful()) {

                    if (response.body().getStatus().toString().equalsIgnoreCase("1")) {

                        Log.d("aysdfastd"," imgs found");
                        if (response.body().getData() != null) {

                            for (int i = 0; i < response.body().getData().size(); i++) {

                                ProductInfo productInfo=new ProductInfo(response.body().getData().get(i).getImageId(),"image",response.body().getData().get(i).getImagePath(),response.body().getData().get(i).getImageTitle(),response.body().getData().get(i).getCreatedDate(),response.body().getData().get(i).getDescription());
                                arrayList2.add(productInfo);

                            }
                            Log.d("aysdfastd",response.body().getMessage()+" imgs "+arrayList.size());
                            img_list_size=img_list_size+response.body().getData().size();
                            productInfoAdapter.removeNull();
                            productInfoAdapter.addData(arrayList2);

                        }
                    } else {
                        Log.d("aysdfastd","images end");
                        img_end=true;
                    }

                    if(img_end)
                    {
                        productInfoAdapter.removeNull();
                    }

                    if(!vid_end){
                        productInfoAdapter.addNullData();
                        Log.d("asjgdashd",arrayList.size()+"");
                        videoFeedlist2((vid_list_size+1));
                    }
                    else if(!img_end){
                        productInfoAdapter.addNullData();
                        Log.d("asjgdashd",arrayList.size()+"");
                        imageFeedlist2((img_list_size+1));
                    }

                    if(img_end && vid_end)
                    {
                        //productInfoAdapter.removeNull();
                    }

                }
                else {

                    Toast.makeText(ProductInstructionActivity.this, "Server or Internet Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ImageFeedResponse> call, Throwable t) {
                Toast.makeText(ProductInstructionActivity.this, "Server or Internet Error" , Toast.LENGTH_SHORT).show();
            }
        });

    }

    public class ProductInfoAdapter extends RecyclerView.Adapter<ProductInfoAdapter.MyViewHolder>{

        ArrayList<ProductInfo> photos;
        Context context;
        Activity activity;
        private int height;
        int VIEW_TYPE_ITEM=0,VIEW_TYPE_LOADING=1;

        public ProductInfoAdapter(ArrayList<ProductInfo> photos, Context context) {
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
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_info_layout,viewGroup,false);
                return new DViewHolder(v);
            } else {
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.progressbar_layout,viewGroup,false);
                return new PViewHolder(v);
            }

        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder holder, final int i) {

            if (holder instanceof DViewHolder) {
                ProductInfo productInfo = photos.get(i);

                //String youtubeLink = BASE_URL + "/watch?v=" + GRID_YOUTUBE_ID;
                String youtubeLink = productInfo.getMedia_url();
                String GRID_YOUTUBE_ID;
                if(youtubeLink.contains("=")){
                    GRID_YOUTUBE_ID = youtubeLink.substring(youtubeLink.lastIndexOf('=') + 1);
                }
                else
                {
                    GRID_YOUTUBE_ID = youtubeLink.substring(youtubeLink.lastIndexOf('/') + 1);
                }

                Log.d("sdadad", "" + productInfo.getMedia_url());

                if (productInfo.getProduct_type().equalsIgnoreCase("image")) {
                    ((DViewHolder) holder).vid_layout.setVisibility(View.GONE);

                    Glide
                            .with(context)
                            .load(productInfo.getMedia_url())
                            .apply(new RequestOptions().centerCrop()).into(((DViewHolder) holder).vid_thumbnail);

                } else {
                    ((DViewHolder) holder).vid_layout.setVisibility(View.VISIBLE);

                    Glide
                            .with(context)
                            .load("http://img.youtube.com/vi/" + GRID_YOUTUBE_ID + "/0.jpg")
                            .apply(new RequestOptions().centerCrop()).into(((DViewHolder) holder).vid_thumbnail);

                }

                ((DViewHolder) holder).txt_title.setText(productInfo.getTitle());
                ((DViewHolder) holder).txt_time.setText(convertedDate(productInfo.getDate()));
                ((DViewHolder) holder).txt_info.setText(productInfo.getDescription());

                ((DViewHolder) holder).play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    /*try {
                        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                        TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
                        final SimpleExoPlayer simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
                        DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
                        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                        Uri uri1 = Uri.parse(productInfo.getMedia_url());
                        MediaSource mediaSource = new ExtractorMediaSource(uri1, dataSourceFactory, extractorsFactory, null, null);
                        holder.vid.setPlayer(simpleExoPlayer);
                        simpleExoPlayer.prepare(mediaSource);
                        simpleExoPlayer.setPlayWhenReady(true);

                    }
                    catch (Exception e)
                    {

                    }*/
                    /*Exoplayer.getSharedInstance(context).stopPlayer(true);

                    Exoplayer.getSharedInstance(context).playerPlaySwitch();
                    extractYoutubeUrl(youtubeLink,holder.vid);
                    holder.play.setVisibility(View.GONE);
                    holder.vid_thumbnail.setVisibility(View.GONE);*/

                        if (productInfo.getProduct_type().equalsIgnoreCase("video")) {
                            context.startActivity(new Intent(context, VideoActivity.class)
                                    .putExtra("youtubeLink", youtubeLink + "")
                                    .putExtra("title", productInfo.getTitle() + "")
                                    .putExtra("time", convertedDate(productInfo.getDate())+ "")
                                    .putExtra("info", productInfo.getDescription() + "")
                            );
                            activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        }

                        Log.d("jdsfhdfsf", youtubeLink + "");
                    }
                });

                ((DViewHolder) holder).share.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                promoType=productInfo.getProduct_type();
                                feed_id=productInfo.getProduct_id();
                                ShareSub(productInfo.getProduct_type(),productInfo.getTitle(),productInfo.getDescription(),productInfo.getMedia_url());
                            }
                        }

                );
            }
        }

        private void extractYoutubeUrl(String youtubeLink,SimpleExoPlayerView simpleExoPlayer) {
            new YouTubeExtractor(context) {
                @Override
                public void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta vMeta) {
                    if (ytFiles != null) {
                        playVideo(ytFiles.get(22).getUrl(),simpleExoPlayer);
                    }
                }
            }.extract(youtubeLink, true, true);
        }

        private void playVideo(String downloadUrl,SimpleExoPlayerView simpleExoPlayer) {
            simpleExoPlayer.setPlayer(Exoplayer.getSharedInstance(context).getSimpleExoPlayerView().getPlayer());
            Exoplayer.getSharedInstance(context).playStream(downloadUrl);
        }

        @Override
        public int getItemCount() {

                return photos.size();

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


        private void shareTextUrl(String link) {
            Intent share = new Intent(android.content.Intent.ACTION_SEND);
            share.setType("text/plain");
            share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

            // Add data to the intent, the receiving app will decide
            // what to do with it.
            share.putExtra(Intent.EXTRA_SUBJECT, "Share this");
            share.putExtra(Intent.EXTRA_TEXT, link+"");

            startActivity(Intent.createChooser(share, "Share link!"));
        }

        private void ShareSub(String type,String title,String desc,String link) {
            final Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT,"Share");
            i.putExtra(Intent.EXTRA_TEXT,link+"");

            ShareLinkContent pcontent = new ShareLinkContent.Builder()
                    .setContentTitle(title+"")
                    .setImageUrl(Uri.parse(link+""))
                    .setContentDescription(desc+"")
                    .setContentUrl(Uri.parse(link+""))
                    .setQuote(title+"")
                    .build();

/*

            final List<ResolveInfo> activities = getPackageManager().queryIntentActivities (i, 0);

            List<String> appNames = new ArrayList<String>();
            for (ResolveInfo info : activities) {
                appNames.add(info.loadLabel(getPackageManager()).toString());
            }

            if(isAppInstalled("com.instagram.android")){
                appNames.add("Instagram");
            }
*/

            Dialog dialog=new Dialog(context);
            dialog.setContentView(R.layout.share_dialog);

            TextView txt_fb,txt_insta;

            txt_fb=dialog.findViewById(R.id.txt_fb);
            txt_insta=dialog.findViewById(R.id.txt_insta);

            txt_fb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Facebook was chosen
                    dialog.dismiss();
                    if(isAppInstalled("com.facebook.katana")){
                        if(type.equalsIgnoreCase("video"))
                        {
                            shareDialog.show(pcontent, ShareDialog.Mode.AUTOMATIC);
                        }
                        else
                        {
                            getImage(link);
                        }
                        Log.d("whichhh","fb");
                    }
                    else
                    {
                        Toast.makeText(context, "Please install facebook first", Toast.LENGTH_SHORT).show();
                    }


                }
            });

            txt_insta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dialog.dismiss();
                    if(isAppInstalled("com.instagram.android")){

                        if(type.equalsIgnoreCase("image"))
                        {
                            shareImage(link,context);
                        }
                        else
                        {
                            shareLink(link,context);
                           // Toast.makeText(context, "Cannot share youtube videos to Instagram", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(context, "Please install instagram first", Toast.LENGTH_SHORT).show();
                    }

                }
            });

            Window window = dialog.getWindow();
            window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            dialog.show();

            /*AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Complete Action using...");
            builder.setItems(appNames.toArray(new CharSequence[appNames.size()]), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {

                    if (item<activities.size()){
                        ResolveInfo info = activities.get(item);

                        if (info.activityInfo.packageName.equals("com.facebook.katana")) {
                            // Facebook was chosen
                            if(type.equalsIgnoreCase("video"))
                            {
                                shareDialog.show(pcontent, ShareDialog.Mode.AUTOMATIC);
                            }
                            else
                            {
                                getImage(link);
                            }
                            Log.d("whichhh","fb");

                        }
                        else if (info.activityInfo.packageName.equals("com.whatsapp")) {
                            // Facebook was chosen
                            if(type.equalsIgnoreCase("video"))
                            {
                                i.setPackage(info.activityInfo.packageName);
                                startActivity(i);
                            }
                            else
                            {
                                shareImageWhatsapp(link,title,context);
                            }
                            Log.d("whichhh","fb");

                        }
                        else
                        {
                            i.setPackage(info.activityInfo.packageName);
                            startActivity(i);
                        }

                    }

                    if (item==activities.size()) {
                        // Instagram was chosen
                        Toast.makeText(context, "videos to Instagram", Toast.LENGTH_SHORT).show();

                        if(type.equalsIgnoreCase("image"))
                        {
                            shareImage(link,context);
                        }
                        else
                        {
                            Toast.makeText(context, "Cannot share youtube videos to Instagram", Toast.LENGTH_SHORT).show();
                        }
                    }

                    // start the selected activity

                }
            });

            AlertDialog alert = builder.create();
            alert.show();*/
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);

            }
        }

        public class DViewHolder extends MyViewHolder {

            LinearLayout share;
            SimpleExoPlayerView vid;
            ImageView play,vid_thumbnail;
            RelativeLayout vid_layout;
            TextView txt_title,txt_time,txt_info;

            public DViewHolder(@NonNull View itemView) {
                super(itemView);
                share=itemView.findViewById(R.id.share);
                vid=itemView.findViewById(R.id.pvideo);
                play=itemView.findViewById(R.id.playvideo);
                vid_thumbnail=itemView.findViewById(R.id.vid_thumbnail);
                vid_layout=itemView.findViewById(R.id.vid_layout);
                txt_title=itemView.findViewById(R.id.txt_title);
                txt_time=itemView.findViewById(R.id.txt_time);
                txt_info=itemView.findViewById(R.id.txt_info);
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

        public void addData(ArrayList<ProductInfo> integersList) {
            photos.addAll(integersList);
            notifyDataSetChanged();
            arrayList2.clear();
        }

        private void getImage(String mediaUrl){
            new DownloadImgTask().execute(mediaUrl);
        }

        private void postFB(Bitmap bm){
            SharePhoto photo = new SharePhoto.Builder().setBitmap(bm).build();
            SharePhotoContent content = new SharePhotoContent.Builder().addPhoto(photo).build();
            ShareDialog dialog = new ShareDialog(activity);
            if (dialog.canShow(SharePhotoContent.class)){
                dialog.show(content);
            }
            else{
                Log.d("Activity", "you cannot share photos :(");
            }

        }

        private class DownloadImgTask extends AsyncTask<String, Void, Bitmap> {

            protected Bitmap doInBackground(String... urls) {
                String urldisplay = urls[0];
                Bitmap bm = null;
                try {
                    InputStream in = new java.net.URL(urldisplay).openStream();
                    bm = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
                return bm;
            }

            protected void onPostExecute(Bitmap result) {
                postFB(result);
            }
        }

        public void shareImage(String url, final Context context) {

            PicassoProvider
                    .get()
                    .load(url)
                    .into(new Target() {
                @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setPackage("com.instagram.android");
                    i.setType("image/*");
                    i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap, context));

                    try {
                        // Fire the Intent.
                        startActivityForResult(i, INSTA_REQUEST_CODE);
                    } catch(ActivityNotFoundException e) {
                        // instagram not installed
                        Toast.makeText(context, "Please install Instagram first", Toast.LENGTH_SHORT).show();
                    }
                    //context.startActivity(Intent.createChooser(i, "Share Image"));
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                }

                @Override public void onPrepareLoad(Drawable placeHolderDrawable) { }
            });
        }

        public void shareLink(String url, final Context context) {

            Intent share = new Intent(Intent.ACTION_SEND);

// Limit this call to instagram
            share.setPackage("com.instagram.android");

// Set the MIME type
            share.setType("text/plain");

// Create the URI from the media

// Add the URI to the Intent.
            share.putExtra(Intent.EXTRA_TEXT, url);

            try {
                // Fire the Intent.
                startActivityForResult(share, INSTA_REQUEST_CODE);
            } catch(ActivityNotFoundException e) {
                // instagram not installed
            }
        }

        public void shareImageWhatsapp(String url,String title, final Context context) {

            PicassoProvider
                    .get()
                    .load(url)
                    .into(new Target() {
                        @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            Intent i = new Intent(Intent.ACTION_SEND);
                            i.setPackage("com.whatsapp");
                            i.setType("image/*");
                            i.putExtra(Intent.EXTRA_TEXT, title+"");
                            i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap, context));

                            try {
                                // Fire the Intent.
                                startActivityForResult(i, WHATS_REQUEST_CODE);
                            } catch(ActivityNotFoundException e) {
                                // instagram not installed
                            }
                            //context.startActivity(Intent.createChooser(i, "Share Image"));
                        }

                        @Override
                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                        }

                        @Override public void onPrepareLoad(Drawable placeHolderDrawable) { }
                    });
        }

        public Uri getLocalBitmapUri(Bitmap bmp, Context context) {
            Uri bmpUri = null;
            try {
                File file =  new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
                FileOutputStream out = new FileOutputStream(file);
                bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
                out.close();
                bmpUri = FileProvider.getUriForFile(context, "com.eleganzit.instapure.provider", file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bmpUri;
        }


    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==INSTA_REQUEST_CODE)
        {
            addPromotion();
            if(resultCode==RESULT_OK){
                Log.d("instastatus","result ok");

            }
            else
            {
                Log.d("instastatus","result not ok");
            }
        }
        else if(requestCode==WHATS_REQUEST_CODE){
            if(resultCode==RESULT_OK){
                Log.d("whatsstatus","result ok");
                addPromotion();
            }
            else
            {
                Log.d("whatsstatus","result not ok");
            }
        }
        else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private boolean isAppInstalled(String packageName) {
        PackageManager pm = getPackageManager();
        boolean installed = false;
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

    }
}
