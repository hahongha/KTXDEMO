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
@Table(name = "building")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper= false)

public class Building extends BaseModel{
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name ="building_id")
	private String buildingId;
	
	@Column(name= "building_name")
	private String buildingName;
	
	@Column(name= "building_des")
	private String buildingDescription;
	
	@Column(name="building_gender")
	private Boolean buildingGender;
	
}
