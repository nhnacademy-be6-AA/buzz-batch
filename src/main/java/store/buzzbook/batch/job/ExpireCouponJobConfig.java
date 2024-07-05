package store.buzzbook.batch.job;

import java.time.LocalDate;
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
import lombok.extern.slf4j.Slf4j;
import store.buzzbook.batch.entity.Coupon;
import store.buzzbook.batch.entity.constant.CouponStatus;
import store.buzzbook.batch.repository.CouponRepository;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ExpireCouponJobConfig {

	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;
	private final CouponRepository couponRepository;

	@Bean("expireCouponJob")
	public Job expireCouponJob(Step expireCouponStep) {
		return new JobBuilder("expireCouponJob", jobRepository)
			.incrementer(new RunIdIncrementer())
			.start(expireCouponStep)
			.build();
	}

	@JobScope
	@Bean("expireCouponStep")
	public Step expireCouponStep(ItemReader<Coupon> expireCouponReader,
		ItemProcessor<Coupon, Coupon> expireCouponProcessor,
		ItemWriter<Coupon> expireCouponWriter) {
		return new StepBuilder("expireCouponStep", jobRepository)
			.<Coupon, Coupon>chunk(500, transactionManager)
			.reader(expireCouponReader)
			.processor(expireCouponProcessor)
			.writer(expireCouponWriter)
			.build();
	}

	@StepScope
	@Bean
	public RepositoryItemReader<Coupon> expireCouponReader() {
		log.info("Expire coupon reader started");
		return new RepositoryItemReaderBuilder<Coupon>()
			.name("expireCouponReader")
			.repository(couponRepository)
			.methodName("findByExpireDateIsBefore")
			.pageSize(500)
			.arguments(Collections.singletonList(LocalDate.now()))
			.sorts(Collections.singletonMap("id", Sort.Direction.ASC))
			.build();
	}

	@StepScope
	@Bean
	public ItemProcessor<Coupon, Coupon> expireCouponProcessor() {
		return item -> {item.changeStatus(CouponStatus.EXPIRED);
			return item;
		};
	}

	@StepScope
	@Bean
	public RepositoryItemWriter<Coupon> expireCouponWriter() {
		return new RepositoryItemWriterBuilder<Coupon>()
			.repository(couponRepository)
			.methodName("save")
			.build();
	}
}
