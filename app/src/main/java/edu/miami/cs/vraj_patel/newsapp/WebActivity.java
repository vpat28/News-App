package edu.miami.cs.vraj_patel.newsapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class WebActivity extends AppCompatActivity {
    // setting the TAG for debugging purposes
    private static String TAG="WebActivity";

    // declaring the webview
    private WebView myWebView;

    // declaring the url string variable
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        FloatingActionButton fab = findViewById(R.id.fab);


        // assigning the views to id's
        myWebView = (WebView) findViewById(R.id.webview);

        Intent intent = getIntent();

        // checking if there is an intent
        if(intent!=null){
            // retrieving the url in the intent
            url = intent.getStringExtra("url_key");

            // loading and displaying a
            // web page in the activity
            myWebView.loadUrl(url);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, intent.getStringExtra("url_key"));
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
            }
        });
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
    }
}