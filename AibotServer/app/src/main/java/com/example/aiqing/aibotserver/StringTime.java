package com.example.aiqing.aibotserver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by aiqing on 2017/9/25.
 */

public class StringTime {
    public static String getStrTime(int paramInt) {
        return new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒").format(new Date(1000L * Long.valueOf(1291778220).longValue()));
    }

    public static long getStringToDate(String paramString) {
        SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Object localObject = new Date();
        try {
            Date localDate = localSimpleDateFormat.parse(paramString);
            localObject = localDate;
            return ((Date)localObject).getTime();
        }
        catch (ParseException localParseException) {
            while (true)
                localParseException.printStackTrace();
        }
    }
}
