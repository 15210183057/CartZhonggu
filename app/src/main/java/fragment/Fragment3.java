package fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import View.GetJsonUtils;

import com.example.a123456.cartzhonggu.MySerchActvity;
import com.example.a123456.cartzhonggu.R;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import adapter.MyLvAdapter3;
import bean.BUCartListBeanNUm;
import bean.BuCartListBean;
import bean.UserBean;
import jiekou.getInterface;
import utils.Mydialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment3 extends Fragment implements AdapterView.OnItemClickListener,View.OnClickListener{

    private View view;
    private ImageView img_topleft;
    private TextView tv_topcenter,img_topright;

    RefreshLayout refreshLayout;
    ListView lv;
    private List<BuCartListBean> list;
    private MyLvAdapter3 adapter;
    private int count;
    private int i=1;//默认加载第一页数据
    Mydialog mydialog;
    TextView tv_quyu;
    String quyu_ID;
    Button btn_serach;
    boolean canlel=false;
    public Fragment3() {
        // Required empty public constructor
    }
    BroadcastReceiver my;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment'
        mydialog=new Mydialog(getContext(),"正在加载请稍后.....");
        mydialog.show();
        my=new MyBroadcastReceiver();
        //注册广播
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("delete");
        intentFilter.addAction("f3");
        getActivity().registerReceiver(my,intentFilter);
        list=new ArrayList<BuCartListBean>();

        getBuCartList(i);
        view=inflater.inflate(R.layout.fragment_fragment3, container, false);
        img_topleft=view.findViewById(R.id.img_left);
        img_topright=view.findViewById(R.id.img_right);
        tv_topcenter=view.findViewById(R.id.tv_center);

        tv_quyu=view.findViewById(R.id.tv_quyue);
        btn_serach=view.findViewById(R.id.btn_serach);
        img_topleft.setVisibility(View.GONE);
        tv_topcenter.setText("已上传车源");
//        img_topright.setVisibility(View.GONE);
        initView();

        Log.e("TAG","标题；"+tv_topcenter.getText().toString());
        tv_quyu.setOnClickListener(this);
        btn_serach.setOnClickListener(this);
        img_topright.setOnClickListener(this);
        return view;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(my);
    }

    private void initView() {
        setDate();
        refreshLayout = (RefreshLayout)view.findViewById(R.id.refreshLayout);
        lv=view.findViewById(R.id.lv);
        lv.setOnItemClickListener(this);
        adapter=new MyLvAdapter3(list,getActivity());
        lv.setAdapter(adapter);
//        refreshLayout.setEnableAutoLoadmore(false);
//        refreshLayout.setEnableRefresh(false);
        if(list!=null) {
            adapter = new MyLvAdapter3(list, getActivity());
            lv.setAdapter(adapter);
        }
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000);
                Log.e("TAG","上拉刷新");
//                tv_quyu.setText("");
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        if(i>1){
//                            i--;
                        list.clear();
                            getBuCartList(1);
//                        }
                    }
                },0);

            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(final RefreshLayout refreshlayout) {
                Log.e("TAG","上拉加载");
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        CarBean carBean=new CarBean();
//                        for(int i=count;i<=count+20;i++){
//                            carBean.tv_name="大众---"+i;
//                            carBean.tv_company_name="中古测试---"+i;
//                            carBean.tv_num1="12321sdfsfdsfsfs---"+i;
//                            carBean.tv_num2="进123rew---"+i;
//                            list.add(carBean);
//                        }
//                        count=list.size();

                        i++;
                        getBuCartList(i);
                        Log.e("TAG","list=="+list.size());
//                        if(!TextUtils.isEmpty(BUCartListBeanNUm.last_page)&&i<Integer.parseInt(BUCartListBeanNUm.last_page)) {
//                            i++;
//                            getBuCartList(i);
//                        }else{
//                            Toast.makeText(getContext(),"数据加载完毕",Toast.LENGTH_SHORT).show();
//                        }
                    }
                },0);
                refreshlayout.finishLoadmore(3000);
            }
        });
    }
    //设置数据源
    private void setDate(){
//        list=new ArrayList<CarBean>();
//        for(int i=0;i<20;i++){
//            CarBean carBean=new CarBean();
//            carBean.tv_name="大众---"+i;
//            carBean.tv_company_name="中古测试---"+i;
//            carBean.tv_num1="12321sdfsfdsfsfs---"+i;
//            carBean.tv_num2="进123rew---"+i;
//            list.add(carBean);
//            count=i;
//        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent=new Intent();
        intent.setAction("new");
        intent.putExtra("Flag","true");
        intent.putExtra("vinnum",list.get(i).vin);
        intent.putExtra("time",list.get(i).regTime);
        intent.putExtra("quyu",list.get(i).name);
        intent.putExtra("cartmodel",list.get(i).brandName+list.get(i).seriseName+list.get(i).modelName);
        intent.putExtra("licheng",list.get(i).mileage);
        intent.putExtra("price",list.get(i).price);
Log.e("TAG","list=="+list.get(i).price);
        intent.putExtra("quyuID",list.get(i).quyuID);
        intent.putExtra("brandID",list.get(i).brandid);
        intent.putExtra("seriseID",list.get(i).seriseID);
        Log.e("TAG","fragment=="+list.get(i).modelID);
        intent.putExtra("modelID",list.get(i).modelID);
        intent.putExtra("modelName",list.get(i).modelName);
        intent.putExtra("seriseName",list.get(i).seriseName);
        intent.putExtra("brandName",list.get(i).brandName);
        intent.putExtra("img1",list.get(i).img1);
        intent.putExtra("img2",list.get(i).img2);
        intent.putExtra("img3",list.get(i).img3);
        intent.putExtra("ID",list.get(i).ListID);

        intent.putExtra("tel",list.get(i).tel);
        intent.putExtra("contact_name",list.get(i).contact_name);
        intent.putExtra("isDaTing",list.get(i).isDaTing);
        intent.putExtra("NameTelID",list.get(i).NameTelID);
        intent.putExtra("currentID",i+"");
        getActivity().sendBroadcast(intent);
    }
    //网络请求，获取数据源,
    private void getBuCartList(int current_page){
        Log.e("TAG","page=="+current_page);

        RequestParams requestParams=new RequestParams(getInterface.getList);
        requestParams.addBodyParameter("userid",UserBean.id);
        requestParams.addBodyParameter("page",current_page+"");
        if(!TextUtils.isEmpty(quyu_ID)) {
            requestParams.addBodyParameter("merchantid", quyu_ID);
        }
        Log.e("TAG","requestParams接口拼接地址为=="+requestParams+"");
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("TAG","resulr=="+result);
                mydialog.dismiss();

                List<BuCartListBean>listBeans=new ArrayList<BuCartListBean>();
                listBeans.clear();
                listBeans= GetJsonUtils.getCartList(getActivity(),result);
//                listBeans= GetJsonUtils.getBuCartList(getActivity(),result);
                list.addAll(listBeans);
                if (list.size()==0){
                    Toast.makeText(getContext(),"没有符合该车商的车辆信息",Toast.LENGTH_SHORT).show();
                }else{

                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
//                if(!TextUtils.isEmpty(ex.getMessage())){
//
//                };
                if(mydialog.isShowing()){
                    mydialog.dismiss();
                }
                Toast.makeText(getContext(),"没有获取到信息",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

                Log.e("TAG","list=="+list.size());
                tv_topcenter.setText(list.size()+"");//设置title
                Log.e("TAG","title=="+BUCartListBeanNUm.total);
                if(list!=null) {
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
//        if(!hidden){
//            list.clear();
//            if(!mydialog.isShowing()){
//                mydialog.show();
//            }
//            getBuCartList(1);
//            i=1;
//            Log.e("TAG","显示11");
//
//        }
//        else{
//            Log.e("TAG","显示22");
//            tv_quyu.setText("按车商信息搜索");
//            quyu_ID="";
//            btn_serach.setVisibility(View.GONE);
//        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_quyue:
                Intent intentS = new Intent(getContext(), MySerchActvity.class);
                intentS.putExtra("f3","f3");
                startActivity(intentS);
                break;
            case R.id.btn_serach:
                //开始搜索
                if(!mydialog.isShowing()){
                    mydialog.show();
                }
                Log.e("TAG","开始搜索="+quyu_ID);
                list.clear();

                getBuCartList(1);
                 i=1;
                break;
            case R.id.img_right:
                if(!mydialog.isShowing()){
                    mydialog.show();
                }
                quyu_ID="";
                tv_quyu.setText("按车商信息搜索");
                i=1;
                list.clear();
                getBuCartList(1);
                break;
        }

    }

    public class MyBroadcastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("delete")) {
                getBuCartList(1);
            }
            if(intent.getAction().equals("f3")){
                String name=intent.getStringExtra("name");
                tv_quyu.setText(name);
                quyu_ID=intent.getStringExtra("ID");
                Log.e("TAG","quyu_ID=="+quyu_ID);
                if(!TextUtils.isEmpty(quyu_ID)){
                    btn_serach.setVisibility(View.VISIBLE);
                }else{
                    btn_serach.setVisibility(View.GONE);
                }
            }
        }
    }

}