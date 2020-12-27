package com.ecom.demo.db.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ecom.demo.db.model.Cart;

@Repository
public interface CartDao extends JpaRepository<Cart, Integer> {
	public List<Cart> findCartByUserId(final Integer userId, Pageable pageable);

	public Cart getCartById(Integer cartId);
	
}
