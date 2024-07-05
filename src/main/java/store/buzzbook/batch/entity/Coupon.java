package store.buzzbook.batch.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.buzzbook.batch.entity.constant.CouponStatus;

/**
 * 쿠폰 엔티티 클래스입니다.
 * <p>
 * 이 클래스는 쿠폰의 ID, 쿠폰 정책, 쿠폰 코드, 생성일, 만료일, 상태를 포함합니다.
 * </p>
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Entity
public class Coupon {

	/**
	 * 쿠폰의 ID입니다.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	/**
	 * 쿠폰 정책을 나타내는 CouponPolicy 객체입니다.
	 * <p>
	 * 쿠폰 정책은 LAZY 로딩 전략을 사용하여 필요할 때 로드됩니다.
	 * </p>
	 */
	@NotNull
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "coupon_policy_id")
	private CouponPolicy couponPolicy;

	/**
	 * 쿠폰 코드입니다.
	 */
	@NotNull
	@Column(unique = true, length = 20)
	private String couponCode;

	/**
	 * 쿠폰의 생성일입니다.
	 */
	@NotNull
	private LocalDate createDate;

	/**
	 * 쿠폰의 만료일입니다.
	 */
	@NotNull
	private LocalDate expireDate;

	/**
	 * 쿠폰의 상태입니다.
	 */
	@NotNull
	@Enumerated(value = EnumType.STRING)
	private CouponStatus status;

	/**
	 * 쿠폰의 상태를 변경합니다.
	 *
	 * @param newStatus 새로운 쿠폰 상태
	 */
	public void changeStatus(CouponStatus newStatus) {
		this.status = newStatus;
	}
}
