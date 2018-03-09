package com.example.a123456.cartzhonggu;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import adapter.MyCartModel;
import adapter.MyCartModel2;
import adapter.MyCartModel3;
import base.BaseActivity;
import bean.BrandBean;
import bean.ModelBean;
import bean.SeriseBean;
import jiekou.getInterface;
import utils.Mydialog;
import View.GetJsonUtils;
public class CartModelActivity extends BaseActivity implements View.OnClickListener,AdapterView.OnItemClickListener, MyCartModel.SelectCallBack{
    private RelativeLayout relative3_newFragment,relative4_newFragment,relative5_newFragment;
    private TextView tv3_newFragment,tv4_newFragment,tv5_newFragment;
    private ListView lv1,lv2,lv3;

    private List<BrandBean> brandList=new ArrayList<BrandBean>();//品牌
    private List<SeriseBean> SeriseList=new ArrayList<SeriseBean>();//车系
    private List<ModelBean> ModelList=new ArrayList<ModelBean>();//车型
    List <String>list=new ArrayList<String>();
    List <String>list2=new ArrayList<String>();
    List <String>list3=new ArrayList<String>();
    private String Serise_id,model_id;
    private Button btn_ok;
    private LinearLayout linear_model;
    private MyCartModel myCartModel;
    private MyCartModel2 myCartModel2;
    private MyCartModel3 myCartModel3;
    private String BrandID,SeriseID,ModelID;//需要回传的所有ID
    Mydialog mydialog;
    private TextView img_left;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_model);
        mydialog=new Mydialog(this,"正在获取信息，请稍等....");
        mydialog.show();
        initView();
        getBrand();
    }

    private void initView() {
        relative3_newFragment=findViewById(R.id.relative3_newFragment);
        relative4_newFragment=findViewById(R.id.relative4_newFragment);
        relative5_newFragment=findViewById(R.id.relative5_newFragment);
        tv3_newFragment=findViewById(R.id.tv3_newFragment);
        tv4_newFragment=findViewById(R.id.tv4_newFragment);
        tv5_newFragment=findViewById(R.id.tv5_newFragment);
        lv1=findViewById(R.id.lv1);
        lv2=findViewById(R.id.lv2);
        lv3=findViewById(R.id.lv3);
        linear_model=findViewById(R.id.linear_model);
        img_left=findViewById(R.id.img_left);
        img_left.setOnClickListener(this);
        relative3_newFragment.setOnClickListener(this);
        relative4_newFragment.setOnClickListener(this);
        relative5_newFragment.setOnClickListener(this);
        btn_ok=findViewById(R.id.btn_okmodel);
        lv1.setOnItemClickListener(this);
        lv2.setOnItemClickListener(this);
        lv3.setOnItemClickListener(this);
        btn_ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_left:
                finish();
                break;
            case R.id.relative3_newFragment:
                Log.e("TAG","list=="+list.size());
                btn_ok.setVisibility(View.GONE);
                linear_model.setVisibility(View.VISIBLE);
                myCartModel=new MyCartModel(CartModelActivity.this,brandList);
                myCartModel.setCall(this);
                lv1.setAdapter(myCartModel);
                break;
            case R.id.relative4_newFragment:
                break;
            case R.id.relative5_newFragment:
                break;
            case R.id.btn_okmodel:
                if(!TextUtils.isEmpty(tv3_newFragment.getText().toString())
                        &&!TextUtils.isEmpty(tv4_newFragment.getText().toString())
                        &&!TextUtils.isEmpty(tv5_newFragment.getText().toString())){
                    Intent intent=new Intent();
                    intent.setAction("cartmodel");
                    intent.putExtra("brand",tv3_newFragment.getText().toString());
                    intent.putExtra("serise",tv4_newFragment.getText().toString());
                    intent.putExtra("model",tv5_newFragment.getText().toString());
                    intent.putExtra("barndID",BrandID);
                    intent.putExtra("seriseID",SeriseID);
                    intent.putExtra("modelID",ModelID);
//                    intent.putExtra("brandID",brandList.get())
                    sendBroadcast(intent);
                    Log.e("TAG","发送BrandID="+BrandID+"=seriseID="+SeriseID+"=modelID="+ModelID);
                    finish();
                }
                break;
        }
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()){
            case R.id.lv1:
                for(int h=0;h<brandList.size();h++){
                    if(i!=h){
                        brandList.get(h).Flag=false;
                    }
                }
                brandList.get(i).Flag=!brandList.get(i).Flag;
                myCartModel.notifyDataSetChanged();
                BrandID=brandList.get(i).brand_id;
                tv3_newFragment.setText(brandList.get(i).brand_name);
                Serise_id=brandList.get(i).brand_id;
                getSerise(Serise_id);

                break;
            case R.id.lv2:
                for(int h=0;h<SeriseList.size();h++){
                    if(i!=h){
                        SeriseList.get(h).Flag=false;
                    }
                }
                SeriseList.get(i).Flag=!SeriseList.get(i).Flag;
                myCartModel2.notifyDataSetChanged();

                SeriseID=SeriseList.get(i).serise_id;
                tv4_newFragment.setText(SeriseList.get(i).serise.trim());
                model_id=SeriseList.get(i).serise_id;
                getModel(model_id);
                break;
            case R.id.lv3:
                for(int h=0;h<ModelList.size();h++){
                    if(i!=h){
                        ModelList.get(h).Flag=false;
                    }
                }
                ModelList.get(i).Flag=!ModelList.get(i).Flag;
                myCartModel3.notifyDataSetChanged();


                ModelID=ModelList.get(i).model_id;
                tv5_newFragment.setText(ModelList.get(i).model_name);
                linear_model.setVisibility(View.GONE);
                btn_ok.setVisibility(View.VISIBLE);
              SeriseList.clear();
              ModelList.clear();
                break;
        }
    }
    //获取品牌
    private void getBrand(){
        RequestParams params=new RequestParams(getInterface.getBrand);
        Log.e("TAG","param=="+params);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("TAG","resutl=="+result);
                mydialog.dismiss();
                brandList=(GetJsonUtils.getBrand(CartModelActivity.this,result));
                list.clear();
                for(int i=0;i<brandList.size();i++){
                    list.add(brandList.get(i).brand_name);
                }

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
    }
    //获取车系
    private void getSerise(final String Serise_id){
        RequestParams params=new RequestParams(getInterface.getSeries);
        params.addBodyParameter("id",Serise_id);
        Log.e("TAG","车系p-"+params);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("TAG","result=="+result);
                SeriseList= GetJsonUtils.getSerise(CartModelActivity.this,result);
                Log.e("TAG","seriseList=="+SeriseList.size());
                list2.clear();
                for(int i=0;i<SeriseList.size();i++){
                    list2.add(SeriseList.get(i).serise);
                }
                Log.e("TAG","list2========================"+list2.size());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                Log.e("TAG","onFilse=="+list2.size());
                myCartModel2=new MyCartModel2(CartModelActivity.this,SeriseList);
                lv2.setAdapter(myCartModel2);
            }
        });
    }
    //获取车型
    private void getModel(final String model_id){
        RequestParams params=new RequestParams(getInterface.getModels);
        params.addBodyParameter("id",model_id);
        Log.e("TAG","params=="+params);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("TAG","result=="+result);
                ModelList= GetJsonUtils.getModel(CartModelActivity.this,result);
                Log.e("TAG","Model=="+ModelList.size());
                list3.clear();
                for(int i=0;i<ModelList.size();i++){
                    list3.add(ModelList.get(i).model_name);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                Log.e("TAG","list3=="+list3.size());
                myCartModel3=new MyCartModel3(CartModelActivity.this,ModelList);
                lv3.setAdapter(myCartModel3);
            }
        });
    }

    @Override
    public void callcolor(int i) {
//        Log.e("TAg","么次都走吗");
//        for(int h=0;h<brandList.size();h++){
//            if(h==i){
//                brandList.get(i).Flag=true;
//                myCartModel.notifyDataSetChanged();
//            }
//        }
        brandList.get(i).Flag=true;
        myCartModel.notifyDataSetChanged();
    }
}
