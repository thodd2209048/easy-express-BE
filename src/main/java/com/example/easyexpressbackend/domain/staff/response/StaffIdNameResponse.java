package com.example.easyexpressbackend.domain.staff.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StaffIdNameResponse {
    private Long id;
    private String name;
}
