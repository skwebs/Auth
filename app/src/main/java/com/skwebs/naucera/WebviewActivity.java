package com.skwebs.naucera;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class WebviewActivity extends AppCompatActivity {

    WebView myWebView;

    final String url = "https://anshumemorial.in";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Objects.requireNonNull(getSupportActionBar()).hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        myWebView = findViewById(R.id.webview);
//        myWebView.setWebViewClient(new MyWebViewClient());


        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.setWebViewClient(new myWebViewClient());
        myWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        WebSettings settings = myWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);
//         Improve loading speed
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        settings.setUseWideViewPort(true);
        settings.setSaveFormData(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);

        myWebView.loadUrl(url);
    }

    @Override
    public void onBackPressed(){
        if (myWebView.canGoBack()){
            myWebView.goBack();
        } else {
            new AlertDialog.Builder(this)
                    .setMessage("Are you sure to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes",((dialog, which) -> WebviewActivity.super.onBackPressed()))
                    .setNegativeButton("No",null)
                    .show();
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            if (url.equals(request.getUrl().getHost())) {
                // This is my website, so do not override; let my WebView load the page
                return false;
            }
            // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
            Intent intent = new Intent(Intent.ACTION_VIEW, request.getUrl());
            startActivity(intent);
            return true;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) {
            myWebView.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }


    private class myWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("mailto:")) {
                startActivity(new Intent(Intent.ACTION_SENDTO, Uri.parse(url)));
            } else if(url.startsWith("tel:") || url.startsWith("whatsapp:")) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
                return true;
            }
            return false;
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            final Uri uri = request.getUrl();
            if (uri.toString().startsWith("mailto:")) {
                //Handle mail Urls
                startActivity(new Intent(Intent.ACTION_SENDTO, uri));
            } else if (uri.toString().startsWith("tel:")) {
                //Handle telephony Urls
                startActivity(new Intent(Intent.ACTION_DIAL, uri));
            } else if (uri.toString().startsWith("whatsapp:")) {
                //Handle whatsapp Urls
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            } else {
                //Handle Web Urls
                view.loadUrl(uri.toString());
            }
            return true;
        }


    }

    private static class myWebChromeClient extends WebChromeClient {

        //  for javaScript Alert : get from -
        //  https://stackoverflow.com/questions/38053779/android-webview-how-to-change-javascript-alert-title-text-in-android-webview
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result){
            new AlertDialog.Builder(view.getContext())
//                    .setTitle("Title")
                    .setMessage(message)
                    .setPositiveButton("OK", (DialogInterface dialog, int which) -> result.confirm())
                    .setOnDismissListener((DialogInterface dialog) -> result.confirm())
                    .create()
                    .show();
            return true;
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, JsResult result){
            new AlertDialog.Builder(view.getContext())
//                    .setTitle("Application says:")
                    .setMessage(message)
                    .setPositiveButton("OK", (DialogInterface dialog, int which) -> result.confirm())
                    .setNegativeButton("Cancel", (DialogInterface dialog, int which) -> result.cancel())
                    .setOnDismissListener((DialogInterface dialog) -> result.cancel())
                    .create()
                    .show();
            return true;
        }

    }
}