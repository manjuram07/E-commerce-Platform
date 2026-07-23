package com.infy.ekart.product.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.infy.ekart.product.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

	// find product by name
	Optional<Product> findByName(String name);

}
