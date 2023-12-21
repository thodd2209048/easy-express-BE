package com.example.easyexpressbackend.dto.order;

import com.example.easyexpressbackend.constant.OrderStatus;
import com.example.easyexpressbackend.exception.ActionNotAllowedException;
import com.example.easyexpressbackend.exception.InvalidValueException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateOrderDto {
    private OrderStatus status;
//    sender
    private String senderName;
    private String senderPhone;
//    time
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;

    public void setStartTime(ZonedDateTime startTime) {
        ZonedDateTime after30Minutes = ZonedDateTime.now().plusMinutes(30);
        if(startTime.isBefore(after30Minutes))
            throw new InvalidValueException("The start time must be at least 30 minutes after the current time.");

        if(startTime.getMinute()!=0 && startTime.getMinute() != 30)
            throw new InvalidValueException("The start time must be on the hour or half hour.");

        LocalTime earliestAllowedTime = LocalTime.of(8, 0);
        LocalTime latestAllowedTime = LocalTime.of(17, 0);
        LocalTime startTimeLocalTime = startTime.withZoneSameInstant(ZoneId.systemDefault()).toLocalTime();
        if (startTimeLocalTime.isBefore(earliestAllowedTime) || startTimeLocalTime.isAfter(latestAllowedTime)) {
            throw new InvalidValueException("The start time must be between 8:00 AM and 5:00 PM (local time).");
        }

        LocalDate startTimeDate = startTime.withZoneSameLocal(ZoneId.systemDefault()).toLocalDate();
        LocalDate nowDate = LocalDateTime.now().toLocalDate();
        if (startTimeDate.isAfter(nowDate.plusDays(3))) {
            throw new InvalidValueException("The pick up date must be within 3 days.");
        }

        this.startTime = startTime;
    }

    public void setEndTime(ZonedDateTime endTime){
        if(startTime == null)
            throw new ActionNotAllowedException("The start time must be set before the end time.");

        ZonedDateTime afterStartTime30Minutes = startTime.plusMinutes(30);
        if(endTime.isBefore(afterStartTime30Minutes))
            throw new InvalidValueException("The end time must be at least 30 minutes after the start time.");

        if(endTime.getMinute()!=0 && endTime.getMinute() != 30)
            throw new InvalidValueException("The end time must be on the hour or half hour.");

        LocalTime earliestAllowedTime = LocalTime.of(8, 30);
        LocalTime latestAllowedTime = LocalTime.of(17, 30);
        LocalTime endTimeLocalTime = endTime.withZoneSameInstant(ZoneId.systemDefault()).toLocalTime();
        if (endTimeLocalTime.isBefore(earliestAllowedTime) || endTimeLocalTime.isAfter(latestAllowedTime)) {
            throw new InvalidValueException("The end time must be between 8:30 AM and 17:30 PM (local time).");
        }

        LocalDate startTimeDate = startTime.withZoneSameLocal(ZoneId.systemDefault()).toLocalDate();
        LocalDate endTimeDate = endTime.withZoneSameLocal(ZoneId.systemDefault()).toLocalDate();
        if (endTimeDate.isEqual(startTimeDate)) {
            throw new InvalidValueException("End time must be on the same day as start time.");
        }

        this.endTime = endTime;
    }
}


