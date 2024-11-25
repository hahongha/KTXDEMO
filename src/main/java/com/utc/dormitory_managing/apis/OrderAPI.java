//package com.utc.dormitory_managing.apis;
//
//import java.net.URISyntaxException;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.utc.dormitory_managing.apis.error.BadRequestAlertException;
//import com.utc.dormitory_managing.dto.ResponseDTO;
//import com.utc.dormitory_managing.dto.OrderDTO;
//import com.utc.dormitory_managing.service.OrderService;
//
//@RestController
//@RequestMapping("/order")
//public class OrderAPI {
//	@Autowired
//	private OrderService OrderService;
//
//	private static final String ENTITY_NAME = "Order";
//
//	@PostMapping("")
//	public ResponseDTO<OrderDTO> create(@RequestBody OrderDTO OrderDTO) throws URISyntaxException {
//		OrderService.create(OrderDTO);
//		return ResponseDTO.<OrderDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(OrderDTO).build();
//
//	}
//
//	@GetMapping("/{id}")
//	public ResponseDTO<OrderDTO> get(@PathVariable(value = "id") String id) {
//		return ResponseDTO.<OrderDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(OrderService.get(id))
//				.build();
//	}
//	@GetMapping("/getAll")
//	public ResponseDTO<List<OrderDTO>> getAll() {
//		return ResponseDTO.<List<OrderDTO>>builder().code(String.valueOf(HttpStatus.OK.value())).data(OrderService.getAll())
//				.build();
//	}
//	
//	@DeleteMapping("/{id}")
//	public ResponseDTO<Void> delete(@PathVariable(value = "id") String id) throws URISyntaxException {
//		if (id == null) {
//			throw new BadRequestAlertException("Bad request: missing id", ENTITY_NAME, "missing_id");
//		}
//		OrderService.delete(id);
//		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
//	}
//	
//	@PutMapping("")
//	public ResponseDTO<OrderDTO> update(@RequestBody OrderDTO OrderDTO) throws URISyntaxException {
//		OrderService.update(OrderDTO);
//		return ResponseDTO.<OrderDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(OrderDTO).build();
//
//	}
//}
