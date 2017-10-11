package com.example.aiqing.aibotserver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by aiqing on 2017/9/22.
 */

public class NetworkConnectChangedReceiver extends BroadcastReceiver {

    private String mDeviceId;
    @Override
    public void onReceive(Context context, Intent intent) {

        mDeviceId = context.getSharedPreferences("DeviceId", Context.MODE_PRIVATE).getString("deviceId", "");
        Log.e("mDeviceId", "onReceive: " + mDeviceId);
        System.out.println("网络状态发生变化");
        if (Build.VERSION.SDK_INT < 21) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo localNetworkInfo2 = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if ((localNetworkInfo2.isConnected())) {
                Toast.makeText(context, "网络已连接", Toast.LENGTH_SHORT).show();
                ChatRoomClient.getInstance().CreatSocket(mDeviceId, context);
                return;
            }
            if (!(localNetworkInfo2.isConnected())) {
                Toast.makeText(context, "网络已断开", Toast.LENGTH_SHORT).show();
                ChatRoomClient.getInstance().CreatSocket(mDeviceId, context);
            }
        } else if (Build.VERSION.SDK_INT > 23) {
            TimerTool.getInstance().initTime(context);
            Log.e("计时器停止", "onReceive: API大于23时使用下面的方式进行网络监听");
            System.out.println("API level 大于23");
            ConnectivityManager localConnectivityManager1 = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            Network[] arrayOfNetwork = localConnectivityManager1.getAllNetworks();
            StringBuilder localStringBuilder = new StringBuilder();
            for (int i = 0; i < arrayOfNetwork.length; i++) {
                NetworkInfo localNetworkInfo1 = localConnectivityManager1.getNetworkInfo(arrayOfNetwork[i]);
                localStringBuilder.append(localNetworkInfo1.getTypeName() + " connect is " + localNetworkInfo1.isConnected());
            }
            Toast.makeText(context, localStringBuilder.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
