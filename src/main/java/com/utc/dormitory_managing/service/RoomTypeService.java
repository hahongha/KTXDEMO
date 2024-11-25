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
import com.utc.dormitory_managing.dto.RoomTypeDTO;
import com.utc.dormitory_managing.entity.RoomType;
import com.utc.dormitory_managing.repository.RoomTypeRepo;

public interface RoomTypeService {
	RoomTypeDTO create(RoomTypeDTO roomTypeDTO);
	RoomTypeDTO update(RoomTypeDTO roomTypeDTO);
	Boolean delete(String id);
	RoomTypeDTO get(String id);
	List<RoomTypeDTO> getAll();
	RoomType getEntity(String id);
}
@Service
class RoomTypeServiceImpl implements RoomTypeService {

	@Autowired
	private RoomTypeRepo RoomTypeRepo;
	
	@Override
	public RoomTypeDTO create(RoomTypeDTO RoomTypeDTO) {
		try {
			ModelMapper mapper = new ModelMapper();
			RoomType RoomType = mapper.map(RoomTypeDTO, RoomType.class);
			RoomType.setRoomTypeId(UUID.randomUUID().toString());
			RoomTypeRepo.save(RoomType);
			return RoomTypeDTO;
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	@Override
	public RoomTypeDTO update(RoomTypeDTO RoomTypeDTO) {
		try {
			ModelMapper mapper = new ModelMapper();
			Optional<RoomType> RoomTypeOptional = RoomTypeRepo.findById(RoomTypeDTO.getRoomTypeId());
			if(RoomTypeOptional.isEmpty()) throw new BadRequestAlertException("Not Found RoomType", "RoomType", "missing");
			RoomType RoomType = mapper.map(RoomTypeDTO, RoomType.class);
			RoomTypeRepo.save(RoomType);
			return RoomTypeDTO;
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	@Override
	public Boolean delete(String id) {
		try {
			Optional<RoomType> RoomTypeOptional = RoomTypeRepo.findById(id);
			if(RoomTypeOptional.isEmpty()) return false;
			RoomTypeRepo.deleteById(id);
			return true;
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	@Override
	public RoomTypeDTO get(String id) {
		try {
			ModelMapper mapper = new ModelMapper();
			Optional<RoomType> RoomTypeOptional = RoomTypeRepo.findById(id);
			if(RoomTypeOptional.isEmpty()) throw new BadRequestAlertException("Not Found RoomType", "RoomType", "missing");
			return mapper.map(RoomTypeOptional.get(), RoomTypeDTO.class);
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
		
	}

	@Override
	public List<RoomTypeDTO> getAll() {
		
		try {
			ModelMapper mapper = new ModelMapper();
			List<RoomType> RoomTypes = RoomTypeRepo.findAll();
			return RoomTypes.stream().map(s -> mapper.map(s, RoomTypeDTO.class))
					.collect(Collectors.toList());
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}
	
	@Override
	public RoomType getEntity(String id) {
		try {
			Optional<RoomType> RoomTypeOptional = RoomTypeRepo.findById(id);
			if(RoomTypeOptional.isEmpty()) throw new BadRequestAlertException("Not Found RoomType", "RoomType", "missing");
			return RoomTypeOptional.get();
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	
}