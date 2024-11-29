package com.utc.dormitory_managing.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {

	private String studentId;
	
	private String fullname;
	
	private Date dateOfBirth;
	
	private Date startDate;
	
	private Date endDate;
	
	private String studentClass;
	
	private String ethnicity;
	
	private String studentAddress;
	
	private Boolean studentGender;
	
	private String studentIdentification;
	
	private String phoneNumber;
	
	private String relativesPhone;
	
	private String studentEmail;
	
	private int studentPriority;
	
	private int studentStatus;
	
	private String roomName;
	
	private UserResponse userResponse;
	
}
