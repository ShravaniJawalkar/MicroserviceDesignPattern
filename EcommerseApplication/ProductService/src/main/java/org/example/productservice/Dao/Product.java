package org.example.productservice.Dao;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


@Entity
@Table(name = "product")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = "category")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "product_id", nullable = false, unique = true)
    private long productId;
    @Column(name = "product_name", nullable = false)
    private String productName;
    @Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(name = "product_description", columnDefinition = "TEXT")
    private String productDescription;
    @Column(name = "product_price", nullable = false)
    private double productPrice;
    @Column(name = "product_quantity", nullable = false)
    private int productQuantity;
    @Enumerated(EnumType.STRING)
    @Column(name = "product_currency")
    private Currency currency;
    @Enumerated(EnumType.STRING)
    @Column(name = "product_status")
    private ProductStatus productStatus;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false,referencedColumnName = "category_id")
    private Category category;
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
