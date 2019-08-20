package com.example.androidnewsapp.Adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidnewsapp.Common.ISO8601Parse;
import com.example.androidnewsapp.DetailArticle;
import com.example.androidnewsapp.Interface.ItemClickListener;
import com.example.androidnewsapp.Model.Article;
import com.example.androidnewsapp.R;
import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

class ListNewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    ItemClickListener itemClickListener;

    TextView article_title;
    RelativeTimeTextView article_time;
    CircleImageView article_image;


    public ListNewsViewHolder(View itemView) {
        super(itemView);
        article_image = (CircleImageView)itemView.findViewById(R.id.article_image);
        article_title = (TextView)itemView.findViewById(R.id.article_title);
        article_time = (RelativeTimeTextView) itemView.findViewById(R.id.article_time);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }
}



public class ListNewsAdapter extends RecyclerView.Adapter<ListNewsViewHolder>{
    private List<Article> articleList;
    private Context context;

    public ListNewsAdapter(List<Article> articleList, Context context) {
        this.articleList = articleList;
        this.context = context;
    }

    @NonNull
    @Override
    public ListNewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.news_layout,parent,false);
        return new ListNewsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListNewsViewHolder holder, int position) {
        Picasso.with(context)
                .load(articleList.get(position).getUrlToImage())
                .into(holder.article_image);

        if(articleList.get(position).getTitle().length() > 65){
            holder.article_title.setText(articleList.get(position).getTitle().substring(0,65) + "...");
        }
        else{
            holder.article_title.setText(articleList.get(position).getTitle());
        }

        Date date = null;
        try {
            date = ISO8601Parse.parse(articleList.get(position).getPublishedAt());
        }catch (ParseException ex){
            ex.printStackTrace();
        }
        holder.article_time.setReferenceTime(date.getTime());

        // Set OnClick Event
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Intent detail = new Intent(context, DetailArticle.class);
                detail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                detail.putExtra("webUrl",articleList.get(position).getUrl());
                context.startActivity(detail);
            }
        });
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }
}
