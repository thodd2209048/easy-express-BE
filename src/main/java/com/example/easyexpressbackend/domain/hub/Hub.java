package com.example.easyexpressbackend.domain.hub;

import com.example.easyexpressbackend.entity.IdentityIdBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Hub extends IdentityIdBaseEntity {
    @Column(unique = true)
    private String name;
    @Column(unique = true)
    private String address;
    private String districtCode;
    private Double lat;
    private Double lng;
    private String cell2Address;
    private String cell8Address;
}
