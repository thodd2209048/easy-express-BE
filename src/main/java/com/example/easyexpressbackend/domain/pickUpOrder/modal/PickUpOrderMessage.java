package com.example.easyexpressbackend.domain.pickUpOrder.modal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PickUpOrderMessage {
    private Long id;
    private String cellAddress;
}
