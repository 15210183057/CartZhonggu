package camera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;

public class MaskView extends android.support.v7.widget.AppCompatImageView {
	private static final String TAG = "MaskView";
	private Paint mLinePaint;
	private Paint mAreaPaint;
	private Rect mCenterRect = null ;
	private Context mContext;

	int widthScreen , heightScreen ;
	
	public MaskView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initPaint();
		mContext = context;
		Point p = DisplayUtil.getScreenMetrics(mContext);
		widthScreen = p.x;
		heightScreen = p.y;
	}
	
	private void initPaint(){
		mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mLinePaint.setColor(Color.YELLOW);
		mLinePaint.setStyle(Style.STROKE);
		mLinePaint.setStrokeWidth(5f);
		mLinePaint.setAlpha(30);
		
		//绘制四周区域
		mAreaPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mAreaPaint.setColor(Color.BLACK);
		mAreaPaint.setStyle(Style.FILL);
		mAreaPaint.setAlpha(120);
	}
	
	public void setCenterRect(Rect r){	
		this.mCenterRect = r;
		postInvalidate();
	}
	
	public void clearCenterRect(Rect r){
		this.mCenterRect = null;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
	Log.i(TAG, "onDraw");
		if(mCenterRect == null){
			return;
		}
		//绘制四周阴影区域
		Log.e("TAG","mCenterRect.top=="+mCenterRect.top);
		canvas.drawRect(0, 0, widthScreen, mCenterRect.top, mAreaPaint);//top
		canvas.drawRect(0, mCenterRect.top, mCenterRect.left - 1 , mCenterRect.bottom + 1, mAreaPaint);//left
		canvas.drawRect(0, mCenterRect.bottom + 1, widthScreen, heightScreen, mAreaPaint); // bottom
		canvas.drawRect(mCenterRect.right + 1, mCenterRect.top, widthScreen, mCenterRect.bottom + 1, mAreaPaint);
		
		//绘制拍照的透明区域
		canvas.drawRect(mCenterRect, mLinePaint);
//		canvas.drawLine(mCenterRect.centerX(), mCenterRect.top, 55, mCenterRect.bottom + 1,mAreaPaint);
		super.onDraw(canvas);
	}
	
	
}
