package com.veselov.andrew.tbw.ui.activities.fragments;
// Android Level 2 Lesson 4
// Homework 30-Dec-2018
// Andrew Veselov
//
// 1. В погодном приложении сделать сохранение и загрузку настроек (например, выбранный домашний город).
//
// 2. * Сделать текстовый мини-браузер с применением WebView, OkHttp и полем ввода страницы.
//

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.veselov.andrew.tbw.R;


public class WorkoutExternalPageFragment extends Fragment {


    public WorkoutExternalPageFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_workout_external_page, container, false);
        final WebView webView = root.findViewById(R.id.ext_page_browse);
        final TextView ext_page_url = root.findViewById(R.id.ext_page_url);
        final TextView status = root.findViewById(R.id.status);
        final ProgressBar progressBar = root.findViewById(R.id.ext_page_progressBar);
        Button goBtn = root.findViewById(R.id.ext_page_go_btn);

        WebViewClient webViewClient = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                status.setVisibility(View.VISIBLE);
                status.setText("Ошибка загрузки: " + failingUrl + ", код: " + errorCode + " [" + description + "]");
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            @Override public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setIndeterminate(false);
                progressBar.setProgress(100);
            }

            @Override public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setIndeterminate(true);
            }
        };

        webView.setWebViewClient(webViewClient);
        webView.getSettings().setJavaScriptEnabled(true);

        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status.setVisibility(View.GONE);

                String url = ext_page_url.getText().toString();
                if (!url.startsWith("http")) {
                    url="http://" + url;
                    ext_page_url.setText(url);
                }
                webView.loadUrl(url);
            }
        });

        return root;
    }

}
