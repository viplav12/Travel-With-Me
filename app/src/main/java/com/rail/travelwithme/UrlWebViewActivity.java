package com.rail.travelwithme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UrlWebViewActivity extends Activity {


    public static String getDivFromUrl(String url, String tagToCount) {

        List<String> tags = null;

        try {
            Document document = Jsoup.connect(url).get();
            tags = new ArrayList<>();
            for (Element e : document.getAllElements()) {
                tags.add(e.tagName().toLowerCase());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        assert tags != null;
        int occurrences = Collections.frequency(tags, tagToCount);
        return Integer.toString(occurrences);
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        StrictMode.ThreadPolicy policy = new StrictMode.
                ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.web_view);


        WebView myWebView = findViewById(R.id.webView);
        TextView divCounter = findViewById(R.id.countDiv);
        myWebView.getSettings().setDomStorageEnabled(true);
        myWebView.getSettings().setJavaScriptEnabled(true);


        myWebView.setWebViewClient(new WebViewClient());


        myWebView.loadUrl("https://www.eurail.com/en/get-inspired");

        divCounter.setText(getDivFromUrl("https://www.eurail.com/en/get-inspired",
                "div"));


    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}