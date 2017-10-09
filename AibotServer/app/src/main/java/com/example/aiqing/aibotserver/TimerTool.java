package com.example.aiqing.aibotserver;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by aiqing on 2017/9/25.
 */

public class TimerTool {
    private static TimerTool instance = null;
    public Context mContext;
    private long mGetData;
    private Timer mTimer;

    public static TimerTool getInstance() {
        if (instance == null)
            instance = new TimerTool();
        return instance;
    }

    private void stopTimer() {
        if (this.mTimer != null) {
            this.mTimer.cancel();
            this.mTimer = null;
        }
    }

    public void initTime(final Context context) {
        String str1 = context.getSharedPreferences("DeviceId", Context.MODE_PRIVATE).getString("lasttime", "");
        Log.e("取出的时间", "initInternet: " + str1);
        mGetData = StringTime.getStringToDate(str1);
        if (mTimer != null)
            stopTimer();
        String str2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        if (StringTime.getStringToDate(str2) - mGetData > 0) {
            Log.e("租赁到期", "initTime: 租赁到期" + str2);
            return;
        }
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
              public void run() {
                   Log.e("星期五", "run: 星期五");
                   if (StringTime.getStringToDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())) - TimerTool.this.mGetData > 0) {
                   Log.e("计时器停止", "run: 计时器停止");
                   TimerTool.this.stopTimer();
                   Intent localIntent = new Intent("com.sunbo.main.LOCKROBOT");
                   localIntent.putExtra("status", 3);
                   context.sendBroadcast(localIntent);
                   }
                  }
                }, 0, 1000);
    }
}
