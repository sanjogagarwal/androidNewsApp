package com.example.androidnewsapp.Common;

import com.example.androidnewsapp.Interface.IconBetterIdeaService;
import com.example.androidnewsapp.Interface.NewsService;
import com.example.androidnewsapp.Model.IconBetterIdea;
import com.example.androidnewsapp.Remote.IconBetterIdeaClient;
import com.example.androidnewsapp.Remote.RetrofitClient;

public class Common {
    private static final String BASE_URL = "https://newsapi.org/";
    public static final String API_KEY = "90e35208ebd3454395ec8af218be4417";

    public static NewsService getNewsService(){
        return RetrofitClient.getClient(BASE_URL).create(NewsService.class);
    }
    public static IconBetterIdeaService getIconService(){
        return IconBetterIdeaClient.getClient().create(IconBetterIdeaService.class);
    }

    //https://newsapi.org/v1/articles?source=the-verge&sortBy=latest&apiKey={API_KEY}
    public static String getAPIUrl(String source, String sortBy, String apiKEY){
        StringBuilder apiUrl = new StringBuilder("https://newsapi.org/v1/articles?source=");
        return apiUrl.append(source)
                .append("&sortBy=")
                .append(sortBy)
                .append("&apiKey=")
                .append(apiKEY)
                .toString();
    }
}
