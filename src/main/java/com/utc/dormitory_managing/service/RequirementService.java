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
import com.utc.dormitory_managing.dto.RequirementDTO;
import com.utc.dormitory_managing.entity.Requirement;
import com.utc.dormitory_managing.repository.RequirementRepo;
public interface RequirementService {
	RequirementDTO create(RequirementDTO RequirementDTO);
	RequirementDTO update(RequirementDTO RequirementDTO);
	Boolean delete(String id);
	RequirementDTO get(String id);
	List<RequirementDTO> getAll();
}
@Service
class RequirementServiceImpl implements RequirementService {

	@Autowired
	private RequirementRepo RequirementRepo;
	
	@Override
	public RequirementDTO create(RequirementDTO RequirementDTO) {
		try {
			ModelMapper mapper = new ModelMapper();
			Requirement Requirement = mapper.map(RequirementDTO, Requirement.class);
			Requirement.setRequirementId(UUID.randomUUID().toString());
			RequirementRepo.save(Requirement);
			return RequirementDTO;
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	@Override
	public RequirementDTO update(RequirementDTO RequirementDTO) {
		try {
			ModelMapper mapper = new ModelMapper();
			Optional<Requirement> RequirementOptional = RequirementRepo.findById(RequirementDTO.getRequirementId());
			if(RequirementOptional.isEmpty()) throw new BadRequestAlertException("Not Found Requirement", "Requirement", "missing");
			Requirement Requirement = mapper.map(RequirementDTO, Requirement.class);
			RequirementRepo.save(Requirement);
			return RequirementDTO;
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	@Override
	public Boolean delete(String id) {
		try {
			Optional<Requirement> RequirementOptional = RequirementRepo.findById(id);
			if(RequirementOptional.isEmpty()) return false;
			RequirementRepo.deleteById(id);
			return true;
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	@Override
	public RequirementDTO get(String id) {
		try {
			ModelMapper mapper = new ModelMapper();
			Optional<Requirement> RequirementOptional = RequirementRepo.findById(id);
			if(RequirementOptional.isEmpty()) throw new BadRequestAlertException("Not Found Requirement", "Requirement", "missing");
			return mapper.map(RequirementOptional.get(), RequirementDTO.class);
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
		
	}

	@Override
	public List<RequirementDTO> getAll() {
		
		try {
			ModelMapper mapper = new ModelMapper();
			List<Requirement> Requirements = RequirementRepo.findAll();
			return Requirements.stream().map(s -> mapper.map(s, RequirementDTO.class))
					.collect(Collectors.toList());
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}
	
}