package store.buzzbook.batch.entity;

import java.time.LocalDate;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.buzzbook.batch.entity.constant.DiscountType;

/**
 * 쿠폰 정책 엔티티 클래스입니다.
 * <p>
 * 이 클래스는 쿠폰 정책의 ID, 쿠폰 타입, 이름, 할인 타입, 할인율, 할인 금액, 기간, 기준 가격, 최대 할인 금액, 시작일, 종료일, 삭제 여부를 포함합니다.
 * </p>
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Entity
public class CouponPolicy {

	/**
	 * 쿠폰 정책의 ID 입니다.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	/**
	 * 쿠폰 타입을 나타내는 CouponType 객체입니다.
	 * <p>
	 * 쿠폰 타입은 LAZY 로딩 전략을 사용하여 필요할 때 로드됩니다.
	 * </p>
	 */
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private CouponType couponType;

	/**
	 * 쿠폰 정책의 이름입니다.
	 */
	@NotNull
	@Column(length = 30)
	private String name;

	/**
	 * 쿠폰 정책의 할인 타입입니다.
	 */
	@NotNull
	@Enumerated(value = EnumType.STRING)
	private DiscountType discountType;

	/**
	 * 쿠폰 정책의 할인율입니다.
	 */
	@NotNull
	@ColumnDefault("0")
	private double discountRate;

	/**
	 * 쿠폰 정책의 할인 금액입니다.
	 */
	@NotNull
	@ColumnDefault("0")
	private int discountAmount;

	/**
	 * 쿠폰 정책의 지속 기간입니다.
	 */
	@NotNull
	private int period;

	/**
	 * 쿠폰 정책의 기준 가격입니다.
	 */
	@NotNull
	private int standardPrice;

	/**
	 * 쿠폰 정책의 최대 할인 금액입니다.
	 */
	@NotNull
	private int maxDiscountAmount;

	/**
	 * 쿠폰 정책의 시작일입니다.
	 */
	@NotNull
	private LocalDate startDate;

	/**
	 * 쿠폰 정책의 종료일입니다.
	 */
	@NotNull
	private LocalDate endDate;

	/**
	 * 쿠폰 정책의 삭제 여부입니다.
	 */
	@NotNull
	private boolean deleted;

	/**
	 * 쿠폰 정책의 종료일을 변경합니다.
	 *
	 * @param endDate 새로운 종료일
	 */
	public void changeEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	/**
	 * 쿠폰 정책을 삭제 상태로 변경합니다.
	 */
	public void delete() {
		this.deleted = true;
	}
}
