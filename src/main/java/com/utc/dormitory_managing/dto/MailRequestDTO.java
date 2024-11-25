package com.utc.dormitory_managing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MailRequestDTO {
	private StaffDTO staffDTO;
	private StudentDTO studentDTO;
	private String subject;
}
