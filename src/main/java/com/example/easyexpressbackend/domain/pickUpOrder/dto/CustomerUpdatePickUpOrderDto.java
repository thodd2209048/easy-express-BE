package com.example.easyexpressbackend.domain.pickUpOrder.dto;

import com.example.easyexpressbackend.domain.pickUpOrder.constant.PickUpOrderStatus;
import com.example.easyexpressbackend.domain.pickUpOrder.utils.PickUpOrderUtils;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerUpdatePickUpOrderDto {
    //    sender
    private String senderName;
    private String senderPhone;
    //    time
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;

    @AssertTrue
    public boolean isValidStartPickUpTime() {
        return PickUpOrderUtils.isValidStartPickUpTime(this.startTime);
    }

    @AssertTrue
    public boolean isValidEndPickUpTime() {
        return  PickUpOrderUtils.isValidEndPickUpTime(this.startTime, this.endTime);
    }

}
