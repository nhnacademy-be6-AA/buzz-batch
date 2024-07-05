package store.buzzbook.batch.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import store.buzzbook.batch.entity.CouponPolicy;

public interface CouponPolicyRepository extends JpaRepository<CouponPolicy, Integer> {

	Optional<CouponPolicy> findByName(String name);

	boolean existsByName(String name);
}
