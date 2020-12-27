package com.ecom.demo.db.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.ecom.demo.db.dao.CartDao;
import com.ecom.demo.db.dao.ProductDao;
import com.ecom.demo.db.dao.UserDao;
import com.ecom.demo.db.model.Cart;
import com.ecom.demo.db.model.Product;
import com.ecom.demo.db.model.User;

@Component("userDBService")
@Transactional
public class UserDbServiceImpl extends UserDbService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private CartDao cartDao;
	
	@Autowired
	private ProductDao productDao;

	@Override
	public List<User> getUser(String phonenum) {
		return userDao.findUserByPhone(phonenum);
	}

	@Override
	public boolean isActive(User user) {
		return (user != null && user.getStatus() == 1) ? true : false;
	}

	@Override
	public boolean verifyPassword(User user, String password) {
		if (user != null && password != null && StringUtils.equals(password, user.getPassword())) {
			return true;
		}
		return false;
	}

	@Override
	public List<Cart> getCartByUserId(Integer userId, Pageable pageable) {
		return cartDao.findCartByUserId(userId, pageable);
	}

	@Override
	public Product addProduct(Product product) {
		return productDao.save(product);
	}

	@Override
	public Optional<Product> getProductById(Integer productId) {
		return productDao.findById(productId);
	}

	@Override
	public Cart addProductToUserCart(Cart cart) {
		return cartDao.save(cart);
	}

	@Override
	public Page<Product> getProducts(Pageable pageable) {
		return productDao.findAll(pageable);
	}

	@Override
	public List<Product> getProductByPriceAndCategoryAndBrand(BigDecimal price, List<Integer> categoryIds, String brand,
			Pageable pageable) {
		boolean isPrice = price != null;
		boolean isCategory = !CollectionUtils.isEmpty(categoryIds);
		boolean isBrand = !StringUtils.isEmpty(brand);
		
		if (isPrice && isCategory && isBrand) {
			return productDao.findProductByPriceAndCategoryInAndBrand(price,categoryIds,brand, pageable);
		} else if(isPrice && !isCategory && !isBrand) {
			return productDao.findProductByPrice(price, pageable);
		} else if(isPrice && isCategory && !isBrand) {
			return productDao.findProductByPriceAndCategoryIn(price, categoryIds, pageable);
		} else if(isPrice && !isCategory && isBrand) {
			return productDao.findProductByPriceAndBrand(price, brand, pageable);
		} else if(!isPrice && isCategory && !isBrand) {
			return productDao.findProductByCategoryIn(categoryIds, pageable);
		} else if(!isPrice && isCategory && isBrand) {
			return productDao.findProductByCategoryInAndBrand(categoryIds, brand, pageable);
		} else if(!isPrice && !isCategory && isBrand) {
			return productDao.findProductByBrand(brand, pageable);
		} 
		
		return productDao.findAll(pageable).toList();
	}

}
