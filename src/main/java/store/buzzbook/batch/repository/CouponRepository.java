package store.buzzbook.batch.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import store.buzzbook.batch.entity.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

	Page<Coupon> findByExpireDateIsBefore(LocalDate dateTime, Pageable pageable);
}
