package com.photozig.prototype.controller;

import android.content.Context;
import android.util.Log;
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
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by macbook on 05/12/2017.
 */

public class DownloadManager {

    private static Deque<Integer> list;
    public static Map<String, Integer> downloadIds;
    public static Map<String, Long> progresses;
    public static String dirPath;
    private static String TAG = "DOWNLOAD_MANAGER";

    /**
     * init
     *
     * @param context application context
     */
    public static void init(Context context){
        // Enabling database for resume support even after the application is killed:
        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
                .build();
        PRDownloader.initialize(context, config);

        list = new LinkedList<>();
        downloadIds = new HashMap<String, Integer>();
        progresses = new HashMap<String, Long>();
        dirPath = Utils.getRootDirPath(context);
    }


    public static void addDownloadID(final ZigObject item, String location){

        // Pausa todos os downloads por um momento
        for(int downloadId: list){
            PRDownloader.pause(downloadId);
        }

        // Verificar se o download ja foi iniciado, se sim, ele se torna prioridade e é retomado de onde parou.
        // O restante da fila será retomada normalmente quando esse terminar.
        if(DownloadManager.downloadIds.get(item.getBg()) != null){
            list.remove(DownloadManager.downloadIds.get(item.getBg()));
            list.addFirst(DownloadManager.downloadIds.get(item.getBg()));
            PRDownloader.resume(list.getFirst());
        }else {

            int downloadId = PRDownloader.download(location + "/" + item.getBg(), dirPath, item.getBg()).build()

                    .setOnProgressListener(
                            new OnProgressListener() {
                                @Override
                                public void onProgress(Progress progress) {
                                    progresses.put(item.getBg(), (progress.currentBytes * 100) / progress.totalBytes);
                                }
                            }
                    ).start(
                            new OnDownloadListener() {
                                @Override
                                public void onDownloadComplete() {

                                    Log.i(TAG, "DOWNLOAD COMPLETED = "+item.getName());
                                    list.removeFirst();
                                    DownloadManager.downloadIds.remove(item.getBg());

                                    if (list.size() > 0) {
                                        PRDownloader.resume(list.getFirst());
                                    }

                                }

                                @Override
                                public void onError(Error error) {
                                }
                            });
            Log.i(TAG, "DOWNLOAD STARTED = "+item.getName());
            DownloadManager.downloadIds.put(item.getBg(), downloadId);

            list.addFirst(downloadId);
        }

    }
}
