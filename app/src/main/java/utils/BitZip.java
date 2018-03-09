package utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import View.GetJsonUtils;
import bean.BeanFlag;
import bean.ZHFBean;
import bean.ZQBean;
import bean.ZQFBean;
import jiekou.getInterface;

/**
 * Created by 123456 on 2018/2/9.
 */

public class BitZip {
    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while ( baos.toByteArray().length / 1024>100) { //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos

            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
            if (options < 11) {//为了防止图片大小一直达不到200kb，options一直在递减，当options<0时，下面的方法会报错
                // 也就是说即使达不到200kb，也就压缩到10了
                image.compress(Bitmap.CompressFormat.JPEG, options, baos);
                break;
            }
            Log.e("TAG","length=="+baos.toByteArray().length/1024);
        }
        Log.e("TAG","baos.toByteArray().length=="+baos.toByteArray().length+"====="+baos.toByteArray().length/1024+"");
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
       ///storage/emulated/0/Android/data/com.example.a123456.zhonggu/cache/1520151569494.jpg
        return bitmap;
    }

    private static void upImg(String s) {
    }

}
