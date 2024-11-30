package com.utc.dormitory_managing.dto;

import com.utc.dormitory_managing.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
	private String userId;
	private String username;
	private String password;
	private Long expired;
	private String refreshToken;
	private String accessToken;
	private RoleDTO role;
	
}
