package com.utc.dormitory_managing.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillFormDTO {
	private RoomDTO roomDTO;
	
	private int waterPreIndex;
	
	private int waterIndex;
	
	private int ePreIndex;
	
	private int eIndex;
	
}
