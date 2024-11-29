package com.utc.dormitory_managing.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "role")
@EqualsAndHashCode(callSuper = false)
public class Role extends BaseModel {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name ="role_id")
	private String roleId;
	@Column
	private String roleName;
}
