package com.ecom.demo.api.resource;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.ecom.demo.api.model.CartResponse;
import com.ecom.demo.api.model.Pagination;
import com.ecom.demo.api.model.ProductRequest;
import com.ecom.demo.api.model.ProductResponse;
import com.ecom.demo.api.model.Response;
import com.ecom.demo.api.model.Response.ResponseCode;
import com.ecom.demo.common.Constants;
import com.ecom.demo.db.model.Product;
import com.ecom.demo.db.model.User;
import com.ecom.demo.db.service.UserDbService;

@Component("productResource")
@Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRES_NEW)
public class ProductResource {

	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private UserDbService userDbService;

	/* fetch all product added by user in cart */
	public ResponseEntity getProducts(User user, Integer start, Integer count, BigDecimal price, String orderByPrice,
			List<Integer> category, String Brand) throws Exception {

		if (start == null)
			start = 0;
		if (count == null)
			count = 100;

		Pageable pageable = null;

		if (!StringUtils.isAllBlank(orderByPrice) && orderByPrice.equals("asc"))
			pageable = PageRequest.of(start, count, Sort.by("price").ascending());
		else
			pageable = PageRequest.of(start, count, Sort.by("price").descending());

		List<Product> products = userDbService.getProductByPriceAndCategoryAndBrand(price, category, Brand, pageable);

		if (CollectionUtils.isEmpty(products)) {
			return ResponseEntity.status(HttpStatus.OK).body(Response.code(ResponseCode.PRODUCT_DOESNT_EXIST));
		}

		Pagination pagination = new Pagination();
		pagination.setScanCompleted(products.size() < count);
		pagination.setTotal(products.size());

		List<ProductResponse> responses = new LinkedList<>();
		for (Product product : products) {
			ProductResponse response = populateProductResponse(product, 1, product.getPrice());
			responses.add(response);
		}

		CartResponse response = new CartResponse();
		response.setProductResponses(responses);
		response.setPagination(pagination);

		return ResponseEntity.ok(response);
	}

	// Add product
	public ResponseEntity addProduct(User user, ProductRequest addProductRequest) throws Exception {

		if (user == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.code(ResponseCode.USER_DOESNT_EXIST));
		}

		if (!Constants.UserRoles.ADMIN.value().equals(user.getRole())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Response.code(ResponseCode.NOT_ADMIN_USER));
		}

		if (addProductRequest == null || !addProductRequest.validate()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.code(ResponseCode.BAD_REQUEST));
		}

		if (!Constants.ProductCategory.productCategoryList().contains(addProductRequest.getProductCategory())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Response.code(ResponseCode.INVALID_PRODUCT_CATEGORY));
		}

		Product product = new Product();
		product.setName(addProductRequest.getProductName());
		product.setBrand(addProductRequest.getProductBrand());
		product.setCategory(addProductRequest.getProductCategory());
		product.setPrice(addProductRequest.getProductPrice());
		if (!StringUtils.isEmpty(addProductRequest.getProductDetails()))
			product.setDetails(addProductRequest.getProductDetails());

		if (userDbService.addProduct(product) == null) {
			throw new Exception("Failed to add product in cart");
		}

		ProductResponse response = populateProductResponse(product, 1, addProductRequest.getProductPrice());
		response.addHeaderCode(ResponseCode.PRODUCT_ADDED_TO_CART);

		return ResponseEntity.ok(response);
	}

	// Populate product response
	private ProductResponse populateProductResponse(Product product, Integer quantity, BigDecimal amount) {
		ProductResponse response = new ProductResponse();

		response.setProductName(product.getName());
		response.setProductBrand(product.getBrand());
		response.setProductCategory(product.getCategory());
		response.setProductDetails(product.getDetails());
		response.setProductPrice(product.getPrice());
		response.setQuantity(quantity);
		response.setAmount(amount.multiply(BigDecimal.valueOf(quantity)));

		return response;
	}
}
