package com.ecom.demo.api.request;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.demo.api.filter.ServletContext;
import com.ecom.demo.api.model.ProductRequest;
import com.ecom.demo.api.resource.ProductResource;

@RestController
@RequestMapping(value = "/", consumes = {MediaType.APPLICATION_JSON_VALUE},
    produces = {MediaType.ALL_VALUE})
public class ProductRequestHandler {
	
	@Autowired
	private ProductResource productResource;
	
	@Autowired
	@Lazy
	private ServletContext servletContext;
	
	@PostMapping("admin/add/product")
	public ResponseEntity addProduct(@RequestBody final ProductRequest addProductRequest)
			throws Exception{
		return productResource.addProduct(servletContext.getUser(), addProductRequest);
	}
	
	@GetMapping("products")
	public ResponseEntity fetchProducts(@RequestParam(value = "start", required=false) final Integer start,
			@RequestParam(value = "count", required=false) final Integer count,
			@RequestParam(value = "price", required=false) final BigDecimal price,
			@RequestParam(value = "orderByPrice", required=false) final String orderByPrice,
			@RequestParam(value = "category", required=false) final List<Integer> category,
			@RequestParam(value = "brand", required=false) final String brand)
			throws Exception {
		return productResource.getProducts(servletContext.getUser(), start, count, price, orderByPrice, category, brand);
	}
}
