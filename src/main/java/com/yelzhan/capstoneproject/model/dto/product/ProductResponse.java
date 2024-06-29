package com.yelzhan.capstoneproject.model.dto.product;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

    private Long id;
    private String productName;
    private String description;
    private int quantity;
    private BigDecimal unitPrice;
    private boolean isAdult;
    private String gender;
    private String category;
}
