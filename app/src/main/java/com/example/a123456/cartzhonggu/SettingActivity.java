package com.example.a123456.cartzhonggu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.a123456.cartzhonggu.LoginActivity;
import com.example.a123456.cartzhonggu.R;

import application.MyApplication;
import base.BaseActivity;
import utils.AppUtils;
import utils.SharedUtils;

public class SettingActivity extends BaseActivity implements View.OnClickListener{
    private ImageView img_back;
    private RelativeLayout relative1,relative11,relative2,relative22,relative3,relative33;
    private TextView tv_tuichu,tv_banben;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
    }

    private void initView() {
        img_back=findViewById(R.id.img_back);

        relative1=findViewById(R.id.relative1);
        relative11=findViewById(R.id.relative11);
        relative2=findViewById(R.id.relative2);
        relative22=findViewById(R.id.relative22);
        relative3=findViewById(R.id.relative3);
        relative33=findViewById(R.id.relative33);
        tv_tuichu=findViewById(R.id.tv_tuichu);
        tv_banben=findViewById(R.id.tv_banben);
        tv_banben.setText("app版本为  v"+ AppUtils.getVersionCode(SettingActivity.this)+".8");
         img_back.setOnClickListener(this);
         relative1.setOnClickListener(this);
        relative11.setOnClickListener(this);
        relative2.setOnClickListener(this);
        relative22.setOnClickListener(this);
        relative3.setOnClickListener(this);
        relative33.setOnClickListener(this);
        tv_tuichu.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_back:
                finish();
                break;
            case R.id.relative1:
                if(relative1.getVisibility()==View.VISIBLE){
                    relative1.setVisibility(View.GONE);
                    relative11.setVisibility(View.VISIBLE);
                }else{
                    relative1.setVisibility(View.VISIBLE);
                    relative11.setVisibility(View.GONE);
                }
                break;
            case R.id.relative2:
                if(relative2.getVisibility()==View.VISIBLE){
                    relative2.setVisibility(View.GONE);
                    relative22.setVisibility(View.VISIBLE);
                }else{
                    relative2.setVisibility(View.VISIBLE);
                    relative22.setVisibility(View.GONE);
                }
                break;
            case R.id.relative3:
                if(relative3.getVisibility()==View.VISIBLE){
                    relative3.setVisibility(View.GONE);
                    relative33.setVisibility(View.VISIBLE);
                }else{
                    relative3.setVisibility(View.VISIBLE);
                    relative33.setVisibility(View.GONE);
                }
                break;
            case R.id.relative11:
                if(relative11.getVisibility()==View.VISIBLE){
                    relative11.setVisibility(View.GONE);
                    relative1.setVisibility(View.VISIBLE);
                }else{
                    relative11.setVisibility(View.VISIBLE);
                    relative1.setVisibility(View.GONE);
                }
                break;
            case R.id.relative22:
                if(relative22.getVisibility()==View.VISIBLE){
                    relative22.setVisibility(View.GONE);
                    relative2.setVisibility(View.VISIBLE);
                }else{
                    relative22.setVisibility(View.VISIBLE);
                    relative2.setVisibility(View.GONE);
                }
                break;
            case R.id.relative33:
                if(relative33.getVisibility()==View.VISIBLE){
                    relative33.setVisibility(View.GONE);
                    relative3.setVisibility(View.VISIBLE);
                }else{
                    relative33.setVisibility(View.VISIBLE);
                    relative3.setVisibility(View.GONE);
                }
                break;
            case R.id.tv_tuichu:
                //Toast.makeText(this,"推出");
//                Intent intent=new Intent();
//                intent.setAction("closeSetting");
//                sendBroadcast(intent);
//                Log.e("TAG","发送广播");
                //推出登陆状态
                SharedUtils sharedUtils=new SharedUtils();
                sharedUtils.saveXML(MyApplication.usermsg,"username","",this);
                sharedUtils.saveXML(MyApplication.usermsg,"password","",this);
                sharedUtils.saveXML(MyApplication.usermsg,"groupids","",this);
                sharedUtils.saveXML(MyApplication.usermsg,"userid","",this);
                Intent intent2=new Intent();
                intent2.setAction("aogo");
                sendBroadcast(intent2);
                Intent intent=new Intent(this,LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
