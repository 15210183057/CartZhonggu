package utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.a123456.cartzhonggu.R;

/**
 * Created by 123456 on 2018/2/13.
 */

public class newAlert extends AlertDialog{
    private Button button1,button2;
    private int i;
    private TextView tv_alert_title,tv_alert_msg;
    public newAlert(Context context,int i) {
        super(context);
        this.i=i;
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myxiajiadialog);
        tv_alert_msg=findViewById(R.id.tv_alert_msg);
        tv_alert_title=findViewById(R.id.tv_alert_title);
        button1=findViewById(R.id.btn_ok);
        button2=findViewById(R.id.btn_canle);
        tv_alert_title.setText("下架");
        tv_alert_msg.setText("确认是否要下架");
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setAction("deleteitem");
                intent.putExtra("i",""+i);
                getContext().sendBroadcast(intent);
                Log.e("TAG","发送删除");
                dismiss();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
