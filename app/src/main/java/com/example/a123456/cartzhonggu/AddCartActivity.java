package com.example.a123456.cartzhonggu;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a123456.cartzhonggu.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import base.BaseActivity;
import jiekou.getInterface;
import utils.Mydialog;

public class AddCartActivity extends BaseActivity implements View.OnClickListener{
private EditText edt_addcart;
private Button btn_addcart;
private ImageView img_topleft,img_topright;
private TextView tv_topcenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cart);
        initView();
    }

    private void initView() {
        edt_addcart=findViewById(R.id.edt_addcart);
        btn_addcart=findViewById(R.id.btn_Addcart);
        img_topleft=findViewById(R.id.img_left);
        img_topright=findViewById(R.id.img_right);
        img_topleft.setVisibility(View.GONE);
        img_topright.setVisibility(View.GONE);
        tv_topcenter=findViewById(R.id.tv_center);
        tv_topcenter.setText("添加车商信息");
        btn_addcart.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        final String cartMeg=edt_addcart.getText().toString();
        //上传服务器
        final Mydialog mydialog=new Mydialog(this,"正在上传商户信息，请稍等...");
        mydialog.show();
        RequestParams params=new RequestParams(getInterface.UpdateCartName);
        params.addBodyParameter("groupid", "2");
        params.addBodyParameter("name",cartMeg);
        params.addBodyParameter("json","1");
        Log.e("TAG","上传商户地址=="+params);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                mydialog.dismiss();
                //{"status":1,"data":"275"}
                Log.e("TAG","上传商户结果--"+result);
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    String status=jsonObject.getString("status");
                    if(status.equals("1")) {
                        String data = jsonObject.getString("data");
                        Intent intent=new Intent();
                        intent.setAction("updataCart");
                        intent.putExtra("quyuName",cartMeg);
                        intent.putExtra("quyuID",data);
                        sendBroadcast(intent);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if(!TextUtils.isEmpty(ex.getMessage())){
                    mydialog.dismiss();
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
}
