package com.example.easyexpressbackend.domain.staff.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddStaffDto {
    @NotEmpty
    private String name;
    @NotNull
    private Long hubId;
}
