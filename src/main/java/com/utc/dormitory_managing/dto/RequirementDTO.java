package com.utc.dormitory_managing.dto;

import com.utc.dormitory_managing.entity.Staff;
import com.utc.dormitory_managing.entity.Student;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
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
	private Student student;
	
	private Staff staff;
	
	//trang thai da xu li hay chua
	private String status;
	
	@Lob
    private byte[] image;
}
