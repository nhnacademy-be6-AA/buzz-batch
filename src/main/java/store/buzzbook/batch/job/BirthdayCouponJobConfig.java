package store.buzzbook.batch.job;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import store.buzzbook.batch.adapter.UserAdapter;
import store.buzzbook.batch.common.util.CodeCreator;
import store.buzzbook.batch.dto.CreateUserCouponRequest;
import store.buzzbook.batch.dto.UserInfo;
import store.buzzbook.batch.entity.Coupon;
import store.buzzbook.batch.entity.CouponPolicy;
import store.buzzbook.batch.entity.CouponType;
import store.buzzbook.batch.entity.constant.CouponScope;
import store.buzzbook.batch.entity.constant.CouponStatus;
import store.buzzbook.batch.entity.constant.DiscountType;
import store.buzzbook.batch.repository.CouponPolicyRepository;
import store.buzzbook.batch.repository.CouponRepository;
import store.buzzbook.batch.repository.CouponTypeRepository;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class BirthdayCouponJobConfig {

	private static final String BIRTHDAY_COUPON_POLICY_NAME = "생일 쿠폰";

	private final UserAdapter userAdapter;
	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;
	private final CouponPolicyRepository couponPolicyRepository;
	private final CouponRepository couponRepository;
	private final CouponTypeRepository couponTypeRepository;

	@Bean("birthdayCouponJob")
	public Job birthdayCouponJob(Step birthdayCouponStep) {
		return new JobBuilder("birthdayCouponJob", jobRepository)
			.incrementer(new RunIdIncrementer())
			.start(birthdayCouponStep)
			.build();
	}

	@JobScope
	@Bean("birthdayCouponStep")
	public Step birthdayCouponStep(ItemReader<UserInfo> birthdayCouponReader,
		ItemProcessor<UserInfo, Coupon> birthdayCouponProcessor,
		ItemWriter<Coupon> birthdayCouponWriter) {
		return new StepBuilder("birthdayCouponStep", jobRepository)
			.<UserInfo, Coupon>chunk(500, transactionManager)
			.reader(birthdayCouponReader)
			.processor(birthdayCouponProcessor)
			.writer(birthdayCouponWriter)
			.build();
	}

	@StepScope
	@Bean
	public ItemReader<UserInfo> birthdayCouponReader() {
		log.info("birthday coupon reader started");

		return new ItemReader<UserInfo>() {
			private List<UserInfo> users = userAdapter.getUsersByBirthday();
			private int nextUserIndex = 0;

			@Override
			public UserInfo read() {
				UserInfo nextUser = null;
				if (nextUserIndex < users.size()) {
					nextUser = users.get(nextUserIndex);
					nextUserIndex++;
				}
				return nextUser;
			}
		};
	}

	@StepScope
	@Bean
	public ItemProcessor<UserInfo, Coupon> birthdayCouponProcessor() {
		LocalDate now = LocalDate.now();
		YearMonth currentYearMonth = YearMonth.of(now.getYear(), now.getMonth());
		LocalDate startDate = currentYearMonth.atDay(1);
		LocalDate expireDate = currentYearMonth.atEndOfMonth();
		String birthdayCouponName = String.format("%d년 %d월 %s", currentYearMonth.getYear(), currentYearMonth.getMonthValue(), BIRTHDAY_COUPON_POLICY_NAME);
		CouponPolicy couponPolicy = getOrCreateCouponPolicy(birthdayCouponName);


		return user -> {
			String couponCode = CodeCreator.createCode();

			Coupon coupon = Coupon.builder()
				.couponPolicy(couponPolicy)
				.couponCode(couponCode)
				.createDate(startDate)
				.expireDate(expireDate)
				.status(CouponStatus.AVAILABLE)
				.build();

			userAdapter.createUserCouponByBatch(CreateUserCouponRequest.builder()
				.couponPolicyId(couponPolicy.getId())
				.couponCode(couponCode)
				.userId(user.id())
				.build());

			return coupon;
		};
	}

	private CouponPolicy getOrCreateCouponPolicy(String birthdayCouponName) {
		return couponPolicyRepository.findByName(birthdayCouponName)
			.orElseGet(() -> {
				CouponType couponType = couponTypeRepository.findByName(CouponScope.GLOBAL).orElseThrow();
				return couponPolicyRepository.save(CouponPolicy.builder()
					.name(birthdayCouponName)
					.discountType(DiscountType.RATE)
					.discountRate(0.2)
					.discountAmount(0)
					.maxDiscountAmount(10000)
					.standardPrice(20000)
					.startDate(LocalDate.EPOCH)
					.endDate(LocalDate.of(2099, 12, 31))
					.period(0)
					.deleted(false)
					.couponType(couponType)
					.build());
			});
	}

	@StepScope
	@Bean
	public RepositoryItemWriter<Coupon> birthdayCouponWriter() {
		return new RepositoryItemWriterBuilder<Coupon>()
			.repository(couponRepository)
			.methodName("save")
			.build();
	}
}
