package com.ecom.demo.api.model;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Accessors(chain = true)
@Setter
@Getter
public class ProductRequest {
	private String productName;
	private Integer productCategory;
	private String productBrand;
	private String productDetails;
	private BigDecimal productPrice;

	public boolean validate() {
		if (StringUtils.isEmpty(productName) || StringUtils.isEmpty(productBrand) || StringUtils.isEmpty(productDetails)
				|| productPrice == null || productCategory == null) {
			return false;
		}
		return true;
	}
}
