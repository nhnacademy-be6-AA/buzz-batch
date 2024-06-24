package store.buzzbook.batch.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import store.buzzbook.batch.entity.CouponLog;

public interface CouponLogRepository extends JpaRepository<CouponLog, Long> {
}
