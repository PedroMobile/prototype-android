package com.photozig.prototype.ui.activity;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.photozig.prototype.R;
import com.photozig.prototype.rest.AppClient;
import com.photozig.prototype.rest.models.AppInfo;
import com.photozig.prototype.rest.models.ZigObject;
import com.photozig.prototype.ui.adapter.ObjectAdapter;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout swipeLayout;

    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    private ArrayList<ZigObject> itens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();

    }

    /**
     * init Activity
     */
    private void init(){
        recyclerView.setHasFixedSize(false);

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).color(getResources().getColor(R.color.gray)).sizeResId(R.dimen.margin_1dp).build());

        // setup SwipeLayout
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeResources(R.color.colorPrimary);

        // init default
        swipeLayout.measure(0,0);
        swipeLayout.setRefreshing(true);

        // Start load feed
        load(this);
    }

    @Override
    public void onRefresh() {
        load(this);
    }

    /**
     * Request group members
     */
    private void load(final Context context){

        AppClient.getApiService().appInfo().enqueue(new Callback<AppInfo>() {
            @Override
            public void onResponse(Call<AppInfo> call, Response<AppInfo> response) {
                swipeLayout.setRefreshing(false);

                ObjectAdapter adapter = new ObjectAdapter(context, response.body().getZigObjects(), response.body().getAssetsLocation());
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<AppInfo> call, Throwable t) {
                swipeLayout.setRefreshing(false);
            }
        });
    }

}
