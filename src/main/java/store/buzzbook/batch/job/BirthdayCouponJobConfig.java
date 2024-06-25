package store.buzzbook.batch.job;

import static store.buzzbook.batch.common.utils.ZonedDateTimeUtils.*;

import java.time.ZonedDateTime;
import java.util.Collections;

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
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;
import store.buzzbook.batch.entity.CouponLog;
import store.buzzbook.batch.entity.CouponPolicy;
import store.buzzbook.batch.entity.User;
import store.buzzbook.batch.entity.constant.CouponStatus;
import store.buzzbook.batch.repository.CouponLogRepository;
import store.buzzbook.batch.repository.CouponPolicyRepository;
import store.buzzbook.batch.repository.UserRepository;

@Configuration
@RequiredArgsConstructor
public class BirthdayCouponJobConfig {

	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;
	private final UserRepository userRepository;
	private final CouponLogRepository couponLogRepository;
	private final CouponPolicyRepository couponPolicyRepository;

	@Bean("birthdayCouponJob")
	public Job birthdayCouponJob(Step birthdayCouponStep) {
		return new JobBuilder("birthdayCouponJob", jobRepository)
			.incrementer(new RunIdIncrementer())
			.start(birthdayCouponStep)
			.build();
	}

	@JobScope
	@Bean("birthdayCouponStep")
	public Step birthdayCouponStep(ItemReader<User> birthdayCouponReader,
		ItemProcessor<User, CouponLog> birthdayCouponProcessor,
		ItemWriter<CouponLog> birthdayCouponWriter) {
		return new StepBuilder("birthdayCouponStep", jobRepository)
			.<User, CouponLog>chunk(500, transactionManager)
			.reader(birthdayCouponReader)
			.processor(birthdayCouponProcessor)
			.writer(birthdayCouponWriter)
			.build();
	}

	@StepScope
	@Bean
	public RepositoryItemReader<User> birthdayCouponReader() {
		return new RepositoryItemReaderBuilder<User>()
			.name("birthdayCouponReader")
			.repository(userRepository)
			.methodName("findUsersWithBirthdayInMonth")
			.pageSize(500)
			.arguments(Collections.singletonList(getCurrentMonth()))
			.sorts(Collections.singletonMap("id", Sort.Direction.ASC))
			.build();
	}

	@StepScope
	@Bean
	public ItemProcessor<User, CouponLog> birthdayCouponProcessor() {
		CouponPolicy couponPolicy = couponPolicyRepository.findById(420)
			.orElseThrow(IllegalStateException::new);

		return item -> couponLogRepository.save(CouponLog.builder()
				.couponPolicy(couponPolicy)
				.createDate(ZonedDateTime.now())
				.expireDate(ZonedDateTime.now().plusDays(30))
				.status(CouponStatus.AVAILABLE)
				.userId(item.getId())
			.build());
	}

	@StepScope
	@Bean
	public RepositoryItemWriter<CouponLog> birthdayCouponWriter() {
		return new RepositoryItemWriterBuilder<CouponLog>()
			.repository(couponLogRepository)
			.methodName("save")
			.build();
	}
}
