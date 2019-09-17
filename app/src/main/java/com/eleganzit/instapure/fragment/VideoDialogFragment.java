package com.eleganzit.instapure.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eleganzit.instapure.R;
import com.eleganzit.instapure.utils.Exoplayer;
import com.eleganzit.instapure.utils.PlayerConfig;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;

public class VideoDialogFragment extends DialogFragment {

    public static final String TAG = "example_dialog";
    String youtubeLink;
    private ImageView close,playvideo;
    YouTubePlayerView ytview;
    YouTubePlayer.OnInitializedListener onInitializedListener;
    Bundle mArgs;
    String GRID_YOUTUBE_ID;

    public static VideoDialogFragment display(FragmentManager fragmentManager, Bundle args) {
        VideoDialogFragment exampleDialog = new VideoDialogFragment();
        exampleDialog.setArguments(args);
        exampleDialog.show(fragmentManager, TAG);
        return exampleDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
        mArgs = getArguments();

        youtubeLink = mArgs.getString("youtubeLink");
        GRID_YOUTUBE_ID = youtubeLink.substring(youtubeLink.lastIndexOf('=') + 1);

        Log.d("jdsfhdfsf",youtubeLink+"");

    }

    private void extractYoutubeUrl(View view) {
        new YouTubeExtractor(getActivity()) {
            @Override
            public void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta vMeta) {
                if (ytFiles != null) {
                    playVideo(ytFiles.get(22).getUrl(),view);
                }
            }
        }.extract(youtubeLink, true, true);
    }

    private void playVideo(String downloadUrl,View view) {
        SimpleExoPlayerView simpleExoPlayer = view.findViewById(R.id.player);
        simpleExoPlayer.setPlayer(Exoplayer.getSharedInstance(getActivity()).getSimpleExoPlayerView().getPlayer());
        Exoplayer.getSharedInstance(getActivity()).playStream(downloadUrl);
    }


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setWindowAnimations(R.style.AppTheme_Slide);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.video_dialog_layout, container, false);

        close=view.findViewById(R.id.close);
        ytview=view.findViewById(R.id.ytview);
        playvideo=view.findViewById(R.id.playvideo);

        onInitializedListener=new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadPlaylist(GRID_YOUTUBE_ID+"");
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };

        playvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ytview.initialize(PlayerConfig.API_KEY,onInitializedListener);
                //extractYoutubeUrl(view);
                playvideo.setVisibility(View.GONE);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }
}