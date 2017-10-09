package com.example.aiqing.aibotserver;

import android.content.Context;
import android.content.Intent;

/**
 * Created by aiqing on 2017/9/25.
 */

public class GotoHome {
    public void gotoHome(Context paramContext)
    {
        Intent localIntent1 = new Intent();
        localIntent1.setAction("android.intent.action.MAIN");
        localIntent1.addCategory("android.intent.category.HOME");
        localIntent1.addFlags(268435456);
        paramContext.startActivity(localIntent1);
        Intent localIntent2 = new Intent();
        localIntent2.setAction("com.qihancloud.gohome");
        paramContext.sendBroadcast(localIntent2);
    }
}
