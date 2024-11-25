package com.utc.dormitory_managing.apis;

import java.net.URISyntaxException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.utc.dormitory_managing.apis.error.BadRequestAlertException;
import com.utc.dormitory_managing.dto.ResponseDTO;
import com.utc.dormitory_managing.dto.ContractDTO;
import com.utc.dormitory_managing.service.ContractService;

@RestController
@RequestMapping("/contract")
public class ContractAPI {
	@Autowired
	private ContractService ContractService;

	private static final String ENTITY_NAME = "Contract";

	@PostMapping("")
	public ResponseDTO<ContractDTO> create(@RequestBody ContractDTO ContractDTO) throws URISyntaxException {
		ContractService.create(ContractDTO);
		return ResponseDTO.<ContractDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(ContractDTO).build();

	}

	@GetMapping("/{id}")
	public ResponseDTO<ContractDTO> get(@PathVariable(value = "id") String id) {
		return ResponseDTO.<ContractDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(ContractService.get(id))
				.build();
	}
	@GetMapping("/getAll")
	public ResponseDTO<List<ContractDTO>> getAll() {
		return ResponseDTO.<List<ContractDTO>>builder().code(String.valueOf(HttpStatus.OK.value())).data(ContractService.getAll())
				.build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseDTO<Void> delete(@PathVariable(value = "id") String id) throws URISyntaxException {
		if (id == null) {
			throw new BadRequestAlertException("Bad request: missing id", ENTITY_NAME, "missing_id");
		}
		ContractService.delete(id);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}
	
	@PutMapping("/{id}")
	public ResponseDTO<ContractDTO> update(@PathVariable(value = "id") String id,@RequestBody ContractDTO ContractDTO) throws URISyntaxException {
		ContractDTO.setContractId(id);
		ContractService.update(ContractDTO);
		return ResponseDTO.<ContractDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(ContractDTO).build();

	}
}
