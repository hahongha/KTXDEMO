package com.utc.dormitory_managing.entity;

import java.util.HashSet;
import java.util.Set;

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
@Table(name = "room")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper= false, exclude = { "floor", "roomType"})

public class Room extends BaseModel{
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name ="room_id")
	private String roomId;
	
	//ten phong:101A2
	@Column(name= "room_name")
	private String roomName;
	
	//mo ta phong
	@Column(name= "room_des")
	private String roomDes;
	
	//loai phong
	@ManyToOne
	@JoinColumn(name = "room_type_id")
	private RoomType roomType;
	
	//tang
	@ManyToOne
	@JoinColumn(name = "floor_id")
	private Floor floor;
	
	//so thanh vien dang o
	@Column
	private int roomNumber = 0;
	
	//trang thai phong: dang su dung, dang sua chua, dang hu hong khong the su dung
	@Column
	private String roomStatus;
	
	//phong nam phong nu
	//nam 0 nu 1
	@Column
	private Boolean roomGender;
	
	//kiem tra trang thai phong con trong khong
	@Column
	private Boolean roomValid = true;
	
}
