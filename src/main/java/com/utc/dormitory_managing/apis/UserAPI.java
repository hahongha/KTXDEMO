package com.utc.dormitory_managing.apis;

import java.net.URISyntaxException;

import java.io.IOException;
import java.util.List;

import com.utc.dormitory_managing.dto.*;
import com.utc.dormitory_managing.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.utc.dormitory_managing.apis.error.BadRequestAlertException;
import com.utc.dormitory_managing.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserAPI {

	@Autowired
	private MailService mailService;
	@Autowired
	private UserService userService;

	private static final String ENTITY_NAME = "User";

	@PostMapping("")
	public ResponseDTO<UserDTO> create(@RequestBody UserDTO userDTO) throws URISyntaxException {
		System.out.println("=========================username: " + userDTO.getUsername());
		if (userDTO.getUsername() == null || userDTO.getPassword() == null) {
			throw new BadRequestAlertException("Bad request: missing data", ENTITY_NAME, "missing");
		}
		userService.create(userDTO);
		return ResponseDTO.<UserDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(userDTO).build();

	}

	@GetMapping("/{id}")
	public ResponseDTO<UserDTO> get(@PathVariable(value = "id") String id) {
		return ResponseDTO.<UserDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(userService.get(id))
				.build();
	}
	@GetMapping("/getAll")
	public ResponseDTO<List<UserDTO>> getAll() {
		return ResponseDTO.<List<UserDTO>>builder().code(String.valueOf(HttpStatus.OK.value())).data(userService.getAll())
				.build();
	}

	@PutMapping("/update-password")
	public ResponseDTO<Void> updatePassword(@ModelAttribute @Valid UserDTO userDTO) throws IOException {
		userService.updatePassword(userDTO);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}
	
	@PutMapping("/update/{id}")
	public ResponseDTO<UserDTO> update(@PathVariable(value = "id") String id,@RequestBody @Valid UserDTO userDTO) throws IOException {
		userDTO.setUserId(id);
		userService.update(userDTO);
		return ResponseDTO.<UserDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(userService.update(userDTO))
				.build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseDTO<Void> delete(@PathVariable(value = "id") String id) throws URISyntaxException {
		if (id == null) {
			throw new BadRequestAlertException("Bad request: missing id", ENTITY_NAME, "missing_id");
		}
		userService.delete(id);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}
	
	@DeleteMapping("/bylistid")
	public ResponseDTO<Void> deletebyList(@RequestBody List<String> ids) throws URISyntaxException {
		if (ids == null) {
			throw new BadRequestAlertException("Bad request: missing id", ENTITY_NAME, "missing_id");
		}
		userService.deleteAll(ids);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}
	
}
