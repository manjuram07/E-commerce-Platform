package com.infy.ekart.product.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Entity
@Table(
        name = "EK_PRODUCT",
        indexes = {
                @Index(name = "idx_product_name", columnList = "name")
        }
)
public class Product {

    @Id
    @Column(name = "PRODUCT_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(
			name = "product_seq", // Name of the sequence generator
			sequenceName = "product_seq", // Name of the database sequence
			allocationSize = 1 // Increment size for the sequence
	) // This annotation is used to specify the sequence generator for the primary key generation
    private Integer productId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "CATEGORY")
    private String category;

    @Column(name = "BRAND")
    private String brand;

    @Column(name = "PRICE")
    private Double price;

    @Column(name = "QUANTITY")
    private Integer availableQuantity;


}
