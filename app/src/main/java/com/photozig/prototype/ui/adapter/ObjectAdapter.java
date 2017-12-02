package com.photozig.prototype.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.photozig.prototype.R;
import com.photozig.prototype.rest.models.ZigObject;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by macbook on 02/12/2017.
 */

public class ObjectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final ArrayList<ZigObject> itens;
    private String location;
    private Context context;

    public ObjectAdapter(Context context, ArrayList<ZigObject> itens, String location) {
        this.context = context;
        this.itens = itens;
        this.location = location;
    }

    /**
     * Provide a reference to the views for each data item
     * Complex data items may need more than one view per item, and
     * you provide access to all the views for a data item in a view holder
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ViewHolder(View v) {
            super(v);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_photo, parent, false);
        // set the view's size, margins, paddings and layout parameters

        return new ViewHolder(v);

    }

    /**
     * Bind view Holder
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        TextView txtName = (TextView) holder.itemView.findViewById(R.id.txt_name);
        LinearLayout relativeClick = (LinearLayout) holder.itemView.findViewById(R.id.relative_click);
        ImageView imgPhoto = (ImageView) holder.itemView.findViewById(R.id.avatar);
        final ProgressWheel progressWheel = (ProgressWheel) holder.itemView.findViewById(R.id.progress_wheel);

        progressWheel.setVisibility(View.VISIBLE);

        final ZigObject item = itens.get(position);

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
        relativeClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(context, ArtistaActivity.class);
                //intent.putExtra("id_artista", item.getId());
                //context.startActivity(intent);
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
