package com.example.androidnewsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;

import com.example.androidnewsapp.Adapter.ListSourceAdapter;
import com.example.androidnewsapp.Common.Common;
import com.example.androidnewsapp.Interface.NewsService;
import com.example.androidnewsapp.Model.WebSite;
import com.google.gson.Gson;

import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    RecyclerView listWebsite;
    RecyclerView.LayoutManager layoutManager;
    NewsService mService;
    ListSourceAdapter adapter;
    SpotsDialog dialog;
    SwipeRefreshLayout swipeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Init cache
        Paper.init(this);

        //Init Service
        mService = Common.getNewsService();

        //Init View
        swipeLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefresh);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadWebsiteSource(true);
            }
        });
        listWebsite = (RecyclerView)findViewById(R.id.list_source);
        listWebsite.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        listWebsite.setLayoutManager(layoutManager) ;

        dialog = new SpotsDialog(this);

        loadWebsiteSource(false);

    }

        private void loadWebsiteSource(boolean isRefreshed){
            if(!isRefreshed){
                String cache = Paper.book().read("cache");

                if(cache!=null && !cache.isEmpty()){   // If have cache
                    WebSite webSite = new Gson().fromJson(cache,WebSite.class);
                    adapter = new ListSourceAdapter(getBaseContext(),webSite);
                    adapter.notifyDataSetChanged();
                    listWebsite.setAdapter(adapter);
                }

                else{    // If not have cache
                   dialog.show();
                   //Fetch new data
                   mService.getSources().enqueue(new Callback<WebSite>() {
                       @Override
                       public void onResponse(Call<WebSite> call, Response<WebSite> response) {
//                           Log.d("my res is:", Entit);
                           if(response.isSuccessful()) {
                               adapter = new ListSourceAdapter(getBaseContext(), response.body());
                               adapter.notifyDataSetChanged();
                               listWebsite.setAdapter(adapter);

                               // Save to cache
                               Paper.book().write("cache", new Gson().toJson(response.body()));
                               dialog.dismiss();
                           }
                           else{

                           }
                       }

                       @Override
                       public void onFailure(Call<WebSite> call, Throwable t) {

                       }
                   });
                }
            }
            else{    // If swipe refresh
                dialog.show();
                //Fetch new data
                mService.getSources().enqueue(new Callback<WebSite>() {
                    @Override
                    public void onResponse(Call<WebSite> call, Response<WebSite> response) {
                        adapter = new ListSourceAdapter(getBaseContext(),response.body());
                        adapter.notifyDataSetChanged();
                        listWebsite.setAdapter(adapter);

                        // Save to cache
                        Paper.book().write("cache",new Gson().toJson(response.body()));

                        // Dismiss refresh progressing
                        swipeLayout.setRefreshing(false);
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<WebSite> call, Throwable t) {

                    }
                });
            }
        }

}
