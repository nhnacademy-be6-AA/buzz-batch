package store.buzzbook.batch.dto;

import lombok.Builder;

@Builder
public record CreateUserCouponRequest(

	long userId,
	int couponPolicyId,
	String couponCode
) {
}
