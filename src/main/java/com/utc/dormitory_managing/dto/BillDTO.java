package com.utc.dormitory_managing.dto;

import java.util.List;

import com.utc.dormitory_managing.entity.BillDetail;
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
	
	private StudentDTO studentPay;
	
	private StaffDTO staff;
	
	private String billStatus;
	
	private Long billValue;
	
	private List<BillDetail> billDetails;
}
