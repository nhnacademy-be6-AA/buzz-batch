package store.buzzbook.batch.common.utils;

import java.time.ZonedDateTime;

public class ZonedDateTimeUtils {

	public static ZonedDateTime getMidnight() {
		return ZonedDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
	}
}
