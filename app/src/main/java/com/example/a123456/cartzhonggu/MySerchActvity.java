package com.example.a123456.cartzhonggu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import base.BaseActivity;
import bean.CartMsgBean;
import bean.NameAndTel;
import jiekou.getInterface;
import utils.Mydialog;
import utils.addCartAlert;
import View.GetJsonUtils;
public class MySerchActvity extends BaseActivity {
private SearchView search;
    private List<CartMsgBean>cartList=new ArrayList<CartMsgBean>();
private List<String>list=new ArrayList<String>();
    private List<String>listID=new ArrayList<String>();
    private List<String>listName=new ArrayList<String>();
    private List<String>listTel=new ArrayList<String>();
    private List<String>listNameTelID=new ArrayList<String>();
private List<String>findList=new ArrayList<String>();
private List<String>findListID=new ArrayList<String>();
private List<String>findListNameTel=new ArrayList<String>();
private List<String>findListNameTelID=new ArrayList<String>();
private ListView listView;
private ArrayAdapter adapter,findAdapter;
Mydialog mydialog;
String str="";
    public static  List<List<NameAndTel>>nameAndTelList=new ArrayList<List<NameAndTel>>();
private String getIntentStr;
    MyBroadcastReceiver myBroad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_serch_actvity);
        registMy();
        mydialog=new Mydialog(this,"正在获取车商信息请稍后");
        mydialog.show();
        str=getIntent().getStringExtra("f3");
        if(getWindow().getAttributes().softInputMode== WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED){
            Log.e("TAG","打开");
        }else{
            Log.e("TAG","失败");
        }
        initView();
    }

    private void initView() {
//        setData();
        getData();
        search=findViewById(R.id.search);
        listView=findViewById(R.id.lv);

        search.setIconifiedByDefault(false);
        search.setSubmitButtonEnabled(true);
//        search.setSubmitButtonEnabled();
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.e("TAG","s=="+s);
                if(TextUtils.isEmpty(s)){
                    listView.setAdapter(adapter);
                    Log.e("TAG","请输入查找内容");
                    Toast.makeText(MySerchActvity.this, "请输入查找内容！", Toast.LENGTH_SHORT).show();
                }else{
                    findList.clear();
                    findListNameTel.clear();
                    findListID.clear();
                    nameAndTelList.clear();
                    for(int i=0;i<list.size();i++){
                        if(list.get(i).equals(s)){
                            findList.add(list.get(i));
                            findListID.add(listID.get(i));
                            findListNameTel.add(listTel.get(i)+"&"+listName.get(i)+"&"+listNameTelID.get(i));
                            nameAndTelList.add(cartList.get(i).list);
                            break;
                        }
                    }
                    if(findList.size()==0){
                        Log.e("TAG","查找的商品不在列表中");
                        if(TextUtils.isEmpty(str)){
                            addCartAlert addCartAlert=new addCartAlert(MySerchActvity.this);
                            addCartAlert.show();

                        }else{
                            Toast.makeText(MySerchActvity.this, "查找的商品不在列表中", Toast.LENGTH_SHORT).show();
                        }
                        if(adapter!=null) {
                            adapter.notifyDataSetChanged();
                        }
                    }else{
                        Log.e("TAG","查找成功");
                        Toast.makeText(MySerchActvity.this, "查找成功", Toast.LENGTH_SHORT).show();
                        adapter=new ArrayAdapter(MySerchActvity.this,R.layout.item,R.id.tvitem_xiala,findList);
                        listView.setAdapter(findAdapter);
                    }
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.e("TAG","onQueryTextChange=="+s);
                if(TextUtils.isEmpty(s)){
                    adapter=new ArrayAdapter(MySerchActvity.this,R.layout.item,R.id.tvitem_xiala,list);
//                    adapter.notifyDataSetChanged();
                    listView.setAdapter(adapter);
                    findList.clear();
                    findListID.clear();
                    findListNameTel.clear();
                }else{
                    findList.clear();
                    findListID.clear();
                    findListNameTel.clear();
                    nameAndTelList.clear();
                    for(int i=0;i<list.size();i++){
                        Log.e("TAG","list.get(i).contains(s)=="+list.get(i).contains(s));
                        if(list.get(i).contains(s)){
                            findList.add(list.get(i));
                            findListID.add(listID.get(i));
                            findListNameTel.add(listTel.get(i)+"&"+listName.get(i)+"&"+listNameTelID.get(i));
                            Log.e("TAG","onQueryTextChangelist.get(i)=="+list.get(i)+"==ID为=="+listID.get(i));
                            nameAndTelList.add(cartList.get(i).list);
                        }
                    }
                    Log.e("TAG","TextUtils.isEmpty(str)"+TextUtils.isEmpty(str));
//                    if(findList.size()==0&&TextUtils.isEmpty(str)){
//                        addCartAlert addCartAlert=new addCartAlert(MySerchActvity.this);
//                        addCartAlert.show();
//                    }
                    adapter=new ArrayAdapter(MySerchActvity.this,R.layout.item,R.id.tvitem_xiala,findList);
                    adapter.notifyDataSetChanged();
                    listView.setAdapter(adapter);
                }
                return false;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent();
                if(!TextUtils.isEmpty(str)&&str.equals("f3")){
                    intent.setAction("f3");
                }else {
                    intent.setAction("quyu");
                }
                Log.e("TAG","size=="+findList.size()+"=="+findListID.size());
                if(findList.size()!=0&&findListID.size()!=0) {
                    Log.e("TAG","findListID.get(i).toString()="+i+"="+findList.get(i).toString()+"="+findListID.get(i).toString()+"="+findList.get(i).toString());
                    intent.putExtra("name", findList.get(i).toString());
                    intent.putExtra("ID", findListID.get(i).toString());
                    Log.e("TAG","sizeName=="+findList.size()+"sizeID=="+findListID.size()+"");
                    if(!TextUtils.isEmpty(findListNameTel.get(i))) {
                        intent.putExtra("tel", findListNameTel.get(i));
                    }
                }else{
                    intent.putExtra("name", list.get(i).toString());
                    intent.putExtra("ID", listID.get(i).toString());
                    if(!TextUtils.isEmpty(listNameTelID.get(i))){
//                        if(!TextUtils.isEmpty(listName.get(i))&&TextUtils.isEmpty(listTel.get(i))) {
//                            intent.putExtra("tel", "null"+listName.get(i).toString() + "&"+listNameTelID.get(i) );
//                        }
//                        else if(!TextUtils.isEmpty(listTel.get(i))&&TextUtils.isEmpty(listName.get(i))){
//                            intent.putExtra("tel", listTel.get(i) + "&null" );
//                        }
//                        else if(!TextUtils.isEmpty(listTel.get(i))&&!TextUtils.isEmpty(listName.get(i))){
//                            intent.putExtra("tel", listTel.get(i) + "&"+listName.get(i) );
//                        }else if(TextUtils.isEmpty(listTel.get(i))&&TextUtils.isEmpty(listName.get(i))){
//                            intent.putExtra("tel", listTel.get(i) + "&"+listName.get(i) );
//                        }
                        intent.putExtra("tel",listTel.get(i) + "&"+listName.get(i)+"&"+listNameTelID.get(i));
                    }

                }
                intent.putExtra("currentID",i+"");
                Log.e("TAG","currentid=="+i);
                sendBroadcast(intent);
                finish();
            }
        });
    }
    private void getData(){
        //：json=1&pagesize=10&where=blu=1 and groupid in(5,3,2) and status = 1
        RequestParams params=new RequestParams(getInterface.getA);
//        params.setMethod(HttpMethod.POST);
        params.setConnectTimeout(10000);
        params.addBodyParameter("json","1");
        params.addBodyParameter("pagesize","100");
        params.addBodyParameter("where","groupid in(2) and status=1");
        Log.e("TAG","params=="+params);
        Log.e("TAG","车商-=="+params.getParams("where"));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("TAG","reulst=="+result);
                mydialog.dismiss();
                cartList.clear();
                cartList= GetJsonUtils.getQuYu(MySerchActvity.this,result);
                Log.e("TAG","cartList=="+cartList.size());
                list.clear();
                nameAndTelList.clear();
                for(int i=0;i<cartList.size();i++){
                    list.add(cartList.get(i).cartmsgname);
                    listID.add(cartList.get(i).cartMsgId);
                    listName.add(cartList.get(i).merchant_name);
                    listTel.add(cartList.get(i).tel);
                    listNameTelID.add(cartList.get(i).ID);
                    List<NameAndTel> lis=cartList.get(i).list;
                    nameAndTelList.add(lis);
                    Log.e("TAG","lis=="+cartList.get(i).list.size());
//                    for(int h=0;h<lis.size();h++){
//                        Log.e("TAG","所有商户下的姓名和电话和ID=i="+i+"="+cartList.get(i).list.get(h).name+"="+lis.get(h).tel+"="+lis.get(h).id);
//                        NameAndTel nameAndTel=new NameAndTel();
//                        nameAndTel.tel=cartList.get(i).list.get(h).tel;
//                        nameAndTel.name=cartList.get(i).list.get(h).name;
//                        nameAndTel.id=cartList.get(i).list.get(h).id;
//                        nameAndTelList.add(nameAndTel);
//
//
//                    }
//                    listTel.add(cartList.get(i).list.get(i).tel);
//                    listName.add(cartList.get(i).list.get(i).name);
//                    Log.e("TAG","")
                    Log.e("TAG","nameAndTelList()=="+nameAndTelList.size());
                }
                if(list.size()>0) {
                    adapter = new ArrayAdapter(MySerchActvity.this, R.layout.item, R.id.tvitem_xiala, list);
                    listView.setAdapter(adapter);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.e("TAG","按下了back键   onBackPressed()");
        finish();
    }
    //注册广播
    private void registMy(){
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("mysearch");
        myBroad=new MyBroadcastReceiver();
        this.registerReceiver(myBroad,intentFilter);
    }
    public class MyBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("TAG","接受广播--"+intent.getAction().equals("mysearch"));

            if(intent.getAction().equals("mysearch")){
                MySerchActvity.this.finish();
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myBroad);
    }
}
