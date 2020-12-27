package com.ecom.demo.api.resource;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.ecom.demo.api.filter.ServletContext;
import com.ecom.demo.api.model.CartRequest;
import com.ecom.demo.api.model.CartResponse;
import com.ecom.demo.api.model.Pagination;
import com.ecom.demo.api.model.ProductRequest;
import com.ecom.demo.api.model.ProductResponse;
import com.ecom.demo.api.model.Response;
import com.ecom.demo.api.model.UserLoginRequest;
import com.ecom.demo.api.model.UserLoginResponse;
import com.ecom.demo.api.model.Response.ResponseCode;
import com.ecom.demo.api.utils.ApplicationUtils;
import com.ecom.demo.db.model.Cart;
import com.ecom.demo.db.model.Product;
import com.ecom.demo.db.model.User;
import com.ecom.demo.db.service.MyUserDetailsService;
import com.ecom.demo.db.service.UserDbService;

@Component("userLoginResource")
@Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRES_NEW)
public class UserResource {

	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private ApplicationUtils jwtTokenUtil;

	@Autowired
	private UserDbService userDbService;

	@Autowired
	private MyUserDetailsService userDetailsService;

	public ResponseEntity loginUser(final UserLoginRequest loginReq) throws Exception {

		if (loginReq == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.code(ResponseCode.BAD_REQUEST));
		}

		if (!loginReq.validate()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.code(ResponseCode.BAD_REQUEST));
		}

		List<User> users = this.userDbService.getUser(loginReq.getLogin());

		if (CollectionUtils.isEmpty(users)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.code(ResponseCode.USER_DOESNT_EXIST));
		}

		User user = users.get(0);

		if (!this.userDbService.isActive(user)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Response.code(ResponseCode.USER_NOT_ACTIVE));
		}

		UserDetails userDetails = userDetailsService.loadUserByUsername(loginReq.getLogin());

		// verify user password
		if (this.userDbService.verifyPassword(user, loginReq.getPassword())) {
			UserLoginResponse response = new UserLoginResponse();
			authenticate(loginReq.getLogin(), loginReq.getPassword());
			final String token = jwtTokenUtil.generateToken(userDetails);

			response.setFname(user.getFirst_name());
			response.setLname(user.getLast_name());
			response.setToken(token);
			return ResponseEntity.ok(response);

		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.code(ResponseCode.INVALID_CREDENTIAL));
	}

	/* fetch all product added by user in cart */
	public ResponseEntity getUserCart(User user, Integer start, Integer count) throws Exception {

		if (start == null)
			start = 0;
		if (count == null)
			count = 100;

		if (user == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.code(ResponseCode.USER_DOESNT_EXIST));
		}

		Pageable pageable = PageRequest.of(start, count);

		List<Cart> carts = userDbService.getCartByUserId(user.getId(), pageable);

		if (CollectionUtils.isEmpty(carts)) {
			return ResponseEntity.status(HttpStatus.OK).body(Response.code(ResponseCode.EMPTY_CART));
		}

		Pagination pagination = new Pagination();
		pagination.setScanCompleted(carts.size() < count);
		pagination.setTotal(carts.size());

		List<ProductResponse> responses = new LinkedList<>();
		for (Cart cart : carts) {
			Product product = cart.getProduct();
			ProductResponse response = populateProductResponse(product, cart.getQuantity(), product.getPrice());
			responses.add(response);
		}

		CartResponse response = new CartResponse();
		response.setProductResponses(responses);
		response.setPagination(pagination);

		return ResponseEntity.ok(response);
	}

	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}

	// Add product to user cart
	public ResponseEntity addProductInCart(User user, CartRequest addProductRequest) throws Exception {

		if (user == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.code(ResponseCode.USER_DOESNT_EXIST));
		}

		if (addProductRequest == null || !addProductRequest.validate()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.code(ResponseCode.BAD_REQUEST));
		}

		Optional<Product> product = userDbService.getProductById(addProductRequest.getProductId());

		if (!product.isPresent()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.code(ResponseCode.PRODUCT_DOESNT_EXIST));
		}

		Cart cart = new Cart();
		cart.setProduct(product.get());
		cart.setQuantity(addProductRequest.getQuantity());
		cart.setAmount(product.get().getPrice().multiply(BigDecimal.valueOf(addProductRequest.getQuantity())));
		cart.setUser(user);

		if (userDbService.addProductToUserCart(cart) == null) {
			throw new Exception("Failed to add product in cart");
		}

		ProductResponse response = populateProductResponse(product.get(), addProductRequest.getQuantity(),
				product.get().getPrice());

		return ResponseEntity.ok(response);
	}

	// Populate product response
	private ProductResponse populateProductResponse(Product product, Integer quantity, BigDecimal amount) {
		ProductResponse response = new ProductResponse();

		response.setProductName(product.getName());
		response.setProductBrand(product.getBrand());
		response.setProductDetails(product.getDetails());
		response.setProductPrice(product.getPrice());
		response.setQuantity(quantity);
		response.setAmount(amount.multiply(BigDecimal.valueOf(quantity)));

		return response;
	}
}
