package cn.lunadeer.miniplayertitle.utils;

public class Time {

    public static Integer getCurrent() {
        // return YYYYMMDD
        return Integer.parseInt(new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date()));
    }

    public static Integer getFromTimestamp(Long timestamp_ms) {
        return Integer.parseInt(new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date(timestamp_ms)));
    }
}
