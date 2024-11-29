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
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import com.utc.dormitory_managing.apis.error.BadRequestAlertException;
import com.utc.dormitory_managing.dto.RequirementDTO;
import com.utc.dormitory_managing.dto.ResponseDTO;
import com.utc.dormitory_managing.service.RequirementService;
@RestController
@RequestMapping("/requirement")
public class RequirementAPI {
	@Autowired
	private RequirementService RequirementService;

	private static final String ENTITY_NAME = "Requirement";

	@PostMapping("")
	public ResponseDTO<RequirementDTO> create(@RequestBody RequirementDTO RequirementDTO) throws URISyntaxException {
		RequirementService.create(RequirementDTO);
		return ResponseDTO.<RequirementDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(RequirementDTO).build();

	}

	@GetMapping("/{id}")
	public ResponseDTO<RequirementDTO> get(@PathVariable(value = "id") String id) {
		return ResponseDTO.<RequirementDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(RequirementService.get(id))
				.build();
	}
	@GetMapping("/getAll")
	public ResponseDTO<List<RequirementDTO>> getAll() {
		return ResponseDTO.<List<RequirementDTO>>builder().code(String.valueOf(HttpStatus.OK.value())).data(RequirementService.getAll())
				.build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseDTO<Void> delete(@PathVariable(value = "id") String id) throws URISyntaxException {
		if (id == null) {
			throw new BadRequestAlertException("Bad request: missing id", ENTITY_NAME, "missing_id");
		}
		RequirementService.delete(id);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}
	
	@PutMapping("/{id}")
	public ResponseDTO<RequirementDTO> update(@PathVariable(value = "id") String id,@RequestBody RequirementDTO RequirementDTO) throws URISyntaxException {
		RequirementDTO.setRequirementId(id);
		RequirementService.update(RequirementDTO);
		return ResponseDTO.<RequirementDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(RequirementDTO).build();

	}
}
