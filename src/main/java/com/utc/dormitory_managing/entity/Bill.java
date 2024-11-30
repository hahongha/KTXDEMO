package com.utc.dormitory_managing.entity;


import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.utc.dormitory_managing.dto.BillDetailDTO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "bill")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false, exclude = {"room","studentPay", "staff","billDetails"})
public class Bill extends BaseModel {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name ="bill_id")
	private String billId;
	
	@Column(name= "bill_name")
	private String billName;
	
	@Column(name= "bill_des")
	private String billDescription;
	
	@ManyToOne
	@JoinColumn(name = "room_id")
	private Room room;
	
	@ManyToOne
	@JoinColumn(name = "student_id")
	private Student studentPay;
	
	@ManyToOne
	@JoinColumn(name = "staff_id")
	private Staff staff;
	
	@Column
	private String billStatus;
	
	@Column
	private Long billValue;
	
	@Column
	private Date dayPay;
	
	@OneToMany(mappedBy = "bill", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BillDetail> billDetails;
	
}
