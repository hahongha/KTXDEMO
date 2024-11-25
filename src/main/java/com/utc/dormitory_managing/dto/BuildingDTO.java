package com.utc.dormitory_managing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuildingDTO {
	private String buildingId;
	private String buildingName;
	private String buildingDescription;
	private Boolean buildingGender;
}
