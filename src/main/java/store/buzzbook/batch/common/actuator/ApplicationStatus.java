package store.buzzbook.batch.common.actuator;

import org.springframework.stereotype.Component;

@Component
public class ApplicationStatus {

	private boolean status = true;

	public void stopService() {
		this.status = false;
	}

	public boolean getStatus() {
		return status;
	}
}
