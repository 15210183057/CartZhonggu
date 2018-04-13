package fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import com.example.a123456.cartzhonggu.BuMessageActivity;
import com.example.a123456.cartzhonggu.R;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import adapter.MyLvFrag2Adapter;
import bean.BeanFlag;
import bean.Bran;
import bean.BuCartListBean;
import bean.CarBean;
import bean.MyNewUpdate;
import bean.UserBean;
import bean.ZHFBean;
import bean.ZQBean;
import bean.ZQFBean;
import utils.MyDBUtils;
import jiekou.getInterface;
import utils.MySuccess;
import View.GetJsonUtils;
import utils.Mydialog;

import static android.content.Context.CONNECTIVITY_SERVICE;

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
    List<Bran>selectList;
    private List<List<Bran>>AllSelectList;

    MyDBUtils utils;
    private String selectPath1,selectPath2,selectPath3,selectPath4,selectPath5,selectPath6,selectPath7,selectPath8,selectPath9;
    private String zqfUrlPath,zfUrlPath,zhfUrlPath,img4UrlPath,img5UrlPath,img6UrlPath,img7UrlPath,img8UrlPath,img9UrlPath;
    int selectcount=0;//选中的条数
    Mydialog mydialog;
    ProgressDialog pro;
    private int RedConut=0;
   Mydialog successdialog;
    //    String quyuTelName;//车商信息对于的用户和电话
    public Fragment2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      successdialog =new Mydialog(getActivity(),"上传成功");

        pro =new ProgressDialog(getContext());
        pro.setTitle("请稍等...");
        pro.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pro.setCancelable(true);
        pro.setCanceledOnTouchOutside(false);
        listBeans=new ArrayList<Bran>();
        selectList=new ArrayList<>();
        AllSelectList=new ArrayList<List<Bran>>();
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
                RedConut=0;
                listBeans.clear();
                setDate();
                adapter.notifyDataSetChanged();
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
                            RedConut += 5;
                        setDate();
                        adapter.notifyDataSetChanged();
                    }
                },0);

                refreshlayout.finishLoadmore(2000);
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean isVisibleToUser) {
        Log.e("TAG","onHiddenChanged==="+isVisibleToUser);
        RedConut=0;
        if(isVisibleToUser){
            img_topleft.setText("全选");
        }
        listBeans.clear();
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
    int h=0;

    //设置数据源
    private void setDate() {

//        listBeans.clear();
//        listBeans .addAll(utils.chaXun());
        Log.e("TAG","RedConut=="+RedConut+"utils.chaXun().size()==="+utils.chaXun().size());
        for(int i=RedConut;i<RedConut+5;i++){
//            Log.e("TAG","itemid=="+listBeans.get(i).itemid);
            if(i<utils.chaXun().size()){
                Log.e("TAG","utils.chaXun().get(i)=="+utils.chaXun().get(i));
                listBeans.add(utils.chaXun().get(i));
            }
        }
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//        Toast.makeText(getContext(),"点击第+"+i+"条数据",Toast.LENGTH_SHORT).show();
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
        switch (view.getId()) {
            case R.id.img_left:
                if (img_topleft.getText().toString().equals("全选")) {
                    img_topleft.setText("取消");
                    for (int i = 0; i < listBeans.size(); i++) {
                        listBeans.get(i).Flag = true;
                        adapter.notifyDataSetChanged();
                    }
                } else if (img_topleft.getText().toString().equals("取消")) {
                    img_topleft.setText("全选");
                    for (int i = 0; i < listBeans.size(); i++) {
                        listBeans.get(i).Flag = false;
                    }
                    adapter.notifyDataSetChanged();
                }
                break;
            case R.id.img_right:
                //点击提交
                List<Integer>list=new ArrayList<>();
                selectList.clear();
                selectcount=0;
                for (int i = 0; i < listBeans.size(); i++) {
                    h = i;
                    Log.e("TAG", "OK==listSizeBean==h==" + h + "====" + listBeans.size());
                    if (listBeans.get(h).Flag) {
                        selectcount++;
                        selectList.add(listBeans.get(h));
                    }
                }
                if(selectcount==list.size()){
                    Toast.makeText(getActivity(),"请选择提交数据",Toast.LENGTH_SHORT).show();
                }else{
                    k=0;
                    pro.setMax(selectList.size()*1);
                    mydialog =new Mydialog(getActivity(),"无网络链接,请检出网络设置");
                    final ConnectivityManager manager = (ConnectivityManager)getActivity().getSystemService(CONNECTIVITY_SERVICE);
                    // 检查网络连接，如果无网络可用，就不需要进行连网操作等
                    NetworkInfo info = manager.getActiveNetworkInfo();
                    if (info == null || !manager.getBackgroundDataSetting()) {
                        Toast.makeText(getActivity(), "无网络链接,请检出网络设置",Toast.LENGTH_SHORT).show();
                        mydialog.show();
                        new Thread(){
                            @Override
                            public void run() {
                                super.run();
                                try {
                                    Thread.sleep(3000);
                                    mydialog.dismiss();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                    }else{
                        mydialog.dismiss();
                        getUpDateMsg(selectcount);
                    }

                }
               break;
        }
    }
    private void upDate(){
//        RequestParams requestParams=new RequestParams();
//        List<KeyValue>list=new ArrayList<>();
//        for(int i=0;i<5;i++){
//            list.add(new KeyValue("canshu","item="+i));
//        }
////        requestParams.addBodyParameter("",list.toString());
//        requestParams.setAutoResume(true);
//
//        MultipartBody body=new MultipartBody(list,"UTF-8");
//        requestParams.setRequestBody(body);
//        requestParams.setMultipart(true);
//        Log.e("TAG","上传惨淡--"+requestParams.getBodyParams());
////        MultipartBody multipartBody=new MultipartBody(new ArrayList<KeyValue>(),"utf-8");
////        requestParams.setRequestBody(multipartBody);
////        requestParams.addParameter("",new ArrayList<String>());
        RequestParams params=new RequestParams(getInterface.UpdateBuCartItem);
        x.http().post(params, new Callback.ProgressCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("TAG","onSuccess=="+result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("TAG","onError=="+ex.getMessage().toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.e("TAG","onCancelled==");
            }

            @Override
            public void onFinished() {
                Log.e("TAG","onFinished==");
            }

            @Override
            public void onWaiting() {
                Log.e("TAG","onWaiting==");
            }

            @Override
            public void onStarted() {
                Log.e("TAG","onStarted==");
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                Log.e("TAG","onLoading==");
            }
        });
    }
    private void updateImag(final String path,String itemid){
        if(!mydialog.isShowing()) {
//            mydialog.show();
        }
        pro.show();
        final RequestParams params=new RequestParams(getInterface.UpdateImag);
        params.setMultipart(true);
        params.setConnectTimeout(80000);
        params.setMaxRetryCount(5);//
        Log.e("TAG","path路径=="+path);
        params.addBodyParameter("imgdata",new File(path));
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File(path));
            int fileLen = fis.available();
            Log.e("TAG","上传文件大小=="+fileLen/1024+"==path="+path.substring(path.length()-5,path.length()));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//           params.setMaxRetryCount(2);
        Log.e("TAG","参数--"+params.getParams("imgdata"));
        Log.e("TAG","上传图片URLparams=="+params);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("TAG","图片上传结果---"+result);
                Log.e("TAG","select4=="+"\n"+path+"\n"+selectPath4+"\n"+path.equals(selectPath4));
                if(!TextUtils.isEmpty(path)&&path.equals(selectPath1)){
                    zqfUrlPath= GetJsonUtils.getZQF(getContext(),result);
//                    str=GetJsonUtils.getZQF(getContext(),result);
                    Log.e("TAG","这里左前方=="+zqfUrlPath);
                }else if(!TextUtils.isEmpty(path)&&path.equals(selectPath2)){
                    //正前方图pain
                    zfUrlPath= GetJsonUtils.getZQF(getContext(),result);
                    Log.e("TAG","这里正前方图片=="+zfUrlPath);
                }else if(!TextUtils.isEmpty(path)&&path.equals(selectPath3)){
                    //正后方图pain
                    zhfUrlPath= GetJsonUtils.getZQF(getContext(),result);
                    Log.e("TAG","这里正前方图片=="+zhfUrlPath);
                }else if(!TextUtils.isEmpty(path)&&path.equals(selectPath4)){
                    //正后方图pain
                    img4UrlPath= GetJsonUtils.getZQF(getContext(),result);
                    Log.e("TAG","这里img4UrlPath图片=="+img4UrlPath);
                }else if(!TextUtils.isEmpty(path)&&path.equals(selectPath5)){
                    //正后方图pain
                    img5UrlPath= GetJsonUtils.getZQF(getContext(),result);
                    Log.e("TAG","这里img5UrlPath图片=="+img5UrlPath);
                }else if(!TextUtils.isEmpty(path)&&path.equals(selectPath6)){
                    //正后方图pain
                    img6UrlPath= GetJsonUtils.getZQF(getContext(),result);
                    Log.e("TAG","这里img6UrlPath图片=="+img6UrlPath);
                }else if(!TextUtils.isEmpty(path)&&path.equals(selectPath7)){
                    //正后方图pain
                    img7UrlPath= GetJsonUtils.getZQF(getContext(),result);
                    Log.e("TAG","这里img7UrlPath图片=="+img7UrlPath);
                }else if(!TextUtils.isEmpty(path)&&path.equals(selectPath8)){
                    //正后方图pain
                    img8UrlPath= GetJsonUtils.getZQF(getContext(),result);
                    Log.e("TAG","这里img8UrlPath图片=="+img8UrlPath);
                }
                else if(!TextUtils.isEmpty(path)&&path.equals(selectPath9)){
                    //正后方图pain
                    img9UrlPath= GetJsonUtils.getZQF(getContext(),result);
                    Log.e("TAG","这里img9UrlPath图片=="+img9UrlPath);
                }
//                ZQFBean.zqpath=str;
//                Log.e("TAG","上传图片结果左前方图片=="+zqfUrlPath);
//                Log.e("TAG","上传图片结果正前方图片="+zfUrlPath);
//                Log.e("TAg","上传图片结果正后方图片="+zhfUrlPath);
//                Log.e("TAG","上传图片结果img4UrlPath图片=="+img4UrlPath);
//                Log.e("TAG","上传图片结果img5UrlPath图片="+img5UrlPath);
//                Log.e("TAg","上传图片结果img69UrlPath图片="+img6UrlPath);
//                Log.e("TAG","上传图片结果img7UrlPath图片=="+img7UrlPath);
//                Log.e("TAG","上传图片结果img8UrlPath图片="+img8UrlPath);
//                Log.e("TAg","上传图片结果img9UrlPath图片="+img9UrlPath);
//                Log.e("TAG","三张大图上传结果=="+(!TextUtils.isEmpty(ZHFBean.zhfpath)&&!TextUtils.isEmpty(ZQBean.zqpath)&&!TextUtils.isEmpty(ZQFBean.zqpath)));
                if(!TextUtils.isEmpty(zqfUrlPath)&&!TextUtils.isEmpty(zfUrlPath)&&!TextUtils.isEmpty(zhfUrlPath)
                        &&!TextUtils.isEmpty(img4UrlPath)&&!TextUtils.isEmpty(img5UrlPath)&&!TextUtils.isEmpty(img6UrlPath)
                        &&!TextUtils.isEmpty(img7UrlPath)&&!TextUtils.isEmpty(img8UrlPath)&&!TextUtils.isEmpty(img9UrlPath)){
                    //上传全部信息
                    Log.e("TAG","上传补录车量信息====="+BeanFlag.Flag);
                        Log.e("TAG","上传接口");
                        updateCartMsg(selectList.get(k).itemid);

                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
//                if(mydialog.isShowing()){
//                    mydialog.dismiss();
//                }
                if(!TextUtils.isEmpty(ZHFBean.zhfpath)&&!TextUtils.isEmpty(ZQBean.zqpath)&&!TextUtils.isEmpty(ZQFBean.zqpath)){
//                    ZQFBean.zqpath="";ZQBean.zqpath="";ZHFBean.zhfpath="";str="";
                }else{

                    if(!TextUtils.isEmpty(ex.getMessage().toString())){
                        cleanDate();
                        if(pro.isShowing()){
                            pro.dismiss();
                        }
                        Log.e("TAG","网络错误=="+(ex instanceof HttpException));
                        if (ex instanceof HttpException) { // 网络错误
                            HttpException httpEx = (HttpException) ex;
                            int responseCode = httpEx.getCode();
                            String responseMsg = httpEx.getMessage();
                            String errorResult = httpEx.getResult();
                            Log.e("TAG","responseCode=="+responseCode+"=responseMsg="+responseMsg+"=errorResult="+errorResult);
                            Toast.makeText(getActivity(),"请检查网络设置",Toast.LENGTH_SHORT).show();
                        } else {
// 其他错误//
                        }
                        Log.e("TAG","ex.getMessage().toString()=="+ex.getMessage().toString());
//                        mydialog.dismiss();
//                        ZQFBean.zqpath="";ZQBean.zqpath="";ZHFBean.zhfpath="";str="";
//                        Toast.makeText(getContext(),"图片上传失败",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                Log.e("TAG","onFinished");

            }
        });
    }
    //上传补录车辆信息
    private void updateCartMsg(final String itemId){
        Log.e("TAG","开始上传");
        RequestParams requestParams=new RequestParams(getInterface.UpCartData);
        //    参数 时间   regDate  merchant_code 商家ID  vin,groupid,carName(车型),
        // mileage,target_price,userid,brandid,seriesid,zhengqian45,zhengqian,zhenghou
        requestParams.addBodyParameter("id",selectList.get(k).itemid);
        requestParams.addBodyParameter("vin",selectList.get(k).vin);
        requestParams.addBodyParameter("merchant_code",selectList.get(k).chengshangid);
        requestParams.addBodyParameter("groupid", UserBean.groupids);
        requestParams.addBodyParameter("userid",UserBean.id);
        requestParams.addBodyParameter("vendorId",selectList.get(k).brandid);
        requestParams.addBodyParameter("brandId",selectList.get(k).sericseid);
        requestParams.addBodyParameter("regDate",selectList.get(k).date.trim());
        requestParams.addBodyParameter("mileage",selectList.get(k).licheng);
        requestParams.addBodyParameter("target_price",selectList.get(k).price);
        //九张大图
        requestParams.addBodyParameter("zhengqian45",zqfUrlPath);
        requestParams.addBodyParameter("zhengqian",zfUrlPath);
        requestParams.addBodyParameter("zhenghou",zhfUrlPath);
        requestParams.addBodyParameter("youhou45",img4UrlPath);
        requestParams.addBodyParameter("fadongji",img5UrlPath);
        requestParams.addBodyParameter("luntai",img6UrlPath);
        requestParams.addBodyParameter("toudeng",img7UrlPath);
        requestParams.addBodyParameter("weideng",img8UrlPath);
        requestParams.addBodyParameter("yibiaopan",img9UrlPath);
//
//        Log.e("TAG","上传车辆信息左前方==="+zhfUrlPath);
//        Log.e("TAG","上传车辆信息正前==="+zfUrlPath);
//        Log.e("TAG","上传车辆信息正后方==="+zhfUrlPath);
        requestParams.addBodyParameter("carStyleId",selectList.get(k).modelnameid);//modelid
        requestParams.addBodyParameter("carName",selectList.get(k).modelname.replace(" ",""));
        requestParams.addBodyParameter("pid",selectList.get(k).picID);
        //上传电话和名字
        //有对应id直接传ID

        requestParams.addBodyParameter("tel",selectList.get(k).tel);
        requestParams.addBodyParameter("name",selectList.get(k).name);
        //车辆分类
        requestParams.addBodyParameter("isDaTing",selectList.get(k).fenleiid);
        requestParams.addBodyParameter("transterstatus",selectList.get(k).guohuid);
        requestParams.addBodyParameter("makeup","2");
        requestParams.setMaxRetryCount(2);
        Log.e("TAG","上传地址=="+requestParams.getUri());
        Log.e("TAG","上传参数=="+requestParams.getBodyParams());
        Log.e("TAG","上传URL=="+requestParams);
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("TAG","上传成功=="+result);
                if(mydialog.isShowing()) {
                    mydialog.dismiss();
                }
                pro.setProgress((k+1)*1);
                if(k+1==selectList.size()){
                    pro.dismiss();
                }
                Log.e("TAG","");
//                mydialog.dismiss();
//                {"status":1,"msg":"添加成功","id":"2261"}
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    String status=jsonObject.getString("status");
                    String msg;
                    if (status.equals("1")){
                        successdialog.show();
                        msg=jsonObject.getString("msg");
//                        mySuccess=new MySuccess(getContext(),"上传成功");
//                        mySuccess.show();
                        selectcount--;
                        cleanDate();
                        utils.setDelete(itemId);
                        Log.e("TAG","list=="+itemId);

                        Log.e("TAG","=selectcount=selectList.size()="+selectcount+"=="+selectList.size());
                        if(selectcount>0) {
                            k++;
                            getUpDateMsg(selectcount);
                        }else{
                            Log.e("TAG","这里最后走");
                            new Thread(){
                                @Override
                                public void run() {
                                    super.run();
                                    try {
                                        Thread.sleep(1000);
                                        if(successdialog.isShowing()){
                                            successdialog.dismiss();
                                        }

                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }.start();
                            listBeans.clear();
                            setDate();
                            adapter.notifyDataSetChanged();
                        }

                        mydialog.dismiss();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if(!TextUtils.isEmpty(ex.getMessage().toString())){
//                    mydialog.dismiss();
//                    ZQFBean.zqpath="";ZQBean.zqpath="";ZHFBean.zhfpath="";
//                    zqfPath="";
//                    zhfPath="";
//                    zqPath="";
                    if(pro.isShowing()){
                        pro.dismiss();
                    }
                    Log.e("TAG","上传信息识别=="+ex.getMessage().toString());
                    Toast.makeText(getContext(),"上传信息失败",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
//                if(mydialog.isShowing()){
//                    mydialog.dismiss();
//                }

            }
        });
    }
    private void cleanDate(){
        zqfUrlPath="";
        zfUrlPath="";
        zhfUrlPath="";
        img4UrlPath="";
        img5UrlPath="";
        img6UrlPath="";
        img7UrlPath="";
        img8UrlPath="";
        img9UrlPath="";
    }
    int k=0;
    private void getUpDateMsg(int selectcount){
        Log.e("TAG","slesctvount--"+selectcount+"==selectList=="+selectList.size()+"==k==="+k);
        mydialog=new Mydialog(getContext(),"一共上传"+selectList.size()+"条数据"+"\n"+"正在上传第"+k+"条数据"+"\n"+"请耐心稍等.........");
      Log.e("TAG","boolean=getUpDateMsg="+(selectcount>0&&k<selectList.size()));
        if(selectcount>0&&k<selectList.size()){
            //
            selectPath1=selectList.get(k).img1;
            selectPath2=selectList.get(k).img2;
            selectPath3=selectList.get(k).img3;

            Log.e("TAG","点击提交img4-k-"+k);
            Log.e("TAG","==="+selectList.get(k).img4);
            selectPath4=selectList.get(k).img4;
            Log.e("TAG","点击提交selectPath4--"+selectPath4);

            selectPath5=selectList.get(k).img5;
            selectPath6=selectList.get(k).img6;
            selectPath7=selectList.get(k).img7;
            selectPath8=selectList.get(k).img8;
            selectPath9=selectList.get(k).img9;
            if(!selectPath1.contains("http:")){
                updateImag(selectPath1,selectList.get(k).itemid);
            }else{
                zqfUrlPath=selectPath1;
            }
            if(!selectPath2.contains("http:")){
                updateImag(selectPath2,selectList.get(k).itemid);
            }else{
                zfUrlPath=selectPath2;
            }
            if(!selectPath3.contains("http:")){
                updateImag(selectPath3,selectList.get(k).itemid);
            }else{
                zhfUrlPath=selectPath3;
            }
            updateImag(selectPath4,selectList.get(k).itemid);
            updateImag(selectPath5,selectList.get(k).itemid);
            updateImag(selectPath6,selectList.get(k).itemid);
            updateImag(selectPath7,selectList.get(k).itemid);
            updateImag(selectPath8,selectList.get(k).itemid);
            updateImag(selectPath9,selectList.get(k).itemid);
        }
    }
}
