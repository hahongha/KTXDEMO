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
import com.utc.dormitory_managing.dto.Building2DTO;
import com.utc.dormitory_managing.dto.BuildingDTO;
import com.utc.dormitory_managing.service.BuildingService;

@RestController
@RequestMapping("/building")
public class BuildingAPI {
	@Autowired
	private BuildingService BuildingService;

	private static final String ENTITY_NAME = "Building";

	@PostMapping("")
	public ResponseDTO<BuildingDTO> create(@RequestBody BuildingDTO BuildingDTO) throws URISyntaxException {
		BuildingService.create(BuildingDTO);
		return ResponseDTO.<BuildingDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(BuildingDTO).build();

	}
	
	@PostMapping("/test")
	public ResponseDTO<Building2DTO> create2(@RequestBody Building2DTO BuildingDTO) throws URISyntaxException {
		BuildingService.create2(BuildingDTO);
		return ResponseDTO.<Building2DTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(BuildingDTO).build();

	}

	@GetMapping("/{id}")
	public ResponseDTO<BuildingDTO> get(@PathVariable(value = "id") String id) {
		return ResponseDTO.<BuildingDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(BuildingService.get(id))
				.build();
	}
	@GetMapping("/getAll")
	public ResponseDTO<List<BuildingDTO>> getAll() {
		return ResponseDTO.<List<BuildingDTO>>builder().code(String.valueOf(HttpStatus.OK.value())).data(BuildingService.getAll())
				.build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseDTO<Void> delete(@PathVariable(value = "id") String id) throws URISyntaxException {
		if (id == null) {
			throw new BadRequestAlertException("Bad request: missing id", ENTITY_NAME, "missing_id");
		}
		BuildingService.delete(id);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}
	
	@PutMapping("/{id}")
	public ResponseDTO<BuildingDTO> update(@PathVariable(value = "id") String id, @RequestBody BuildingDTO BuildingDTO) throws URISyntaxException {
		BuildingDTO.setBuildingId(id);
		BuildingService.update(BuildingDTO);
		return ResponseDTO.<BuildingDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(BuildingDTO).build();

	}
	
//	@PutMapping("/{id}")
//	public ResponseDTO<BuildingDTO> update(@RequestBody BuildingDTO BuildingDTO) throws URISyntaxException {
//		BuildingService.update(BuildingDTO);
//		return ResponseDTO.<BuildingDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(BuildingDTO).build();
//
//	}
}

