package com.utc.dormitory_managing.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "requirement")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false, exclude = {"student", "staff"})
//yeu cau, khieu nai
public class Requirement extends BaseModel{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name ="requirement_id")
	private String requirementId;
	
	//loai yeu cau khieu nai gi
	@Column(name= "requirement_name")
	private String requirementName;
	
	//mo ta
	@Column(name= "requirement_des")
	private String requirementDes;
	
	//nguoi khieu nai
	@ManyToOne
	@JoinColumn(name="student_id")
	private Student student;
	
	//staff tiep nhan va xu li
	@ManyToOne
	@JoinColumn(name="staff_id")
	private Staff staff;
	
	//trang thai da xu li hay chua
	@Column(name="requirement_status")
	private String status;
	
	@Column(name ="requirement_img")
	@Lob
    private byte[] image;
}

