package com.example.aiqing.aibotserver;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

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

    public static ChatRoomClient getInstance() {
        if (instance == null) {
            instance = new ChatRoomClient();
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
                        Log.e("收到消息", "run: "+message );
                        if (message == null) {
                            Intent localIntent1 = new Intent("com.sunbo.main.LOCKROBOT");
                            localIntent1.putExtra("status", 3);
                            context.sendBroadcast(localIntent1);
                            return;
                        }
                        System.out.println("第一次服务端发来信息:" + message);
                        Log.e("第一次服务端发来信息", "run: " + message);
                        String[] arrayOfString = message.split("@");

                        //send@2@2017-10-09 11:45:00@null@2017-10-09 11:45:00

                        String str1 = arrayOfString[1];//状态
                        String str2 = arrayOfString[(-2 + arrayOfString.length)];//2017-10-23 15:42:31 到期时间
                        String str3 = arrayOfString[(-1 + arrayOfString.length)];//2017-09-28 14:51:10 系统时间
                        String str4 = arrayOfString[(-3 + arrayOfString.length)];//2017-09-23 15:42:31 开租时间

                        if (str2==null){
                            //买断
                            Intent intent = new Intent("com.sunbo.main.LOCKROBOT");
                            intent.putExtra("status",4);
                            context.sendBroadcast(intent);
                        }

                        long stringToDate = StringTime.getStringToDate(str3);
                        //  ChatRoomClient.access$102(ChatRoomClient.this, StringTime.getStringToDate(str3));
                        Log.e("666", "run: " + stringToDate);
                        //Log.e("666", "run: " + ChatRoomClient.this.mStringToDate);
                        Intent localIntent2 = new Intent("com.qihancloud.setting.RESET_TIME");
                        localIntent2.putExtra("time", stringToDate);
//						localIntent2.putExtra("time", ChatRoomClient.this.mStringToDate);
                        localIntent2.putExtra("auto", 0);
                        context.sendBroadcast(localIntent2);
                        SharedPreferences.Editor localEditor = context.getSharedPreferences("DeviceId", Context.MODE_PRIVATE).edit();
                        localEditor.putString("lasttime", str2);
                        localEditor.putString("mDeviceId", paramString.toString());
                        localEditor.commit();
                        Log.e("数据", "run: 状态是" + str1 + "到期时间是" + str2);
                        if (str1.equals("0")) {
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
                            localIntent4.putExtra("remainTime", l3 / 1000L);
                            context.sendBroadcast(localIntent4);

                        } else if (str1.equals("2")) {
                            Intent localIntent3 = new Intent("com.sunbo.main.LOCKROBOT");
                            localIntent3.putExtra("status", 4);
                            context.sendBroadcast(localIntent3);
                        }
                        //  TimerTool.getInstance().initTime(this);
                    }
                } catch (Exception localIOException) {
                    Log.e("连接异常", "CreatSocket: "+localIOException );
                    localIOException.printStackTrace();
                    Log.e("创建socket失败", "run: 创建socket失败");
                    TimerTool.getInstance().initTime(context);
                }
        Toast.makeText(context, "断开连接", Toast.LENGTH_SHORT).show();
            }
        }.start();
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
