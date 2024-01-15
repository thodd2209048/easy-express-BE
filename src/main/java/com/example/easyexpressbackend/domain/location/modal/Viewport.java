package com.example.easyexpressbackend.domain.location.modal;

import com.uber.h3core.util.LatLng;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Viewport {
    private Location northeast;
}
