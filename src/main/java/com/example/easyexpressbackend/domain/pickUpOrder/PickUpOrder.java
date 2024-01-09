package com.example.easyexpressbackend.domain.pickUpOrder;

import com.example.easyexpressbackend.entity.IdentityIdBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString
@EqualsAndHashCode(callSuper = false)
public class PickUpOrder extends IdentityIdBaseEntity {
    //CUSTOMER DETAILS
    private String customerName;
    private String phone;
    private String address;
    private String districtCode;
    //SHIPMENT DETAILS
    @Column(columnDefinition = "text")
    private String description;
    private Double weightInKg;
    private Double lengthInCm;
    private Double widthInCm;
    private Double heightInCm;
}
