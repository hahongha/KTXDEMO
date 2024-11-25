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
import com.utc.dormitory_managing.dto.StaffDTO;
import com.utc.dormitory_managing.service.StaffService;

@RestController
@RequestMapping("/staff")
public class StaffAPI {
	@Autowired
	private StaffService StaffService;

	private static final String ENTITY_NAME = "Staff";

	@PostMapping("")
	public ResponseDTO<StaffDTO> create(@RequestBody StaffDTO StaffDTO) throws URISyntaxException {
		StaffService.create(StaffDTO);
		return ResponseDTO.<StaffDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(StaffDTO).build();

	}

	@GetMapping("/{id}")
	public ResponseDTO<StaffDTO> get(@PathVariable(value = "id") String id) {
		return ResponseDTO.<StaffDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(StaffService.get(id))
				.build();
	}
	@GetMapping("/getAll")
	public ResponseDTO<List<StaffDTO>> getAll() {
		return ResponseDTO.<List<StaffDTO>>builder().code(String.valueOf(HttpStatus.OK.value())).data(StaffService.getAll())
				.build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseDTO<Void> delete(@PathVariable(value = "id") String id) throws URISyntaxException {
		if (id == null) {
			throw new BadRequestAlertException("Bad request: missing id", ENTITY_NAME, "missing_id");
		}
		StaffService.delete(id);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}
	
	@PutMapping("/{id}")
	public ResponseDTO<StaffDTO> update(@PathVariable(value = "id") String id, @RequestBody StaffDTO StaffDTO) throws URISyntaxException {
		StaffService.update(StaffDTO);
		return ResponseDTO.<StaffDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(StaffDTO).build();

	}
}

