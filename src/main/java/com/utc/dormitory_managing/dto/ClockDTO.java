package com.utc.dormitory_managing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClockDTO {
	private String id;
	private String name;
	private int previosIndex;
	private int lastIndex;
	private int month;
	private int year;
	private Long value;
}
