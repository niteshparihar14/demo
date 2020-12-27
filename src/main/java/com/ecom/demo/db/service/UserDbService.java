package com.ecom.demo.db.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ecom.demo.db.model.Cart;
import com.ecom.demo.db.model.Product;
import com.ecom.demo.db.model.User;

public abstract class UserDbService {

	abstract public List<User> getUser(final String phonenum);
	
	abstract public boolean isActive(final User user);

	abstract public boolean verifyPassword(final User user, final String password );
	
	abstract public List<Cart> getCartByUserId(final Integer userId, Pageable pageable);
	
	abstract public Page<Product> getProducts(Pageable pageable);
	
	abstract public Product addProduct(final Product product);
	
	abstract public Cart addProductToUserCart(final Cart cart);

	public abstract Optional<Product> getProductById(Integer productId);
	
	abstract public List<Product> getProductByPriceAndCategoryAndBrand(final BigDecimal price,
			final List<Integer> category, final String Brand, Pageable pageable);

	abstract public Cart getCartById(Integer cartId);

	public abstract void deleteProductFromCart(Cart cart);
	
}
