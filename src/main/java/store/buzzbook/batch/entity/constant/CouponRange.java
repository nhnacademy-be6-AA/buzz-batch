package store.buzzbook.batch.entity.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CouponRange {
	GLOBAL, BOOK, CATEGORY;

	@JsonCreator
	public static CouponRange fromString(String value) {
		if (value == null) {
			throw new IllegalArgumentException("enum 값을 찾을 수 없습니다.");
		}

		return switch (value.toLowerCase()) {
			case "global" -> GLOBAL;
			case "book" -> BOOK;
			case "category" -> CATEGORY;
			default -> throw new IllegalArgumentException("enum 값을 찾을 수 없습니다.");
		};
	}

	@JsonValue
	@Override
	public String toString() {
		return name().toLowerCase();
	}
}
