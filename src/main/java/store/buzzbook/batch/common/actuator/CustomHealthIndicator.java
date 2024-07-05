package store.buzzbook.batch.common.actuator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomHealthIndicator implements HealthIndicator {

	private final ApplicationStatus applicationStatus;

	@Override
	public Health health() {
		if (!applicationStatus.getStatus()) {
			return Health.down().build();
		}
		return Health.up().withDetail("service", "start").build();
	}
}
