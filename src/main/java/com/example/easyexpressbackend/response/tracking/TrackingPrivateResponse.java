package com.example.easyexpressbackend.response.tracking;

import com.example.easyexpressbackend.response.StaffResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TrackingPrivateResponse extends TrackingPublicResponse {
    private StaffResponse staff;
}
