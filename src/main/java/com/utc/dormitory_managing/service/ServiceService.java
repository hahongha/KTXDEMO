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
import com.utc.dormitory_managing.dto.ServiceDTO;
import com.utc.dormitory_managing.entity.Services;
import com.utc.dormitory_managing.repository.ServiceRepo;

public interface ServiceService {
	ServiceDTO create(ServiceDTO serviceDTO);
	ServiceDTO update(ServiceDTO serviceDTO);
	Boolean delete(String id);
	ServiceDTO get(String id);
	ServiceDTO getByName(String name);
	List<ServiceDTO> getAll();
}
@Service
class ServiceServiceImpl implements ServiceService {

	@Autowired
	private ServiceRepo serviceRepo;
	
	@Override
	public ServiceDTO create(ServiceDTO serviceDTO) {
		try {
			ModelMapper mapper = new ModelMapper();
			Optional<Services> serviceOptional = serviceRepo.findByServiceName(serviceDTO.getServiceName());
			if(serviceOptional.isPresent()) throw new BadRequestAlertException("Service is available", "service", "missing");
			Services services = mapper.map(serviceDTO, Services.class);
			services.setServiceId(UUID.randomUUID().toString());
			serviceRepo.save(services);
			return serviceDTO;
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	@Override
	public ServiceDTO update(ServiceDTO serviceDTO) {
		try {
			ModelMapper mapper = new ModelMapper();
			Optional<Services> serviceOptional = serviceRepo.findById(serviceDTO.getServiceId());
			if(serviceOptional.isEmpty()) throw new BadRequestAlertException("Not Found Service", "Service", "missing");
			Services service = mapper.map(serviceDTO, Services.class);
			serviceRepo.save(service);
			return serviceDTO;
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	@Override
	public Boolean delete(String id) {
		try {
			Optional<Services> ServiceOptional = serviceRepo.findById(id);
			if(ServiceOptional.isEmpty()) return false;
			serviceRepo.deleteById(id);
			return true;
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	@Override
	public ServiceDTO get(String id) {
		try {
			ModelMapper mapper = new ModelMapper();
			Optional<Services> serviceOptional = serviceRepo.findById(id);
			if(serviceOptional.isEmpty()) throw new BadRequestAlertException("Not Found Service", "Service", "missing");
			return mapper.map(serviceOptional.get(), ServiceDTO.class);
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
		
	}
	
	@Override
	public ServiceDTO getByName(String name) {
		try {
			ModelMapper mapper = new ModelMapper();
			Optional<Services> serviceOptional = serviceRepo.findByServiceName(name);
			if(serviceOptional.isEmpty()) throw new BadRequestAlertException("Not Found Service", "Service", "missing");
			return mapper.map(serviceOptional.get(), ServiceDTO.class);
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	@Override
	public List<ServiceDTO> getAll() {
		
		try {
			ModelMapper mapper = new ModelMapper();
			List<Services> services = serviceRepo.findAll();
			return services.stream().map(s -> mapper.map(s, ServiceDTO.class))
					.collect(Collectors.toList());
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}
	
}
