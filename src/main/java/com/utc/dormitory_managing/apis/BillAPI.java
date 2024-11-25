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
import com.utc.dormitory_managing.dto.BillDTO;
import com.utc.dormitory_managing.dto.BillFormDTO;
import com.utc.dormitory_managing.service.BillService;

@RestController
@RequestMapping("/bill")
public class BillAPI {
	@Autowired
	private BillService BillService;

	private static final String ENTITY_NAME = "Bill";

	@PostMapping("")
	public ResponseDTO<BillDTO> create(@RequestBody BillDTO BillDTO) throws URISyntaxException {
		BillService.create(BillDTO);
		return ResponseDTO.<BillDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(BillDTO).build();

	}
	
	@PostMapping("/testCurrentMonth")
	public ResponseDTO<BillDTO> createMonth(@RequestBody BillFormDTO billFormDTO) throws URISyntaxException {
		
		return ResponseDTO.<BillDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(BillService.createMonth(billFormDTO)).build();

	}

	@GetMapping("/{id}")
	public ResponseDTO<BillDTO> get(@PathVariable(value = "id") String id) {
		return ResponseDTO.<BillDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(BillService.get(id))
				.build();
	}
	@GetMapping("/getAll")
	public ResponseDTO<List<BillDTO>> getAll() {
		return ResponseDTO.<List<BillDTO>>builder().code(String.valueOf(HttpStatus.OK.value())).data(BillService.getAll())
				.build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseDTO<Void> delete(@PathVariable(value = "id") String id) throws URISyntaxException {
		if (id == null) {
			throw new BadRequestAlertException("Bad request: missing id", ENTITY_NAME, "missing_id");
		}
		BillService.delete(id);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}
	
	@PutMapping("/{id}")
	public ResponseDTO<BillDTO> update(@PathVariable(value = "id") String id,@RequestBody BillDTO BillDTO) throws URISyntaxException {
		BillDTO.setBillId(id);
		BillService.update(BillDTO);
		return ResponseDTO.<BillDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(BillDTO).build();

	}
}
