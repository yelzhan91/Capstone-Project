package com.yelzhan.capstoneproject.model.dto.request.category;

import com.yelzhan.capstoneproject.model.dto.request.CreateRequest;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest implements CreateRequest {

    @NotEmpty(message = "Category cannot be empty")
    private String category;
    private String description;

}
