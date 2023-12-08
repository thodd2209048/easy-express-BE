package com.example.easyexpressbackend.dto.hub;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddHubDto {
    @NotEmpty
    private String name;
    @NotEmpty
    private String location;
}
