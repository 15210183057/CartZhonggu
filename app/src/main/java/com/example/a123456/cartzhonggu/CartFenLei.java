package com.example.a123456.cartzhonggu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.a123456.cartzhonggu.R;

import java.util.ArrayList;
import java.util.List;

import adapter.CartFenLeiAdapter;
import base.BaseActivity;
import bean.CartFenleiBean;
import utils.Mydialog;

public class CartFenLei extends BaseActivity implements View.OnClickListener,AdapterView.OnItemClickListener{
    private ListView lv;
    private Button btn_okmodel;
    List<CartFenleiBean>list=new ArrayList<CartFenleiBean>();
    CartFenLeiAdapter adapter;
    RelativeLayout relative3_newFragment;
    TextView tv3_newFragment,img_left;
    String CartFenleiID;
    Mydialog mydialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_fen_lei);
        initView();
    }

    private void initView() {
        img_left=findViewById(R.id.img_left);
        lv=findViewById(R.id.lv1);
        btn_okmodel=findViewById(R.id.btn_okmodel);
        relative3_newFragment=findViewById(R.id.relative3_newFragment);
        tv3_newFragment=findViewById(R.id.tv3_newFragment);

        btn_okmodel.setOnClickListener(this);
        relative3_newFragment.setOnClickListener(this);
        lv.setOnItemClickListener(this);
        img_left.setOnClickListener(this);
        getData();
    }
    private void getData(){
//        1=大厅车辆 ，2=市场车辆，3=商户自用车，4=新车登记商户卡车辆，5=职工车辆
        CartFenleiBean cartFenleiBean=new CartFenleiBean();
        cartFenleiBean.fenleiName="大厅车辆";
        cartFenleiBean.fenleiName_id="1";
        CartFenleiBean cartFenleiBean2=new CartFenleiBean();
        cartFenleiBean2.fenleiName="市场车辆";
        cartFenleiBean2.fenleiName_id="2";
        CartFenleiBean cartFenleiBean3=new CartFenleiBean();
        cartFenleiBean3.fenleiName="商户自用车";
        cartFenleiBean3.fenleiName_id="3";
        CartFenleiBean cartFenleiBean4=new CartFenleiBean();
        cartFenleiBean4.fenleiName="新车登记商户卡车辆";
        cartFenleiBean4.fenleiName_id="4";
        CartFenleiBean cartFenleiBean5=new CartFenleiBean();
        cartFenleiBean5.fenleiName="职工车辆";
        cartFenleiBean5.fenleiName_id="5";
        list.add(cartFenleiBean);
        list.add(cartFenleiBean2);
        list.add(cartFenleiBean3);
        list.add(cartFenleiBean4);
        list.add(cartFenleiBean5);
        adapter=new CartFenLeiAdapter(this,list);
        lv.setAdapter(adapter);
    }
    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btn_okmodel){
            //发送广播
            Intent intent= new Intent();
            intent.setAction("cartfenlei");
            intent.putExtra("fenleiname",tv3_newFragment.getText().toString());
            intent.putExtra("fenleiID",CartFenleiID);
            sendBroadcast(intent);
            finish();
        }
        if(view.getId()==R.id.relative3_newFragment){
            btn_okmodel.setVisibility(View.GONE);
            lv.setVisibility(View.VISIBLE);
        }
        if(view.getId()==R.id.img_left){
            finish();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(adapterView.getId()==R.id.lv1){
            tv3_newFragment.setText(list.get(i).fenleiName);
            CartFenleiID=list.get(i).fenleiName_id.toString();
            btn_okmodel.setVisibility(View.VISIBLE);
            lv.setVisibility(View.GONE);
        }
    }
}
