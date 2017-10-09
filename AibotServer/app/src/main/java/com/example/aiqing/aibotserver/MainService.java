package com.example.aiqing.aibotserver;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import com.qihancloud.opensdk.base.BindBaseService;
import com.qihancloud.opensdk.beans.FuncConstant;
import com.qihancloud.opensdk.function.unit.SystemManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by aiqing on 2017/9/22.
 */

public class MainService extends BindBaseService {

    private String mDeviceId;
    private CountDownTimer mTimer;
    private SystemManager mUnitManager;

    private void initConnectHttp() {
        ChatRoomClient.getInstance().CreatSocket(mDeviceId, getApplicationContext());
    }

    private void initInternet() {
        // NetworkInfo localNetworkInfo = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                initConnectHttp();
            } else if (networkInfo != null && networkInfo.getState() != NetworkInfo.State.DISCONNECTED) {
                initConnectHttp();
                Toast.makeText(this, "没有链接网络，请检查网络连接！", Toast.LENGTH_SHORT).show();
                String str = getSharedPreferences("data_last", MODE_PRIVATE).getString("lasttime", "");
                Log.e("取出的时间", "initInternet: " + str);
            mTimer = new CountDownTimer(StringTime.getStringToDate(str), 1000) {
                public void onFinish() {
                    Log.e("结束了", "onFinish: finish");
                    Intent localIntent = new Intent("com.sunbo.main.LOCKROBOT");
                    localIntent.putExtra("status", 3);
                    MainService.this.sendBroadcast(localIntent);
                }

                public void onTick(long paramAnonymousLong) {
                    new SimpleDateFormat("yyyy-MM-dd HH:mm").setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
                    Calendar localCalendar = Calendar.getInstance();
                    int i = localCalendar.get(Calendar.YEAR);
                    int j = 1 + localCalendar.get(Calendar.MONTH);
                    int k = localCalendar.get(Calendar.DATE);
                    int m = localCalendar.get(Calendar.HOUR);
                    int n = localCalendar.get(Calendar.MINUTE);
                    int i1 = localCalendar.get(Calendar.SECOND);
                    Log.e("日期时间", "onTick: " + i + ":" + j + ":" + k + "" + m + ":" + n + ":" + i1);
                }
            };
            }
        }
    }
    public IBinder onBind(Intent paramIntent) {
        return null;
    }

    public void onCreate() {
    }

    public void onDestroy() {
        startService(new Intent(this, MainService.class));
        super.onDestroy();
    }

    protected void onMainServiceConnected() {
    }

    public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2) {
        mUnitManager = ((SystemManager) getUnitManager(FuncConstant.SYSTEM_MANAGER));
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
        mUnitManager.doHomeAction();
        mDeviceId = mUnitManager.getDeviceId();
        mDeviceId = ("con:" + mDeviceId);
       // initInternet();
        return super.onStartCommand(paramIntent, paramInt1, paramInt2);
    }
}
