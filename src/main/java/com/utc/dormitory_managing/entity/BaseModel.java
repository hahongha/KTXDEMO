package com.utc.dormitory_managing.entity;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

@MappedSuperclass
@Data
public abstract class BaseModel implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Column(name = "create_at")
	@CreationTimestamp
	private Date createAt;

	@Column(name = "update_at")
	private Date updateAt;

	@Column(name = "create_by")
	private String createBy;
	
	@Column(name = "update_by")
	private String updateBy;
}