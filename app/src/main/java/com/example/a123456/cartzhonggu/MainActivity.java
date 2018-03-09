package com.example.a123456.cartzhonggu;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.example.a123456.cartzhonggu.FrameActivity;
import com.example.a123456.cartzhonggu.LoginActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import base.BaseActivity;
import bean.UserBean;
import utils.AppUtils;
import utils.CommonProgressDialog;

public class MainActivity extends BaseActivity {
    private CommonProgressDialog commonProgressDialog;
    private static final String DOWNLOAD_NAME = "channelWe";
    boolean mShowRequestPermission = true;//用户是否禁止权限
    String newVersion,url,content;//服务器获取的app版本，更新地址url,更新内容；
    int versionCode;//获取当前app版本

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getBan();
//        findViewById(R.id.HelloTV).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(MainActivity.this,TestActivity.class);
//                startActivity(intent);
//            }
//        });
        //停留1秒自动跳转哈哈哈
        Thread myThread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    versionCode = AppUtils.getVersionCode(MainActivity.this);
                    getUpdate(versionCode);
                    Log.e("TAG","y用户名=="+ UserBean.username);
//                    if(!TextUtils.isEmpty(UserBean.username)&&!TextUtils.isEmpty(UserBean.password)){
//                        Intent intent=new Intent(MainActivity.this,FrameActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }else{
//                        Intent intent=new Intent(MainActivity.this,LoginActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        myThread.start();
    }
    //版本更新
    private void getUpdate(int versionCode) {
        //一般情况下是在这里拿本地的版本号和服务器的版本号进行比较的，我没有服务器就模拟拿数据了
        newVersion="1";
        content ="\n"+
                "就不告诉你我们更新了什么-。-\n" +
                "\n" +
                "----------万能的分割线-----------\n" +
                "\n" +
                "1.新产品上线了，界面全新改版\n" +
                "2.修复了若干bug \n";//更新内容
        //http://open.zgcw.cn/app/infoadd.apk
        //http://openbox.mobilem.360.cn/index/d/sid/3429345
       url= "http://openbox.mobilem.360.cn/index/d/sid/3429345"; //安装包下载地址
        double newVersionCode = Double.parseDouble(newVersion);
        int  cc= (int)(newVersionCode);
        if(cc!=versionCode){
            if(cc>versionCode){
                //版本号不同，这时候我们需要开始弹窗了
                //需要强制更新的在这里选择不同的弹窗方式
                setPermissions();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ShowDialog(url,content);
                    }
                });

            }
        }else{
            Log.e("TAG","y用户名=="+ UserBean.username);
//            setPermissions();
            if(!TextUtils.isEmpty(UserBean.username)&&!TextUtils.isEmpty(UserBean.password)){
                Intent intent=new Intent(MainActivity.this,FrameActivity.class);
                startActivity(intent);
                finish();
            }else{
                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    /**
     * 升级版本
     * @param url
     * @param content
     */
    AlertDialog.Builder  builder;
    private void ShowDialog(final String url, String content) {
         builder= new AlertDialog.Builder(this);
        builder.setTitle("版本更新");
        builder.setMessage(content);
        builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //这里进行更新的操作
                dialogInterface.dismiss();
                //提示版本更新的信息消失之后，开始在弹出本地下载的进度条。这里自定义一个dialog
                commonProgressDialog = new CommonProgressDialog(MainActivity.this);
                commonProgressDialog.setCanceledOnTouchOutside(false);
                commonProgressDialog.setTitle("正在下载");
                commonProgressDialog.setCustomTitle(
                        LayoutInflater.from(MainActivity.this).inflate(R.layout.title_dialog,null)
                );
                commonProgressDialog.setIndeterminate(true);
                commonProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                commonProgressDialog.setCancelable(true);

                //downFile(URLData.DOWNLOAD_URL);
                final DownloadTask downloadTask = new DownloadTask(MainActivity.this);
                downloadTask.execute(url);
                commonProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        downloadTask.cancel(true);
                    }
                });

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if(!TextUtils.isEmpty(UserBean.username)&&!TextUtils.isEmpty(UserBean.password)){
                    Intent intent=new Intent(MainActivity.this,FrameActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        builder.create().show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("TAG","Mainactiviyt=mShowRequestPermission="+mShowRequestPermission);
        if(!mShowRequestPermission){
            Log.e("TAG","不再提示==");
        }

    }

    /**
     * 下载应用
     *
     * @author Administrator
     */
    class DownloadTask extends AsyncTask<String, Integer, String> {

        private Context context;
        private PowerManager.WakeLock mWakeLock;

        public DownloadTask(Context context) {
            this.context = context;
        }

        // 下载存储的文件名
        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            File file = null;
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                // expect HTTP 200 OK, so we don't mistakenly save error
                // report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP "
                            + connection.getResponseCode() + " "
                            + connection.getResponseMessage();
                }
                int fileLength = connection.getContentLength();
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    file = new File(Environment.getExternalStorageDirectory(),
                            DOWNLOAD_NAME);

                    if (!file.exists()) {
                        // 判断父文件夹是否存在
                        if (!file.getParentFile().exists()) {
                            file.getParentFile().mkdirs();
                        }
                    }

                } else {
                    Toast.makeText(MainActivity.this, "sd卡未挂载",
                            Toast.LENGTH_LONG).show();
                }
                input = connection.getInputStream();
                output = new FileOutputStream(file);
                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);

                }
            } catch (Exception e) {
                System.out.println(e.toString());
                return e.toString();

            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }
                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
            PowerManager pm = (PowerManager) context
                    .getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();
            commonProgressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
            commonProgressDialog.setIndeterminate(false);
            commonProgressDialog.setMax(100);
            commonProgressDialog.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            mWakeLock.release();
            commonProgressDialog.dismiss();
            Log.e("TAG", "result==" + result);
            if (result != null) {
                // 申请多个权限。
//                AndPermission.with(MainActivity.this)
//                        .requestCode(REQUEST_CODE_PERMISSION_SD)
//                        .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
//                        // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框，避免用户勾选不再提示。
//                        .rationale(rationaleListener
//                        )
//                        .send();
//                setPermissions();
                Toast.makeText(context, "您未打开SD卡权限,安装失败，请重新打开APP" , Toast.LENGTH_LONG).show();
            } else {
                // Toast.makeText(context, "File downloaded",
                // Toast.LENGTH_SHORT)
                // .show();
                update();
            }

        }
    }

    private void update() {
        //安装应用
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(MainActivity.this, "com.example.a123456.zhonggu.fileprovider", new File(Environment
                    .getExternalStorageDirectory(), DOWNLOAD_NAME));
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(new File(Environment
                    .getExternalStorageDirectory(), DOWNLOAD_NAME)), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
//        MainActivity.this.startActivity(intent);
        this.startActivityForResult(intent,2);
    }

    static final String[] PERMISSION = new String[]{
//            Manifest.permission.READ_CONTACTS,// 写入权限
            Manifest.permission.READ_EXTERNAL_STORAGE,  //读取权限
//            Manifest.permission.WRITE_CALL_LOG,        //读取设备信息
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    /**
     * 设置Android6.0的权限申请
     */
    private void setPermissions() {
        Log.e("TAG","是否开启权限=="+(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED));
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //Android 6.0申请权限
            ActivityCompat.requestPermissions(this,PERMISSION,1);
        }else{
//            int versionCode = AppUtils.getVersionCode(MainActivity.this);
//            getUpdate(versionCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("TAG","权限requestCode=="+requestCode);
        switch (requestCode){
            case 1:
                for(int i = 0; i < grantResults.length;i++){
                    Log.e("TAG","这里再次判断=="+(grantResults[i]!=PackageManager.PERMISSION_GRANTED));
                      if(grantResults[i]!=PackageManager.PERMISSION_GRANTED){
         //判断是否勾选禁止后不再询问
                        boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permissions[i]);
                       Log.e("TAG","show=="+showRequestPermission);
                        if (showRequestPermission) {//
//                            setPermissions();//重新申请权限
//                            mShowRequestPermission=true;

//                            if(!TextUtils.isEmpty(UserBean.username)&&!TextUtils.isEmpty(UserBean.password)){
//                                Intent intent=new Intent(MainActivity.this,FrameActivity.class);
//                                startActivity(intent);
//                                finish();
//                            }else{
//                                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
//                                startActivity(intent);
//                                finish();
//                            }
                            return;
                       }else{
//                            Log.e("TAG","这里是else=="+showRequestPermission);
                            mShowRequestPermission=false;//已经禁止
                       }
                }else{

                }
            }
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("TAG","onActivityResult方法=requestCode="+requestCode+"=resultCode"+resultCode+"=data");
        if(requestCode==2){
            if(!TextUtils.isEmpty(UserBean.username)&&!TextUtils.isEmpty(UserBean.password)){
                Intent intent=new Intent(MainActivity.this,FrameActivity.class);
                startActivity(intent);
                finish();
            }else{
                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
