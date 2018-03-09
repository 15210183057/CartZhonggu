package utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;


import com.example.a123456.cartzhonggu.R;

import java.util.ArrayList;
import java.util.List;

import bean.NameAndTel;

/**
 * Created by 123456 on 2018/3/3.
 */

public class NameAndTelDialog extends AlertDialog implements View.OnClickListener{
    private Button button,btn_add;
    ListView listView;
    EditText edt_username,edt_tel;
    private LinearLayout linear;
    Context context;
    List<NameAndTel>list;
    String strNameAndTelAndID;
    public NameAndTelDialog(Context context, List<NameAndTel> list ) {
        super(context);
        this.context=context;
        this.list=list;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nameandteldialog);
        linear=findViewById(R.id.linear);
        button=findViewById(R.id.btn_add);
        listView=findViewById(R.id.lv);
        edt_tel=findViewById(R.id.edt_usertel);
        edt_username=findViewById(R.id.edt_username);
        button=findViewById(R.id.btn_ok);
        button.setOnClickListener(this);
        btn_add=findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);
        final List<String>list1=new ArrayList<>();
        final List<String>list2=new ArrayList<>();
        for(int i=0;i<list.size();i++){
            list1.add("姓名="+list.get(i).name.trim()+"       电话="+list.get(i).tel.trim());
            Log.e("TAG","list=id="+list.get(i).id);
            list2.add(list.get(i).id);
        }
        Log.e("TAG","size=="+list2.size()+"==="+!TextUtils.isEmpty(list2.get(0))+"=="+list2.get(0));
        Log.e("TAG","boolean=="+(list1!=null&&!TextUtils.isEmpty(list2.get(0))));
        if(list1!=null&&!TextUtils.isEmpty(list2.get(0))){
            listView.setAdapter(new ArrayAdapter(getContext(),R.layout.item,R.id.tvitem_xiala,list1));
        }else{
            dismiss();
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                strNameAndTelAndID=list.get(i).name.trim()+"&"+list.get(i).tel.trim()+"&"+list.get(i).id;
                Intent intent=new Intent();
                intent.putExtra("strNameAndTelAndID",strNameAndTelAndID);
                intent.setAction("strNameAndTelAndID");
                getContext().sendBroadcast(intent);
                Log.e("TAG","strNameAndTelAndID=="+strNameAndTelAndID);
                dismiss();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_ok:
                String strname=edt_username.getText().toString();
                String strTel=edt_tel.getText().toString();
                dismiss();
                break;
            case R.id.btn_add:
//                listView.setVisibility(View.GONE);
//                linear.setVisibility(View.VISIBLE);
                Intent intent=new Intent();
                intent.putExtra("strNameAndTelAndID",strNameAndTelAndID);
                intent.setAction("strNameAndTelAndID");
                getContext().sendBroadcast(intent);
                Log.e("TAG","strNameAndTelAndID=="+strNameAndTelAndID);
                dismiss();
                break;
        }
    }
}
