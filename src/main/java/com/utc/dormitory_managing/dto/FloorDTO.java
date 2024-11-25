package com.utc.dormitory_managing.dto;

import com.utc.dormitory_managing.entity.Building;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FloorDTO {
	private String floorId;
	
	private String floorName;
	
	private Building building;
}
