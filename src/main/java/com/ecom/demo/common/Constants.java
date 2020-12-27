package com.ecom.demo.common;

import java.util.Arrays;
import java.util.List;

public final class Constants {

	public enum UserRoles {
		USER("U"), ADMIN("A");

		private String role;

		private UserRoles(String role) {
			this.role = role;
		}

		public String value() {
			return this.role;
		}
	}

	public enum ProductCategory {

		ELECTRONICS(0, "Electronics"), HOME_APPLIANCES(1, "Home_Applinces"), CLOTHES(2, "Clothes"),
		GROSERY(3, "Grosery");

		private int categoryId;
		private String categoryName;

		private ProductCategory(int categoryId, String categoryName) {
			this.categoryId = categoryId;
			this.categoryName = categoryName;
		}

		public String categoryName() {
			return this.categoryName;
		}

		public int categoryId() {
			return this.categoryId;
		}

		public boolean equals(int categoryId) {
			return this.categoryId == categoryId;
		}

		public boolean equals(String categoryName) {
			return this.categoryName.equals(categoryName);
		}

		public static List<Integer> productCategoryList() {
			return Arrays.asList(ProductCategory.ELECTRONICS.categoryId(), ProductCategory.HOME_APPLIANCES.categoryId(),
					ProductCategory.CLOTHES.categoryId(), ProductCategory.GROSERY.categoryId());
		}
	}
}
