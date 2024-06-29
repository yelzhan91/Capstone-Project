package com.yelzhan.capstoneproject.model.dto.request.product;

import com.yelzhan.capstoneproject.model.dto.request.CreateRequest;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest implements CreateRequest {

    @NotEmpty(message = "Product name cannot be empty.")
    private String productName;

    @NotEmpty(message = "Description cannot be empty.")
    private String description;

    @NotEmpty(message = "Category cannot be empty.")
    private String category;

    private String image;

    private boolean isAdult;

    @NotEmpty(message = "Gender cannot be empty.")
    private String gender;

    @Positive
    @Min(value = 1, message = "Minimal value for quantity is 1")
    private Integer quantity;

    @NotNull
    @Positive(message = "Price cannot be negative value")
    private BigDecimal unitPrice;
}
