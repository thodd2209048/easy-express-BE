package com.example.easyexpressbackend.response.staff;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrudStaffResponse {
    private Long id;
    private String name;
    private Long hubId;
    private String hubName;
}
