package com.ebradev.moroccoworldnews;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class ArticleActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView title, authorDate, headline;
    private String newsUrl;

    private LinearLayout progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progress = findViewById(R.id.progress);

        imageView = findViewById(R.id.news_image);
        title = findViewById(R.id.news_title);
        authorDate = findViewById(R.id.news_author_date);
        headline = findViewById(R.id.news_headline);

        String id = getIntent().getStringExtra("id");
        //Toast.makeText(this, id, Toast.LENGTH_LONG).show();
        String url = "https://polar-reaches-82218.herokuapp.com/articles/" + id;

        Volley.newRequestQueue(this).add(new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                    title.setText(jsonObject.getString("title"));
                    authorDate.setText(jsonObject.getString("author") + "-" + jsonObject.getString("date"));
                    headline.setText(jsonObject.getString("headline"));
                    Glide.with(ArticleActivity.this).load(jsonObject.getString("image")).centerCrop().into(imageView);
                    newsUrl = jsonObject.getString("url");
                    progress.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Server Internal Error", Toast.LENGTH_LONG).show();
            }
        }));

    }

    public void ReadMore(View view) {
        //Toast.makeText(this, newsUrl, Toast.LENGTH_LONG).show();
        Uri uri = Uri.parse(newsUrl);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
