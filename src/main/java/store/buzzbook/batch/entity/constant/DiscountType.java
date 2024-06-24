package store.buzzbook.batch.entity.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum DiscountType {
	RATE, AMOUNT;

	@JsonCreator
	public static DiscountType fromString(String value) {
		if (value == null) {
			throw new IllegalArgumentException("enum 값을 찾을 수 없습니다.");
		}

		return switch (value.toLowerCase()) {
			case "rate" -> RATE;
			case "amount" -> AMOUNT;
			default -> throw new IllegalArgumentException("enum 값을 찾을 수 없습니다.");
		};
	}

	@JsonValue
	public String toString() {
		return name().toLowerCase();
	}
}
