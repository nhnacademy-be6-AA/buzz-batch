package store.buzzbook.batch.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import store.buzzbook.batch.entity.CouponType;
import store.buzzbook.batch.entity.constant.CouponScope;

public interface CouponTypeRepository extends JpaRepository<CouponType, Integer> {

	Optional<CouponType> findByName(CouponScope name);
}
