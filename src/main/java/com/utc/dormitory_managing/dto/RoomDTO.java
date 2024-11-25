package com.utc.dormitory_managing.dto;

import java.util.HashSet;
import java.util.Set;

import com.utc.dormitory_managing.entity.Floor;
import com.utc.dormitory_managing.entity.RoomType;
import com.utc.dormitory_managing.entity.Student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDTO {
	private String roomId;
	
	private String roomName;
	
	private String roomDes;
	
	private RoomType roomType;
	
	private Floor floor;
	
	private int maxNumber;
	
	private int roomNumber;
	
	private String roomStatus;
	
	private Boolean roomGender;
	
	private Boolean roomValid;
}
