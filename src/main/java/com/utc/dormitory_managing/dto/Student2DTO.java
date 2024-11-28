package com.utc.dormitory_managing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Student2DTO {
	private StudentDTO studentDTO;
	private RoomDTO roomDTO;
	private UserDTO userDTO;
}
