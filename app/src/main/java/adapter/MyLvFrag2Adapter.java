package adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.a123456.cartzhonggu.R;

import java.util.List;

import bean.Bran;
import bean.BuCartListBean;
import bean.CarBean;
import bean.UserBean;

public class MyLvFrag2Adapter extends BaseAdapter {
//    private List<CarBean> list;
    private List<Bran> list;
    private Context ctx;
    public SelectCallBack call;
    public MyLvFrag2Adapter (Context ctx,List<Bran> list){
        this.ctx=ctx;
        this.list=list;
    }
    public void setCall(SelectCallBack call){
        this.call=call;
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
        if(view==null){
            view= View.inflate(ctx, R.layout.item_mylv,null);
            holder=new ViewHolder();
            holder.tv_name=view.findViewById(R.id.tv_name_mylvitem);
            holder.tv_num1=view.findViewById(R.id.tv_num_mylvitem);
            holder.tv_company=view.findViewById(R.id.tv_company_mylvitem);
            holder.tv_num2=view.findViewById(R.id.tv_num2_mylvitem);
            holder.img_select=view.findViewById(R.id.img_item_mylv_select);

            holder.tv_modelname=view.findViewById(R.id.tv_model_mylvitem);//车型
            holder.tv_cheshang=view.findViewById(R.id.tv_company_mylvitem);//车商
            holder.tv_time=view.findViewById(R.id.tv_time_itemylv2);
            holder.tv_username=view.findViewById(R.id.tv_user_itemylv2);
            view.setTag(holder);
        }else{
            holder= (ViewHolder) view.getTag();
        }
        String str=list.get(i).brandname;
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
//        holder.tv_company.setText(list.get(i).cheshang);
        holder.tv_num1.setText(list.get(i).vin);
//        holder.tv_num2.setText(list.get(i).licensePlate);//车牌
        holder.tv_time.setText(list.get(i).date);
        holder.tv_username.setText(UserBean.username);
        holder.tv_modelname.setText(list.get(i).modelname);
        holder.tv_cheshang.setText(list.get(i).cheshang);
        if(list.get(i).Flag){
            holder.img_select.setImageResource(R.mipmap.icon02);
        }else{
            holder.img_select.setImageResource(R.mipmap.icon01);
        }
        holder.img_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call.call(i);
            }
        });
        return view;
    }
    public class ViewHolder{
        TextView tv_name,tv_company,tv_num1,tv_num2;
        ImageView img_select;
        TextView tv_modelname,tv_cheshang,tv_time,tv_username;
    }
    public interface SelectCallBack{
      void  call(int i);
    }
}
