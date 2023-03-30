package edu.miami.cs.vraj_patel.newsapp;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.jacksonandroidnetworking.JacksonParserFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // TODO : set the API_KEY variable to your api key
     String API_KEY="667cf68eaa6e48b0b06f3bf0a9590003";
   // fbbc8a18e6934ad49468e2a21663801c
    // setting the TAG for debugging purposes
    private static String TAG="MainActivity";

    // declaring the views
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;

    // declaring an ArrayList of articles
    private ArrayList<NewsArticle> mArticleList;

    private ArticleAdapter mArticleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle bundle = new Bundle();
        BottomNavigationView bottomNavigationView;
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                       // Toast.makeText(MainActivity.this, "Home Button Works", Toast.LENGTH_SHORT).show();
                    case R.id.search:
                        Intent nextActivity = new Intent(MainActivity.this,SearchActivity.class);
                        nextActivity.putExtra("apikey", API_KEY);
                        startSearch.launch(nextActivity);
                    case R.id.settings:
                        Intent anotherActivity = new Intent(MainActivity.this,SettingsActivity.class);
                     //   startSettings.launch(anotherActivity);
                }

                return false;
            }
        });
        // initializing the Fast Android Networking Library
        AndroidNetworking.initialize(getApplicationContext());

        // setting the JacksonParserFactory
        AndroidNetworking.setParserFactory(new JacksonParserFactory());

        // assigning views to their ids
        mProgressBar=(ProgressBar)findViewById(R.id.progressbar_id);
        mRecyclerView=(RecyclerView)findViewById(R.id.recyclerview_id);

        // setting the recyclerview layout manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // initializing the ArrayList of articles
        mArticleList=new ArrayList<>();

        // calling get_news_from_api()
        Boolean refreshNeeded = this.getIntent().getBooleanExtra("Coming from search",true);
        if(refreshNeeded) {
            get_news_from_api();
        }
    }


    public void get_news_from_api(){
        // clearing the articles list before adding news ones
        mArticleList.clear();

        // Making a GET Request using Fast
        // Android Networking Library
        // the request returns a JSONObject containing
        // news articles from the news api
        // or it will return an error
        AndroidNetworking.get("https://newsapi.org/v2/top-headlines")
                .addQueryParameter("country", "us")
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
                            mArticleAdapter=new ArticleAdapter(getApplicationContext(),mArticleList);
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
    ActivityResultLauncher<Intent> startSearch = registerForActivityResult( //ARL for Editor activity
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if(result.getResultCode()==RESULT_OK){ // Result_OK is sent back if a description was successfully set

                    }else{ // user pressed back button or didnt save in the Editor activity

                    }

                }
            });
    ActivityResultLauncher<Intent> startSettings = registerForActivityResult( //ARL for Editor activity
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if(result.getResultCode()==RESULT_OK){ // Result_OK is sent back if a description was successfully set

                    }else{ // user pressed back button or didnt save in the Editor activity

                    }

                }
            });
}
