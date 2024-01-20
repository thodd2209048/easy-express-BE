package com.example.easyexpressbackend.domain.pickUpOrder.utils;

import com.example.easyexpressbackend.exception.ActionNotAllowedException;
import com.example.easyexpressbackend.exception.InvalidValueException;

import java.time.*;

public class PickUpOrderUtils {
    public static boolean isValidStartPickUpTime(ZonedDateTime startTime) {
        ZonedDateTime after30Minutes = ZonedDateTime.now().plusMinutes(30);
        if (startTime.isBefore(after30Minutes))
            throw new InvalidValueException("The start time must be at least 30 minutes after the current time.");

        if (startTime.getMinute() != 0 && startTime.getMinute() != 30)
            throw new InvalidValueException("The start time must be on the hour or half hour.");

        ZonedDateTime startTimeInVietnamZoneId = startTime.withZoneSameInstant(ZoneId.of("Asia/Ho_Chi_Minh"));
        LocalDateTime startTimeLocal = startTimeInVietnamZoneId.toLocalDateTime();

        LocalDateTime earliestAllowedTime = startTimeLocal.withHour(8).withMinute(0).withSecond(0);
        LocalDateTime latestAllowedTime = startTimeLocal.withHour(17).withMinute(0).withSecond(0);
        if (startTimeLocal.isBefore(earliestAllowedTime) || startTimeLocal.isAfter(latestAllowedTime)) {
            throw new InvalidValueException(
                    "The start time (" + startTimeLocal + ") must be between 8:00 AM and 17:00 PM (local time).");
        }

        LocalDate startTimeDate = startTime.withZoneSameLocal(ZoneId.of("Asia/Ho_Chi_Minh")).toLocalDate();
        LocalDate nowDate = ZonedDateTime.now().withZoneSameLocal(ZoneId.of("Asia/Ho_Chi_Minh")).toLocalDate();
        if (startTimeDate.isAfter(nowDate.plusDays(3))) {
            throw new InvalidValueException("The pick up date must be within 3 days.");
        }
        return true;
    }

    public static boolean isValidEndPickUpTime(ZonedDateTime startTime, ZonedDateTime endTime) {
        if (startTime == null)
            throw new ActionNotAllowedException("The start time must be set before the end time.");

        ZonedDateTime afterStartTime30Minutes = startTime.plusMinutes(30);
        if (endTime.isBefore(afterStartTime30Minutes))
            throw new InvalidValueException("The end time must be at least 30 minutes after the start time.");

        if (endTime.getMinute() != 0 && endTime.getMinute() != 30)
            throw new InvalidValueException("The end time must be on the hour or half hour.");

        ZonedDateTime endTimeInVietnamZoneId = endTime.withZoneSameInstant(ZoneId.of("Asia/Ho_Chi_Minh"));
        LocalDateTime endTimeLocal = endTimeInVietnamZoneId.toLocalDateTime();

        LocalDateTime earliestAllowedTime = endTimeLocal.withHour(8).withMinute(30).withSecond(0);
        LocalDateTime latestAllowedTime = endTimeLocal.withHour(17).withMinute(30).withSecond(0);
        if (endTimeLocal.isBefore(earliestAllowedTime) || endTimeLocal.isAfter(latestAllowedTime)) {
            throw new InvalidValueException(
                    "The end time (" + endTimeLocal + ") must be between 8:30 AM and 17:30 PM (local time).");
        }

        LocalDate startTimeDate = startTime.withZoneSameLocal(ZoneId.of("Asia/Ho_Chi_Minh")).toLocalDate();
        LocalDate endTimeDate = endTime.withZoneSameLocal(ZoneId.of("Asia/Ho_Chi_Minh")).toLocalDate();
        if (!endTimeDate.isEqual(startTimeDate)) {
            throw new InvalidValueException("End time must be on the same day as start time.");
        }

        return true;
    }
}
