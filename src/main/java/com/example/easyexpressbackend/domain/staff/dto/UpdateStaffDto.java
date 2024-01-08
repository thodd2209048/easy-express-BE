package com.example.easyexpressbackend.domain.staff.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStaffDto {
    private String name;
    private Long hubId;
}
