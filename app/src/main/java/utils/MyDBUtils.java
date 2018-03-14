package utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import bean.Bran;

/**
 * Created by 123456 on 2018/3/14.
 * 增删该查
 */

public class MyDBUtils {
    private MyDateDB myDateDB;
    String dbName="bulu";
    SQLiteDatabase database;
    MyDBUtils myDBUtils;
    public MyDBUtils(Context context){
        myDateDB=new MyDateDB(context,dbName,null,1);
    }
    //查询操作
    public  List<Bran> chaXun() {
        SQLiteDatabase db = myDateDB.getWritableDatabase();
        ArrayList<Bran> list = new ArrayList<Bran>();
        Cursor cursor = db.rawQuery("select * from " + dbName, null);
        for (int i = 0; i < cursor.getCount(); i++) {
            Bran bran = new Bran();
            cursor.moveToNext();
            bran.itemid = cursor.getString(1);
            bran.cheshang = cursor.getString(2);
            bran.chengshangid = cursor.getString(3);
            bran.vin = cursor.getString(4);
            bran.date = cursor.getString(5);
            bran.name = cursor.getString(6);
            bran.tel = cursor.getString(7);
            bran.brandname = cursor.getString(8);
            bran.brandid= cursor.getString(9);
            bran.serisename = cursor.getString(10);
            bran.sericseid = cursor.getString(11);
            bran.modelname = cursor.getString(12);
            bran.modelnameid = cursor.getString(13);
            bran.fenleiname = cursor.getString(14);
            bran.fenleiid = cursor.getString(15);
            bran.guohuname = cursor.getString(16);
            bran.guohuid = cursor.getString(17);
            bran.licheng = cursor.getString(18);
            bran.price = cursor.getString(19);
            bran.img1 = cursor.getString(20);
            bran.img2 = cursor.getString(21);
            bran.img3 = cursor.getString(22);
            bran.img4 = cursor.getString(23);
            bran.img5 = cursor.getString(24);
            bran.img6 = cursor.getString(25);
            bran.img7 = cursor.getString(26);
            bran.img8 = cursor.getString(27);
            bran.img9 = cursor.getString(28);
            list.add(bran);
        }
        cursor.close();
        db.close();
        if(list.size()>0) {
            Log.e("TAG", "查询结果list==" + list.size() + "==" + list.get(list.size() - 1).modelname);
        }
        return list;
    }
//添加
    public void addBuLu(String[]strings){
        database = myDateDB.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("id",strings[0].toString());
        values.put("cheshang",strings[1].toString());
        values.put("cheshangid",strings[2].toString());
        values.put("vin",strings[3].toString());
        values.put("date",strings[4].toString());

        values.put("name",strings[5].toString());
        values.put("tel",strings[6].toString());
        values.put("brandname",strings[7].toString());
        values.put("brandid",strings[8].toString());
        Log.e("TAG","string{9}="+strings[9].toString());
        values.put("serisename",strings[9].toString());

        values.put("seriseid",strings[10].toString());
        values.put("modelname",strings[11].toString());
        values.put("modelid",strings[12].toString());
        values.put("cartfenlei",strings[13].toString());
        values.put("cartfenleiid",strings[14].toString());

        values.put("guohuname",strings[15].toString());
        values.put("guohuid",strings[16].toString());
        values.put("licheng",strings[17].toString());
        values.put("price",strings[18].toString());
        values.put("img1",strings[19].toString());

        values.put("img2",strings[20].toString());
        values.put("img3",strings[21].toString());
        values.put("img4",strings[22].toString());
        values.put("img5",strings[23].toString());
        values.put("img6",strings[24].toString());

        values.put("img7",strings[25].toString());
        values.put("img8",strings[26].toString());
        Log.e("TAG","string[27]=="+strings[27].toString());
        values.put("img9",strings[27].toString());
        database.insert(dbName,null,values);
        database.close();
    }
    //修改
    public void setBulu(String []str){
        database = myDateDB.getWritableDatabase();
        database.execSQL("update "+dbName+" set id=?,cheshang=?,cheshangid=?,vin=?,date=?," +
                "name=?,tel=?,brandname=?,brandid=?,"+
                "serisename=?,seriseid=?,modelname=?,modelid=?,"+
                "cartfenlei=?,cartfenleiid=?,guohuname=?,guohuid=?,"+
                "licheng=?,price=?,img1=?,img2=?,"+
                "img3=?,img4=?,img5=?,img6=?,"+
                "img7=?,img8=?,img9=? "+
                "where id="+str[0].toString(),str);
        database.close();
    }
    //删除
    public void setDelete(String id){
        database= myDateDB.getWritableDatabase();
        database.delete(dbName," id=? ",new String[]{id});
    }
}
