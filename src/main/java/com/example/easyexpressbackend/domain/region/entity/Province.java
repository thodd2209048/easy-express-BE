package com.example.easyexpressbackend.domain.region.entity;

import com.example.easyexpressbackend.entity.SequenceIdBaseEntity;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@EqualsAndHashCode(callSuper=false)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Province extends SequenceIdBaseEntity {
    private String name;
    private String code;
    private String codename;
}
