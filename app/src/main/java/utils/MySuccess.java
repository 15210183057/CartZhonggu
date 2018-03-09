package utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.a123456.cartzhonggu.R;

import base.BaseActivity;
import fragment.Fragment3;

/**
 * Created by 123456 on 2018/2/11.
 */
public class MySuccess extends AlertDialog {
    private Button btn1,btn2;
    private TextView tv_success;
    private String string;
    public MySuccess(Context context,String string) {
        super(context);
        this.string=string;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mysuccesslog);
        btn1=findViewById(R.id.btn1);
        btn2=findViewById(R.id.btn2);
        tv_success=findViewById(R.id.tv_success);
        tv_success.setText(string);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
//                intent.putExtra("f","1");
                intent.setAction("goon");
                getContext().sendBroadcast(intent);
                dismiss();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setAction("success");
                intent.putExtra("f","3");
                getContext().sendBroadcast(intent);
                dismiss();
//                finish();
            }
        });
    }
}
