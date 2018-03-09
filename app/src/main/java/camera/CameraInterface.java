package camera;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.File;
import java.io.IOException;
import java.util.List;

import utils.BitZip;

@SuppressWarnings("deprecation")
public class CameraInterface extends Service{
	private static final String TAG = "CameraInterface";
	private Camera mCamera;
	private Camera.Parameters mParams;
	private boolean isPreviewing = false ;
	private float mPreviewRate = -1f;
	private static CameraInterface mCameraInterface;
	private Context mContext;
	
	int DST_RECT_WIDTH, DST_RECT_HEIGHT;
	
	public interface CamOpenOverCallback{
		public void cameraHasOpened();
	}

	public CameraInterface(){

	}
	
	public CameraInterface(Context mContext) {
		super();
		this.mContext = mContext;
	}

//	public static synchronized CameraInterface getInstance(){
//		if(mCameraInterface == null){
//			mCameraInterface = new CameraInterface();
//		}
//		return mCameraInterface;
//	}
	//chuan Context 
	public static synchronized CameraInterface getInstance(Context mContext){
		if(mCameraInterface == null){
			mCameraInterface = new CameraInterface(mContext);
		}
		return mCameraInterface;
	}
	
	
	
	/**打开Camera
	 *@param  callback
	 */
	public void doOpenCamera(CamOpenOverCallback callback){
		Log.i(TAG, "Camera open ...");
		//TODO 取消休眠
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		mCamera = Camera.open();
		Log.i(TAG, "Camera open over..");
		if(callback != null){
			callback.cameraHasOpened();
		}
	}
	
	/**使用SurfaceView 开启预览
	 * @param holder
	 * @param previewRate
	 */
	
	public void doStartPreView(SurfaceHolder holder , float previewRate){
		Log.i(TAG, "doStartPreview");
		if(isPreviewing){
			mCamera.stopPreview();
			return;
		}
		if(mCamera != null){
			try {
				mCamera.setPreviewDisplay(holder);
			} catch (IOException e) {
				e.printStackTrace();
			}
			initCamera(previewRate);
		}
	}
	
	/**
	 * 使用TextureView 预览 Camera
	 * @param previewRate
	 */
	
	public void doStartPreview(SurfaceTexture surface , float previewRate){
		Log.i(TAG, "doStartPreview ..");
		if(isPreviewing){
			mCamera.stopPreview();
			return;
		}
		if(mCamera != null){
		try {
			mCamera.setPreviewTexture(surface);
		} catch (IOException e) {
			e.printStackTrace();
		}
		initCamera(previewRate);
	}
}
	/**
	 * 停止预览，释放Camera
	 * @param 
	 */
	public void doStopCamera(){
		if(mCamera != null){
			mCamera.setPreviewCallback(null);
			mCamera.stopPreview();
			isPreviewing = false;
			mPreviewRate = -1f;
			mCamera.release();
			mCamera = null;
		}
	}

	/**
	 * 拍照
	 */
	public void doTakePicture(){
		if(isPreviewing && (mCamera != null)){
			mCamera.takePicture(mShutterCallback, null, mJpegPictureCallback);
		}
	}
//	public boolean Flag=true;
	public void doTakePicture(int w, int h){
		if(isPreviewing && (mCamera != null)){
			Log.i(TAG, "矩形拍照尺寸：width =  " + w + "height =" + h);
			DST_RECT_WIDTH = w;
			DST_RECT_HEIGHT = h;
//			if(Flag) {
			try {
				mCamera.takePicture(mShutterCallback, null, mRectJpegPictureCallback);

			}catch (Exception e){
				Log.e("TAG","e=="+e.getMessage().toString());
			}
//				Flag=false;
//			}
		}
	}

	public Point doGetPrictureSize(){
		Size s= mCamera.getParameters().getPictureSize();
		return new Point(s.width,s.height);
	}
	
	private void initCamera(float previewRate) {
		if(mCamera != null){
			mParams = mCamera.getParameters();
			mParams.setPictureFormat(PixelFormat.JPEG);//设置拍照后的存储的图片格式
			
			//设置PreviewSize 和 PictureSize
			Size pictureSize = CamParaUtil.getInstance().getPropPictureSize(
					mParams.getSupportedPictureSizes(), previewRate, 800);
			mParams.setPictureSize(pictureSize.width, pictureSize.height);
			
			Size previewSize = CamParaUtil.getInstance().getPropPreviewSize(
					mParams.getSupportedPreviewSizes(), previewRate, 800);
			mParams.setPreviewSize(previewSize.width, previewSize.height);
			
			mCamera.setDisplayOrientation(90);
			
			List<String> focusModes = mParams.getSupportedFocusModes();
			if(focusModes.contains("continuous-video")){
				mParams.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
			}
			mCamera.setParameters(mParams);
			mCamera.startPreview(); // 开启预览
			
			isPreviewing = true ;
			mPreviewRate = previewRate;
			
			mParams = mCamera.getParameters();//重新get一次
			Log.i(TAG, "最终设置:PreviewSize--With = " + mParams.getPreviewSize().width
					+ "Height = " + mParams.getPreviewSize().height);
			Log.i(TAG, "最终设置:PictureSize--With = " + mParams.getPictureSize().width
					+ "Height = " + mParams.getPictureSize().height);
		}
	}
	
	//为了实现拍照的快门声音及拍照保存照片需要下面三个回调变量
	ShutterCallback mShutterCallback = new ShutterCallback() {
		//快门按下的回调，在这里我们可以设置类似播放咔嚓之类的操作，默认的就是咔嚓
		@Override
		public void onShutter() {
		}
	};
	
	PictureCallback mRawCallback = new PictureCallback() {
		//拍摄的未压缩原数据的回调，可以为null
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			
		}
	};
	
	/**
	 * 常规的回调
	 */
	PictureCallback mJpegPictureCallback = new PictureCallback() {
		//对jpeg图像数据的回调，最重要的一个回调
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			Bitmap b = null;
			if(data != null){
				b = BitmapFactory.decodeByteArray(data, 0, data.length);//data是字节数据，将其解析成类图
				mCamera.stopPreview();
				isPreviewing = false;
			}
			//保存图片到sdcard
			if(b != null){
				//设置FOCUS_MODE_CONTINUOUS_VIDEO 之后，myParam.set("rotation",90);失效
				//图片不能旋转，在这里旋转
				Bitmap rotaBitmap = ImageUtil.getRotateBitmap(b, 90.0f);
				Log.e("TAG","b图pain高=="+b.getHeight());
				//压缩图片
				rotaBitmap= BitZip.compressImage(rotaBitmap);
				new FileUtil(mContext).saveBitmap(rotaBitmap);
			}
			//再次进入预览
			mCamera.startPreview();
			isPreviewing = true ;
			
		}
	};
	
	/**
	 * 拍摄指定区域的rect
	 */
	PictureCallback mRectJpegPictureCallback = new PictureCallback() {
		//对jpeg 图像数据的回调，最重要的一个回调
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			Bitmap b = null;
			if(data != null){
				b = BitmapFactory.decodeByteArray(data, 0, data.length);//data是字节数据，将其解析成类图
				mCamera.stopPreview();
				isPreviewing = false;
			}
			//保存图片到sdcard
			Log.e("TAG","这里走吗==？");
			if(b != null){
				//设置FOCUS_MODE_CONTINUOUS_VIDEO 之后，myParam.set("rotation",90);失效
				//图片不能旋转，在这里旋转
				Bitmap rotaBitmap;
				Log.e("TAG","高度==="+DST_RECT_HEIGHT);
//				if(DST_RECT_HEIGHT>1000){
//					rotaBitmap = ImageUtil.getRotateBitmap(b, 0f);
//				}else{
					rotaBitmap = ImageUtil.getRotateBitmap(b, 90.0f);
//				}
				Log.e("TAG","rotaBitmap=rotaBitmap.getHeight()/2="+rotaBitmap.getHeight()/2+rotaBitmap);
//				int y =   rotaBitmap.getWidth()/2;
//				int x =  rotaBitmap.getHeight()/2;
				int x = rotaBitmap.getWidth()/2 - DST_RECT_WIDTH/2;
				if(DST_RECT_HEIGHT>rotaBitmap.getHeight()){
					DST_RECT_HEIGHT=rotaBitmap.getHeight();
				}
				int y = rotaBitmap.getHeight()/2 - DST_RECT_HEIGHT/2;
//				if(x<=0){
//					x = rotaBitmap.getWidth()/2;
//				}
//				if(y<=0){
//					y=rotaBitmap.getHeight()/2;
//				}
//				if(y+DST_RECT_HEIGHT>rotaBitmap.getHeight()){
//					y=rotaBitmap.getHeight();
//				}
				Log.i(TAG, " rotaBitmap.getWidth() = "+  rotaBitmap.getWidth()+"x==y=="+x+"="+y);
				Log.i(TAG, "rotaBitmap.getHeight() = " + rotaBitmap.getHeight()+"=="+DST_RECT_HEIGHT);
				Bitmap rectBitmap = Bitmap.createBitmap(rotaBitmap, x, y, DST_RECT_WIDTH, DST_RECT_HEIGHT);
				Log.e("TAG","b图pain高=="+rectBitmap.getHeight());
				Bitmap bitmap=BitZip.compressImage(rectBitmap);
				new FileUtil(mContext).saveBitmap(BitZip.compressImage(bitmap));
				if(rotaBitmap.isRecycled()){
					rotaBitmap.recycle();
					rotaBitmap = null;
				}
				if(rectBitmap.isRecycled()){ 
					rectBitmap.recycle();
					rectBitmap = null;
				}
				
				String thumbPath = FileUtil.getJpegName();
				File file = new File(thumbPath);
				Uri uri = Uri.fromFile(file);
				Log.d(TAG, "thumbPath==============================================================="+thumbPath);
				if(thumbPath != null){
					Intent intent = new Intent();
					intent.putExtra("thumbPath", thumbPath);
					intent.setAction(FileUtil.ACTION_SERVICE);
					mContext.sendBroadcast(intent);
				}
			}
			//再次进入预览
			mCamera.startPreview();
			isPreviewing = true;
			if(!b.isRecycled()){
				b.recycle();
				b = null;
			}
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	private int getBitmapDegree(String path) {
		int degree = 0;
		try {
			// 从指定路径下读取图片，并获取其EXIF信息
			ExifInterface exifInterface = new ExifInterface(path);
			// 获取图片的旋转信息
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}
	public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
		Bitmap returnBm = null;

		// 根据旋转角度，生成旋转矩阵
		Matrix matrix = new Matrix();
		matrix.postRotate(degree);
		try {
			// 将原始图片按照旋转矩阵进行旋转，并得到新的图片
			returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
		} catch (OutOfMemoryError e) {
		}
		if (returnBm == null) {
			returnBm = bm;
		}
		if (bm != returnBm) {
			bm.recycle();
		}
		return returnBm;
	}

}
