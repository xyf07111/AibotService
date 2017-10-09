package com.example.aiqing.aibotserver;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;

import com.qihancloud.opensdk.base.BindBaseActivity;
import com.qihancloud.opensdk.beans.FuncConstant;
import com.qihancloud.opensdk.function.unit.SystemManager;

import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends BindBaseActivity {
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
//        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
//        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//        registerReceiver(new NetworkConnectChangedReceiver(), filter);
//
//        //获取机器码
//        SystemManager systemManager  = (SystemManager) getUnitManager(FuncConstant.SYSTEM_MANAGER);
//        String deviceId = systemManager.getDeviceId();
//        Log.e("机器码", "onCreate: "+ deviceId);
//    }
//
//    @Override
//    protected void onMainServiceConnected() {
//        try {
//            //获取服务器信息
//            ChatRoomClient chatRoomClient = new ChatRoomClient("", 0000);
//            String message = chatRoomClient.reciveMessage();
//            Log.e("小宝", "onMainServiceConnected: "+ message);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    private NetworkConnectChangedReceiver netWorkStateReceiver;
    private Socket mSocket;
    private PrintWriter mPw;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService(new Intent(this, MainService.class));

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

        Intent intent = new Intent("com.sunbo.main.LOCKROBOT");
        intent.putExtra("status", 3);
        sendBroadcast(intent);

        ChatRoomClient.getInstance().CreatSocket(deviceId,this);

        if (netWorkStateReceiver == null)
            netWorkStateReceiver = new NetworkConnectChangedReceiver();
        IntentFilter localIntentFilter = new IntentFilter();
        localIntentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        localIntentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netWorkStateReceiver, localIntentFilter);
        System.out.println("注册");
    }

    protected void onMainServiceConnected() {
    }

    public void onPause() {
        unregisterReceiver(netWorkStateReceiver);
        System.out.println("注销");
        super.onPause();
    }

    public void onResume() {
//        if (netWorkStateReceiver == null)
//            netWorkStateReceiver = new NetworkConnectChangedReceiver();
//        IntentFilter localIntentFilter = new IntentFilter();
//        localIntentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
//        localIntentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//        registerReceiver(netWorkStateReceiver, localIntentFilter);
//        System.out.println("注册");
        super.onResume();
    }
}
