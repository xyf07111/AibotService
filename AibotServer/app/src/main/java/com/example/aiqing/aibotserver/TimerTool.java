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
        if (str1==null){
            return;
        }
        Log.e("取出的时间", "initInternet: " + str1);
        mGetData = StringTime.getStringToDate(str1);
        if (mTimer != null) {
            stopTimer();
        }
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.e("计时器信息", "run: " + "88888888");
                String currentStr =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                Long currentTime =  StringTime.getStringToDate(currentStr);
                //当租赁时间到期停止计时器
                if ( currentTime - mGetData > 0) {
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
