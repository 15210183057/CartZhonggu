package utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 本地xml保存工具类
 * */
public class SharedUtils {

	private String name = "usermsg";

	public void saveXML(String name,String key,String value,Context ctx){
		SharedPreferences shared = ctx.getSharedPreferences(name, 0);
		Editor edt = shared.edit();
		edt.putString(key, value);
		edt.commit();
	}

	public String readXML(String name,String key,Context ctx){
		String result = null;
		SharedPreferences shared = ctx.getSharedPreferences(name, 0);
		result = shared.getString(key, "");
		return result;
	}

}
