package com.example.easyexpressbackend.domain.staff;

import com.example.easyexpressbackend.entity.IdentityIdBaseEntity;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper=false)
public class Staff extends IdentityIdBaseEntity {
    private String name;
    private Long hubId;
//    @JsonIgnore
//    private Long id;
//    @JsonIgnore
//    private ZonedDateTime createdAt;
//    @JsonIgnore
//    private ZonedDateTime updatedAt;
}
