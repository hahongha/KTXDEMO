package com.utc.dormitory_managing.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "floor")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper= false)

public class Floor extends BaseModel{
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name ="floor_id")
	private String floorId;
	
	@Column(name= "floor_name")
	private String floorName;
	
	@ManyToOne
	@JoinColumn(name = "building_id")
	private Building building;
}
