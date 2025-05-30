package org.example.productservice.Dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductSearchRequest {
    @NotNull
    @JsonProperty("category")
    private String category;
    @NotNull
    @JsonProperty("price-range")
    private Double priceRange;
}
