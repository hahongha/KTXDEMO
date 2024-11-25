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
import com.utc.dormitory_managing.dto.StudentDTO;
import com.utc.dormitory_managing.service.StudentService;

@RestController
@RequestMapping("/student")
public class StudentAPI {
	@Autowired
	private StudentService studentService;

	private static final String ENTITY_NAME = "Student";

	@PostMapping("")
	public ResponseDTO<StudentDTO> create(@RequestBody StudentDTO studentDTO) throws URISyntaxException {
		studentService.create(studentDTO);
		return ResponseDTO.<StudentDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(studentDTO).build();

	}

	@GetMapping("/{id}")
	public ResponseDTO<StudentDTO> get(@PathVariable(value = "id") String id) {
		return ResponseDTO.<StudentDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(studentService.get(id))
				.build();
	}
	@GetMapping("/getAll")
	public ResponseDTO<List<StudentDTO>> getAll() {
		return ResponseDTO.<List<StudentDTO>>builder().code(String.valueOf(HttpStatus.OK.value())).data(studentService.getAll())
				.build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseDTO<Void> delete(@PathVariable(value = "id") String id) throws URISyntaxException {
		if (id == null) {
			throw new BadRequestAlertException("Bad request: missing id", ENTITY_NAME, "missing_id");
		}
		studentService.delete(id);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}
	
	@PutMapping("/{id}")
	public ResponseDTO<StudentDTO> update(@PathVariable(value = "id") String id,@RequestBody StudentDTO studentDTO) throws URISyntaxException {
		studentDTO.setStudentId(id);
		studentService.update(studentDTO);
		return ResponseDTO.<StudentDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(studentDTO).build();

	}
}
