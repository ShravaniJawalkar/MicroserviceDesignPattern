package org.example.productservice.Service;

import org.example.productservice.Dao.*;
import org.example.productservice.Repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Transactional
    public ResponseEntity<CategoryResponse> createCategory(CategoryRequest category) {
        Category categoryDao = Category.builder()
                .categoryName(category.getCategoryName())
                .build();
        Category savedCategory = categoryRepository.save(categoryDao);
        CategoryResponse categoryResponse = CategoryResponse.builder()
                .categoryId(savedCategory.getCategoryId())
                .categoryName(savedCategory.getCategoryName())
                .products(transformProducts(savedCategory.getProducts()))
                .build();

        return new ResponseEntity<>(categoryResponse, HttpStatus.CREATED);

    }

    private List<ProductResponse> transformProducts(List<Product> products) {
        List<ProductResponse> productResponses = new ArrayList<>();
        if (products != null && !products.isEmpty()) {
            products.forEach(product -> {
                ProductResponse productResponse = ProductResponse.builder()
                        .productName(product.getProductName())
                        .productDescription(product.getProductDescription())
                        .categoryName(product.getCategory().getCategoryName())
                        .price(product.getProductPrice())
                        .quantity(product.getProductQuantity()).build();
                productResponses.add(productResponse);
            });
        }
        return productResponses;
    }

    @Transactional
    public ResponseEntity<String> updateCategory(Long id, String categoryName) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Category not found with id: " + id));
        category.setCategoryName(categoryName);
        categoryRepository.save(category);
        return new ResponseEntity<>("Category updated successfully", HttpStatus.OK);
    }

    public ResponseEntity<String> deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            return new ResponseEntity<>("Category not found with id: " + id, HttpStatus.NOT_FOUND);
        }
        categoryRepository.deleteById(id);
        return new ResponseEntity<>("Category deleted successfully", HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<CategoryResponse> getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Category not found with id: " + id));
        CategoryResponse categoryResponse = CategoryResponse.builder()
                .categoryId(category.getCategoryId())
                .categoryName(category.getCategoryName())
                .products(transformProducts(category.getProducts()))
                .build();
        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        List<CategoryResponse> categoryResponses = new ArrayList<>();
        categoryRepository.findAll().forEach(category -> {
            CategoryResponse categoryResponse = CategoryResponse.builder()
                    .categoryId(category.getCategoryId())
                    .categoryName(category.getCategoryName())
                    .products(transformProducts(category.getProducts()))
                    .build();
            categoryResponses.add(categoryResponse);
        });
        return new ResponseEntity<>(categoryResponses, HttpStatus.OK);
    }
}
