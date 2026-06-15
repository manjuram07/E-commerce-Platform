package com.infy.ekart.product.repository;


import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.infy.ekart.product.entity.Product;

public interface ProductRepository extends CrudRepository<Product, Integer> {

	// find product by name
	Optional<Product> findByName(String name);

}
