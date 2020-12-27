package com.ecom.demo.api.model;

import java.util.LinkedList;
import java.util.List;

public class Response {

	public class ResponseHeader {
		private Integer code;
		private String message;

		public ResponseHeader(Integer code, String message) {
			this.code = code;
			this.message = message;
		}

		public final Integer getCode() {
			return code;
		}

		public final void setCode(Integer code) {
			this.code = code;
		}

		public final String getMessage() {
			return message;
		}

		public final void setMessage(String message) {
			this.message = message;
		}
	}

	private List<ResponseHeader> responseCode = null;

	public final List<ResponseHeader> getCodes() {
		return responseCode;
	}

	public final void setCodes(List<ResponseHeader> responseCode) {
		this.responseCode = responseCode;
	}

	public Response addHeaderCode(ResponseCode code) {
		if (this.responseCode == null)
			responseCode = new LinkedList<ResponseHeader>();

		this.responseCode.add(new ResponseHeader(code.getCode(), code.getMassage()));
		return this;
	}

	public static Response code(ResponseCode responseCode) {
		Response response = new Response();
		return response.addHeaderCode(responseCode);
	}

	public enum ResponseCode {
		BAD_REQUEST(00, "Invalid request."), USER_DOESNT_EXIST(01, "User not exist."),
		NOT_ADMIN_USER(02, "Not a admin user."), INVALID_PRODUCT_CATEGORY(03, "Invalid product category."),
		PRODUCT_ADDED_TO_CART(04, "Product added to cart."),EMPTY_CART(05, "Cart is empty."),INVALID_CREDENTIAL(06, "Invalid Phone/Password."),
		USER_NOT_ACTIVE(07, "User not active."),PRODUCT_DOESNT_EXIST(8, "Product not exist.");

		private int code;
		private String massage;

		private ResponseCode(int code, String message) {
			this.code = code;
			this.massage = message;
		}

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}

		public String getMassage() {
			return massage;
		}

		public void setMassage(String massage) {
			this.massage = massage;
		}
	}

}
