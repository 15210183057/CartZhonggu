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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a123456.cartzhonggu.R;

import java.util.ArrayList;
import java.util.List;

import application.MyApplication;
import base.BaseActivity;
import bean.BuCartListBean;
import utils.SharedUtils;

public class CartListInfoMsgActivity extends BaseActivity implements View.OnClickListener{
    private ImageView img_msg_back;
    private TextView tv_msg_title;
    private TextView tv_msg_save;
    private WebView webView;
    private ProgressBar progressBar;
    String UrlStr="http://www.baidu.com";
    public int count ;//记录保存多少条数据
    private List<BuCartListBean> list=new ArrayList<BuCartListBean>();
    private List<BuCartListBean> list2=new ArrayList<BuCartListBean>();
    SharedUtils utils = new SharedUtils();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cartlistinfomsg);
//        utils.saveXML(MyApplication.cartlistmsg,"count",count+"",CartListInfoMsgActivity.this);
        if(!utils.readXML(MyApplication.cartlistmsg,"count",this).isEmpty()) {
            count = Integer.parseInt(utils.readXML(MyApplication.cartlistmsg, "count", this));
        }
        initView();
        getData();
    }
    String posion;
    private void getData() {
        BuCartListBean buCartListBean=new BuCartListBean();

        posion=getIntent().getStringExtra("i");
        buCartListBean.vin=(getIntent().getStringExtra("vin"+posion));
        buCartListBean.name=(getIntent().getStringExtra("name"+posion));
        buCartListBean.licensePlate=(getIntent().getStringExtra("licensePlate"+posion));
        buCartListBean.cardType=(getIntent().getStringExtra("cardType"+posion));

        list.add(buCartListBean);
    }

    private void initView() {
        img_msg_back=findViewById(R.id.img_msg_back);
        tv_msg_title=findViewById(R.id.tv_msg_title);
        tv_msg_save=findViewById(R.id.tv_msg_save);

        img_msg_back.setOnClickListener(this);
        tv_msg_save.setOnClickListener(this);

        webView=findViewById(R.id.web_msg);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_msg_back:
                finish();
                break;
            case R.id.tv_msg_save:
                count++;
                Log.e("TAG","count条数=="+count);
                    utils.saveXML(MyApplication.cartlistmsg,"vin"+posion,list.get(0).vin,CartListInfoMsgActivity.this);
                    utils.saveXML(MyApplication.cartlistmsg,"name"+posion,list.get(0).name,CartListInfoMsgActivity.this);
                    utils.saveXML(MyApplication.cartlistmsg,"licensePlate"+posion,list.get(0).licensePlate,CartListInfoMsgActivity.this);
                    utils.saveXML(MyApplication.cartlistmsg,"cardType"+posion,list.get(0).cardType,CartListInfoMsgActivity.this);
                    //当前key值的posion
                Log.e("TAG","存入的所有posion"+"posion=="+posion+"=="+count);
                    utils.saveXML(MyApplication.cartlistmsg,"posion"+count,posion,CartListInfoMsgActivity.this);
                    //一共保存条数count
                    utils.saveXML(MyApplication.cartlistmsg,"count",count+"",CartListInfoMsgActivity.this);
                Toast.makeText(CartListInfoMsgActivity.this,"保存",Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }

}
