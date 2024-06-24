package store.buzzbook.batch.entity.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CouponStatus {
	USED,
	AVAILABLE,
	EXPIRED;

	@JsonCreator
	public static CouponStatus fromString(String value) {
		if (value == null) {
			throw new IllegalArgumentException("enum 값을 찾을 수 없습니다.");
		}

		return switch (value.toLowerCase()) {
			case "used" -> USED;
			case "available" -> AVAILABLE;
			case "expired" -> EXPIRED;
			default -> throw new IllegalArgumentException("enum 값을 찾을 수 없습니다.");
		};
	}

	@JsonValue
	@Override
	public String toString() {
		return name().toLowerCase();
	}
}
