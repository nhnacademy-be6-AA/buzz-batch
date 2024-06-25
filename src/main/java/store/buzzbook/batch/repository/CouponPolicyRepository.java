package store.buzzbook.batch.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import store.buzzbook.batch.entity.CouponPolicy;

public interface CouponPolicyRepository extends JpaRepository<CouponPolicy, Integer> {
}
