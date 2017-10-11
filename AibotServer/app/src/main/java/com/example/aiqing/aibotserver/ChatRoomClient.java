package com.example.aiqing.aibotserver;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatRoomClient {

    private static ChatRoomClient instance = null;
    private BufferedReader br;
    private String mS;
    private long mStringToDate;
    private PrintWriter pw;
    private Socket s;
    public String lastTime = null;

    public static  ChatRoomClient getInstance() {
        if (instance == null) {
            synchronized (ChatRoomClient.class){
                if (instance == null) {
                    instance = new ChatRoomClient();
                }
            }
        }
        return instance;
    }

    public void CreatSocket(final String paramString, final Context context) {

        new Thread() {
            public void run() {
                try {
                    InitSocket();
                    sendMessage(paramString.toString());
                    while (true) {
                        String message = br.readLine();
                        //send@1@2017-10-10 11:22:05@2017-11-10 11:22:14@2017-10-10 14:20:43
                        Log.e("收到消息", "run: "+message );
                        //Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        if (message == null) {
                            continue;
//                            Log.e("收到消息未空", "run: " + message+"isconnected"+s.isConnected());
//                            if (instance.lastTime == null || s.isConnected()) {
//                                return;
//                            }
//                            Log.e("收到消息未空------", "run: "+"isconnected"+s.isConnected());
//                            Intent localIntent1 = new Intent("com.sunbo.main.LOCKROBOT");
//                            localIntent1.putExtra("status", 3);
//                            context.sendBroadcast(localIntent1);
//                            return;
                        }
                        Log.e("第一次服务端发来信息", "run: " + message);
                        String[] arrayOfString = message.split("@");

                        //send@2@2017-10-09 11:45:00@null@2017-10-09 11:45:00

                        String str1 = arrayOfString[1];//状态
                        String str2 = arrayOfString[(-2 + arrayOfString.length)];//2017-10-23 15:42:31 到期时间
                        String str3 = arrayOfString[(-1 + arrayOfString.length)];//2017-09-28 14:51:10 系统时间
                        String str4 = arrayOfString[(-3 + arrayOfString.length)];//2017-09-23 15:42:31 开租时间

                        long stringToDate = StringTime.getStringToDate(str3);
                        Intent localIntent2 = new Intent("com.qihancloud.setting.RESET_TIME");
                        localIntent2.putExtra("time", stringToDate);
                        localIntent2.putExtra("auto", 0);
                        context.sendBroadcast(localIntent2);

                       lastTime=str2;

                        SharedPreferences.Editor localEditor = context.getSharedPreferences("DeviceId", Context.MODE_PRIVATE).edit();
                        localEditor.putString("lasttime", str2);
                        localEditor.putString("mDeviceId", paramString.toString());
                        localEditor.commit();

                        if (str1.equals("0")) {
                            Log.e("锁定0==", "锁定0==");
                            new GotoHome().gotoHome(context);
                            Intent localIntent5 = new Intent("com.sunbo.main.LOCKROBOT");
                            localIntent5.putExtra("status", 3);
                            context.sendBroadcast(localIntent5);
                        } else if (str1.equals("1")) {
                            Log.e("开启租赁", "run: 开启租赁");
                            Intent localIntent4 = new Intent("com.sunbo.main.LOCKROBOT");
                            localIntent4.putExtra("status", 2);
                            long l1 = StringTime.getStringToDate(str4);
                            long l2 = StringTime.getStringToDate(str2);
                            localIntent4.putExtra("startTime", l1);
                            long l3 = l2 - l1;
                            Log.e("123456", "run: 租赁开始时间戳" + l1 + "结束时间戳" + l2 + "差" + l3);
                            localIntent4.putExtra("endTime", l2);
                            localIntent4.putExtra("remainTime", l3 / 1000);
                            context.sendBroadcast(localIntent4);
                            TimerTool.getInstance().initTime(context);
                        } else if (str1.equals("2")) {
                            Intent localIntent3 = new Intent("com.sunbo.main.LOCKROBOT");
                            localIntent3.putExtra("status", 4);
                            context.sendBroadcast(localIntent3);
                        }
                        //  TimerTool.getInstance().initTime(this);
                    }
                } catch (Exception localIOException) {
                    localIOException.printStackTrace();
                    if (instance.lastTime == null) {
                        Log.e("lastTime==null", "空的时间");
                        Intent intent2 = new Intent("com.sunbo.main.LOCKROBOT");
                        intent2.putExtra("status", 3);
                        context.sendBroadcast(intent2);
                    }else{
                        TimerTool.getInstance().initTime(context);
                    }
                }
            }
        }.start();
       // Toast.makeText(context, "断开连接", Toast.LENGTH_SHORT).show();

    }

    public void InitSocket() throws IOException {
//		s = new Socket("relay.aqcome.com", 8881); //正式
//        s = new Socket("120.132.117.157", 8882);//测试
        s = new Socket("relay.aqcome.com", 8882);//测试
        br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        pw = new PrintWriter(s.getOutputStream());
    }

    public void close() {
        try {
            this.s.close();
            return;
        } catch (IOException localIOException) {
            localIOException.printStackTrace();
        }
    }

    public String reciveMessage() {
        try {
            String str = this.br.readLine();
            return str;
        } catch (IOException localIOException) {
            localIOException.printStackTrace();
        }
        return null;
    }

    public void sendMessage(String paramString) {
        this.pw.println(paramString);
        this.pw.flush();
    }
}
