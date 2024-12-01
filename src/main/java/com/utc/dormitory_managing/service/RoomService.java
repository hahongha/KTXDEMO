package com.utc.dormitory_managing.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import com.utc.dormitory_managing.apis.error.BadRequestAlertException;
import com.utc.dormitory_managing.dto.RoomDTO;
import com.utc.dormitory_managing.entity.Floor;
import com.utc.dormitory_managing.entity.Room;
import com.utc.dormitory_managing.entity.RoomType;
import com.utc.dormitory_managing.repository.FloorRepo;
import com.utc.dormitory_managing.repository.RoomRepo;
import com.utc.dormitory_managing.repository.RoomTypeRepo;

import jakarta.persistence.NoResultException;

public interface RoomService {
	RoomDTO create(RoomDTO roomDTO);
	RoomDTO update(RoomDTO roomDTO);
	Boolean delete(String id);
	RoomDTO get(String id);
	List<RoomDTO> getAll();
	RoomDTO getByName(String name);
	
	Long checkRoomNumber(RoomType roomType, Boolean gender);
}
@Service
class RoomServiceImpl implements RoomService {

	@Autowired
	private RoomRepo RoomRepo;
	
	@Autowired
	private RoomTypeRepo roomTypeRepo;
	
	@Autowired
	private FloorRepo floorRepo;
	
	@Override
	public RoomDTO create(RoomDTO RoomDTO) {
		try {
			ModelMapper mapper = new ModelMapper();
			Room Room = mapper.map(RoomDTO, Room.class);
			RoomType roomType = roomTypeRepo.findById(RoomDTO.getRoomType().getRoomTypeId()).orElseThrow(NoResultException::new);
			Floor floor = floorRepo.findById(RoomDTO.getFloor().getFloorId()).orElseThrow(NoResultException::new);
			Room.setRoomId(UUID.randomUUID().toString());
			Room.setRoomType(roomType);
			Room.setFloor(floor);
			RoomRepo.save(Room);
			return RoomDTO;
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	@Override
	public RoomDTO update(RoomDTO RoomDTO) {
		try {
			ModelMapper mapper = new ModelMapper();
			Optional<Room> RoomOptional = RoomRepo.findById(RoomDTO.getRoomId());
			if(RoomOptional.isEmpty()) throw new BadRequestAlertException("Not Found Room", "Room", "missing");
			Room Room = mapper.map(RoomDTO, Room.class);
			RoomRepo.save(Room);
			return RoomDTO;
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	@Override
	public Boolean delete(String id) {
		try {
			Optional<Room> RoomOptional = RoomRepo.findById(id);
			if(RoomOptional.isEmpty()) return false;
			RoomRepo.deleteById(id);
			return true;
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	@Override
	public RoomDTO get(String id) {
		try {
			ModelMapper mapper = new ModelMapper();
			Optional<Room> RoomOptional = RoomRepo.findById(id);
			if(RoomOptional.isEmpty()) throw new BadRequestAlertException("Not Found Room", "Room", "missing");			
			return mapper.map(RoomOptional.get(), RoomDTO.class);
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
		
	}

	@Override
	public List<RoomDTO> getAll() {
		
		try {
			ModelMapper mapper = new ModelMapper();
			List<Room> Rooms = RoomRepo.findAll();
			return Rooms.stream().map(s -> mapper.map(s, RoomDTO.class))
					.collect(Collectors.toList());
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	@Override
	public Long checkRoomNumber(RoomType roomType, Boolean gender) {
		try {
			Long roomTypeNumber = RoomRepo.getRoomNumber(roomType.getRoomTypeId(), gender) * roomType.getRoomTypeNumber();
			return roomTypeNumber;
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	@Override
	public RoomDTO getByName(String name) {
		try {
			ModelMapper mapper = new ModelMapper();
			Optional<Room> RoomOptional = RoomRepo.findByRoomName(name);
			if(RoomOptional.isEmpty()) throw new BadRequestAlertException("Not Found Room", "Room", "missing");
			return mapper.map(RoomOptional.get(), RoomDTO.class);
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}
	
}
