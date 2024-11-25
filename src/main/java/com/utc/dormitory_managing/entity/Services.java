package com.utc.dormitory_managing.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "service")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper= false)
public class Services extends BaseModel {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="service_id")
	private String serviceId;
	
	@Column
	private String serviceName;
	
	@Column
	private Long servicePrice;
	
	@Column
	private Long serviceValue;
	
	@Column
	private Long serviceNumber;
}
