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
import com.utc.dormitory_managing.dto.RoomTypeDTO;
import com.utc.dormitory_managing.service.RoomTypeService;

@RestController
@RequestMapping("/roomType")
public class RoomTypeAPI {
	@Autowired
	private RoomTypeService RoomTypeService;

	private static final String ENTITY_NAME = "RoomType";

	@PostMapping("")
	public ResponseDTO<RoomTypeDTO> create(@RequestBody RoomTypeDTO RoomTypeDTO) throws URISyntaxException {
		RoomTypeService.create(RoomTypeDTO);
		return ResponseDTO.<RoomTypeDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(RoomTypeDTO).build();

	}

	@GetMapping("/{id}")
	public ResponseDTO<RoomTypeDTO> get(@PathVariable(value = "id") String id) {
		return ResponseDTO.<RoomTypeDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(RoomTypeService.get(id))
				.build();
	}
	@GetMapping("/getAll")
	public ResponseDTO<List<RoomTypeDTO>> getAll() {
		return ResponseDTO.<List<RoomTypeDTO>>builder().code(String.valueOf(HttpStatus.OK.value())).data(RoomTypeService.getAll())
				.build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseDTO<Void> delete(@PathVariable(value = "id") String id) throws URISyntaxException {
		if (id == null) {
			throw new BadRequestAlertException("Bad request: missing id", ENTITY_NAME, "missing_id");
		}
		RoomTypeService.delete(id);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}
	
	@PutMapping("/{id}")
	public ResponseDTO<RoomTypeDTO> update(@PathVariable(value = "id") String id,@RequestBody RoomTypeDTO RoomTypeDTO) throws URISyntaxException {
		RoomTypeDTO.setRoomTypeId(id);
		RoomTypeService.update(RoomTypeDTO);
		return ResponseDTO.<RoomTypeDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(RoomTypeDTO).build();

	}
}
