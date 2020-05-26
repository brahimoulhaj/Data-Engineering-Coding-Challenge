package com.ebradev.moroccoworldnews.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ebradev.moroccoworldnews.R;
import com.ebradev.moroccoworldnews.models.News;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class GridViewAdapter extends ArrayAdapter<News> {

    private Context context;
    private ArrayList<News> news;

    public GridViewAdapter(@NonNull Context context, @NonNull ArrayList<News> news) {
        super(context, R.layout.layout_news, news);
        this.context = context;
        this.news = news;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = ((Activity) context).getLayoutInflater().inflate(R.layout.layout_news, parent, false);

        ImageView image = view.findViewById(R.id.news_image);
        TextView title = view.findViewById(R.id.news_title);
        TextView authorDate = view.findViewById(R.id.news_author_date);

        News article = news.get(position);

        Glide.with(context).load(article.getImage()).centerCrop().into(image);
        title.setText(article.getTitle());
        authorDate.setText(article.getAuthor() + " - " + article.getDate());

        return view;
    }
}
