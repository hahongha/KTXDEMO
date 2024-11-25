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
import com.utc.dormitory_managing.dto.RoomDTO;
import com.utc.dormitory_managing.service.RoomService;

@RestController
@RequestMapping("/room")
public class RoomAPI {
	@Autowired
	private RoomService RoomService;

	private static final String ENTITY_NAME = "Room";

	@PostMapping("")
	public ResponseDTO<RoomDTO> create(@RequestBody RoomDTO RoomDTO) throws URISyntaxException {
		RoomService.create(RoomDTO);
		return ResponseDTO.<RoomDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(RoomDTO).build();

	}

	@GetMapping("/{id}")
	public ResponseDTO<RoomDTO> get(@PathVariable(value = "id") String id) {
		return ResponseDTO.<RoomDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(RoomService.get(id))
				.build();
	}
	@GetMapping("/getAll")
	public ResponseDTO<List<RoomDTO>> getAll() {
		return ResponseDTO.<List<RoomDTO>>builder().code(String.valueOf(HttpStatus.OK.value())).data(RoomService.getAll())
				.build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseDTO<Void> delete(@PathVariable(value = "id") String id) throws URISyntaxException {
		if (id == null) {
			throw new BadRequestAlertException("Bad request: missing id", ENTITY_NAME, "missing_id");
		}
		RoomService.delete(id);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}
	
	@PutMapping("/{id}")
	public ResponseDTO<RoomDTO> update(@PathVariable(value = "id") String id,@RequestBody RoomDTO RoomDTO) throws URISyntaxException {
		RoomDTO.setRoomId(id);
		RoomService.update(RoomDTO);
		return ResponseDTO.<RoomDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(RoomDTO).build();

	}
}
