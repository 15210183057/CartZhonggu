package adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.a123456.cartzhonggu.R;

import java.util.List;

import bean.BuCartListBean;
import bean.CarBean;

public class MyLvAdapter extends BaseAdapter{
    private List<BuCartListBean> list;
    private Context ctx;
    public MyLvAdapter (List<BuCartListBean>list, Context ctx){
        this.ctx=ctx;
        this.list=list;
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view==null){
            view= View.inflate(ctx, R.layout.item_mylv2,null);
           holder=new ViewHolder();
            holder.tv_name=view.findViewById(R.id.tv_name_mylvitem);
            holder.tv_num1=view.findViewById(R.id.tv_num_mylvitem);
            holder.tv_company=view.findViewById(R.id.tv_company_mylvitem);
            holder.tv_num2=view.findViewById(R.id.tv_num2_mylvitem);
            view.setTag(holder);
        }else{
            holder= (ViewHolder) view.getTag();
        }
        String str=list.get(i).cardType;
        for(int h=0;h<str.length();h++){
            char  item =  str.charAt(h);
            if(item<='Z'&&item>='A'){
              str=  str.replace(String.valueOf(item),"");
                h--;
            }else if(item>='0'&&item<='9'){
                str=  str.replace(String.valueOf(item),"");
                h--;
            }
        }
        str=str.replace("（","");
        holder.tv_name.setText(str);
        holder.tv_company.setText(list.get(i).name);
        holder.tv_num1.setText(list.get(i).vin);
        holder.tv_num2.setText(list.get(i).licensePlate);
        return view;
    }
    public class ViewHolder{
        TextView tv_name,tv_company,tv_num1,tv_num2;
    }
}
