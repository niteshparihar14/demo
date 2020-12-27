package com.ecom.demo.api.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.demo.api.filter.ServletContext;
import com.ecom.demo.api.model.CartRequest;
import com.ecom.demo.api.model.ProductRequest;
import com.ecom.demo.api.model.UserLoginRequest;
import com.ecom.demo.api.resource.UserResource;

@RestController
@RequestMapping(value = "/user", consumes = {MediaType.APPLICATION_JSON_VALUE},
    produces = {MediaType.ALL_VALUE})
public class UserRequestHandler {
	
	@Autowired
	private UserResource userResource;
	
	@Autowired
	@Lazy
	private ServletContext servletContext;

	@PostMapping("/login")
	public ResponseEntity loginUser(@RequestBody final UserLoginRequest loginReq)
			throws Exception{
		return userResource.loginUser(loginReq);
	}
	
	@GetMapping("/cart")
	public ResponseEntity userCart(@RequestParam(value = "start", required=false) final Integer start,
			@RequestParam(value = "count", required=false) final Integer count)
			throws Exception{
		return userResource.getUserCart(servletContext.getUser(), start, count);
	}
	
	@PostMapping("/add/product/cart")
	public ResponseEntity addProductInCart(@RequestBody final CartRequest addProductRequest)
			throws Exception{
		return userResource.addProductInCart(servletContext.getUser(), addProductRequest);
	}
}
