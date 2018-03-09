package camera;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;


import com.example.a123456.cartzhonggu.R;

import java.io.File;

public class CameraActivity extends Activity implements CameraInterface.CamOpenOverCallback {
	private static final String TAG = "CameraActivity";
	
	CameraSurfaceView surfaceView = null;
	ImageButton shutterBtn;
	MaskView maskView = null;
	float previewRate = -1f;
	 int DST_CENTER_RECT_WIDTH = 350;//单位是dp
	 public static int DST_CENTER_RECT_HEIGHT = 350;
	Point rectPictureSize = null;
	
	private String thumbPath = null;
	private ImageView imageView;
	private String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        name=getIntent().getStringExtra("name");
        Log.e("TAG","getIntent().getStringExtra(\"height\")===name=="+getIntent().getStringExtra("height")+"==="+name);
		DST_CENTER_RECT_HEIGHT=Integer.parseInt(getIntent().getStringExtra("height"));
        setPermissions();
        initUI();
        initViewParams();
//        Intent intent = new Intent(this,CameraActivity.class);
//        this.bindService(intent, conn, Context.BIND_AUTO_CREATE);
        startCamera();
        RegisterReceiever();
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
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
			//Android 6.0申请权限
			ActivityCompat.requestPermissions(this,PERMISSION,1);
		}else{
			Log.i("TAG","权限申请ok");
		}
	}
	private void startCamera() {
		Thread openThread = new Thread(){
        	@Override
        	public void run() {
        	CameraInterface.getInstance(CameraActivity.this).doOpenCamera(CameraActivity.this);
        	}
        };
        openThread.start();
	}

	private void initUI() {
		surfaceView =(CameraSurfaceView) findViewById(R.id.camera_surfaceview);
		shutterBtn = (ImageButton)findViewById(R.id.btn_shutter);
		shutterBtn.setOnClickListener(new BtnListener());
		maskView = (MaskView)findViewById(R.id.view_mask);
	}

	private void initViewParams() {
		LayoutParams params = surfaceView.getLayoutParams();
		Point p = DisplayUtil.getScreenMetrics(this);
		params.width = p.x;
		params.height = p.y;
		
		Log.i(TAG, "screenwidth = "+ p.x +"screenheight = "+p.y);
		previewRate = DisplayUtil.getScreenRate(this);//默认全屏的比例预览
		surfaceView.setLayoutParams(params);
		
		LayoutParams p2 = shutterBtn.getLayoutParams();
		p2.width = DisplayUtil.dip2px(this, 80);
		p2.height = DisplayUtil.dip2px(this, 80);
		shutterBtn.setLayoutParams(p2);

	}
	
	@Override
	public void cameraHasOpened() {
		SurfaceHolder holder = surfaceView.getSurfaceHolder();
		CameraInterface.getInstance(CameraActivity.this).doStartPreView(holder, previewRate);
		if(maskView != null){
			Rect screenCenterRect = createCenterScreenRect(
					DisplayUtil.dip2px(this, DST_CENTER_RECT_WIDTH),
					DisplayUtil.dip2px(this, DST_CENTER_RECT_HEIGHT));//DST_CENTER_RECT_HEIGHT
			maskView.setCenterRect(screenCenterRect);
		}
	}
	
	private void RegisterReceiever() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(FileUtil.ACTION_SERVICE);
		CameraActivity.this.registerReceiver(reciever, intentFilter);
		Log.i(TAG, "RegisterReceiever ======================== ");
	}
	
//	private ServiceConnection conn = new ServiceConnection() {
//		
//		@Override
//		public void onServiceDisconnected(ComponentName name) {
//			
//		}
//		
//		@Override
//		public void onServiceConnected(ComponentName name, IBinder service) {
//			Log.i(TAG, "服务已经连接。。。=+++++++++++++++++++++++++++++++++++");
//		}
//	};
	
	private BroadcastReceiver reciever = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			 String action = intent.getAction();
			 if(action.equals(FileUtil.ACTION_SERVICE)){
				 if(intent != null){
					 thumbPath= intent.getStringExtra("thumbPath");
					 if(thumbPath != null){
						 File file = new File(thumbPath);
						 Uri uri = Uri.fromFile(file);
						 Intent i = new Intent(CameraActivity.this, ShowImageActivity.class);
						 i.setData(uri);
						 i.putExtra("thumbPath", thumbPath);
						 if(!TextUtils.isEmpty(name)) {
							 i.putExtra("name", name);
						 }
//						 FileUtil f=new FileUtil(context);
//						 TODO 在CameraActivity里设置图片
//						 imageView.setImageBitmap(f.readBitmap(thumbPath));
						 Log.i(TAG, "Uri =============================================================== " + uri.toString());
						 startActivity(i);
						 finish();
					 }
				 }
			 }
		}
		
	};
	
	
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		startCamera();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStop() {
		super.onStop();
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(reciever);
	};
	
	private class BtnListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.btn_shutter:
				if(rectPictureSize == null){
					rectPictureSize = createCenterPictureRect(
							DisplayUtil.dip2px(CameraActivity.this, DST_CENTER_RECT_WIDTH),
							DisplayUtil.dip2px(CameraActivity.this, DST_CENTER_RECT_HEIGHT));
				}
				CameraInterface.getInstance(CameraActivity.this).doTakePicture(rectPictureSize.x,rectPictureSize.y);
				
				break;
			}
		}
		
	}
	
	/**
	 * 生成拍照后图片的中间矩形的宽度和高度
	 * @param w 屏幕上的矩形宽度，单位px
	 * @param h 屏幕上的矩形高度
	 * @return
	 */
	private Point createCenterPictureRect(int w, int h){
		int wScreen = DisplayUtil.getScreenMetrics(this).x;
		int hScreen = DisplayUtil.getScreenMetrics(this).y;
		int wSavePicture = CameraInterface.getInstance(CameraActivity.this).doGetPrictureSize().y;//因为图片旋转了，所以宽高换位
		int hSavePicture = CameraInterface.getInstance(CameraActivity.this).doGetPrictureSize().x;
		float wRate = (float)(wSavePicture) / (float)(wScreen);
		float hRate = (float)(hSavePicture) / (float)(hScreen);
		float rate = (wRate <= hRate) ? wRate : hRate;//也可以按照最小比率计算

		Log.e("TAG","w==wRate=wScreen="+w+"="+wRate+"="+wScreen);

		int wRectPicture = (int)(w * wRate);
		int hRectPicture = (int)(h * hRate);
		Log.e("TAG","wRectPicture==w * wRate=="+w * wRate+"="+wRectPicture);
		Log.e("TAG","hRectPicture==hre==="+h * hRate+"="+hRectPicture);
		Log.e("TAG","re==="+wRectPicture);
		return new Point(wRectPicture,hRectPicture);
	}
	
	/**
	 * 生成屏幕中间的矩形
	 * @param w 目标矩形的宽度，单位px
	 * @param h 目标矩形的高度，单位px
	 * @return
	 */
	private Rect createCenterScreenRect(int w,int h){
		int x1 = DisplayUtil.getScreenMetrics(this).x / 2 - w / 2;
		int y1 = DisplayUtil.getScreenMetrics(this).y / 2 - h / 2;
		int x2 = x1 + w ;
		int y2 = y1 + h;
		return new Rect(x1, y1, x2, y2);
	}

}
