package com.example.a123456.cartzhonggu;

import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.a123456.cartzhonggu.R;

import base.BaseActivity;

public class WebViewActivity extends BaseActivity {
private WebView webView;
private ProgressBar progressBar;
String UrlStr="http://www.baidu.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        UrlStr=getIntent().getStringExtra("url");
        initView();
    }

    private void initView() {
        webView=findViewById(R.id.web);
        progressBar=findViewById(R.id.pro);
        WebSettings webSettings=webView.getSettings();
        // webview从5.0开始默认不允许混合模式,https中不能加载http资源,需要设置开启。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                Log.e("TAG","error这个方法=="+error.getPrimaryError());

                super.onReceivedSslError(view, handler, error);
                handler.proceed();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (URLUtil.isNetworkUrl(url)) {
                    view.loadUrl(url);
                } else {
                    // 不是网址时，可能是打开app的链接，可以打开链接或者忽略
                    // 打开app的方法
                    /*try {
                        startActivity(Intent.parseUri(url, Intent.URI_INTENT_SCHEME));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }*/
                }
                return true;
            }

//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                view.loadUrl(request.toString());
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    Log.e("TAG","7.0路径=="+request.getUrl().toString());
//                    view.loadUrl(request.getUrl().toString());
//                } else {
//                    Log.e("TAG","路径222222=="+request.toString());
//                    view.loadUrl(request.toString());
//                }
//
//                return true;
//            }
        });

        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if(newProgress==100){
                    progressBar.setVisibility(View.GONE);
                }else{
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                }
            }
        });
        //加载URL
        webView.loadUrl(UrlStr);
    }
    //设置返回键动作（防止按返回键直接退出程序)
    @Override
    public void onBackPressed() {
        if(webView.canGoBack()) {//当webview不是处于第一页面时，返回上一个页面
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
