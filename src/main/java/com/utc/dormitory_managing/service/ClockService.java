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
import com.utc.dormitory_managing.dto.ClockDTO;
import com.utc.dormitory_managing.entity.Clock;
import com.utc.dormitory_managing.entity.Room;
import com.utc.dormitory_managing.repository.ClockRepo;
import com.utc.dormitory_managing.repository.RoomRepo;

import jakarta.persistence.NoResultException;

public interface ClockService {
	ClockDTO create(ClockDTO ClockDTO);
	ClockDTO update(ClockDTO ClockDTO);
	Boolean delete(String id);
	ClockDTO get(String id);
	List<ClockDTO> getAll();
}
@Service
class ClockServiceImpl implements ClockService {

	@Autowired
	private ClockRepo ClockRepo;
	
	@Autowired
	private RoomRepo roomRepo;
	
	@Override
	public ClockDTO create(ClockDTO ClockDTO) {
		try {
			ModelMapper mapper = new ModelMapper();
			Clock Clock = mapper.map(ClockDTO, Clock.class);
			Room room = roomRepo.findById(ClockDTO.getRoom().getRoomId()).orElseThrow(NoResultException::new);
			Clock.setRoom(room);
			Clock.setClockId(UUID.randomUUID().toString());
			Long value =(long) (Clock.getLastIndex()- Clock.getPreviosIndex())* 4000;
			Clock.setValue(value);
			ClockRepo.save(Clock);
			return ClockDTO;
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	@Override
	public ClockDTO update(ClockDTO ClockDTO) {
		try {
			ModelMapper mapper = new ModelMapper();
			Optional<Clock> ClockOptional = ClockRepo.findById(ClockDTO.getClockId());
			if(ClockOptional.isEmpty()) throw new BadRequestAlertException("Not Found Clock", "Clock", "missing");
			Clock Clock = mapper.map(ClockDTO, Clock.class);
			Room room = roomRepo.findById(ClockDTO.getRoom().getRoomId()).orElseThrow(NoResultException::new);
			Clock.setRoom(room);
			Long value =(long) (Clock.getLastIndex()- Clock.getPreviosIndex())* 4000;
			Clock.setValue(value);
			ClockRepo.save(Clock);
			return ClockDTO;
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	@Override
	public Boolean delete(String id) {
		try {
			Optional<Clock> ClockOptional = ClockRepo.findById(id);
			if(ClockOptional.isEmpty()) return false;
			ClockRepo.deleteById(id);
			return true;
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	@Override
	public ClockDTO get(String id) {
		try {
			ModelMapper mapper = new ModelMapper();
			Optional<Clock> ClockOptional = ClockRepo.findById(id);
			if(ClockOptional.isEmpty()) throw new BadRequestAlertException("Not Found Clock", "Clock", "missing");
			return mapper.map(ClockOptional.get(), ClockDTO.class);
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
		
	}

	@Override
	public List<ClockDTO> getAll() {
		
		try {
			ModelMapper mapper = new ModelMapper();
			List<Clock> Clocks = ClockRepo.findAll();
			return Clocks.stream().map(s -> mapper.map(s, ClockDTO.class))
					.collect(Collectors.toList());
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}
	
}
