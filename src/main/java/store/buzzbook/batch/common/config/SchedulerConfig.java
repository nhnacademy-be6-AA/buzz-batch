package store.buzzbook.batch.common.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class SchedulerConfig {

	private final JobLauncher jobLauncher;
	private final Job expireCouponJob;

	@Scheduled(cron = "${schedule.expire.coupon.cron}")
	public void expireCouponJob() throws JobExecutionException {
		JobParameters jobParameters = new JobParametersBuilder()
			.addLong("expired-coupon-job-execution-time", System.currentTimeMillis())
			.toJobParameters();

		jobLauncher.run(expireCouponJob, jobParameters);
	}
}
