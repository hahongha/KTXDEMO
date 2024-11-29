package com.utc.dormitory_managing.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractDTO {
	private String contractId;
	
	private StudentDTO student;
	
	private StaffDTO staff;
	
	private RoomTypeDTO roomType;
//	
	//chi phí miễn giảm
	private Long reduceCost;
	
	private Date startDate;
	
	private Long contractRent;
	
	private Date endDate;
	
	private String contractStatus;
}
