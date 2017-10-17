package com.example.aiqing.aibotserver;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;

import com.qihancloud.opensdk.base.BindBaseService;
import com.qihancloud.opensdk.beans.FuncConstant;
import com.qihancloud.opensdk.function.unit.SystemManager;

/**
 * Created by aiqing on 2017/9/22.
 */

public class MainService extends BindBaseService {

  //  private String mDeviceId;
    private CountDownTimer mTimer;
    private SystemManager mUnitManager;
    private String mDeviceId;

    public IBinder onBind(Intent paramIntent) {

        return null;
    }

    public void onCreate() {
       // Toast.makeText(this, "onCreate()", Toast.LENGTH_SHORT).show();
        SystemManager systemManager = (SystemManager) getUnitManager(FuncConstant.SYSTEM_MANAGER);
        String de = systemManager.getDeviceId();
        //拼接 con:12345678
        final String deviceId = "con:" + de;
        ChatRoomClient.getInstance().CreatSocket(deviceId,this);
    }

    public void onDestroy() {
     //   startService(new Intent(this, MainService.class));
        super.onDestroy();
    }

    protected void onMainServiceConnected() {
    }

    public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2) {

//        if (ChatRoomClient.getInstance().lastTime == null) {
//            Intent intent6=new Intent("com.sunbo.main.LOCKROBOT");
//            intent6.putExtra("status",3);
//            sendBroadcast(intent6);
//        }

        return super.onStartCommand(paramIntent, paramInt1, paramInt2);
    }
}
