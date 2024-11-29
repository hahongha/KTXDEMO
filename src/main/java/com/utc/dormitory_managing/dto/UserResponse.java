package com.utc.dormitory_managing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserResponse {
	private String userName;
	private String passWord;
}
