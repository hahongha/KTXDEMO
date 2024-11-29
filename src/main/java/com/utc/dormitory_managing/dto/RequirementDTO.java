package com.utc.dormitory_managing.dto;


import java.util.Date;

import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequirementDTO {
	private String requirementId;
	
	//loai yeu cau khieu nai gi
	private String requirementName;
	
	//mo ta
	private String requirementDes;
	
	//nguoi khieu nai
	private StudentDTO student;
	
	private StaffDTO staff;
	
	//trang thai da xu li hay chua
	private String status;
	
	private Date createAt;
	
	private Date updateAt;
	
	@Lob
    private byte[] image;
}
