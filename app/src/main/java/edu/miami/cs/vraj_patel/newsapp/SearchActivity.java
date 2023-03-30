package edu.miami.cs.vraj_patel.newsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.jacksonandroidnetworking.JacksonParserFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    // TODO : set the API_KEY variable to your api key
     String API_KEY = "667cf68eaa6e48b0b06f3bf0a9590003";
    // fbbc8a18e6934ad49468e2a21663801c
    // setting the TAG for debugging purposes
    private static String TAG = "SearchActivity";

    // declaring the views
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;

    // declaring an ArrayList of articles
    private ArrayList<NewsArticle> mArticleList;

    private SearchArticleAdapter mArticleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Bundle bundle = new Bundle();
        BottomNavigationView bottomNavigationView;
        SearchView search = findViewById(R.id.simpleSearchView);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.search);
        // assigning views to their ids
        mProgressBar=(ProgressBar)findViewById(R.id.progressbar_id);
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView=(RecyclerView)findViewById(R.id.recyclerview_id);
        mRecyclerView.setVisibility(View.INVISIBLE);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class).putExtra("Coming from search",true));


                    case R.id.search:


                }

                return false;
            }
        });
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //  Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
                mRecyclerView.setVisibility(View.VISIBLE);
                getallnews(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        search.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mArticleList.clear();
                mRecyclerView.setVisibility(View.INVISIBLE);
                return false;
            }
        });
        // initializing the Fast Android Networking Library
        AndroidNetworking.initialize(getApplicationContext());

        // setting the JacksonParserFactory
        AndroidNetworking.setParserFactory(new JacksonParserFactory());



        // setting the recyclerview layout manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // initializing the ArrayList of articles
        mArticleList=new ArrayList<>();
    }
    public void getallnews(String query){
        // clearing the articles list before adding news ones
        mArticleList.clear();

        // Making a GET Request using Fast
        // Android Networking Library
        // the request returns a JSONObject containing
        // news articles from the news api
        // or it will return an error


        //;

        AndroidNetworking.get("https://newsapi.org/v2/everything")
                .addQueryParameter("q", query)
                .addQueryParameter("apiKey",API_KEY)
                .addHeaders("token", "1234")
                .setTag("test")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener(){
                    @Override
                    public void onResponse(JSONObject response) {
                        // disabling the progress bar
                        mProgressBar.setVisibility(View.GONE);
                        // handling the response
                        try {
                            // storing the response in a JSONArray
                            JSONArray articles=response.getJSONArray("articles");

                            // looping through all the articles
                            // to access them individually
                            for (int j=0;j<articles.length();j++)
                            {
                                // accessing each article object in the JSONArray
                                JSONObject article=articles.getJSONObject(j);

                                // initializing an empty ArticleModel
                                NewsArticle currentArticle=new NewsArticle();

                                // storing values of the article object properties
                                String author=article.getString("author");
                                String title=article.getString("title");
                                String description=article.getString("description");
                                String url=article.getString("url");
                                String urlToImage=article.getString("urlToImage");
                                String publishedAt=article.getString("publishedAt");
                                String content=article.getString("content");

                                // setting the values of the ArticleModel
                                // using the set methods
                                currentArticle.setAuthor(author);
                                currentArticle.setTitle(title);
                                currentArticle.setDescription(description);
                                currentArticle.setUrl(url);
                                currentArticle.setUrlToImage(urlToImage);
                                currentArticle.setPublishedAt(publishedAt);
                                currentArticle.setContent(content);

                                // adding an article to the articles List
                                mArticleList.add(currentArticle);
                            }

                            // setting the adapter
                            mArticleAdapter=new SearchArticleAdapter(getApplicationContext(),mArticleList);
                            mRecyclerView.setAdapter(mArticleAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            // logging the JSONException LogCat
                            Log.d(TAG,"Error : "+e.getMessage());
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        // logging the error detail and response to LogCat
                        Log.d(TAG,"Error detail : "+error.getErrorDetail());
                        Log.d(TAG,"Error response : "+error.getResponse());
                    }
                });
    }
}