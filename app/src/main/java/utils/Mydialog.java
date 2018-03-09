package utils;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a123456.cartzhonggu.R;


/**
 * Created by 123456 on 2018/2/11.
 */

public class Mydialog extends AlertDialog {

    private ImageView mImg;
    private TextView mTv;
    public String string;
    public Mydialog(Context context,String string) {
        super(context);
        this.string=string;
        setCanceledOnTouchOutside(false);//设置点击对话框外位置取消对话框
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mydialog);
        mTv=findViewById(R.id.tv_mydialog);
        mTv.setText(string);
    }
}
