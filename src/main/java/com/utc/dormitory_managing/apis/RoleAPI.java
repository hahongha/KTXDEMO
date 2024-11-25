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
import com.utc.dormitory_managing.dto.RoleDTO;
import com.utc.dormitory_managing.service.RoleService;

@RestController
@RequestMapping("/Role")
public class RoleAPI {
	@Autowired
	private RoleService RoleService;

	private static final String ENTITY_NAME = "Role";

	@PostMapping("")
	public ResponseDTO<RoleDTO> create(@RequestBody RoleDTO RoleDTO) throws URISyntaxException {
		if(RoleDTO.getRoleName().toUpperCase().equals("ROOT")) throw new BadRequestAlertException("NOT CREATE THIS ROLE", "ROLE", "UNIQUE");
		RoleService.create(RoleDTO);
		return ResponseDTO.<RoleDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(RoleDTO).build();

	}

	@GetMapping("/{id}")
	public ResponseDTO<RoleDTO> get(@PathVariable(value = "id") String id) {
		return ResponseDTO.<RoleDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(RoleService.get(id))
				.build();
	}
	@GetMapping("/getAll")
	public ResponseDTO<List<RoleDTO>> getAll() {
		return ResponseDTO.<List<RoleDTO>>builder().code(String.valueOf(HttpStatus.OK.value())).data(RoleService.getAll())
				.build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseDTO<Void> delete(@PathVariable(value = "id") String id) throws URISyntaxException {
		if (id == null) {
			throw new BadRequestAlertException("Bad request: missing id", ENTITY_NAME, "missing_id");
		}
		RoleService.delete(id);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}
	
	@PutMapping("/{id}")
	public ResponseDTO<RoleDTO> update(@PathVariable(value = "id") String id, @RequestBody RoleDTO RoleDTO) throws URISyntaxException {
		RoleDTO.setRoleId(id);
		RoleService.update(RoleDTO);
		return ResponseDTO.<RoleDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(RoleDTO).build();

	}
}


