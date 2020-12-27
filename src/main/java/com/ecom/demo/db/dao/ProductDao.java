package com.ecom.demo.db.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ecom.demo.db.model.Product;

@Repository
public interface ProductDao extends JpaRepository<Product, Integer> {

	public List<Product> findProductById(final Integer Id);

	public List<Product> findProductByPriceAndCategoryInAndBrand(@Param("price") BigDecimal price,
			@Param("categoryId") List<Integer> categoryId, @Param("brand") String brand, Pageable pageable);

	public List<Product> findProductByPrice(@Param("price") BigDecimal price, Pageable pageable);

	public List<Product> findProductByPriceAndCategoryIn(@Param("price") BigDecimal price,
			@Param("categoryId") List<Integer> categoryId, Pageable pageable);

	public List<Product> findProductByPriceAndBrand(@Param("price") BigDecimal price, @Param("brand") String brand,
			Pageable pageable);

	public List<Product> findProductByCategoryIn(@Param("categoryId") List<Integer> categoryId, Pageable pageable);

	public List<Product> findProductByCategoryInAndBrand(@Param("categoryId") List<Integer> categoryId,
			@Param("brand") String brand, Pageable pageable);

	public List<Product> findProductByBrand(@Param("brand") String brand, Pageable pageable);
}
