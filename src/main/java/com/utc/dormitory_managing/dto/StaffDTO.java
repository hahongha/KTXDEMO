package com.utc.dormitory_managing.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StaffDTO {
	private String staffId;
	
	private String fullname;
	
	private Date dateOfBirth;
	
	private Date startDate;
	
	private Date endDate;
	
	private String staffHome;
	
	private Boolean staffGender;
	
	
	private String staffIdentification;
	
	private String phoneNumber;
	
	private String relativesPhone;
	
	private String staffEmail;
	
}
