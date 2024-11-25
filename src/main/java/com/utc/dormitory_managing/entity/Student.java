package com.utc.dormitory_managing.entity;

import java.util.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "student")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false, exclude = {"user", "room"})
public class Student extends BaseModel{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "student_id")
	private String studentId;
	
	@Column(name = "student_name")
	private String fullname;
	
	@Column
	private Date startDate;
	
	//cấu hình mặc định 4 năm
	@Column
	private Date endDate;
	
	@Column
	private String ethnicity;
	
	//thông tin khoa, khóa với lớp
	@Column
	private String studentClass;
	
	@Column
	private Date dateOfBirth;
	
	@Column
	private String studentAddress;
	
	@Column
	private Boolean studentGender;
	
	
	@Column (name = "student_identification", unique = true)
	private String studentIdentification;
	
	@Column(name ="student_phone",unique = true)
	private String phoneNumber;
	
	@Column
	private String relativesPhone;
	
	@Column(name = "student_email", unique = true)
	private String studentEmail;
	
	//có thuộc diện ưu tiên không thuộc mức nào
	//các bạn tìm các diện ưu tiên rồi set cho tớ nhé
	//có thể dùng radio/dropdown
	@Column(name = "student_priority")
	private int studentPriority;
	
	@Column(name = "student_status")
	private int studentStatus;
	

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "userId", referencedColumnName = "user_id")
	private User user;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="room_id")
    private Room room;
}
