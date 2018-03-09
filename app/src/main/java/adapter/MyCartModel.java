package adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.a123456.cartzhonggu.R;

import java.util.List;

import base.BaseActivity;
import bean.BrandBean;

/**
 * Created by 123456 on 2018/2/11.
 */

public class MyCartModel extends BaseAdapter {
    public Context context;
    public List<BrandBean> list;
    public SelectCallBack callBack;
    public MyCartModel(Context context, List<BrandBean> list) {
        this.context = context;
        this.list = list;
    }
    public void setCall(SelectCallBack callBack){
        this.callBack=callBack;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = View.inflate(context, R.layout.item, null);
            holder = new ViewHolder();
            holder.tv = view.findViewById(R.id.tvitem_xiala);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.tv.setText(list.get(i).brand_name);
//        Log.e("TAG","点击的是第+"+i);
        if(list.get(i).Flag){
            holder.tv.setBackgroundResource(R.color.titleBar);
//            Log.e("TAG","这里走了为true");
        }else{
            holder.tv.setBackgroundResource(R.drawable.juxingnull);
        }
//        list.get(i).Flag=false;
//        holder.tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                callBack.callcolor(i);
//                Log.e("TAG","点击事件中途");
//            }
//        });
        return view;
    }

    class ViewHolder {
        TextView tv;
    }

    public interface SelectCallBack {
        void callcolor(int i);
    }
}
