package application;

import android.Manifest;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import org.xutils.x;

import bean.UserBean;
import utils.SharedUtils;

public class MyApplication extends Application {
//    public static String username;
//    public static String password;
    public static MyApplication myApplication;
    public UserBean userBean=new UserBean();
    public SharedUtils sharedUtils;
    public static String usermsg;//用户信息文件名
    public static String cartlistmsg;//
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(true);
        sharedUtils=new SharedUtils();
        userBean.username= sharedUtils.readXML(usermsg,"username",this);
        userBean.password= sharedUtils.readXML(usermsg,"password",this);
        userBean.groupids=sharedUtils.readXML(usermsg,"groupids",this);
        userBean.id=sharedUtils.readXML(usermsg,"userid",this);
    }
    public static MyApplication getInstance(){
        myApplication=new MyApplication();
        return myApplication;
    }
}
