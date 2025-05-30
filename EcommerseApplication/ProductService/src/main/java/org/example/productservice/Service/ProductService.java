package org.example.productservice.Service;

import jakarta.validation.Valid;
import org.example.productservice.Dao.*;
import org.example.productservice.Repository.CategoryRepository;
import org.example.productservice.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.util.Optionals.ifPresentOrElse;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @Transactional
    public ResponseEntity<ProductResponse> createProducts(ProductRequest productRequest) {
        Product product = Product.builder()
                .productName(productRequest.getProductName())
                .productDescription(productRequest.getDescription())
                .productQuantity(productRequest.getStockQuantity())
                .productPrice(productRequest.getPrice())
                .currency(productRequest.getCurrency())
                .productStatus(productRequest.getProductStatus())
                .build();
        categoryRepository.getCategoryByCategoryName(productRequest.getProductCategory()).ifPresentOrElse(
                product::setCategory,
                () -> {
                    throw new RuntimeException("Category not found: " + productRequest.getProductCategory());
                }
        );
        Product savedProduct = productRepository.save(product);

        ProductResponse productResponse = ProductResponse.builder()
                .productName(savedProduct.getProductName())
                .productDescription(savedProduct.getProductDescription())
                .categoryName(savedProduct.getCategory().getCategoryName())
                .price(savedProduct.getProductPrice())
                .quantity(savedProduct.getProductQuantity())
                .build();
        return new ResponseEntity<>(productResponse, HttpStatus.CREATED);

    }

    @Transactional
    public ResponseEntity<String> updateProduct(long id, ProductUpdateRequest productRequest) {
        productRepository.findById(id).ifPresentOrElse(
                product -> {
                    product.setProductName(productRequest.getProductName());
                    product.setProductPrice(productRequest.getPrice());
                    productRepository.save(product);
                },
                () -> {
                    throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Product not found with id: " + id);
                }
        );
        return new ResponseEntity<>("Product Updated SuccessFully", HttpStatus.OK);
    }

    public ResponseEntity<String> deleteProducts(long id) {
        if (!validateIfProductExist(id)) {
            return new ResponseEntity<>("Product not found with id: " + id, HttpStatus.NOT_FOUND);
        }
        productRepository.deleteById(id);
        return new ResponseEntity<>("Product Deleted Successfully", HttpStatus.OK);
    }

    private boolean validateIfProductExist(long id) {
        return productRepository.existsById(id);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<List<ProductResponse>> getAllProducts(ProductSearchRequest productRequest) {
        Category category = categoryRepository.getCategoryByCategoryName(productRequest.getCategory()).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Category not found"));
        List<Product> products = category.getProducts();
        if (products.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<ProductResponse> productResponses = new ArrayList<>();
        products.forEach(product -> {
            if (product.getProductPrice()!= productRequest.getPriceRange()) {
                return; // Skip products outside the price range
            }
            ProductResponse productResponse = ProductResponse.builder()
                    .productName(product.getProductName())
                    .productDescription(product.getProductDescription())
                    .categoryName(product.getCategory().getCategoryName())
                    .price(product.getProductPrice())
                    .quantity(product.getProductQuantity()).build();
            productResponses.add(productResponse);
        });
        return new ResponseEntity<>(productResponses, HttpStatus.OK);
    }

    public ResponseEntity<ProductResponse> getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Product not found with id: " + productId));
        ProductResponse productResponse = ProductResponse.builder()
                .productName(product.getProductName())
                .productDescription(product.getProductDescription())
                .categoryName(product.getCategory().getCategoryName())
                .quantity(product.getProductQuantity())
                .price(product.getProductPrice()).build();
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

}
