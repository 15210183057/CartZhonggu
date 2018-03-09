package base;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;


import com.example.a123456.cartzhonggu.R;

import utils.StatusBarCompat;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //        继承AppCompatActivity时使用：
//        supportRequestWindowFeature(Window.FEATURENOTITLE)
//
//        继承activity时使用：
//        requestWindowFeature(Window.FEATURENOTITLE)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

//        StatusBarCompat.compat(this,Color.parseColor("#D60C18"));//设置状态栏颜色
          StatusBarCompat.compat(this,getResources().getColor(R.color.titleBar));
    }
}
