package com.photozig.prototype.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.guilhe.circularprogressview.CircularProgressView;
import com.google.gson.Gson;
import com.photozig.prototype.R;
import com.photozig.prototype.controller.DownloadManager;
import com.photozig.prototype.rest.models.AppInfo;
import com.photozig.prototype.rest.models.ZigObject;
import com.photozig.prototype.ui.activity.PhotoActivity;
import com.photozig.prototype.ui.viewholder.PhotoHolder;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by macbook on 02/12/2017.
 */

public class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {

    private static final String TAG = "PHOTO";
    private final ArrayList<ZigObject> itens;
    private final AppInfo appInfo;

    private String location;
    private Context context;

    public PhotoAdapter(Context context, AppInfo appInfo) {

        this.context = context;
        this.appInfo = appInfo;
        this.itens = appInfo.getZigObjects();
        this.location = appInfo.getAssetsLocation();

    }

    @Override
    public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_photo, parent, false);
        // set the view's size, margins, paddings and layout parameters

        return new PhotoHolder(v);

    }

    /**
     * Bind view Holder
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final PhotoHolder holder, final int position) {

        TextView txtName = (TextView) holder.itemView.findViewById(R.id.txt_name);
        ImageView btnPlay = (ImageView) holder.itemView.findViewById(R.id.btn_play);
        ImageView imgPhoto = (ImageView) holder.itemView.findViewById(R.id.avatar);
        final ProgressWheel progressWheel = (ProgressWheel) holder.itemView.findViewById(R.id.progress_wheel);

        // views download
        final ImageView clickDownload = (ImageView) holder.itemView.findViewById(R.id.click_download);
        final CircularProgressView progressDownload = (CircularProgressView) holder.itemView.findViewById(R.id.progress_download);

        // pre sets
        progressWheel.setVisibility(View.VISIBLE);


        final ZigObject item = itens.get(position);
        holder.setItem(item);

        txtName.setText(item.getName());


        // Photo
        String url = this.location+"/"+item.getIm();
        Picasso.with(context).load(url).error(R.mipmap.ic_launcher).into(imgPhoto, new Callback() {
            @Override
            public void onSuccess() {
                progressWheel.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                progressWheel.setVisibility(View.GONE);
            }
        });

        //click
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PhotoActivity.class);
                intent.putExtra("appinfo", new Gson().toJson(appInfo));
                intent.putExtra("position", position);
                context.startActivity(intent);
            }
        });

        // click to start downloading
        clickDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DownloadManager.addDownloadID(item, location);
            }
        });

    }

    /**
     *
     * @return count list item
     */
    @Override
    public int getItemCount() {
        return itens.size();
    }

}
