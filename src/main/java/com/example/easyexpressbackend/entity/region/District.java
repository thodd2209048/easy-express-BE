package com.example.easyexpressbackend.entity.region;

import com.example.easyexpressbackend.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@EqualsAndHashCode(callSuper=false)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class District extends BaseEntity {
    private String name;
    private String code;
    private String codename;
    private String provinceCode;
}
