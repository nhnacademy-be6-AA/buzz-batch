package store.buzzbook.batch.job;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Collections;
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
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;
import store.buzzbook.batch.common.utils.ZonedDateTimeUtils;
import store.buzzbook.batch.entity.CouponLog;
import store.buzzbook.batch.entity.constant.CouponStatus;
import store.buzzbook.batch.repository.CouponLogRepository;

@Configuration
@RequiredArgsConstructor
public class ExpireCouponJobConfig {

	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;
	private final CouponLogRepository couponLogRepository;

	@Bean("expireCouponJob")
	public Job expireCouponJob(Step expireCouponStep) {
		return new JobBuilder("expireCouponJob", jobRepository)
			.incrementer(new RunIdIncrementer())
			.start(expireCouponStep)
			.build();
	}

	@JobScope
	@Bean("expireCouponStep")
	public Step expireCouponStep(ItemReader<CouponLog> expireCouponReader,
		ItemProcessor<CouponLog, CouponLog> expireCouponProcessor,
		ItemWriter<CouponLog> expireCouponWriter) {
		return new StepBuilder("expireCouponStep", jobRepository)
			.<CouponLog, CouponLog>chunk(500, transactionManager)
			.reader(expireCouponReader)
			.processor(expireCouponProcessor)
			.writer(expireCouponWriter)
			.build();
	}

	@StepScope
	@Bean
	public RepositoryItemReader<CouponLog> expireCouponReader() {
		return new RepositoryItemReaderBuilder<CouponLog>()
			.name("expireCouponReader")
			.repository(couponLogRepository)
			.methodName("findByExpireDateIsAfter")
			.pageSize(500)
			.arguments(Collections.singletonList(ZonedDateTimeUtils.getMidnight()))
			.sorts(Collections.singletonMap("id", Sort.Direction.ASC))
			.build();
	}

	@StepScope
	@Bean
	public ItemProcessor<CouponLog, CouponLog> expireCouponProcessor() {
		return item -> {item.setStatus(CouponStatus.EXPIRED);
			return item;
		};
	}

	@StepScope
	@Bean
	public RepositoryItemWriter<CouponLog> expireCouponWriter() {
		return new RepositoryItemWriterBuilder<CouponLog>()
			.repository(couponLogRepository)
			.methodName("save")
			.build();
	}
}
