package com.utc.dormitory_managing.dto;

import com.utc.dormitory_managing.entity.Bill;
import com.utc.dormitory_managing.entity.Services;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillDetailDTO {
	private String billDetailId;
	
	private Services services;
	
	private BillDTO bill;
	
	private int number;
	
	private Long value;
}
