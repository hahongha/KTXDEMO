package com.utc.dormitory_managing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Building2DTO {
	private String buildingId;
	private String buildingName;
	private String buildingDescription;
	private Boolean buildingGender;
	private int numberF;
	private int numberR;
}
