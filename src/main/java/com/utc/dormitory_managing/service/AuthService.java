package com.utc.dormitory_managing.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import com.utc.dormitory_managing.apis.error.BadRequestAlertException;
import com.utc.dormitory_managing.configuration.ApplicationProperties.StatusContractRef;
import com.utc.dormitory_managing.dto.ContractDTO;
import com.utc.dormitory_managing.dto.LoginRequest;
import com.utc.dormitory_managing.dto.ResponseDTO;
import com.utc.dormitory_managing.dto.RoomTypeDTO;
import com.utc.dormitory_managing.dto.StudentDTO;
import com.utc.dormitory_managing.entity.RoomType;
import com.utc.dormitory_managing.entity.Student;
import com.utc.dormitory_managing.entity.User;
import com.utc.dormitory_managing.repository.UserRepo;
import com.utc.dormitory_managing.security.securityv2.JwtTokenProvider;

import jakarta.transaction.Transactional;

public interface AuthService {
	ResponseDTO<String> signin(LoginRequest loginRequest, User user);

	ResponseDTO<String> handleRefreshToken(String refreshToken_in);
	
	ResponseDTO<String> signup(StudentDTO studentDTO, RoomTypeDTO roomTypeDTO, ContractDTO contractDTO);

	Boolean checkValidRoom(StudentDTO studentDTO, RoomTypeDTO roomTypeDTO);

}

@Service
class AuthServiceImpl implements AuthService {

	@Autowired
	UserRepo userRepo;

	@Autowired
	UserService userService;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtTokenProvider tokenProvider;
	
	@Autowired
	StudentService studentService;
	
	@Autowired
	MailService mailService;
	
	@Autowired
	RoomService roomService;
	
	@Autowired
	ContractService contractService;
	
	@Autowired
	RoomTypeService roomTypeService;

	@Override
	public ResponseDTO<String> signin(LoginRequest loginRequest, User user) {
		try {
			System.err.println(loginRequest.toString());
//			System.err.println(user.toString());
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
			// Set thông tin authentication vào Security Context
			SecurityContextHolder.getContext().setAuthentication(authentication);
			String accessToken = tokenProvider.generateAccessToken(authentication);
			String refreshToken = tokenProvider.generateRefreshToken(user.getUserId());
			user.setAccessToken(accessToken);
			user.setRefreshToken(refreshToken);

			userRepo.save(user);

			return ResponseDTO.<String>builder().code(String.valueOf(HttpStatus.OK.value())).accessToken(accessToken)
					.refreshToken(refreshToken).build();

		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	@Override
	public ResponseDTO<String> handleRefreshToken(String refreshToken_in) {
		try {

			String user_id = tokenProvider.getUserIdFromJWT(refreshToken_in);
			System.out.println("user_id: " + user_id);

			User user = userRepo.findByUserId(user_id).get();

			Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

			String accessToken = tokenProvider.generateAccessToken(authentication);
			String refreshToken = tokenProvider.generateRefreshToken(user.getUserId());

			user.setAccessToken(accessToken);
			user.setRefreshToken(refreshToken);

			return ResponseDTO.<String>builder().code(String.valueOf(HttpStatus.OK.value())).accessToken(accessToken)
					.refreshToken(refreshToken).build();

		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	@Override
	@Transactional
	public ResponseDTO<String> signup(StudentDTO studentDTO, RoomTypeDTO roomTypeDTO, ContractDTO contractDTO) {
		try {
			if(!checkValidRoom(studentDTO, roomTypeDTO)) throw new BadRequestAlertException("Not Valid Room", "room", "valid");
			studentService.create(studentDTO);
			contractDTO.setStudent(studentDTO);
			contractDTO.setRoomType(roomTypeDTO);
			contractService.create(contractDTO);
			return ResponseDTO.<String>builder().code(String.valueOf(HttpStatus.OK.value())).build();
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	@Override
	public Boolean checkValidRoom(StudentDTO studentDTO, RoomTypeDTO roomTypeDTO) {
		try {
			RoomType roomType = roomTypeService.getEntity(roomTypeDTO.getRoomTypeId());
			//check loai phong trong
			Long contractNumber= contractService.checkContractNumber(roomType,studentDTO.getStudentGender(), StatusContractRef.SUSPEND.toString());
			Long roomNumber = roomService.checkRoomNumber(roomType, studentDTO.getStudentGender());
			if(contractNumber < roomNumber) return true;
			return false;
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}


}

