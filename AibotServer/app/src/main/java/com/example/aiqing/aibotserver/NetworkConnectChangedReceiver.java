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

//        // 监听wifi的连接状态即是否连上了一个有效无线路由
//        if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
//            Parcelable parcelableExtra = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
//            if (null != parcelableExtra) {
//                // 获取联网状态的NetWorkInfo对象
//                NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
//                //获取的State对象则代表着连接成功与否等状态
//                NetworkInfo.State state = networkInfo.getState();
//                //判断网络是否已经连接
//                boolean isConnected = state == NetworkInfo.State.CONNECTED;
//                Log.e("TAG", "isConnected:" + isConnected);
//                if (isConnected) {
//                    Toast.makeText(context, "已经连接", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(context, "连接失败", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }



     // 监听网络连接，包括wifi和移动数据的打开和关闭,以及连接上可用的连接都会接到监听
//        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
//            //获取联网状态的NetworkInfo对象
//            NetworkInfo info = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
//            if (info != null) {
//                //如果当前的网络连接成功并且网络连接可用
//                if (NetworkInfo.State.CONNECTED == info.getState() && info.isAvailable()) {
//                    if (info.getType() == ConnectivityManager.TYPE_WIFI || info.getType() == ConnectivityManager.TYPE_MOBILE) {
//                        Toast.makeText(context, "连上网络", Toast.LENGTH_SHORT).show();
//                        Log.e(TAG, "onReceive: " + "连上");
//                        //TODO 访问服务器
//                        String deviceId = context.getSharedPreferences("DeviceId", Context.MODE_PRIVATE).getString("deviceId", "");
//                        Log.e("设备码", "onReceive: "+ deviceId);
//                    }
//                } else {
//                    Toast.makeText(context, "未连上网络", Toast.LENGTH_SHORT).show();
//                    Log.e(TAG, "onReceive: " + "未连上");
//                    //TODO 启动定时器
//
//                }
//            }
//        }

        mDeviceId = context.getSharedPreferences("DeviceId", Context.MODE_PRIVATE).getString("deviceId", "");
        Log.e("mDeviceId", "onReceive: " + mDeviceId);
        System.out.println("网络状态发生变化");
        if (Build.VERSION.SDK_INT < 21) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo localNetworkInfo2 = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo localNetworkInfo3 = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if ((localNetworkInfo2.isConnected()) && (localNetworkInfo3.isConnected())) {
                Toast.makeText(context, "WIFI已连接,移动数据已连接", Toast.LENGTH_SHORT).show();
                ChatRoomClient.getInstance().CreatSocket(mDeviceId, context);
                Log.e("计时器停止", "onReceive: WIFI已连接,移动数据已连接");
                return;
            }
            if ((localNetworkInfo2.isConnected()) && (!localNetworkInfo3.isConnected())) {
                Log.e("计时器停止", "onReceive: WIFI已连接,移动数据已断开");
                ChatRoomClient.getInstance().CreatSocket(mDeviceId, context);
                Toast.makeText(context, "WIFI已连接,移动数据已断开", Toast.LENGTH_SHORT).show();
                return;
            }
            if ((!localNetworkInfo2.isConnected()) && (localNetworkInfo3.isConnected())) {
                Log.e("计时器停止", "onReceive: WIFI已断开,移动数据已连接");
                ChatRoomClient.getInstance().CreatSocket(mDeviceId, context);
                Toast.makeText(context, "WIFI已断开,移动数据已连接", Toast.LENGTH_SHORT).show();
                return;
            }
//            Toast.makeText(context, "WIFI已断开,移动数据已断开", Toast.LENGTH_SHORT).show();
//            TimerTool.getInstance().initTime(context);
//            Log.e("计时器停止", "onReceive: WIFI已断开,移动数据已断开");
//            return;
        }else if (Build.VERSION.SDK_INT>23) {
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
