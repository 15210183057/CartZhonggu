package com.example.a123456.cartzhonggu;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import base.BaseActivity;
import bean.MyNewUpdate;
import fragment.Fragment1;
import fragment.Fragment2;
import fragment.Fragment3;
import fragment.Fragment4;
import fragment.newFragment;

public class FrameActivity extends BaseActivity implements View.OnClickListener{
    private RelativeLayout TopView_Bottem1,TopView_Bottem2,TopView_Bottem3,TopView_Bottem4;
    private LinearLayout TopView_FragmentGroup;
    private ImageView img1,img2,img3,img4;
    private TextView tv1,tv2,tv3,tv4;
    private Fragment fragment1, fragment2,fragment3,fragment4,newfragment;
    private FragmentManager fragmentManager;
    FragmentTransaction ft;
    MyBroadcastReceiver myBroadcastReceiver;
    //记录用户首次点击返回键的时间
    private long firstTime = 0;
    String show;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_frame);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//        if (getSupportActionBar() != null){
//            getSupportActionBar().hide();
//        }
        fragmentManager=getSupportFragmentManager();
        initView();
        setOnClick();
        MyRegistReciver();
        setPermissions();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("TAG","这里走吗？？？？？？？？？？？？？？");
    }

    private void setOnClick() {
        TopView_Bottem1.setOnClickListener(this);
        TopView_Bottem2.setOnClickListener(this);
        TopView_Bottem3.setOnClickListener(this);
        TopView_Bottem4.setOnClickListener(this);
    }

    private void initView() {
        TopView_FragmentGroup=findViewById(R.id.TopView_FragmentGroup);
        TopView_Bottem1=findViewById(R.id.TopView_Bottem1);
        TopView_Bottem2=findViewById(R.id.TopView_Bottem2);
        TopView_Bottem3=findViewById(R.id.TopView_Bottem3);
        TopView_Bottem4=findViewById(R.id.TopView_Bottem4);
        img1=findViewById(R.id.Img1);
        img2=findViewById(R.id.Img2);
        img3=findViewById(R.id.Img3);
        img4=findViewById(R.id.Img4);
        tv1=findViewById(R.id.Tv1);
        tv2=findViewById(R.id.Tv2);
        tv3=findViewById(R.id.Tv3);
        tv4=findViewById(R.id.Tv4);


       // 默认显示第一个fragment
      FragmentTransaction ft=fragmentManager.beginTransaction();
      tv1.setTextColor(Color.WHITE);
      img1.setImageResource(R.mipmap.banyuan_c);
        if(fragment1==null){
            fragment1=new Fragment1();
            ft.add(R.id.TopView_FragmentGroup,fragment1);
        }else{
            ft.show(fragment1);
        }
//        if(newfragment==null){
//            newfragment=new newFragment();
//            ft.add(R.id.TopView_FragmentGroup,newfragment);
//        }else{
//            ft.show(newfragment);
//        }
        ft.commit();

    }

    @Override
    public void onClick(View view) {
        HideFragement();
        clearBottem();
        ft=fragmentManager.beginTransaction();
        switch (view.getId()){
            case R.id.TopView_Bottem1:
                tv1.setTextColor(Color.WHITE);
                img1.setImageResource(R.mipmap.banyuan_c);
//                if(newfragment==null){
//                    newfragment=new newFragment();
//                    ft.add(R.id.TopView_FragmentGroup,newfragment);
//                }else{
//                    ft.show(newfragment);
//                }
                if(fragment1==null){
                    fragment1=new newFragment();
                    ft.add(R.id.TopView_FragmentGroup,newfragment);
                }else{
                    ft.show(fragment1);
                }
                break;
            case R.id.TopView_Bottem2:
                tv2.setTextColor(Color.WHITE);
                img2.setImageResource(R.mipmap.shangchuan_c);
                if(fragment2==null){
                    fragment2=new Fragment2();
                    ft.add(R.id.TopView_FragmentGroup,fragment2);
                }else{
                    ft.show(fragment2);
                }
                break;
            case R.id.TopView_Bottem3:
                tv3.setTextColor(Color.WHITE);
                img3.setImageResource(R.mipmap.yshangchua_c);
                if(fragment3==null){
                    fragment3=new Fragment3();
                    ft.add(R.id.TopView_FragmentGroup,fragment3);
                }else{
                    ft.show(fragment3);
                }
                break;
            case R.id.TopView_Bottem4:
                Log.e("TAG","点击了第四==");
                tv4.setTextColor(Color.WHITE);
                img4.setImageResource(R.mipmap.wode_c);
                if(fragment4==null){
                    fragment4=new Fragment4();
                    ft.add(R.id.TopView_FragmentGroup,fragment4);
                }else{
                    ft.show(fragment4);
                }
                break;
        }
        ft.commit();

    }
    //隐藏所有Fragement
    private void HideFragement(){
        ft=fragmentManager.beginTransaction();
        if(fragment1!=null){
            ft.hide(fragment1);
        }
        if(newfragment!=null){
            ft.hide(newfragment);
        }
        if(fragment2!=null){
            ft.hide(fragment2);
        }
        if(fragment3!=null){
            ft.hide(fragment3);
        }
        if(fragment4!=null){
            ft.hide(fragment4);
        }
        ft.commitAllowingStateLoss();
    }
    //所有底部图片以及textview编程灰色
    private void clearBottem(){
        img1.setImageResource(R.mipmap.banyuan);
        img2.setImageResource(R.mipmap.shangchuan);
        img3.setImageResource(R.mipmap.yshangchuan);
        img4.setImageResource(R.mipmap.wode);

        tv1.setTextColor(Color.BLACK);
        tv2.setTextColor(Color.BLACK);
        tv3.setTextColor(Color.BLACK);
        tv4.setTextColor(Color.BLACK);
    }
    //注册广播
    private void MyRegistReciver(){
      myBroadcastReceiver=new MyBroadcastReceiver();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("closeSetting");
        intentFilter.addAction("new");
        intentFilter.addAction("success");
        intentFilter.addAction("aogo");
        this.registerReceiver(myBroadcastReceiver,intentFilter);
    }
    //接受广播退出APP
    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("TAG","广播111---------"+intent.getAction().equals("new"));
            if(intent.getAction().equals("new")){
                Log.e("TAG","intent.getStringExtra(\"cartmodel\")====="+intent.getStringExtra("cartmodel"));
                MyNewUpdate.cartmodel=(intent.getStringExtra("cartmodel"));
                MyNewUpdate.Flag=(intent.getStringExtra("Flag"));
                MyNewUpdate.quyu=(intent.getStringExtra("quyu"));
                MyNewUpdate.time=(intent.getStringExtra("time"));
                MyNewUpdate.vinnum=(intent.getStringExtra("vinnum"));
                MyNewUpdate.licheng=intent.getStringExtra("licheng");
                MyNewUpdate.price=intent.getStringExtra("price");

//                intent.putExtra("modelName",list.get(i).modelName);
//                intent.putExtra("seriseName",list.get(i).seriseName);
//                intent.putExtra("brandName",list.get(i).brandName);
                MyNewUpdate.quyuID=intent.getStringExtra("quyuID");
                MyNewUpdate.seriseID=intent.getStringExtra("seriseID");
                MyNewUpdate.brandid=intent.getStringExtra("brandID");
                MyNewUpdate.modelID=intent.getStringExtra("modelID");
                MyNewUpdate.img1=intent.getStringExtra("img1");
                MyNewUpdate.img2=intent.getStringExtra("img2");
                MyNewUpdate.img3=intent.getStringExtra("img3");
                MyNewUpdate.ItemID=intent.getStringExtra("ID");
                MyNewUpdate.cartmodel=intent.getStringExtra("modelName");

                MyNewUpdate.tel=intent.getStringExtra("tel");
                MyNewUpdate.contact_name=intent.getStringExtra("contact_name");
                MyNewUpdate.isDaTing=intent.getStringExtra("isDaTing");
                MyNewUpdate.NameTelID=intent.getStringExtra("NameTelID");
                MyNewUpdate.currentID=intent.getStringExtra("currentID");
                //通知Fragment vin不可编辑
                Intent intent1=new Intent();
                intent1.setAction("update");
                sendBroadcast(intent1);
                Log.e("TAG","发送广播");
                clearBottem();
                HideFragement();
                FragmentTransaction ft=fragmentManager.beginTransaction();
                tv1.setTextColor(Color.WHITE);
                img1.setImageResource(R.mipmap.banyuan_c);
                if(newfragment==null){
                    newfragment=new newFragment();
                    ft.add(R.id.TopView_FragmentGroup,newfragment);
                }else{
                    ft.show(newfragment);
                }
                ft.commitAllowingStateLoss();

            }else if(intent.getAction().equals("closeSetting")){
                finish();
                System.exit(0);
            }else if(intent.getAction().equals("success")){
                clearBottem();
                String str=intent.getStringExtra("f");
                if(str.equals("1")){
                    Intent intent1=new Intent();
                    intent1.setAction("goon");
                    FrameActivity.this.sendBroadcast(intent1);
                    Log.e("TAG","发送默认显示广播");
                    FragmentTransaction ft=fragmentManager.beginTransaction();
                    tv1.setTextColor(Color.WHITE);
                    img1.setImageResource(R.mipmap.banyuan_c);
                    if(newfragment==null){
                        newfragment=new newFragment();
                        ft.add(R.id.TopView_FragmentGroup,newfragment);
                    }else{
                        ft.show(newfragment);
                    }
                    ft.commitAllowingStateLoss();
                }else{
                    HideFragement();
                    FragmentTransaction ft=fragmentManager.beginTransaction();
                    tv3.setTextColor(Color.WHITE);
                    img3.setImageResource(R.mipmap.yshangchua_c);
                    if(fragment3==null){
                        fragment3=new Fragment3();
                        ft.add(R.id.TopView_FragmentGroup,fragment3);
                    }else{
                        ft.show(fragment3);
                    }
                    ft.commitAllowingStateLoss();
                }
            }else if(intent.getAction().equals("aogo")){
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myBroadcastReceiver);
    }

    /**
     * 第二种办法
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {
                Toast.makeText(FrameActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                firstTime = secondTime;
                return true;
            } else {
                finish();
                Log.e("TAG","这里走了吗");
                System.exit(0);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    static final String[] PERMISSION = new String[]{
            Manifest.permission.READ_CONTACTS,// 写入权限
            Manifest.permission.READ_EXTERNAL_STORAGE,  //读取权限
            Manifest.permission.WRITE_CALL_LOG,        //读取设备信息
            Manifest.permission.CAMERA
    };
    /**
     * 设置Android6.0的权限申请
     */
    private void setPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            //Android 6.0申请权限
            ActivityCompat.requestPermissions(this,PERMISSION,1);
        }else{
        }
    }
}
