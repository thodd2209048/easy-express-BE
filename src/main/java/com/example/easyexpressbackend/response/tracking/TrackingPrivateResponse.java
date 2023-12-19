package com.example.easyexpressbackend.response.tracking;

import com.example.easyexpressbackend.constant.Status;
import com.example.easyexpressbackend.response.HubResponse;
import com.example.easyexpressbackend.response.StaffResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TrackingPrivateResponse extends TrackingResponse {
    private StaffResponse staff;
}
