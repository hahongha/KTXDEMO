package com.utc.dormitory_managing.dto;


import com.utc.dormitory_managing.entity.Floor;
import com.utc.dormitory_managing.entity.RoomType;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDTO {
	private String roomId;
	
	private String roomName;
	
	private String roomDes;
	
	private RoomTypeDTO roomType;
	
	private FloorDTO floor;
	
	private int maxNumber;
	
	private int roomNumber;
	
	private String roomStatus;
	
	private Boolean roomGender;
	
	private Boolean roomValid;
	
	private int preWater=0;
	
	private int lastWater=0;
	
	private int preElectronic=0;
	
	private int lastElectronic=0;
	
}
