package store.buzzbook.batch.repository;

import java.time.ZonedDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import store.buzzbook.batch.entity.CouponLog;

public interface CouponLogRepository extends JpaRepository<CouponLog, Long> {

	Page<CouponLog> findByExpireDateIsBefore(ZonedDateTime dateTime, Pageable pageable);
}
