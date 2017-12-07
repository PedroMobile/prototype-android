package com.photozig.prototype.ui.viewholder;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.downloader.PRDownloader;
import com.github.guilhe.circularprogressview.CircularProgressView;
import com.photozig.prototype.R;
import com.photozig.prototype.controller.DownloadManager;
import com.photozig.prototype.rest.models.ZigObject;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by macbook on 05/12/2017.
 */

public class PhotoHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "HOLDER";

    private ZigObject item;

    public PhotoHolder( View itemView) {
        super(itemView);

        Timer timer = new Timer();

        final Handler handler = new Handler();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @SuppressWarnings("unchecked")
                    public void run() {
                        try {
                            if (item!= null) {
                                updateStateDownload(item);
                            }
                        }
                        catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 500);
    }

    private void updateStateDownload(ZigObject item){

        // views download
        ImageView clickDownload = (ImageView) this.itemView.findViewById(R.id.click_download);
        CircularProgressView progressDownload = (CircularProgressView) this.itemView.findViewById(R.id.progress_download);
        TextView txtComplete = (TextView) this.itemView.findViewById(R.id.txt_complete);

        if(DownloadManager.downloadIds.get(item.getBg()) != null){

            int downloadId = DownloadManager.downloadIds.get(item.getBg());
            txtComplete.setVisibility(View.GONE);

            // status download
            switch (PRDownloader.getStatus(downloadId)){
                case RUNNING:
                    clickDownload.setVisibility(View.GONE);
                    progressDownload.setVisibility(View.VISIBLE);

                    if(DownloadManager.progresses.get(item.getBg()) != null){
                        progressDownload.setProgress(DownloadManager.progresses.get(item.getBg()));
                    }

                    break;

                case PAUSED:
                    clickDownload.setVisibility(View.GONE);
                    progressDownload.setVisibility(View.VISIBLE);
                    progressDownload.setProgress(DownloadManager.progresses.get(item.getBg()));
                    break;

                case COMPLETED:
                    clickDownload.setVisibility(View.GONE);
                    progressDownload.setVisibility(View.GONE);
                    break;

                case CANCELLED:
                    clickDownload.setVisibility(View.VISIBLE);
                    progressDownload.setVisibility(View.GONE);
                    break;
            }
        }else {
            // Verify if file existe
            File file = new File(DownloadManager.dirPath+"/"+item.getBg());
            if(file.exists()){
                clickDownload.setVisibility(View.GONE);
                progressDownload.setVisibility(View.GONE);
                txtComplete.setVisibility(View.VISIBLE);
            }else{
                clickDownload.setVisibility(View.VISIBLE);
                progressDownload.setVisibility(View.GONE);
                txtComplete.setVisibility(View.GONE);
            }

        }

    }

    public void setItem(ZigObject item) {
        this.item = item;
    }
}
