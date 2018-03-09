package utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a123456.cartzhonggu.R;
import com.example.a123456.cartzhonggu.AddCartActivity;

/**
 * Created by 123456 on 2018/2/26.
 */

public class addCartAlert extends AlertDialog{
    private Button button1,button2;
    private int i;
    private TextView tv_alert_title,tv_alert_msg;
    public addCartAlert(Context context) {
        super(context);
        setCanceledOnTouchOutside(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myxiajiadialog);
        tv_alert_msg=findViewById(R.id.tv_alert_msg);
        tv_alert_title=findViewById(R.id.tv_alert_title);
        button1=findViewById(R.id.btn_ok);
        button2=findViewById(R.id.btn_canle);
        tv_alert_title.setText("添加车商信息");
        tv_alert_msg.setText("查找的商品不在列表中，是否手动添加车商信息");
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"添加",Toast.LENGTH_LONG).show();
                Intent intent=new Intent(getContext(), AddCartActivity.class);
                getContext().startActivity(intent);
                Intent intent1=new Intent();
                intent1.setAction("mysearch");
                getContext().sendBroadcast(intent1);
                dismiss();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"取消",Toast.LENGTH_LONG).show();
                dismiss();
            }
        });
    }
}
