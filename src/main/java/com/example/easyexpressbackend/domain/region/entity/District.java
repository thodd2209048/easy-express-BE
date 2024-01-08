package com.example.easyexpressbackend.domain.region.entity;

import com.example.easyexpressbackend.entity.SequenceIdBaseEntity;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@EqualsAndHashCode(callSuper=false)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class District extends SequenceIdBaseEntity {
    private String name;
    private String code;
    private String codename;
    private String provinceCode;
}
