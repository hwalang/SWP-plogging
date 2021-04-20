package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.myapplication.fragment.NavigationIssueFragment;
import com.squareup.picasso.Picasso;

public class Detailed extends AppCompatActivity {
    TextView tvTitle,tvSource, tvDate,tvDesc;
    ImageView imageView;
    WebView webView;
    ProgressBar loader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        tvTitle = findViewById(R.id.tvTitle);
        tvSource = findViewById(R.id.tvSource);
        tvDate = findViewById(R.id.tvDate);
        tvDesc = findViewById(R.id.tvDesc);
        imageView = findViewById(R.id.image);
        webView = (WebView) this.findViewById(R.id.webView);


        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String source = intent.getStringExtra("source");
        String time = intent.getStringExtra("time");
        String desc = intent.getStringExtra("desc");
        String imageurl = intent.getStringExtra("imageurl");
        String url = intent.getStringExtra("url");

        tvTitle.setText(title);
        tvSource.setText(source);
        tvDate.setText(time);
        tvDesc.setText(desc);

        Picasso.with(Detailed.this).load(imageurl).into(imageView);


        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.loadUrl(url);



    }
}