package com.example.aiqing.aibotserver;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.qihancloud.opensdk.base.BindBaseActivity;
import com.qihancloud.opensdk.beans.FuncConstant;
import com.qihancloud.opensdk.function.unit.SystemManager;

import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends BindBaseActivity {

    private NetworkConnectChangedReceiver netWorkStateReceiver;
    private Socket mSocket;
    private PrintWriter mPw;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SystemManager systemManager = (SystemManager) getUnitManager(FuncConstant.SYSTEM_MANAGER);
        String de = systemManager.getDeviceId();

        //拼接 con:12345678
        final String deviceId = "con:" + de;
        Log.e("首页获取机器码", "onCreate: " + deviceId);
        //存储机器码到首选项
        SharedPreferences sp = getSharedPreferences("DeviceId", MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("deviceId", deviceId);
        edit.commit();
        finish();

        Toast.makeText(this, "111111111111111", Toast.LENGTH_SHORT).show();
        Intent intent9 = new Intent("com.sunbo.main.LOCKROBOT");
        intent9.putExtra("status", 3);
        sendBroadcast(intent9);

        startService(new Intent(this, MainService.class));

//        ChatRoomClient.getInstance().CreatSocket(deviceId,this);
    }

    protected void onMainServiceConnected() {
    }

    public void onPause() {
        unregisterReceiver(netWorkStateReceiver);
        System.out.println("注销");
        super.onPause();
    }

    public void onResume() {
       if (netWorkStateReceiver == null){
        netWorkStateReceiver = new NetworkConnectChangedReceiver();
        IntentFilter localIntentFilter = new IntentFilter();
        localIntentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        localIntentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        localIntentFilter.addAction("android.intent.action.BOOT_COMPLETED");
        registerReceiver(netWorkStateReceiver, localIntentFilter);
        System.out.println("注册");
    }
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
