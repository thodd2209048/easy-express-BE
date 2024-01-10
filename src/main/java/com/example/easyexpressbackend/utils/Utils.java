package com.example.easyexpressbackend.utils;

import com.example.easyexpressbackend.exception.InvalidValueException;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class Utils {
    public static String convertToHumanTime(ZonedDateTime zonedDateTime) {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy - hh:mm:ss").format(zonedDateTime);
    }
}
