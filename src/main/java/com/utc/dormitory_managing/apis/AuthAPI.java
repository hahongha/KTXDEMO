package com.utc.dormitory_managing.apis;

import java.nio.file.AccessDeniedException;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import com.utc.dormitory_managing.dto.ContractDTO;
import com.utc.dormitory_managing.dto.FloorDTO;
import com.utc.dormitory_managing.dto.LoginRequest;
import com.utc.dormitory_managing.dto.ResponseDTO;
import com.utc.dormitory_managing.dto.RoomTypeDTO;
import com.utc.dormitory_managing.dto.StudentDTO;
import com.utc.dormitory_managing.dto.UserDTO;
import com.utc.dormitory_managing.entity.User;
import com.utc.dormitory_managing.repository.UserRepo;
import com.utc.dormitory_managing.service.AuthService;
import com.utc.dormitory_managing.service.StudentService;
import com.utc.dormitory_managing.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthAPI {
	@Autowired
	UserRepo userRepo;

	@Autowired
	UserService userService;

	@Autowired
	AuthService authService;
	@Autowired
	StudentService studentService;

	@PostMapping("/signin")
	public ResponseDTO<String> signin(@Valid @RequestBody LoginRequest loginRequest) {
		try {
			Optional<User> userOptional = userRepo.findByUsername(loginRequest.getUsername());
			if (userOptional.isEmpty()) throw new AccessDeniedException("Password wrong !!!");
			User user = userOptional.get();			
			Boolean compare_password = BCrypt.checkpw(loginRequest.getPassword(), user.getPassword());
			
			if (!compare_password)
				throw new AccessDeniedException("Password wrong !!!");

			return authService.signin(loginRequest, user);

		} catch (Exception e) {
			throw Problem.builder().withStatus(Status.INTERNAL_SERVER_ERROR).withDetail("SERVER ERROR").build();
		}
	}

	@PostMapping("/signup")
	public ResponseDTO<String> signup(@Valid @RequestBody ContractDTO contractDTO) {
		try {
			return authService.signup(contractDTO);
		} catch (Exception e) {
			throw Problem.builder().withStatus(Status.INTERNAL_SERVER_ERROR).withDetail("SERVER ERROR").build();
		}
	}
	
	@PostMapping("/refreshToken")
	public ResponseDTO<String> handleRefreshToken(
			@RequestParam(value = "refreshtoken", required = true) String refreshtoken) {
		try {
			return authService.handleRefreshToken(refreshtoken);

		} catch (Exception e) {
			throw Problem.builder().withStatus(Status.INTERNAL_SERVER_ERROR).withDetail("SERVER ERROR").build();
		}
	}
	
	@PostMapping("/checkEmail/{email}")
	public ResponseDTO<Boolean> checkEmail(@PathVariable(value = "email") String email){
		return ResponseDTO.<Boolean>builder().code(String.valueOf(HttpStatus.OK.value())).data(studentService.existsByEmail(email))
				.build();
	}
	
	@PostMapping("/checkStudentId/{studentId}")
	public ResponseDTO<Boolean> checkStudentId(@PathVariable(value = "studentId") String studentId){
		return ResponseDTO.<Boolean>builder().code(String.valueOf(HttpStatus.OK.value())).data(studentService.existsById(studentId))
				.build();
	}
	
	@PostMapping("/checkStudentIdentification/{id}")
	public ResponseDTO<Boolean> checkStudentIdentification(@PathVariable(value = "id") String id){
		return ResponseDTO.<Boolean>builder().code(String.valueOf(HttpStatus.OK.value())).data(studentService.existsBystudentIdentification(id))
				.build();
	}
	
	
	
	
}
