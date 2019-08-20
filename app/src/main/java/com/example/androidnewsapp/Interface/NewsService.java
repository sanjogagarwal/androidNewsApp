package com.example.androidnewsapp.Interface;

import com.example.androidnewsapp.Model.News;
import com.example.androidnewsapp.Model.WebSite;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface NewsService {
    @GET("v1/sources?language=en")
    Call<WebSite> getSources();

    @GET
    Call<News> getNewestArticle(@Url String url);
}
