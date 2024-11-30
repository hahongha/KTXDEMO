package com.utc.dormitory_managing.entity;

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
@Table(name = "billDetail")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper= false)
public class BillDetail {
	@Column
	@Id
	private String billDetailId;
	
	@ManyToOne
	@JoinColumn(name = "service_id")
	private Services services;
	
	@ManyToOne
	@JoinColumn(name = "bill_id")
	private Bill bill;
	
	@Column
	private int number = 1;
	
	@Column
	private Long value;
	
}
