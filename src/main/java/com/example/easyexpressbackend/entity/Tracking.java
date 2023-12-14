package com.example.easyexpressbackend.entity;

import com.example.easyexpressbackend.constant.Status;
import jakarta.persistence.Entity;
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
    private Long staffId;
    private Status status;
}
