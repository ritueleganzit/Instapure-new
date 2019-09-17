package com.eleganzit.instapure;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.eleganzit.instapure.utils.PlayerConfig;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class VideoActivity extends YouTubeBaseActivity {

    String youtubeLink;
    private ImageView close;//,playvideo;
    YouTubePlayerView ytview;
    YouTubePlayer.OnInitializedListener onInitializedListener;
    Bundle mArgs;
    String GRID_YOUTUBE_ID;
    private String title,time,info;
    TextView txt_title,txt_time,txt_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video);

        youtubeLink = getIntent().getStringExtra("youtubeLink");
        title = getIntent().getStringExtra("title");
        time = getIntent().getStringExtra("time");
        info = getIntent().getStringExtra("info");

        if(youtubeLink.contains("=")){
            GRID_YOUTUBE_ID = youtubeLink.substring(youtubeLink.lastIndexOf('=') + 1);
        }
        else
        {
            GRID_YOUTUBE_ID = youtubeLink.substring(youtubeLink.lastIndexOf('/') + 1);
        }

        Log.d("sjdgfteftvy",youtubeLink+"  "+GRID_YOUTUBE_ID);
        ytview=findViewById(R.id.ytview);
        txt_title=findViewById(R.id.txt_title);
        txt_time=findViewById(R.id.txt_time);
        txt_info=findViewById(R.id.txt_info);


        //playvideo=findViewById(R.id.playvideo);

        close=findViewById(R.id.close);



        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        onInitializedListener=new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo(GRID_YOUTUBE_ID+"");
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };
        ytview.initialize(PlayerConfig.API_KEY,onInitializedListener);
        txt_title.setText(title);
        txt_time.setText(time);
        txt_info.setText(info);


/*

        playvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //extractYoutubeUrl(view);
                playvideo.setVisibility(View.GONE);
            }
        });
*/

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    
}
