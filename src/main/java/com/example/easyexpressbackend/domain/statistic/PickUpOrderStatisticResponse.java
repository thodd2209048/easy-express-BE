package com.example.easyexpressbackend.domain.statistic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PickUpOrderStatisticResponse {
    private ZonedDateTime createdAt;
    private Long unfinishedOrder;
    private Long inProcessOrder;
    private Long missedDeadlineOrder;
}
