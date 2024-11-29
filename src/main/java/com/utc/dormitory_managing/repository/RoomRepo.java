package com.utc.dormitory_managing.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.utc.dormitory_managing.entity.Room;

public interface RoomRepo extends JpaRepository<Room, String>  {
	@Query("SELECT Count(a.roomId) from Room a where a.roomType.roomTypeId = :x and a.roomGender = :y")
	Long getRoomNumber(@Param("x") String roomTypeId,@Param("y") Boolean roomGender );
	
	@Query("SELECT a from Room a where a.roomType.roomTypeId = :x ")
	List<Room> findByRoomType(@Param("x") String roomTypeId);
	
	@Query("SELECT a from Room a where a.roomName = :x ")
	Optional<Room> findByRoomName(@Param("x") String roomName);
	
	@Query("SELECT a from Room a where a.roomType.roomTypeId = :x and a.roomGender= :y and a.roomValid = :z ")
	List<Room> findByRoomTypeGender(@Param("x") String roomTypeId, @Param("y") Boolean gender, @Param("z") Boolean roomValid );

}
