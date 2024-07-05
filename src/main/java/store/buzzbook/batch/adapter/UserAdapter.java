package store.buzzbook.batch.adapter;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import store.buzzbook.batch.dto.CreateUserCouponRequest;
import store.buzzbook.batch.dto.UserInfo;

@FeignClient(name = "userAdapter", url = "http://${api.gateway.host}:" + "${api.gateway.port}/api/account/coupons")
public interface UserAdapter {

	@PostMapping
	void createUserCoupon(@RequestBody CreateUserCouponRequest request);

	@GetMapping("/birthday")
	List<UserInfo> getUsersByBirthday();

}
