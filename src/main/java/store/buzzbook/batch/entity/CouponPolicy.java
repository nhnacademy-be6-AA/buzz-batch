package store.buzzbook.batch.entity;

import java.time.ZonedDateTime;

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
import lombok.Setter;
import store.buzzbook.batch.entity.constant.DiscountType;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Entity
public class CouponPolicy {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotNull
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private CouponType couponType;

	@NotNull
	@Column(length = 30)
	private String name;

	@NotNull
	@Enumerated(value = EnumType.STRING)
	private DiscountType discountType;

	@NotNull
	@ColumnDefault("1.0")
	private double discountRate;

	@NotNull
	private int discountAmount;

	@NotNull
	private int period;

	@NotNull
	private int standardPrice;

	@NotNull
	private int maxDiscountAmount;

	@NotNull
	private ZonedDateTime startDate;

	@Setter
	@NotNull
	private ZonedDateTime endDate;

	@Setter
	@NotNull
	private boolean isDeleted;
}
