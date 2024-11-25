package com.utc.dormitory_managing.entity;

import java.util.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "staff")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper= false)
public class Staff extends BaseModel{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "staff_id")
	private String staffId;
	
	@Column(name = "staff_name")
	private String fullname;
	
	@Column(name = "staff_birth")
	private Date dateOfBirth;
	
	@Column
	private Date startDate;
	
	@Column
	private Date endDate;
	
	@Column
	private String staffHome;
	
	@Column
	private Boolean staffGender;
	
	
	@Column (name = "staff_identification", unique = true)
	private String staffIdentification;
	
	@Column(name ="staff_phone",unique = true)
	private String phoneNumber;
	
	@Column
	private String relativesPhone;
	
	@Column(name = "staff_email", unique = true)
	private String staffEmail;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "userId", referencedColumnName = "user_id")
	private  User user;
}
