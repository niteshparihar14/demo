package com.ecom.demo.api.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Accessors(chain = true)
@Setter
@Getter
public class CartRequest {
	private Integer productId;
	private Integer quantity;

  public boolean validate() {
    if (productId == null || quantity == null) {
      return false;
    }
    return true;
  }
}
