package com.example.a123456.cartzhonggu;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import application.MyApplication;
import base.BaseActivity;
import bean.BeanFlag;
import bean.ModelNameandID;
import bean.MyNewUpdate;
import bean.ZHFBean;
import bean.ZQBean;
import bean.ZQFBean;
import camera.CameraActivity;
import camera.FileUtil;
import fragment.newFragment;
import utils.MyModelDialog;
import utils.MySuccess;
import utils.Mydialog;
import utils.NameAndTelDialog;
import utils.SharedUtils;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

public class BuMessageActivity extends BaseActivity implements View.OnClickListener{
    private ImageView img_paizhao;
    private EditText edit_num;
    private View view;
    private PopupWindow window;
    private View popView,BrandPopView,SerisePopView;
    private TextView tv_paizhao,tv_canle,tv_xiangce;
    private ImageView img_topleft,img_topright;
    private TextView tv_topcenter;
    private TextView tv_time;//注册日期
    private EditText edt_licheng,edt_price,tv_tel;
    EditText edt_name;//里程，价格,联系电话
    private ImageView img_newfragment,img2_newfragment,img3_newfragment;
    private Button btn_commit;
    private LinearLayout linear3_newfragment,linear_nameandtel;
    private TextView tv_quyue,tv_cartFenlei;//tv_cartFenlei 车辆分类
    private TextView tv_cartmodel;
    List imgListPath=new ArrayList();
    Mydialog mydialog;
    String zqfPath,zqPath,zhfPath;
    String quyuID,brandid,modelid,seriesid,cartName,fenleiID;//商家信息ID,品牌ID，车系ID,车型,分类
    MySuccess mySuccess,mySuccess1;
    private TextView tv_getprice,tv_getmodel;
    Mydialog mydialog1;
    String quyuTelName;//车商信息对于的用户和电话
    String picID,currentID;//接收销售人员姓名和电话,当前ID
    private String picName;
    public String str;
    SharedUtils utils = new SharedUtils();
    public int count ;//记录保存多少条数据
    String posion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_bu_message);
        MyRegistReciver();
        initView();
        posion=getIntent().getStringExtra("i");
        if(!utils.readXML(MyApplication.cartlistmsg,"count",this).isEmpty()) {
            count = Integer.parseInt(utils.readXML(MyApplication.cartlistmsg, "count", this));
        }
    }

    private void initView() {
        img_topleft=findViewById(R.id.img_left);
        img_topright=findViewById(R.id.img_right);
        tv_topcenter=findViewById(R.id.tv_center);
        tv_topcenter.setText("待补充车源");
        img_topleft.setVisibility(View.GONE);
        img_topright.setVisibility(View.GONE);

        tv_getprice=findViewById(R.id.tv_getprice);
        tv_getprice.setOnClickListener(this);
        //所有要填写的控件
        //一下三个控件，当vin码识别识别，需要手动填写
        linear3_newfragment=findViewById(R.id.linear3_newfragment);
        tv_cartmodel=findViewById(R.id.tv_cartmodel);
        tv_time=findViewById(R.id.tv_time);//日期
//        linear_nameandtel=view.findViewById(R.id.linear_nameandtel);
        edt_name=findViewById(R.id.edt_name);
        tv_tel=findViewById(R.id.tv_tel);//联系电话
        edt_price=findViewById(R.id.edt_price);//价格
        edt_licheng=findViewById(R.id.edt_licheng);//里程
        tv_quyue=findViewById(R.id.tv_quyue);
        tv_cartFenlei=findViewById(R.id.tv_cartFenlei);
        //设置里程和价格的数据，小数点后为0的话不现实0
//        getSubStr(edt_price);
//        getSubStr(edt_licheng);
        img_newfragment=findViewById(R.id.img_newfragment);//左前45°
        img2_newfragment=findViewById(R.id.img2_newfragment);//正前
        img3_newfragment=findViewById(R.id.img3_newfragment);//正后

        img_paizhao=findViewById(R.id.img_paizhao);//vin拍照
        edit_num=findViewById(R.id.edt_vinnum);//vin码显示

        btn_commit=findViewById(R.id.btn_commit);//提交按钮

        img_paizhao.setOnClickListener(this);
        tv_time.setOnClickListener(this);
        btn_commit.setOnClickListener(this);

        edit_num.addTextChangedListener(new MyEditTextChangeListener(edit_num));
        edt_licheng.addTextChangedListener(new MyEditTextChangeListener(edt_licheng));
        edt_price.addTextChangedListener(new MyEditTextChangeListener(edt_price));
        tv_tel.addTextChangedListener(new MyEditTextChangeListener(tv_tel));
        edt_name.addTextChangedListener(new MyEditTextChangeListener(edt_name));

        img_newfragment.setOnClickListener(this);
        img2_newfragment.setOnClickListener(this);
        img3_newfragment.setOnClickListener(this);
        tv_cartmodel.setOnClickListener(this);
        tv_quyue.setOnClickListener(this);
        tv_cartFenlei.setOnClickListener(this);
        edt_name.setOnClickListener(this);
        tv_getmodel=findViewById(R.id.tv_getmodel);
        tv_getmodel.setOnClickListener(this);
    }
    class MyEditTextChangeListener implements TextWatcher {
        EditText editText;
        public MyEditTextChangeListener(EditText editText){
            this.editText=editText;
        }
        /**
         * 编辑框的内容发生改变之前的回调方法
         */
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            Log.e("TAG","编辑框的内容发生改变之前的回调方法");
        }

        /**
         * 编辑框的内容正在发生改变时的回调方法 >>用户正在输入
         * 我们可以在这里实时地 通过搜索匹配用户的输入
         */
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            Log.e("TAG","发生改变");
            editText.setBackgroundResource(R.drawable.juxingnull);
        }

        /**
         * 编辑框的内容改变以后,用户没有继续输入时 的回调方法
         */
        @Override
        public void afterTextChanged(Editable editable) {
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edt_name:
                edt_name.setText("");
//                if(BeanFlag.Flag){
//                    if(NameAndTel.NameAndTellist!=null){
//                        NameAndTelDialog nameAndTelDialog = new NameAndTelDialog(getContext(),NameAndTel.NameAndTellist.get(Integer.parseInt(currentID)));
//                        nameAndTelDialog.show();
//                    }
//                }
//                setName();
                break;
            case R.id.tv_getmodel:
                //获取车型车系车牌
                if (!TextUtils.isEmpty(edit_num.getText().toString())
                        && !TextUtils.isEmpty(tv_time.getText().toString())
                        && !tv_time.getText().toString().equals("请选择日期")) {
//                    getPrice("model");
                } else {
                    Toast.makeText(this, "vin码或注册日期不能为空", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.tv_getprice:
                //获取价格
                if (!TextUtils.isEmpty(edit_num.getText().toString())
                        && !TextUtils.isEmpty(tv_time.getText().toString())
                        && !tv_time.getText().toString().equals("请选择日期")) {
//                    getPrice("price");
                } else {
                    Toast.makeText(this, "vin或者注册时间不能为空", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.tv_quyue:
                Intent intentS = new Intent(this, MySerchActvity.class);
//                intentS.putExtra("myserch","update");
                startActivity(intentS);
                break;
            case R.id.img_paizhao:
                getPopView();
                break;
            case R.id.tv_paizhao:
                if(setPermissions()) {
                    Intent intent = new Intent(this, CameraActivity.class);
                    intent.putExtra("height", "70");
                    startActivity(intent);
                    window.dismiss();
                }
                break;
            case R.id.tv_xiangce:
                if (setPermissions()) {
                    Intent intent1 = new Intent(this, CameraActivity.class);
                    int h=getWindowManager().getDefaultDisplay().getHeight();
                    Log.e("TAG","h=="+h);
                    intent1.putExtra("height", "300");
                    startActivity(intent1);
                    window.dismiss();
                }
                break;
            case R.id.tv_canle:
                if(window!=null&&window.isShowing()){
                    window.dismiss();
                }
                break;
            case R.id.tv_time:
                showDate(tv_time);
                break;
            case R.id.tv_cartFenlei:
                Intent intent=new Intent(this, CartFenLei.class);
                startActivity(intent);
                break;
            case R.id.btn_commit:
//                updateImag(zqfPath);
//                updateImag(zqPath);
//                updateImag(zhfPath);
                Log.e("TAG","点击提交");
//                getSubStr(edt_price);
//                getSubStr(edt_licheng);
                if (TextUtils.isEmpty(tv_quyue.getText().toString())||tv_quyue.getText().toString().trim().equals("请选择车商信息")){
                    tv_quyue.setBackgroundResource(R.drawable.rednull);
                    Toast.makeText(this,"车商信息不能为空",Toast.LENGTH_LONG).show();
                }else if (!IsNullEdit(edit_num)||edit_num.getText().toString().length()!=17){
                    edit_num.setBackgroundResource(R.drawable.rednull);
                    Toast.makeText(this,"VIN码不能为空并且只能为17位",Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(tv_time.getText().toString())||tv_time.getText().toString().trim().equals("请选择日期")) {
                    tv_time.setBackgroundResource(R.drawable.rednull);
                    Toast.makeText(this,"注册日期不能为空",Toast.LENGTH_LONG).show();
                }
//                else if(!IsNullEdit(edt_name)){
//                    edt_name.setBackgroundResource(R.drawable.rednull);
//                    Toast.makeText(getContext(),"姓名不能为空",Toast.LENGTH_LONG).show();
//                }else if(!IsNullEdit(tv_tel)){
//                    tv_tel.setBackgroundResource(R.drawable.rednull);
//                    Toast.makeText(getContext(),"联系电话不能为空",Toast.LENGTH_LONG).show();
//                }
                else if(TextUtils.isEmpty(tv_cartmodel.getText().toString())||tv_cartmodel.getText().toString().trim().equals("请选择品牌，车系和车型")){
                    tv_cartmodel.setBackgroundResource(R.drawable.rednull);
                    Toast.makeText(this,"品牌，车系，车型不能为空",Toast.LENGTH_LONG).show();
                }else if (TextUtils.isEmpty(tv_cartFenlei.getText().toString())||tv_cartFenlei.getText().toString().trim().equals("请选取车辆分类信息")) {
                    tv_cartFenlei.setBackgroundResource(R.drawable.rednull);
                    Toast.makeText(this,"车辆分类信息不能为空",Toast.LENGTH_LONG).show();
                }else if(!IsNullEdit(edt_licheng)){
                    Toast.makeText(this,"里程不能为空",Toast.LENGTH_LONG).show();
                }
                else if(!IsNullEdit(edt_price)){
                    Toast.makeText(this,"价格不能为空",Toast.LENGTH_LONG).show();
                }

                else{
                    Log.e("TAG","BeanFlag.Flag=="+ BeanFlag.Flag);
                    if(BeanFlag.Flag){
                        mydialog=new Mydialog(this,"请稍等...");
                        Log.e("TAG","先上传图片===="+zqfPath);
                        Log.e("TAG","先上传图片"+(TextUtils.isEmpty(zqfPath)&&TextUtils.isEmpty(zqPath)&&TextUtils.isEmpty(zhfPath)));
                        if(TextUtils.isEmpty(zqfPath)&&TextUtils.isEmpty(zqPath)&&TextUtils.isEmpty(zhfPath)){
                            //走修改接口
//                            setCartMsg();
                            mydialog.show();
                            Log.e("TAG","走修改接口");
                        }else{
                            mydialog.show();
                            Log.e("TAG","修改图片==zqfPath="+zqfPath);
                            Log.e("TAG","修改图片==zqPath="+zqPath);
                            Log.e("TAG","修改图片==zhfPath="+zhfPath);
                            if(!TextUtils.isEmpty(zqfPath)){
//                                updateImag(zqfPath);
                            }
                            if(!TextUtils.isEmpty(zqPath)){
//                                updateImag(zqPath);
                            }
                            if(!TextUtils.isEmpty(zhfPath)){
//                                updateImag(zhfPath);
                            }


                        }
                    }else {

                        if (TextUtils.isEmpty(zqfPath)){
                            //img_newfragment.getDrawable().getCurrent().getConstantState()==getResources().getDrawable(R.drawable.zq45d).getConstantState()
//                             img_newfragment.setImageDrawable(getActivity().getDrawable(R.drawable.rednull));
                            Toast.makeText(this,"左前45°图片不能为空",Toast.LENGTH_LONG).show();
                        }
                        else if (TextUtils.isEmpty(zqPath)){
//                             img2_newfragment.setImageDrawable(getActivity().getDrawable(R.drawable.rednull));
                            Toast.makeText(this,"正前图片不能为空",Toast.LENGTH_LONG).show();
                        }
                        else if (TextUtils.isEmpty(zhfPath)){
//                             img3_newfragment.setImageDrawable(getActivity().getDrawable(R.drawable.rednull));
                            Toast.makeText(this,"正后图片不能为空",Toast.LENGTH_LONG).show();
                        }else {
//                            mydialog.show();
                            initGetDate();
//                            Log.e("TAG", "修改=" + zqfPath);
//                            updateImag(zqfPath);
//                            updateImag(zqPath);
//                            updateImag(zhfPath);
                        }
                    }
                }
                break;
            case R.id.img_newfragment:
                img_newfragment.setBackgroundResource(0);
                picName="zuoqian";
                getPicView(img_newfragment);
//               if( setPermissions()) {
//                Intent intent3=new Intent(getContext(),CameraActivity.class);
//                intent3.putExtra("name","zuoqian");
//                   int h=getActivity().getWindowManager().getDefaultDisplay().getHeight();
//                   Log.e("TAG","h=="+h);
//                   intent3.putExtra("height", 466+"");
//                startActivity(intent3);
//               }
                break;
            case R.id.img2_newfragment:
                img2_newfragment.setBackgroundResource(0);
                getPicView(img2_newfragment);
                picName="zhengqian";
//                if(setPermissions()){
//                    Intent intent4=new Intent(getContext(),CameraActivity.class);
//                    intent4.putExtra("name","zhengqian");
//                    int h=getActivity().getWindowManager().getDefaultDisplay().getHeight()-200;
//                    Log.e("TAG","h的高低=="+h);
//                    intent4.putExtra("height",""+466);
//                    startActivity(intent4);
//                }

                break;
            case R.id.img3_newfragment:
                getPicView(img3_newfragment);
                picName="zhenghou";
                img3_newfragment.setBackgroundResource(0);

                break;
            case R.id.tv_cartmodel:
                Intent intent6=new Intent(this, CartModelActivity.class);
                startActivity(intent6);
                break;
            case R.id.tv_paizhao2:
                //调取相机功能
//                startActivity(new Intent(getContext(), TakePhoteActivity.class));
                if(setPermissions()) {
                    Intent intent5 = new Intent(this, CameraActivity.class);
                    intent5.putExtra("name", picName);
                    intent5.putExtra("height", "466");
                    startActivity(intent5);
                }
                window.dismiss();
                break;
            case R.id.tv_xiangce2:
                takePicture();
                window.dismiss();
                break;
            case R.id.tv_canle2:
                if(window!=null&&window.isShowing()){
                    window.dismiss();
                }
                break;
        }
    }

    private void initGetDate() {
        count++;
        Log.e("TAG","count条数=="+count);
        utils.saveXML(MyApplication.cartlistmsg,"vin"+posion,edit_num.getText().toString(),this);
        utils.saveXML(MyApplication.cartlistmsg,"name"+posion,tv_cartmodel.getText().toString(),this);
        utils.saveXML(MyApplication.cartlistmsg,"licensePlate"+posion,tv_quyue.getText().toString(),this);
        utils.saveXML(MyApplication.cartlistmsg,"cardType"+posion,tv_cartmodel.getText().toString()
                ,this);
        //当前key值的posion
        Log.e("TAG","存入的所有posion"+"posion=="+posion+"=="+count);
        utils.saveXML(MyApplication.cartlistmsg,"posion"+count,posion,this);
        //一共保存条数count
        utils.saveXML(MyApplication.cartlistmsg,"count",count+"",this);
        Toast.makeText(this,"保存",Toast.LENGTH_SHORT).show();
        finish();
    }

    //点击提交判断是否有空的Edittext
    private boolean IsNullEdit(EditText editText){
        if(editText!=null) {
            String str = editText.getText().toString();
            if (TextUtils.isEmpty(str)) {
                editText.setBackgroundResource(R.drawable.rednull);
                return false;
            }else{
                return true;
            }
        }
        return false;
    }
    //显示日期
    private void showDate(final TextView Tv) {
        Calendar calend1 = Calendar.getInstance();
        calend1.setTimeInMillis(System.currentTimeMillis());
        int year = calend1.get(Calendar.YEAR);
        int month = calend1.get(Calendar.MONTH) + 1;
        int day = calend1.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog3 = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view,
                                          int year, int monthOfYear,
                                          int dayOfMonth) {
                        String month="";
                        String day="";
                        if((monthOfYear+1)<10){
                            month="0"+(monthOfYear+1);
                        }else{
                            month=""+(monthOfYear+1);
                        }
                        if(dayOfMonth<10){
                            day="0"+dayOfMonth;
                        }else{
                            day=dayOfMonth+"";
                        }
                        Tv.setText(year+"-"+month+"-"+day);
                        Tv.setBackgroundResource(R.drawable.juxingnull);
                    }
                }, year, month, day);
        dialog3.show();
    }
    //判断小数点后面是否都为"0",截取字符串
    private void getSubStr(EditText editText){
        String editStr=editText.getText().toString();
        if(editStr.contains(".")) {
//            if(editStr.length()>5){
//                Log.e("TAG","一共长的=="+editStr.length());
//                Log.e("TAG","小数点的位置"+editStr.indexOf(".")+"hhh=="+editStr.substring(0,editStr.indexOf(".")+3));
//                editStr=editStr.substring(0,5);
//             }
            int i = editStr.indexOf(".");
            String subStr = editStr.substring(i, editStr.length() - 1);
            int count = 0;
            for (int h = 0; h < subStr.length(); h++) {
                char item = subStr.charAt(h);
                if ((String.valueOf(item)).equals("0")) {
                    count++;
                }
            }
            if (count == subStr.length() - 1) {
                editText.setText(editStr.substring(0, i));
            }else{
                editText.setText(editStr.substring(0,editStr.indexOf(".")+3));
            }
        }
    }
    //获取popwindow
    private void getPopView(){
        popView= View.inflate(this,R.layout.popwiew,null);
        LinearLayout pop_linear=popView.findViewById(R.id.pop_linear);
        tv_paizhao=popView.findViewById(R.id.tv_paizhao);
        tv_xiangce=popView.findViewById(R.id.tv_xiangce);
        tv_canle=popView.findViewById(R.id.tv_canle);
        window=new PopupWindow(this);
        int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        pop_linear.measure(w, h);
        int pop_height = pop_linear.getMeasuredHeight();
        int pop_width = pop_linear.getMeasuredWidth();
        int width=getWindowManager().getDefaultDisplay().getWidth();
        int height=getWindowManager().getDefaultDisplay().getHeight();
        window.setWidth(width/3);
        window.setHeight(pop_height);
        // 设置PopupWindow的背景
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 设置PopupWindow是否能响应外部点击事件
        window.setOutsideTouchable(true);
        // 设置PopupWindow是否能响应点击事件
        window.setTouchable(true);
        // 显示PopupWindow，其中：
        // 第一个参数是PopupWindow的锚点，第二和第三个参数分别是PopupWindow相对锚点的x、y偏移
        window.setContentView(popView);
        window.setAnimationStyle(R.style.animTranslate);
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp=getWindow().getAttributes();
                lp.alpha=1.0f;
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                getWindow().setAttributes(lp);
            }
        });
//        window.showAtLocation(img_circle, Gravity.BOTTOM,0,0);
        window.showAsDropDown(img_paizhao,0,0);
        WindowManager.LayoutParams lp=getWindow().getAttributes();
        lp.alpha=0.3f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
        // 或者也可以调用此方法显示PopupWindow，其中：
        // 第一个参数是PopupWindow的父View，第二个参数是PopupWindow相对父View的位置，
        // 第三和第四个参数分别是PopupWindow相对父View的x、y偏移
        // window.showAtLocation(parent, gravity, x, y);
        tv_xiangce.setOnClickListener(this);
        tv_paizhao.setOnClickListener(this);
        tv_canle.setOnClickListener(this);
    }
    static final String[] PERMISSION = new String[]{
            Manifest.permission.READ_CONTACTS,// 写入权限
            Manifest.permission.READ_EXTERNAL_STORAGE,  //读取权限
            Manifest.permission.WRITE_CALL_LOG,        //读取设备信息
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    /**
     * 设置Android6.0的权限申请
     */
    private boolean setPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //Android 6.0申请权限
            Toast.makeText(this,"没有相关权限，请先开启",Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this,PERMISSION,1);
        }else{
            return true;
        }
        return false;
    }
    //获取popwindow
    ImageView selectImag=null;
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private void getPicView(ImageView imageView){
        selectImag=imageView;
        popView= View.inflate(this,R.layout.popwiew2,null);
        LinearLayout pop_linear=popView.findViewById(R.id.pop_linear);
        tv_paizhao=popView.findViewById(R.id.tv_paizhao2);
        tv_xiangce=popView.findViewById(R.id.tv_xiangce2);
        tv_canle=popView.findViewById(R.id.tv_canle2);
        window=new PopupWindow(this);
        int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        pop_linear.measure(w, h);
        int pop_height = pop_linear.getMeasuredHeight();
        int pop_width = pop_linear.getMeasuredWidth();
        Log.e("TAG","测量h="+pop_height);
        int width=getWindowManager().getDefaultDisplay().getWidth();
        int height=getWindowManager().getDefaultDisplay().getHeight();
        window.setWidth(width);
        window.setHeight(pop_height);
        // 设置PopupWindow的背景
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 设置PopupWindow是否能响应外部点击事件
        window.setOutsideTouchable(true);
        // 设置PopupWindow是否能响应点击事件
        window.setTouchable(true);
        // 显示PopupWindow，其中：
        // 第一个参数是PopupWindow的锚点，第二和第三个参数分别是PopupWindow相对锚点的x、y偏移
        window.setContentView(popView);
        window.setAnimationStyle(R.style.animTranslate);
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp=getWindow().getAttributes();
                lp.alpha=1.0f;
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                getWindow().setAttributes(lp);
            }
        });
        window.showAtLocation(tv_topcenter, Gravity.BOTTOM,0,0);
        WindowManager.LayoutParams lp=getWindow().getAttributes();
        lp.alpha=0.3f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
        // 或者也可以调用此方法显示PopupWindow，其中：
        // 第一个参数是PopupWindow的父View，第二个参数是PopupWindow相对父View的位置，
        // 第三和第四个参数分别是PopupWindow相对父View的x、y偏移
        // window.showAtLocation(parent, gravity, x, y);
        tv_xiangce.setOnClickListener(this);
        tv_paizhao.setOnClickListener(this);
        tv_canle.setOnClickListener(this);
        Log.e("TAG","window=="+window.getWidth()+"height=="+window.getHeight());
    }
    //调取本地相机
    public void takePicture(){
        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        Bundle bundle=new Bundle();
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FileUtil fileUtil=new FileUtil(this);

        if (requestCode == PHOTO_REQUEST_GALLERY) {
            if (data != null) {
                // 得到图片的全路径
                Log.e("TAG","这里走了吗");
                String photoPath="";
                Uri uri = data.getData();
                ContentResolver resolver=this.getContentResolver();
                Bitmap bitmap= null;
                //                    bitmap = MediaStore.Images.Media.getBitmap(resolver,uri);
////                    selectImag.setImageBitmap(bitmap);
//                    //获取图片路径
//                    String picPath="";
//                    //获取照片路径
//                    String[] filePathColumn = {MediaStore.Audio.Media.DATA};
//                    Cursor cursor = this.getContentResolver().query(data.getData(), filePathColumn, null, null, null);
//                    cursor.moveToFirst();
//                    photoPath  = cursor.getString(cursor.getColumnIndex(filePathColumn[0]));
//                    cursor.close();
//                    Log.i(TAG, "photoPath = "+photoPath+"length=="+new File(photoPath).length()/1024);
                photoPath=getImageAbsolutePath(this,uri);
                Log.e("TAG","photoPath=="+photoPath);
                if(photoPath!=null) {
                    // 设置参数
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true; // 只获取图片的大小信息，而不是将整张图片载入在内存中，避免内存溢出
                    BitmapFactory.decodeFile(photoPath, options);
                    int height = options.outHeight;
                    int width= options.outWidth;
                    int inSampleSize = 2; // 默认像素压缩比例，压缩为原图的1/2
                    int minLen = Math.min(height, width); // 原图的最小边长
                    if(minLen > 100) { // 如果原始图像的最小边长大于100dp（此处单位我认为是dp，而非px）
                        float ratio = (float)minLen / 100.0f; // 计算像素压缩比例
                        inSampleSize = (int)ratio;
                    }
                    options.inJustDecodeBounds = false; // 计算好压缩比例后，这次可以去加载原图了
                    options.inSampleSize = inSampleSize; // 设置为刚才计算的压缩比例
                    Bitmap bm = BitmapFactory.decodeFile(photoPath, options); // 解码文件
                    Log.w("TAG", "size: " + bm.getByteCount()/1024 + " width: " + bm.getWidth() + " heigth:" + bm.getHeight()); // 输出图像数据
                    selectImag.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    selectImag.setImageBitmap(bm);
                    new FileUtil(this).saveBitmap(bm);
                    photoPath=FileUtil.getJpegName();
                    selectImag.setImageBitmap(new FileUtil(this).readBitmap(photoPath));
                }

                //                selectImag.setImageBitmap(bitmap);
                if(selectImag==img3_newfragment){
                    zhfPath=photoPath;
                    Log.e("TAG","图库里的zhfpath=="+zhfPath);
                }else if(selectImag==img2_newfragment){
                    zqPath=photoPath;
                    Log.e("TAG","图库里的zhpath=="+zqPath);
                }else if(selectImag==img_newfragment){
                    zqfPath=photoPath;
                    Log.e("TAG","图库里的path=="+zqfPath);
                }
            }
        }
    }
    MyBroadcastReceiver myBroadcastReceiver;
    //注册广播
    private void MyRegistReciver(){
        myBroadcastReceiver=new MyBroadcastReceiver();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("vin");
        intentFilter.addAction("quyu");
        intentFilter.addAction("cartmodel");
        intentFilter.addAction("update");
        intentFilter.addAction("goon");
        intentFilter.addAction("modelname");
        intentFilter.addAction("updataCart");//获取收到添加车商信息
        intentFilter.addAction("cartfenlei");//
        intentFilter.addAction("strNameAndTelAndID");
        registerReceiver(myBroadcastReceiver,intentFilter);
    }
    //接受广播退出APP
    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("TAG","接收广播newFragment===="+intent.getStringExtra("path"));
            if(intent.getAction().equals("update")){
                tv_quyue.setBackgroundResource(R.drawable.juxingnull);
                tv_cartmodel.setBackgroundResource(R.drawable.juxingnull);
                tv_time.setBackgroundResource(R.drawable.juxingnull);
                tv_cartFenlei.setBackgroundResource(R.drawable.juxingnull);
                img_newfragment.setBackgroundResource(R.drawable.zq45d);
                img2_newfragment.setBackgroundResource(R.drawable.zqf);
                img3_newfragment.setBackgroundResource(R.drawable.zhf);
                MyNewUpdate myNewUpdate=new MyNewUpdate();

                //TODO

                fenleiID=MyNewUpdate.isDaTing;
//                1=大厅车辆 ，2=市场车辆，3=商户自用车，4=新车登记商户卡车辆，5=职工车辆
                if(fenleiID.equals("1")){
                    tv_cartFenlei.setText("大厅车辆");
                }else if(fenleiID.equals("2")){
                    tv_cartFenlei.setText("市场车辆");
                }
                else if(fenleiID.equals("3")){
                    tv_cartFenlei.setText("商户自用车");
                }
                else if(fenleiID.equals("4")){
                    tv_cartFenlei.setText("新车登记商户卡车辆");
                }
                else if(fenleiID.equals("5")){
                    tv_cartFenlei.setText("职工车辆");
                }
                if(!TextUtils.isEmpty(MyNewUpdate.tel)){
                    tv_tel.setText(MyNewUpdate.tel.trim());
                }
                if(!TextUtils.isEmpty(MyNewUpdate.contact_name)) {
                    edt_name.setText(MyNewUpdate.contact_name.trim());
                }

                edit_num.setText(MyNewUpdate.vinnum);
                edit_num.setFocusableInTouchMode(false);
                edit_num.setFocusable(false);
                tv_quyue.setText(MyNewUpdate.quyu);
                tv_cartmodel.setText(MyNewUpdate.cartmodel);
                tv_time.setText(MyNewUpdate.time);
                edt_licheng.setText(MyNewUpdate.licheng);
                edt_price.setText(MyNewUpdate.price);
                Log.e("TAG","==MyNewUpdate.price=="+MyNewUpdate.price);
                Log.e("TAG","edt_price=="+edt_price.getText());
                str=MyNewUpdate.img1;
                Log.e("TAG","修改接受到的图片地址=="+ ZQFBean.zqpath);
                ZQBean.zqpath=MyNewUpdate.img2;
                ZHFBean.zhfpath=MyNewUpdate.img3;
                if(!TextUtils.isEmpty(MyNewUpdate.img1)) {
                    Glide.with(BuMessageActivity.this).load(MyNewUpdate.img1).placeholder(R.drawable.zq45d).error(R.drawable.zq45d).into(img_newfragment);
                }
                if(!TextUtils.isEmpty(MyNewUpdate.img2)) {
                    Glide.with(BuMessageActivity.this).load(MyNewUpdate.img2).placeholder(R.drawable.zqf).error(R.drawable.zqf).into(img2_newfragment);
                }
                if(!TextUtils.isEmpty(MyNewUpdate.img3)) {
                    Glide.with(BuMessageActivity.this).load(MyNewUpdate.img3).placeholder(R.drawable.zhf).error(R.drawable.zhf).into(img3_newfragment);
                }
                quyuID=MyNewUpdate.quyuID;
                seriesid=MyNewUpdate.seriseID;
                modelid=MyNewUpdate.modelID;
                brandid=MyNewUpdate.brandid;
                cartName=MyNewUpdate.cartmodel;
                picID=MyNewUpdate.NameTelID;
                BeanFlag.Flag=true;
                currentID=MyNewUpdate.currentID;
            }
            else if(intent.getAction().equals("quyu")){

                String name=intent.getStringExtra("name");
                quyuID=intent.getStringExtra("ID");
                Log.e("TAG","接受区域广播=="+intent.getStringExtra("tel"));
                quyuTelName=intent.getStringExtra("tel");
                currentID=intent.getStringExtra("currentID");
                if(TextUtils.isEmpty(quyuTelName)||quyuTelName.equals("&&&")){
                    tv_tel.setText("");
                    edt_name.setText("");
                    picID="0";
                }else{
                    String []arr=quyuTelName.split("&");
                    if(!quyuTelName.equals("&&&")) {
                        Log.e("TAG","quyuName--"+quyuTelName);
                        tv_tel.setText(arr[0]);
                        edt_name.setText(arr[1]);
                        picID=arr[2];
                        if(MySerchActvity.nameAndTelList.get(Integer.parseInt(currentID))!=null) {
                            Log.e("TAG","");
                            NameAndTelDialog nameAndTelDialog = new NameAndTelDialog(BuMessageActivity.this, MySerchActvity.nameAndTelList.get(Integer.parseInt(currentID)));
                            nameAndTelDialog.show();

                        }
                    }
//                    Log.e("TAG","判断=="+(TextUtils.isEmpty(tv_tel.getText().toString())||TextUtils.isEmpty(edt_name.getText().toString())));
//                    Log.e("TAG","判断1"+(tv_tel.getText().toString()==null)+"="+(tv_tel.getText().toString().equals("null"))+"==="+tv_tel.getText().toString()+"="+(!TextUtils.isEmpty(tv_tel.getText().toString())));
//                    Log.e("TAG","判断2=+"+edt_name.getText().toString()+"+="+!TextUtils.isEmpty(edt_name.getText().toString()));
//                    if(edt_name.getText().toString().equals("null")||tv_tel.getText().toString().equals("null")||TextUtils.isEmpty(tv_tel.getText().toString())||TextUtils.isEmpty(edt_name.getText().toString())){
//                        linear_nameandtel.setVisibility(View.VISIBLE);
//                    }else{
//                        linear_nameandtel.setVisibility(View.GONE);
//                    }
                }
                Log.e("TAG","接收到的销售人员电话和姓名为="+quyuTelName+"=ID="+picID);
                tv_quyue.setText(name);
                tv_quyue.setBackgroundResource(R.drawable.juxingnull);
//                if(MySerchActvity.nameAndTelList.get(Integer.parseInt(currentID))!=null){
//                    if(!(MySerchActvity.nameAndTelList.get(Integer.parseInt(currentID)).get(0).id).equals("")) {
//                    }
//                }
            }else if(intent.getAction().equals("vin")){
                String name = intent.getStringExtra("name");
                if (!TextUtils.isEmpty(name)) {
                    String path = intent.getStringExtra("path");
                    imgListPath.add(path);
                    FileUtil fileUtil = new FileUtil(context);
                    if (name.equals("zhengqian")) {
                        zqPath=path;
                        img2_newfragment.setImageBitmap(fileUtil.readBitmap(path));
                    } else if (name.equals("zuoqian")) {
                        zqfPath=path;
                        Log.e("TAG","path=="+path);
                        Log.e("TAG","拍照返回=="+zqfPath);
                        img_newfragment.setImageBitmap(BitmapFactory.decodeFile(path));
//                        img_newfragment.setImageBitmap(fileUtil.readBitmap(path));
                    } else if (name.equals("zhenghou")) {
                        zhfPath=path;
                        img3_newfragment.setImageBitmap(fileUtil.readBitmap(path));
                    }
                } else {
                    //上传vin码返回
                    Log.e("TAG", "广播接受上传vin码返回====" + intent.getStringExtra("vinnum"));
                    String str = intent.getStringExtra("vinnum");
                    if (TextUtils.isEmpty(str)) {
//                        linear3_newfragment.setVisibility(View.GONE);
                    } else {
                        String vinStr = intent.getStringExtra("vinnum");
                        if (TextUtils.isEmpty(vinStr)) {
//                            linear3_newfragment.setVisibility(View.VISIBLE);
                        } else {
                            Log.e("TAG","MyodelIDandName=="+ ModelNameandID.list.size());
                            MyModelDialog myModelDialog=new MyModelDialog(BuMessageActivity.this,ModelNameandID.list);
                            myModelDialog.show();
//                            linear3_newfragment.setVisibility(View.GONE);
                            edit_num.setText(vinStr);
                            edt_licheng.setText(intent.getStringExtra("licheng"));
                            edt_price.setText(intent.getStringExtra("price"));
                            tv_time.setText(intent.getStringExtra("data"));
                            brandid=intent.getStringExtra("vinbrand_id");
                            seriesid=intent.getStringExtra("vinseries_id");
                            List list=new ArrayList();
                            cartName=intent.getStringExtra("CartName");
                            list.add(cartName);
                            modelid=intent.getStringExtra("model_id");
                            tv_cartmodel.setText(cartName);
                        }
                    }
                }
            }else if(intent.getAction().equals("cartmodel")){
                tv_cartmodel.setText(intent.getStringExtra("brand")+
                        intent.getStringExtra("serise")+
                        intent.getStringExtra("model"));
                seriesid=intent.getStringExtra("seriseID");
                brandid=intent.getStringExtra("barndID");
                cartName=intent.getStringExtra("model");
                modelid=intent.getStringExtra("modelID");
                tv_cartmodel.setBackgroundResource(R.drawable.juxingnull);
            }else if(intent.getAction().equals("goon")){
                Log.e("TAG","接收继续上传的广播");
                //继续上传接口
                BeanFlag.Flag=false;
                edit_num.setText("");
                edt_price.setText("");
                edt_licheng.setText("");
                tv_time.setText("");
                tv_cartmodel.setText("");
                tv_cartFenlei.setText("");
                ZQFBean.zqpath="";ZQBean.zqpath="";ZHFBean.zhfpath="";str="";
                zqfPath="";zqPath="";zhfPath="";
                edit_num.setFocusableInTouchMode(true);
                edit_num.setFocusable(true);
                img_newfragment.setImageBitmap(BitmapFactory.decodeResource(BuMessageActivity.this.getResources(),R.drawable.zq45d));
                img2_newfragment.setImageBitmap(BitmapFactory.decodeResource(BuMessageActivity.this.getResources(),R.drawable.zqf));
                img3_newfragment.setImageBitmap(BitmapFactory.decodeResource(BuMessageActivity.this.getResources(),R.drawable.zhf));
            }else if(intent.getAction().equals("modelname")){
                cartName=intent.getStringExtra("modelname");
                linear3_newfragment.setVisibility(View.VISIBLE);
                tv_cartmodel.setText(cartName);
                modelid=intent.getStringExtra("modelID");
            }else if(intent.getAction().equals("updataCart")){
                quyuID=intent.getStringExtra("quyuID");
                tv_quyue.setText(intent.getStringExtra("quyuName"));
            }else if(intent.getAction().equals("cartfenlei")){
                tv_cartFenlei.setText(intent.getStringExtra("fenleiname"));
                fenleiID=intent.getStringExtra("fenleiID");
                tv_cartFenlei.setBackgroundResource(R.drawable.juxingnull);
            }else if(intent.getAction().equals("strNameAndTelAndID")){
                //广播返回姓名，电话，id
                String str=intent.getStringExtra("strNameAndTelAndID");
                if(!TextUtils.isEmpty(str)&&!str.equals("&&")){
                    String [] arr=str.split("&");
                    picID=arr[2];
                    edt_name.setText(arr[0]);
                    tv_tel.setText(arr[1]);
                }else{
                    picID="0";
                }
            }
        }
    }

    @TargetApi(19)
    public static String getImageAbsolutePath(Activity context, Uri imageUri) {
        if (context == null || imageUri == null)
            return null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(imageUri)) {
                String id = DocumentsContract.getDocumentId(imageUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } // MediaStore (and general)
        else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(imageUri))
                return imageUri.getLastPathSegment();
            return getDataColumn(context, imageUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            return imageUri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
