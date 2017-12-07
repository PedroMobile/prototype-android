package com.photozig.prototype.ui.activity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.downloader.PRDownloader;
import com.github.guilhe.circularprogressview.CircularProgressView;
import com.google.gson.Gson;
import com.photozig.prototype.R;
import com.photozig.prototype.controller.DownloadManager;
import com.photozig.prototype.rest.models.AppInfo;
import com.photozig.prototype.rest.models.TxtObject;
import com.photozig.prototype.rest.models.ZigObject;
import com.universalvideoview.UniversalVideoView;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhotoActivity extends AppCompatActivity {

    private static final String TAG = "VIDEO";

    @BindView(R.id.btn_previous)
    ImageView btnPrevious;

    @BindView(R.id.btn_next)
    ImageView btnNext;

    @BindView(R.id.progress_download)
    CircularProgressView progressDownload;

    @BindView(R.id.txt_subtitle)
    TextView txtSubtitle;

    @BindView(R.id.video2)
    UniversalVideoView videoView;




    private int position;
    private AppInfo appInfo;
    private Timer timer;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying;
    private boolean isCompleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        getSupportActionBar().hide();
        ButterKnife.bind(this);

        init();

    }

    /**
     * Init
     */
    private void init(){

        this.appInfo =  new Gson().fromJson(getIntent().getStringExtra("appinfo"), AppInfo.class);
        this.position =  getIntent().getIntExtra("position", 0);

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnCompletionListener(completionListener);

        playVideo(position);

        startObserver();
    }

    /**
     * Click prev
     */
    @OnClick(R.id.btn_previous)
    void clickPrevious(){
        position--;
        mediaPlayer.reset();
        isCompleted = false;
        playVideo(position);
    }

    /**
     * Click next
     */
    @OnClick(R.id.btn_next)
    void clickNext(){
        position++;
        mediaPlayer.reset();
        isCompleted = false;
        playVideo(position);
    }

    /**
     * Play Video
     *
     * @param position
     */
    private void playVideo( int position) {

        final ZigObject item = appInfo.getZigObjects().get(position);

        txtSubtitle.setText("");

        File file = new File(DownloadManager.dirPath+"/"+item.getBg());

        if(file.exists()){

            progressDownload.setVisibility(View.GONE);
            videoView.setVisibility(View.VISIBLE);

            if(isPlaying) {
                videoView.setVideoPath(DownloadManager.dirPath+"/"+item.getBg());
                videoView.start();
            }

            if (!isCompleted){
                playSong(item);
            }


        }else{

            videoView.stopPlayback();
            videoView.setVisibility(View.GONE);
            progressDownload.setVisibility(View.VISIBLE);
            progressDownload.setProgress(0);

            // Start download
            DownloadManager.addDownloadID(item, appInfo.getAssetsLocation());
        }

        // Verify if can able button prev
        if(position > 0){
            btnPrevious.setVisibility(View.VISIBLE);
        }else{
            btnPrevious.setVisibility(View.GONE);
        }

        // Verify if can able button next
        if(position < this.appInfo.getZigObjects().size()-1){
            btnNext.setVisibility(View.VISIBLE);
        }else{
            btnNext.setVisibility(View.GONE);
        }
    }

    /**
     *
     * @param item
     */
    private void playSong(ZigObject item){

        try {
            if (!mediaPlayer.isPlaying()){

                mediaPlayer.setDataSource(appInfo.getAssetsLocation() + "/" + item.getSg());
                //mediaPlayer.setDataSource("https://ia802508.us.archive.org/5/items/testmp3testfile/mpthreetest.mp3");
                mediaPlayer.setOnPreparedListener(preparedListener);
                mediaPlayer.prepare();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private MediaPlayer.OnPreparedListener preparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            if (mediaPlayer != null) {
                isPlaying = true;
                isCompleted = false;
                mediaPlayer.start();

            }
        }
    };
    private MediaPlayer.OnCompletionListener completionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            isPlaying = false;
            isCompleted = true;
            videoView.pause();
        }
    };

    /**
     * Start Observer status
     */
    private void startObserver(){
        timer = new Timer();

        final Handler handler = new Handler();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @SuppressWarnings("unchecked")
                    public void run() {

                        ZigObject item = appInfo.getZigObjects().get(position);
                        try {
                            updateStateDownload(item);
                        }
                        catch (Exception e) {
                            // TODO Auto-generated catch block
                        }

                        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                            if(item.getTxts() != null){
                                for(TxtObject txtObject : item.getTxts()){
                                    if( (mediaPlayer.getCurrentPosition() / 1000) > txtObject.getTime() ){
                                        txtSubtitle.setText(txtObject.getTxt());
                                    }
                                }
                            }
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 100);
    }



    /**
     *
     * Check Download Status
     *
     * @param item
     */
    private void updateStateDownload(ZigObject item){

        if(DownloadManager.downloadIds.get(item.getBg()) != null){

            int downloadId = DownloadManager.downloadIds.get(item.getBg());

            // status download
            switch (PRDownloader.getStatus(downloadId)){
                case RUNNING:

                    if(DownloadManager.progresses.get(item.getBg()) != null){
                        progressDownload.setProgress(DownloadManager.progresses.get(item.getBg()));
                    }

                    break;
                case PAUSED:
                    progressDownload.setVisibility(View.VISIBLE);
                    progressDownload.setProgress(DownloadManager.progresses.get(item.getBg()));
                    break;
            }
        }else {
            // Verify if file existe
            File file = new File(DownloadManager.dirPath+"/"+item.getBg());
            if(file.exists()){
                progressDownload.setVisibility(View.GONE);
                if (!videoView.isPlaying()){
                    playVideo(position);
                }
            }
        }

    }

    @Override
    public void onPause(){
        super.onPause();
        mediaPlayer.pause();
    }

    @Override
    public void onResume(){
        super.onResume();
        mediaPlayer.start();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mediaPlayer.reset();
        timer.cancel();
    }
}
