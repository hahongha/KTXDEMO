package com.utc.dormitory_managing.entity;

import java.util.Date;

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
@Table(name = "clock")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper= false, exclude = {"room"})
//se duoc tao dau thang
//dong ho do dien va nuoc

public class Clock extends BaseModel {
private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name ="clock_id")
	private String clockId;
	
	@Column(name= "clock_des")
	private String clockDes;
	
	@ManyToOne
	@JoinColumn(name = "room_id")
	private Room room;
	
	@Column
	private int previosIndex;
	
	@Column
	private int lastIndex;
	
	@Column
	private Date startDate;
	
	@Column
	private Date endDate;
	
	@Column
	private Long value;
}
