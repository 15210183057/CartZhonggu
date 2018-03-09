package fragment;


import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


import com.example.a123456.cartzhonggu.R;
import com.example.a123456.cartzhonggu.SettingActivity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import bean.BUCartListBeanNUm;
import camera.FileUtil;
import mycamare.TakePhoteActivity;
import View.CircleImageView;
import utils.BitZip;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment4 extends Fragment implements View.OnClickListener{

    private View view;
    private ImageView img_topleft,img_topright;
    private TextView tv_topcenter,tv_all_num,tv_yue_num,tv_bucong_num,tv_shangchuan_num;
     CircleImageView img_circle;
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
//PopWindow界面控件
    TextView tv_paizhao,tv_xiangce,tv_canle;
    Button btn;
    private View popView;
    private PopupWindow window;
    ImageView selectImag;

    //拍照传递回来的uri
    MyReciver myReciver;
    public Fragment4() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_fragment4, container, false);
        img_topleft=view.findViewById(R.id.img_left);
        img_topright=view.findViewById(R.id.img_right);
        tv_topcenter=view.findViewById(R.id.tv_center);

        img_topleft.setVisibility(View.GONE);
        tv_topcenter.setText("我的");
        img_topright.setImageResource(R.mipmap.sz);
        //圆角图pain
        img_circle=view.findViewById(R.id.img_circle);
        //设置监听
        img_topright.setOnClickListener(this);
        img_circle.setOnClickListener(this);
        initView();
        myReciver=new MyReciver();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("image");
        getActivity().registerReceiver(myReciver,intentFilter);
        return view;
    }

    private void initView() {
        tv_all_num=view.findViewById(R.id.tv_all_num);
        tv_bucong_num=view.findViewById(R.id.tv_bucong_num);
        tv_shangchuan_num=view.findViewById(R.id.tv_shangchuan_num);
        tv_yue_num=view.findViewById(R.id.tv_yue_num);
        tv_yue_num.setText(BUCartListBeanNUm.total);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_right:
                Intent intent=new Intent(getContext(), SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.img_circle:
//                Toast.makeText(getContext(),"点击头像",Toast.LENGTH_SHORT).show();
//                getPopView(img_circle);
                break;
            case R.id.tv_paizhao:
                //调取相机功能
            startActivity(new Intent(getContext(), TakePhoteActivity.class));
                window.dismiss();
                break;
            case R.id.tv_xiangce:
                takePicture();
                window.dismiss();
                break;
            case R.id.tv_canle:
                if(window!=null&&window.isShowing()){
                    window.dismiss();
                }
                break;
        }
    }


    //获取popwindow
    private void getPopView(ImageView imageView){
        selectImag=imageView;
        popView= View.inflate(getContext(),R.layout.popwiew,null);
        LinearLayout pop_linear=popView.findViewById(R.id.pop_linear);
        tv_paizhao=popView.findViewById(R.id.tv_paizhao);
        tv_xiangce=popView.findViewById(R.id.tv_xiangce);
        tv_canle=popView.findViewById(R.id.tv_canle);
        window=new PopupWindow(getContext());
        int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        pop_linear.measure(w, h);
        int pop_height = pop_linear.getMeasuredHeight();
        int pop_width = pop_linear.getMeasuredWidth();
       Log.e("TAG","测量h="+pop_height);
        int width=getActivity().getWindowManager().getDefaultDisplay().getWidth();
        int height=getActivity().getWindowManager().getDefaultDisplay().getHeight();
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
                WindowManager.LayoutParams lp=getActivity().getWindow().getAttributes();
                lp.alpha=1.0f;
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                getActivity().getWindow().setAttributes(lp);
            }
        });
        window.showAtLocation(img_circle, Gravity.BOTTOM,0,0);
        WindowManager.LayoutParams lp=getActivity().getWindow().getAttributes();
        lp.alpha=0.3f;
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getActivity().getWindow().setAttributes(lp);
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
        FileUtil fileUtil=new FileUtil(getContext());

        if (requestCode == PHOTO_REQUEST_GALLERY) {
            if (data != null) {
                // 得到图片的全路径
                Log.e("TAG","这里走了吗");
                Uri uri = data.getData();
                ContentResolver resolver=getActivity().getContentResolver();
                Bitmap bitmap= null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(resolver,uri);
                    //获取图片路径
                    String picPath="";
                    //获取照片路径
                    String[] filePathColumn = {MediaStore.Audio.Media.DATA};
                    Cursor cursor = getActivity().getContentResolver().query(data.getData(), filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    String photoPath = cursor.getString(cursor.getColumnIndex(filePathColumn[0]));
                    cursor.close();
                    Log.i(TAG, "photoPath = "+photoPath+"length=="+new File(photoPath).length()/1024);
//                    bitmap=BitZip.compressImage(bitmap);
//                    new FileUtil(getContext()).saveBitmap(BitZip.compressImage(bitmap));
//                    String filePath = uri.getEncodedPath();
//                    final String imagePath = Uri.decode(filePath);
////                    fileUtil.saveBitmap(bitmap);
//                   String str= saveJPG_After(getContext(),bitmap,"");
//                    Log.e("TAG","获取本地图库的路径11=="+photoPath);
                    if(photoPath!=null) {
//                            photoPath.replace("png","jpg");
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
                        int options = 100;
                            while (baos.toByteArray().length / 1024 > 100) { //循环判断如果压缩后图片是否大于100kb,大于继续压缩
                                baos.reset();//重置baos即清空baos
                                bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
                                options -= 10;//每次都减少10
                                if(options<11){
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
                                    break;
                                }
                            Log.e("TAG", "baos.toByteArray().length==" + baos.toByteArray().length / 1024 + "");

                        }
                        Log.e("TAG", "总集baos.toByteArray().length==" + baos.toByteArray().length / 1024 + "");

                    }
//                    Log.e("TAG","length=="+new File(str).length()/1024);
                    selectImag.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                selectImag.setImageBitmap(bitmap);
            }
        }
    }
    public static String  saveJPG_After(Context context,Bitmap bitmap, String newImgpath) {
        //复制Bitmap  因为png可以为透明，jpg不支持透明，把透明底明变成白色
        //主要是先创建一张白色图片，然后把原来的绘制至上去
//        String fileName = getFile()+"/"+dataTake+".jpg";
        newImgpath=context.getExternalCacheDir().getAbsolutePath()+"/"+System.currentTimeMillis()+".jpg";

        Bitmap outB=bitmap.copy(Bitmap.Config.ARGB_8888,true);
        Canvas canvas=new Canvas(outB);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        File file = new File(newImgpath);
        if(file.exists()){
            return newImgpath;
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            int options = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
            Log.e("TAG","长度为：=="+baos.toByteArray().length/1024);
            while (baos.toByteArray().length/1024>100){
                baos.reset();
                Log.e("TAG","options=="+options);
                bitmap.compress(Bitmap.CompressFormat.JPEG,options,baos);
                options-=10;
                Log.e("TAG","循环长的=="+baos.toByteArray().length/1024);
            }
//                        int options = 100;
//                        while ( baos.toByteArray().length / 1024>100) { //循环判断如果压缩后图片是否大于100kb,大于继续压缩
//                            baos.reset();//重置baos即清空baos
//                            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
//                            options -= 10;//每次都减少10
//                        }
            if (outB.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
      return newImgpath;
    }
    private Bitmap readBitmap(String newImgpath){
        if(!Environment.getExternalStorageState().
                equals(Environment.MEDIA_MOUNTED))
            return null;
        Log.e("TAG","读取newImgpath="+newImgpath);

        Bitmap  bitmap1=BitmapFactory.decodeFile(newImgpath);
        return bitmap1;
    }
    public class MyReciver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Uri source=Uri.parse(intent.getStringExtra("imgUrl"));
            try {
                selectImag.setImageBitmap(MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),source));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(myReciver);
    }
}
