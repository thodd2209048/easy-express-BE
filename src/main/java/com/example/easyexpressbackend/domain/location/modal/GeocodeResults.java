package com.example.easyexpressbackend.domain.location.modal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeocodeResults {
    private List<Result> results;
}
