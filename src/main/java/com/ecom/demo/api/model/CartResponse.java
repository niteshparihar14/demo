package com.ecom.demo.api.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class CartResponse extends Response {

	private Pagination pagination;
	private List<ProductResponse> productResponses;
	
	public Pagination getPagination() {
		return pagination;
	}

	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}

	public List<ProductResponse> getProductResponses() {
		return productResponses;
	}

	public void setProductResponses(List<ProductResponse> productResponses) {
		this.productResponses = productResponses;
	}
	
}
