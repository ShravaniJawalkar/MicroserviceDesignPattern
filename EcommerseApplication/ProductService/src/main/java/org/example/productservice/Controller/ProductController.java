package org.example.productservice.Controller;

import jakarta.validation.Valid;
import org.example.productservice.Dao.ProductRequest;
import org.example.productservice.Dao.ProductResponse;
import org.example.productservice.Dao.ProductSearchRequest;
import org.example.productservice.Dao.ProductUpdateRequest;
import org.example.productservice.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping("/create-products")
    public ResponseEntity<ProductResponse> createProducts(@Valid @RequestBody ProductRequest productRequest) {
        return productService.createProducts(productRequest);
    }

    @PutMapping("/update-product/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable long id, @Valid @RequestBody ProductUpdateRequest productRequest) {
        return productService.updateProduct(id, productRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable long id) {
        return productService.deleteProducts(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable long id) {
        return productService.getProductById(id);
    }

    @GetMapping("/all-products")
    public ResponseEntity<List<ProductResponse>> getAllProducts(@Valid @RequestBody ProductSearchRequest productRequest) {
        return productService.getAllProducts(productRequest);
    }
}
