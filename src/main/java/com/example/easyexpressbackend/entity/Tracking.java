package com.example.easyexpressbackend.entity;

import com.example.easyexpressbackend.constant.ShipmentStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class Tracking extends BaseEntity{
    private String shipmentNumber;
    private Long hubId;
    private Long staffId;
    private String districtCode;
    @Enumerated(EnumType.STRING)
    private ShipmentStatus shipmentStatus;
}
