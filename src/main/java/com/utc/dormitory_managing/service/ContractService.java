package com.utc.dormitory_managing.service;

import java.util.Date;
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
import com.utc.dormitory_managing.configuration.ApplicationProperties.StatusContractRef;
import com.utc.dormitory_managing.dto.ContractDTO;
import com.utc.dormitory_managing.entity.Contract;
import com.utc.dormitory_managing.entity.RoomType;
import com.utc.dormitory_managing.repository.ContractRepo;
import com.utc.dormitory_managing.utils.Utils;
import com.utc.dormitory_managing.utils.Utils.DateRange;

public interface ContractService {
	ContractDTO create(ContractDTO contractDTO);
	ContractDTO update(ContractDTO contractDTO);
	Boolean delete(String id);
	ContractDTO get(String id);
	List<ContractDTO> getAll();
	ContractDTO updateStatus(ContractDTO contractDTO, String status);
	//kiem tra so luong hop dong con dang hoat dong
	Long checkContractNumber(RoomType roomType, Boolean gender, String contractStatus);
	//kiem tra xem cac hop dong sap het han trong vong 1 thang toi// hop dong cua cac sinh vien nam cuoi
	List<ContractDTO> contractIsExpired();
	
	void contractExpired();
}
@Service
class ContractServiceImpl implements ContractService {

	@Autowired
	private ContractRepo ContractRepo;
	
	@Override
	public ContractDTO create(ContractDTO ContractDTO) {
		try {
			ModelMapper mapper = new ModelMapper();
			Contract Contract = mapper.map(ContractDTO, Contract.class);
			Contract.setContractId(UUID.randomUUID().toString());
			Contract.setContractStatus(StatusContractRef.WAITING.toString());
			ContractRepo.save(Contract);
			return ContractDTO;
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	@Override
	public ContractDTO update(ContractDTO ContractDTO) {
		try {
			ModelMapper mapper = new ModelMapper();
			Optional<Contract> ContractOptional = ContractRepo.findById(ContractDTO.getContractId());
			if(ContractOptional.isEmpty()) throw new BadRequestAlertException("Not Found Contract", "Contract", "missing");
			Contract Contract = mapper.map(ContractDTO, Contract.class);
			ContractRepo.save(Contract);
			return ContractDTO;
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}
	
	

	@Override
	public Boolean delete(String id) {
		try {
			Optional<Contract> ContractOptional = ContractRepo.findById(id);
			if(ContractOptional.isEmpty()) return false;
			ContractRepo.deleteById(id);
			return true;
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	@Override
	public ContractDTO get(String id) {
		try {
			ModelMapper mapper = new ModelMapper();
			Optional<Contract> ContractOptional = ContractRepo.findById(id);
			if(ContractOptional.isEmpty()) throw new BadRequestAlertException("Not Found Contract", "Contract", "missing");
			return mapper.map(ContractOptional.get(), ContractDTO.class);
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
		
	}

	@Override
	public List<ContractDTO> getAll() {
		
		try {
			ModelMapper mapper = new ModelMapper();
			List<Contract> Contracts = ContractRepo.findAll();
			return Contracts.stream().map(s -> mapper.map(s, ContractDTO.class))
					.collect(Collectors.toList());
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	@Override
	public ContractDTO updateStatus(ContractDTO contractDTO, String status) {
		try {
			ModelMapper mapper = new ModelMapper();
			Optional<Contract> contractOptional = ContractRepo.findById(contractDTO.getContractId());
			if(contractOptional.isEmpty()) throw new BadRequestAlertException("Not Found Contract", "Contract", "missing");
			Contract contract = contractOptional.get();
			contract.setContractStatus(status);
			ContractRepo.save(contract);
			return mapper.map(contract, ContractDTO.class);
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	@Override
	public Long checkContractNumber(RoomType roomType, Boolean gender, String status) {
		Long contractNumber = ContractRepo.getContractNumber(roomType.getRoomTypeId(), status, gender);
		return contractNumber;
	}

	//hợp đông sắp hết hạn
	@Override
	public List<ContractDTO> contractIsExpired() {
		ModelMapper mapper = new ModelMapper();
		DateRange dateRange = Utils.getCurrentMonth();
		List<Contract> contractExpired = ContractRepo.findContractExpired(StatusContractRef.ACTIVE.toString(), dateRange.getStartDate(), dateRange.getEndDate());
		return contractExpired.stream().map(c -> mapper.map(c, ContractDTO.class)).collect(Collectors.toList());
	}
	//cap nhat trang thai cac hop dong da het han
	@Override
	public void contractExpired() {
		ModelMapper mapper = new ModelMapper();
		List<Contract> contractExpired = ContractRepo.contractExpired(StatusContractRef.ACTIVE.toString(), new Date());
		List<ContractDTO> contractDTO = contractExpired.stream().map(c -> mapper.map(c, ContractDTO.class)).collect(Collectors.toList());
		for (ContractDTO contractDTO2 : contractDTO) {
			updateStatus(contractDTO2, StatusContractRef.SUSPEND.toString());
		}
	}


}
