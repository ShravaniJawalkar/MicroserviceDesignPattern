package org.example.productservice.Dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequest {
    @NotNull
    @JsonProperty("product_name")
    private String productName;
    @NotNull
    @JsonProperty("description")
    private String description;
    @NotNull
    @JsonProperty("price")
    private double price;
    @NotNull
    @Enumerated(value = EnumType.STRING)
    @JsonProperty("currency")
    private Currency currency;
    @NotNull
    @JsonProperty("stock_quantity")
    private int stockQuantity;
    @NotNull
    @JsonProperty("product-status")
    @Enumerated(value = EnumType.STRING)
    private ProductStatus productStatus;
    @NotNull
    @JsonProperty("product-category")
    private String productCategory;
}
