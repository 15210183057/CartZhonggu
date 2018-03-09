package adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.a123456.cartzhonggu.R;

import java.util.List;

import bean.CartFenleiBean;
import bean.SeriseBean;

/**
 * Created by 123456 on 2018/2/11.
 */

public class CartFenLeiAdapter extends BaseAdapter {
    public Context context;
    public List<CartFenleiBean> list;
    public SelectCallBack callBack;
    public CartFenLeiAdapter(Context context, List<CartFenleiBean> list) {
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
        holder.tv.setText(list.get(i).fenleiName);
        Log.e("TAG","点击的是第+"+i);
        if(list.get(i).Flag){
            holder.tv.setBackgroundResource(R.color.titleBar);
            Log.e("TAG","这里走了为true");
        }else{
            holder.tv.setBackgroundResource(R.drawable.juxingnull);
        }
        return view;
    }

    class ViewHolder {
        TextView tv;
    }

    public interface SelectCallBack {
        void callcolor(int i);
    }
}
