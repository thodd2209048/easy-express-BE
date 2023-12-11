package com.example.easyexpressbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.ZonedDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper=false)
public class Staff extends BaseEntity{
    private String name;
    private Long hubId;
//    @JsonIgnore
//    private Long id;
//    @JsonIgnore
//    private ZonedDateTime createdAt;
//    @JsonIgnore
//    private ZonedDateTime updatedAt;
}
