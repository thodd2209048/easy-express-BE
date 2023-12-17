package com.example.easyexpressbackend.utils;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Utils {
    public static String convertToHumanTime(ZonedDateTime zonedDateTime) {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy - hh:mm:ss").format(zonedDateTime);
    }
}
