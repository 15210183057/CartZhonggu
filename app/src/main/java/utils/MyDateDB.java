package utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by 123456 on 2018/3/9.
 */

public class MyDateDB extends SQLiteOpenHelper {

    public static final String CREATE_BOOK = "create table bulu (" +
            "_id integer primary key autoincrement, " +
            "id integer, "+
            "cheshang text, " +
            "cheshangid text, "+
            "vin text, "+
            "date text, "+
            "name text, "+
            "tel text, "+
            "brandname text, "+
            "brandid text, "+
            "serisename text, "+
            "seriseid text, "+
            "modelname text, "+
            "modelid text, "+
            "cartfenlei text, "+
            "cartfenleiid text, "+
            "guohuname text, "+
            "guohuid text, "+
            "licheng text, "+
            "price real, " +
            "img1 text, "+
            "img2 text, "+
            "img3 text, "+
            "img4 text, "+
            "img5 text, "+
            "img6 text, "+
            "img7 text, "+
            "img8 text, "+
            "img9 text, "+
            "picID text)";

    private Context mContext;

    public MyDateDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

        mContext = context;
    }

    /**
     * 数据库已经创建过了， 则不会执行到，如果不存在数据库则会执行
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BOOK); // 执行这句才会创建表

        Toast.makeText(mContext, "创建 succeeded", Toast.LENGTH_SHORT).show();

    }

    /**
     * 创建数据库时不会执行，增大版本号升级时才会执行到
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 在这里面可以把旧的表 drop掉 从新创建新表，
        // 但如果数据比较重要更好的做法还是把旧表的数据迁移到新表上，比如升级qq聊天记录被删掉肯定招骂
        Toast.makeText(mContext, "onUpgrade oldVersion：" + oldVersion + " newVersion:" + newVersion, Toast.LENGTH_SHORT).show();
    }
}
