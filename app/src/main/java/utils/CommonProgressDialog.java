package utils;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.example.a123456.cartzhonggu.R;

import java.text.NumberFormat;

/**
 * 版本更新自定义dialog
 * @author: ${momoThree}
 * Date : 2017/10/25.
 * Title:
 */

public class


CommonProgressDialog extends AlertDialog {

    private static final String TAG = "CommonProgressDialog";
    private ProgressBar mProgress;
    private TextView mProgressNumber;
    private TextView mProgressPercent;
    private TextView mProgressMessage;

    private Handler   mViewUpdateHandler;

    private  int mMax;
    private CharSequence mMessage;
    private  boolean  mHasStarted;
    private  int mProgressVal;

    private  String  mProgressNumberFormat;
    private NumberFormat mProgressPercentFormat;

    public CommonProgressDialog(Context context) {
        super(context);
        initFormats();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_progress_dialog);
        mProgress = (ProgressBar) findViewById(R.id.progress);
        mProgressNumber = (TextView) findViewById(R.id.progress_number);
        mProgressPercent = (TextView) findViewById(R.id.progress_percent);
        mProgressMessage = (TextView) findViewById(R.id.progress_message);

        mViewUpdateHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                int progress = mProgress.getProgress();
                int max = mProgress.getMax();
                double dProgress =  (double)progress/(double)(1024*1024);
                double   dMax = (double)max/(double)(1024 * 1024);
                if(mProgressNumberFormat !=null){
                    String format = mProgressNumberFormat;
                    mProgressNumber.setText(String.format(format,dProgress,dMax));
                } else {
                    mProgressNumber.setText("");
                }

                if(mProgressNumberFormat !=null){
                    double percent = (double) progress/(double) max;
                    SpannableString  tmp = new SpannableString(
                            mProgressPercentFormat.format(percent));
                    tmp.setSpan(new StyleSpan(Typeface.BOLD),0,tmp.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                  mProgressPercent.setText(tmp);
                } else {
                    mProgressPercent.setText("");
                }
            }
        };
        onProgressChanged();
        if(mMessage != null){
            setMessage(mMessage);
        }

        if(mMax>0){
            setMax(mMax);
        }
        if(mProgressVal>0){
            setProgress(mProgressVal);
        }

    }


    private void  initFormats(){
        mProgressNumberFormat = "%1.2fM/%2.2fM";
        mProgressPercentFormat = NumberFormat.getPercentInstance();
        mProgressPercentFormat.setMaximumFractionDigits(0);
    }

    private void onProgressChanged() {
        mViewUpdateHandler.sendEmptyMessage(0);
    }

    public void setMax(int max){
        if(mProgress !=null){
            mProgress.setMax(max);
            onProgressChanged();
        }else{
            mMax = max;
        }
    }
    public  void  setProgress(int value){
        if(mHasStarted){
            mProgress.setProgress(value);
            onProgressChanged();
        } else{
            mProgressVal = value;
        }
    }
    public  void setProgressStyle(int style){
        // mProgressStyle = style;
    }

    public  void  setIndeterminate(boolean indeterminate){
        if(mProgress !=null){
            mProgress.setIndeterminate(indeterminate);
        }
    }

    @Override
    public void setMessage(CharSequence message) {
        if(mProgressMessage !=null){
            mProgressMessage.setText(message);
        } else {
            mMessage = message ;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mHasStarted = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mHasStarted =false ;
    }
}
