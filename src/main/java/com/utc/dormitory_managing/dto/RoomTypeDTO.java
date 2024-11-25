package com.utc.dormitory_managing.dto;


//import com.utc.dormitory_managing.entity.Services;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomTypeDTO {
	private String roomTypeId;
	
	private String roomTypeName;
	
	private String roomTypeDes;
	
	private Long roomTypePrice;
	
	private Long roomTypeDeposit;
	
	private int roomNumber;
}
