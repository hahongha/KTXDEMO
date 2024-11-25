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
import com.utc.dormitory_managing.dto.BuildingDTO;
import com.utc.dormitory_managing.entity.Building;
import com.utc.dormitory_managing.repository.BuildingRepo;

public interface BuildingService {
	BuildingDTO create(BuildingDTO buildingDTO);
	BuildingDTO update(BuildingDTO buildingDTO);
	Boolean delete(String id);
	BuildingDTO get(String id);
	List<BuildingDTO> getAll();
}
@Service
class BuildingServiceImpl implements BuildingService {

	@Autowired
	private BuildingRepo BuildingRepo;
	
	@Override
	public BuildingDTO create(BuildingDTO BuildingDTO) {
		try {
			ModelMapper mapper = new ModelMapper();
			Building Building = mapper.map(BuildingDTO, Building.class);
			Building.setBuildingId(UUID.randomUUID().toString());
			BuildingRepo.save(Building);
			return BuildingDTO;
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	@Override
	public BuildingDTO update(BuildingDTO BuildingDTO) {
		try {
			ModelMapper mapper = new ModelMapper();
			Optional<Building> BuildingOptional = BuildingRepo.findById(BuildingDTO.getBuildingId());
			if(BuildingOptional.isEmpty()) throw new BadRequestAlertException("Not Found Building", "Building", "missing");
			Building Building = mapper.map(BuildingDTO, Building.class);
			BuildingRepo.save(Building);
			return BuildingDTO;
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	@Override
	public Boolean delete(String id) {
		try {
			Optional<Building> BuildingOptional = BuildingRepo.findById(id);
			if(BuildingOptional.isEmpty()) return false;
			BuildingRepo.deleteById(id);
			return true;
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	@Override
	public BuildingDTO get(String id) {
		try {
			ModelMapper mapper = new ModelMapper();
			Optional<Building> BuildingOptional = BuildingRepo.findById(id);
			if(BuildingOptional.isEmpty()) throw new BadRequestAlertException("Not Found Building", "Building", "missing");
			return mapper.map(BuildingOptional.get(), BuildingDTO.class);
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
		
	}

	@Override
	public List<BuildingDTO> getAll() {
		
		try {
			ModelMapper mapper = new ModelMapper();
			List<Building> Buildings = BuildingRepo.findAll();
			return Buildings.stream().map(s -> mapper.map(s, BuildingDTO.class))
					.collect(Collectors.toList());
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}
	
}
