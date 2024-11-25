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
import com.utc.dormitory_managing.dto.RoleDTO;
import com.utc.dormitory_managing.entity.Role;
import com.utc.dormitory_managing.repository.RoleRepo;

public interface RoleService {
	RoleDTO create(RoleDTO RoleDTO);
	RoleDTO update(RoleDTO RoleDTO);
	Boolean delete(String id);
	RoleDTO get(String id);
	List<RoleDTO> getAll();
}
@Service
class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleRepo RoleRepo;
	
	@Override
	public RoleDTO create(RoleDTO RoleDTO) {
		try {
			if(RoleRepo.findByRoleName(RoleDTO.getRoleName()).isPresent()) throw new BadRequestAlertException("Role is exist", "role", "exist");
			ModelMapper mapper = new ModelMapper();
			Role Role = mapper.map(RoleDTO, Role.class);
			Role.setRoleId(UUID.randomUUID().toString());
			RoleRepo.save(Role);
			return RoleDTO;
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	@Override
	public RoleDTO update(RoleDTO RoleDTO) {
		try {
			ModelMapper mapper = new ModelMapper();
			Optional<Role> RoleOptional = RoleRepo.findById(RoleDTO.getRoleId());
			if(RoleOptional.isEmpty()) throw new BadRequestAlertException("Not Found Role", "Role", "missing");
			Role Role = mapper.map(RoleDTO, Role.class);
			RoleRepo.save(Role);
			return RoleDTO;
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	@Override
	public Boolean delete(String id) {
		try {
			Optional<Role> RoleOptional = RoleRepo.findById(id);
			if(RoleOptional.isEmpty()) return false;
			if(RoleOptional.get().getRoleName().toUpperCase().equals("ROOT")) return false;
			RoleRepo.deleteById(id);
			return true;
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	@Override
	public RoleDTO get(String id) {
		try {
			ModelMapper mapper = new ModelMapper();
			Optional<Role> RoleOptional = RoleRepo.findById(id);
			if(RoleOptional.isEmpty()) throw new BadRequestAlertException("Not Found Role", "Role", "missing");
			return mapper.map(RoleOptional.get(), RoleDTO.class);
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
		
	}

	@Override
	public List<RoleDTO> getAll() {
		
		try {
			ModelMapper mapper = new ModelMapper();
			List<Role> Roles = RoleRepo.findAll();
			return Roles.stream().map(s -> mapper.map(s, RoleDTO.class))
					.collect(Collectors.toList());
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}
	
}
