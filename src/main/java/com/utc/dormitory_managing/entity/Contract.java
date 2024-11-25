package com.utc.dormitory_managing.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "contract")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper= false, exclude = {"student","staff", "roomType"})

public class Contract extends BaseModel{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name= "contract_id")
	private String contractId;
	
	@ManyToOne
	@JoinColumn(name = "student_id")
	private Student student;
	
	@ManyToOne
	@JoinColumn(name = "staff_id")
	private Staff staff;
	
	@ManyToOne
	@JoinColumn(name ="room_type_id")
	private RoomType roomType;
	
	@Column
	//chi phí miễn giảm
	private Long reduceCost;
	
	@Column
	private Date startDate;
	
	//tong so tien thue bang tien thuc te tru chi phi mien giam
	@Column
	private Long contractRent;
	
	@Column
	private Date endDate;
	
	//trang thai hop dong
	@Column
	private String contractStatus;
}
