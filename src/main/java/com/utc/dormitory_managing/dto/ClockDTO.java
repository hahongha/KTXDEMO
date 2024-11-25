package com.utc.dormitory_managing.dto;

import java.util.Date;

import com.utc.dormitory_managing.entity.Room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClockDTO {
	private String clockId;
	
	private String clockName;
	
	private String clockDes;
	
	private Room room;
	
	private int previosIndex;
	
	private int lastIndex;
	
	private Date startDate;
	
	private Date endDate;
	
	private Long value;
}
