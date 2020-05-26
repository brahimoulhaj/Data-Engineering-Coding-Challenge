package com.ebradev.moroccoworldnews;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ebradev.moroccoworldnews.adapters.GridViewAdapter;
import com.ebradev.moroccoworldnews.models.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class SearchActivity extends AppCompatActivity {

    private GridView gridView;
    private ArrayList<News> news;
    private GridViewAdapter adapter;
    private LinearLayout progress;

    private ImageView search, back;
    private EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().hide();

        toolbar();

        gridView = findViewById(R.id.grid_view);
        news = new ArrayList<>();
        adapter = new GridViewAdapter(this, news);
        gridView.setAdapter(adapter);

        progress = findViewById(R.id.progress);
        progress.setVisibility(View.GONE);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchActivity.this, ArticleActivity.class);
                intent.putExtra("id", news.get(position).getId());
                startActivity(intent);
            }
        });

    }

    private void toolbar() {
        search = findViewById(R.id.search_icon);
        back = findViewById(R.id.back_icon);
        etSearch = findViewById(R.id.keyword);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                news.clear();
                adapter.notifyDataSetChanged();
                search();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void search() {
        progress.setVisibility(View.VISIBLE);
        String keyword = etSearch.getText().toString().trim();
        //etSearch.setText(keyword);
        String url = "https://polar-reaches-82218.herokuapp.com/search/"+keyword;
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String id = jsonObject.getString("_id");
                        id = id.replace("ObjectId('", "");
                        id = id.replace("')", "");
                        News article = new News(
                                id,
                                jsonObject.getString("title"),
                                jsonObject.getString("author"),
                                jsonObject.getString("date"),
                                jsonObject.getString("headline"),
                                jsonObject.getString("image"),
                                jsonObject.getString("url")
                        );
                        news.add(article);
                    }
                    progress.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(this).add(request);
    }
}
