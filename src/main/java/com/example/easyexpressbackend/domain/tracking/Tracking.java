package com.example.easyexpressbackend.domain.tracking;

import com.example.easyexpressbackend.domain.shipment.constant.ShipmentStatus;
import com.example.easyexpressbackend.entity.IdentityIdBaseEntity;
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
public class Tracking extends IdentityIdBaseEntity {
    private String shipmentNumber;
    private Long hubId;
    private Long staffId;
    private String districtCode;
    @Enumerated(EnumType.STRING)
    private ShipmentStatus shipmentStatus;
    private String newShipmentNumber;
}
