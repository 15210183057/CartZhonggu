package adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.a123456.cartzhonggu.R;

import java.util.List;

import bean.BuCartListBean;
import bean.CarBean;
import bean.UserBean;
import utils.newAlert;

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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view==null){
            view= View.inflate(ctx, R.layout.item_mylv2,null);
           holder=new ViewHolder();
            holder.tv_name=view.findViewById(R.id.tv_name_mylvitem);
            holder.tv_num1=view.findViewById(R.id.tv_num_mylvitem);
            holder.tv_company=view.findViewById(R.id.tv_company_mylvitem);
            holder.tv_num2=view.findViewById(R.id.tv_num2_mylvitem);
            holder.img_item_mylv=view.findViewById(R.id.img_item_mylv);
            holder.btn_xiajia=view.findViewById(R.id.btn_xiajia);
            holder.tv_user=view.findViewById(R.id.tv_user_itemylv2);
            holder.tv_price=view.findViewById(R.id.tvprice_mylvitem);
            holder.tv_time=view.findViewById(R.id.tv_time_itemylv2);
            holder.tv_model_mylvitem=view.findViewById(R.id.tv_model_mylvitem);
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

        holder.tv_model_mylvitem.setText(list.get(i).modelName);
        holder.btn_xiajia.setVisibility(View.GONE);
        holder.btn_xiajia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ctx,"点击下架",Toast.LENGTH_LONG);
                Log.e("TAG","点击下架=="+i);
                newAlert alert=new newAlert(ctx,i);
                alert.show();
            }
        });
        holder.tv_time.setText("采集时间："+list.get(i).time);
        holder.tv_user.setText("采集员："+ UserBean.username);
        holder.tv_price.setText("价格："+list.get(i).price+"万");
        Glide.with(ctx).load(list.get(i).img1).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(holder.img_item_mylv);
        return view;
    }
    public class ViewHolder{
        TextView tv_name,tv_company,tv_num1,tv_num2;
        private ImageView img_item_mylv;
        TextView btn_xiajia,tv_user,tv_time,tv_price;
        TextView tv_model_mylvitem;
    }
}
