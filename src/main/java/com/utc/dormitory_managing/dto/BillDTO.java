package com.utc.dormitory_managing.dto;

import com.utc.dormitory_managing.entity.Staff;
import com.utc.dormitory_managing.entity.Student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillDTO {

	private String billId;
	
	private String billName;
	
	private String billDescription;
	
	private Student studentPay;
	
	private Staff staff;
	
	private String billStatus;
	
	private Long billValue;
}
