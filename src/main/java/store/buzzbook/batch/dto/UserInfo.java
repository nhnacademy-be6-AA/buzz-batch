package store.buzzbook.batch.dto;

import java.time.LocalDate;

import lombok.Builder;

@Builder
public record UserInfo(
	Long id,
	String loginId,
	String contactNumber,
	String name,
	String email,
	LocalDate birthday
) {
}
