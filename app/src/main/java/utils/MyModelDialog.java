package utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.example.a123456.cartzhonggu.R;

import java.util.List;
import bean.ModelNameandID;
import bean.JaShiZhengBean;
import bean.modelNameBean;

/**
 * Created by 123456 on 2018/2/13.
 */

public class MyModelDialog extends AlertDialog{
    private ListView listView;
    private List<modelNameBean>list;
    public MyModelDialog(Context context, List list) {
        super(context);
        this.list=list;
        if(list.size()!=0) {
            setCanceledOnTouchOutside(false);
        }
//        setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mymodeldialog);
        listView=findViewById(R.id.lv);
        listView.setAdapter(new MyDialogAdapter());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent();
                intent.setAction("modelname");
                intent.putExtra("modelname",list.get(i).CartName);
                intent.putExtra("modelID",list.get(i).model_id);
                getContext().sendBroadcast(intent);
                dismiss();
            }
        });
    }
    class MyDialogAdapter extends BaseAdapter{

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
        MyViewHolder holder;
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if(view==null){
                holder=new MyViewHolder();
                view=View.inflate(getContext(),R.layout.item,null);
                holder.tv=view.findViewById(R.id.tvitem_xiala);
                view.setTag(holder);
            }else{
                holder= (MyViewHolder) view.getTag();
            }
            holder.tv.setText(list.get(i).CartName);
            return view;
        }
    }
    class MyViewHolder{
        TextView tv;
    }
}

