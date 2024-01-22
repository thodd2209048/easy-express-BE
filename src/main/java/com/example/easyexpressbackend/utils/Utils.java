package com.example.easyexpressbackend.utils;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Utils {
    public static String convertToHumanTime(ZonedDateTime zonedDateTime) {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy - hh:mm:ss").format(zonedDateTime);
    }

    public static ZonedDateTime getStartOfDate(ZonedDateTime now){
        ZonedDateTime nowInVietnamZone = now.withZoneSameLocal(ZoneId.of("Asia/Ho_Chi_Minh"));
        return nowInVietnamZone.withHour(0).withMinute(0).withSecond(0).withNano(0);
    }

    public static ZonedDateTime getEndOfDate(ZonedDateTime now){
        ZonedDateTime nowInVietnamZone = now.withZoneSameLocal(ZoneId.of("Asia/Ho_Chi_Minh"));
        return nowInVietnamZone.withHour(0).withMinute(0).withSecond(0).withNano(0).minusNanos(1);
    }
}
