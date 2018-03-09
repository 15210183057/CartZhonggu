package com.example.a123456.cartzhonggu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.a123456.cartzhonggu.R;
import com.example.a123456.cartzhonggu.WebViewActivity;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestActivity extends AppCompatActivity implements View.OnClickListener{
TextView tv1,tv2,tv3;
ImageView img;
WebView webView;
ProgressBar progressBar;
    String imgUrl="http://images-shichai.test.cnfol.com/original/201606/4_1464745054_1.jpg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initView();
    }

    private void initView() {
        tv1=findViewById(R.id.tv1);
        tv2=findViewById(R.id.tv2);
        tv3=findViewById(R.id.tv3);
        img=findViewById(R.id.img);
        progressBar=findViewById(R.id.pro);
        webView=findViewById(R.id.web);
        tv2.setOnClickListener(this);
        tv1.setOnClickListener(this);
        tv3.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv1:
                RequestParams params=new RequestParams("https://www.baidu.com/s");
                x.http().get(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.e("TAG","请求成共="+result);
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {

                    }
                });
                break;
            case R.id.tv2:
                //加载图pain
                Glide.with(this).load(imgUrl).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(img);
                break;
            case R.id.tv3:
                Intent intent=new Intent(TestActivity.this,WebViewActivity.class);
                startActivity(intent);
//                webView.loadUrl("http://www.baidu.com");
//                WebSettings webSettings=webView.getSettings();
//                webSettings.setJavaScriptEnabled(true);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//                }
                //WebView加载页面优先使用缓存加载
//                webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//                webView.setWebViewClient(new WebViewClient(){
//                    @Override
//                    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//                        super.onReceivedSslError(view, handler, error);
//                        Log.e("TAG","error错误=="+error);
//                        handler.proceed();
//                    }
//
//                    @Override
//                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                        view.loadUrl(url);
//                        Log.e("TAG","shouldOverrideUrlLoading=url=="+url);
//                        return true;
//                    }
//
//                    @Override
//                    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
////                        view.loadUrl("http://www.baidu.com");
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
////                            view.loadUrl("http://www.baidu.com");
//                            if(isUrl(request.getUrl().toString())){
//                               view.loadUrl(request.getUrl().toString());
//                            }
//                            Log.e("TAG","路径=="+request.getUrl().toString());
//                        } else {
//                            view.loadUrl(request.toString());
//                            Log.e("TAG","路径2222222=="+request.toString());
//                        }
//                        return true;
//                    }
//                });
                break;
        }
    }
    public static boolean isUrl (String pInput) {
        if(pInput == null){
            return false;
        }
        String regEx = "^(http|https|ftp)//://([a-zA-Z0-9//.//-]+(//:[a-zA-"
                + "Z0-9//.&%//$//-]+)*@)?((25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{"
                + "2}|[1-9]{1}[0-9]{1}|[1-9])//.(25[0-5]|2[0-4][0-9]|[0-1]{1}"
                + "[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)//.(25[0-5]|2[0-4][0-9]|"
                + "[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)//.(25[0-5]|2[0-"
                + "4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])|([a-zA-Z0"
                + "-9//-]+//.)*[a-zA-Z0-9//-]+//.[a-zA-Z]{2,4})(//:[0-9]+)?(/"
                + "[^/][a-zA-Z0-9//.//,//?//'///////+&%//$//=~_//-@]*)*$";
        Pattern p = Pattern.compile(regEx);
        Matcher matcher = p.matcher(pInput);
        return matcher.matches();
    }
}
