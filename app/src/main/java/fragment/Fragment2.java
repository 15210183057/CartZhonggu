package fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a123456.cartzhonggu.R;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.xutils.common.util.KeyValue;
import org.xutils.http.RequestParams;
import org.xutils.http.body.MultipartBody;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import adapter.MyLvFrag2Adapter;
import application.MyApplication;
import bean.Bran;
import bean.BuCartListBean;
import bean.CarBean;
import utils.MyDBUtils;
import utils.SharedUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment2 extends Fragment implements View.OnClickListener,AdapterView.OnItemClickListener, MyLvFrag2Adapter.SelectCallBack{
    private View view;
    private TextView tv_topcenter,img_topleft,img_topright,tv_f2_null;
    private ImageView img_f2_center;
    RefreshLayout refreshLayout;
    ListView lv;
    private List<CarBean> list;
    private MyLvFrag2Adapter adapter;
    private int count;
    List<Bran>listBeans;
    MyDBUtils utils;
    public Fragment2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        upDate();
        listBeans=new ArrayList<Bran>();
        utils=new MyDBUtils(getContext());
        view=inflater.inflate(R.layout.fragment_fragment2, container, false);
        img_topleft=view.findViewById(R.id.img_left);
        img_topright=view.findViewById(R.id.img_right);
        tv_topcenter=view.findViewById(R.id.tv_center);

//        img_topleft.setImageResource(R.mipmap.qx);
        tv_topcenter.setText("待上传车源");
//        img_topright.setImageResource(R.mipmap.tj);
        img_f2_center=view.findViewById(R.id.img_f2_center);
        tv_f2_null=view.findViewById(R.id.tv_f2_null);
        img_topleft.setOnClickListener(this);
        img_topright.setOnClickListener(this);

        initView();
        return view;
    }


    private void initView() {
        setDate();
        if(listBeans.size()==0){
            img_f2_center.setVisibility(View.VISIBLE);
            tv_f2_null.setVisibility(View.VISIBLE);
        }else{
            img_f2_center.setVisibility(View.GONE);
            tv_f2_null.setVisibility(View.GONE);
        }
        refreshLayout = (RefreshLayout)view.findViewById(R.id.refreshLayout);

        lv=view.findViewById(R.id.lv);
        lv.setOnItemClickListener(this);
        if(listBeans!=null) {
            adapter = new MyLvFrag2Adapter(getActivity(), listBeans);
            adapter.setCall(this);
            lv.setAdapter(adapter);
            Log.e("TAG","initVIew=="+adapter+"list=="+listBeans);
        }
//        refreshLayout.setEnableAutoLoadmore(true);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000);
                Log.e("TAG","上拉刷新");
//                for(int i=count;i<=count+20;i++){
//                    list.add("第+"+i+"条数据");
//                    adapter.notifyDataSetChanged();
//                    count=i;
//                }
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {

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
//                        adapter.notifyDataSetChanged();
                    }
                },3000);

                refreshlayout.finishLoadmore(2000);
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean isVisibleToUser) {
        Log.e("TAG","onHiddenChanged==="+isVisibleToUser);
        setDate();
        if(listBeans!=null){
            if(listBeans.size()>0) {
                img_f2_center.setVisibility(View.GONE);
                tv_f2_null.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }else{
                img_f2_center.setVisibility(View.VISIBLE);
                tv_f2_null.setVisibility(View.VISIBLE);
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    //设置数据源
    private void setDate() {
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
        //读取本地数据
//        SharedUtils sharedUtils=new SharedUtils();
//        String str=sharedUtils.readXML(MyApplication.cartlistmsg,"count",getActivity());
//        int size=0;
//        if(!TextUtils.isEmpty(str)) {
//           size  = Integer.parseInt(str);
//        }
//        listBeans.clear();
//        for(int i=0;i<size;i++) {
//           BuCartListBean buCartListBean = new BuCartListBean();
//           String posion=sharedUtils.readXML(MyApplication.cartlistmsg,"posion"+i,getActivity());
//           Log.e("TAG","posion=="+posion);
//           buCartListBean.vin = sharedUtils.readXML(MyApplication.cartlistmsg, "vin" + i, getActivity());
//           buCartListBean.cardType = sharedUtils.readXML(MyApplication.cartlistmsg, "cardType" + i, getActivity());
//           buCartListBean.name = sharedUtils.readXML(MyApplication.cartlistmsg, "name" +i , getActivity());
//           buCartListBean.licensePlate = sharedUtils.readXML(MyApplication.cartlistmsg, "licensePlate" + i, getActivity());
//           if(!buCartListBean.vin.isEmpty()
//                         && !buCartListBean.cardType.isEmpty()
//                            &&!buCartListBean.name.isEmpty()
//                            &&!buCartListBean.licensePlate.isEmpty()){
//               listBeans.add(buCartListBean);
//           }else{
////               Log.e("TAG","为空");
//           }
//       }
        listBeans.clear();
        listBeans .addAll(utils.chaXun());
        for(int i=0;i<listBeans.size();i++){
            Log.e("TAG","itemid=="+listBeans.get(i).itemid);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(getContext(),"点击第+"+i+"条数据",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void call(int i) {
        Log.e("TAG","Flag=="+listBeans.get(i).Flag);
        if(listBeans.get(i).Flag){
            Log.e("TAG","点击Call");
            listBeans.get(i).Flag=false;
            img_topleft.setText("全选");
        }else{
            listBeans.get(i).Flag=true;
            img_topleft.setText("取消");
        }
        Log.e("TAG","最后提交=="+listBeans.get(i).Flag);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_left:
                if(img_topleft.getText().toString().equals("全选")){
                    img_topleft.setText("取消");
                    for(int i=0;i<listBeans.size();i++){
                        listBeans.get(i).Flag=true;
                        adapter.notifyDataSetChanged();
                    }
                }else if(img_topleft.getText().toString().equals("取消")){
                    img_topleft.setText("全选");
                    for(int i=0;i<listBeans.size();i++){
                        listBeans.get(i).Flag=false;
                    }
                    adapter.notifyDataSetChanged();
                }
                break;
            case R.id.img_right:
                //点击提交
                for(int i=0;i<listBeans.size();i++){
                    if(listBeans.get(i).Flag){
                       Log.e("TAG", listBeans.get(i).itemid);
                       utils.setDelete(listBeans.get(i).itemid);
                    }
                }
                setDate();
                adapter.notifyDataSetChanged();
                break;
        }
    }
    private void upDate(){
        RequestParams requestParams=new RequestParams();
        List<KeyValue>list=new ArrayList<>();
        for(int i=0;i<5;i++){
            list.add(new KeyValue("canshu","item="+i));
        }
//        requestParams.addBodyParameter("",list.toString());
        requestParams.setAutoResume(true);

        MultipartBody body=new MultipartBody(list,"UTF-8");
        requestParams.setRequestBody(body);
        requestParams.setMultipart(true);
        Log.e("TAG","上传惨淡--"+requestParams.getBodyParams());
//        MultipartBody multipartBody=new MultipartBody(new ArrayList<KeyValue>(),"utf-8");
//        requestParams.setRequestBody(multipartBody);
//        requestParams.addParameter("",new ArrayList<String>());
    }
}
