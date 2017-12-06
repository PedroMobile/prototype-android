package com.photozig.prototype.controller;

import android.content.Context;
import android.view.View;

import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.downloader.Progress;
import com.downloader.Status;
import com.downloader.request.DownloadRequestBuilder;
import com.photozig.prototype.rest.models.ZigObject;
import com.photozig.prototype.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by macbook on 05/12/2017.
 */

public class DownloadManager {

    private static ArrayList<Integer> list;
    public static Map<String, Integer> downloadIds;
    public static Map<String, Long> progresses;
    public static String dirPath;

    /**
     * init
     *
     * @param context application context
     */
    public static void init(Context context){
        // Enabling database for resume support even after the application is killed:
        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
                .setDatabaseEnabled(true)
                .build();
        PRDownloader.initialize(context, config);

        list = new ArrayList<>();
        downloadIds = new HashMap<String, Integer>();
        progresses = new HashMap<String, Long>();
        dirPath = Utils.getRootDirPath(context);
    }


    public static void addDownloadID(final ZigObject item, String location){

        DownloadManager.downloadIds.put(item.getBg() , PRDownloader.download(location+"/"+item.getBg(), dirPath,  item.getBg()).build()

                .setOnProgressListener(
                        new OnProgressListener() {
                            @Override
                            public void onProgress(Progress progress) {
                                progresses.put(item.getBg(), (progress.currentBytes*100)/progress.totalBytes) ;
                            }
                        }
                ).start(
                        new OnDownloadListener() {
                            @Override
                            public void onDownloadComplete() {}

                            @Override
                            public void onError(Error error) {}
                        }));

        /*if(!list.contains(newID)) {
            for (int downloadID : list) {
                if (PRDownloader.getStatus(downloadID) == Status.RUNNING && downloadID != newID) {
                    PRDownloader.pause(downloadID);
                }
            }

            list.add(newID);

        }*/
    }
}
