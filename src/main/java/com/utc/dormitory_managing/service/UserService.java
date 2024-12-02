package com.utc.dormitory_managing.service;

import java.util.*;
import java.util.stream.Collectors;

import com.utc.dormitory_managing.dto.NewPasswordDTO;
import com.utc.dormitory_managing.entity.Staff;
import com.utc.dormitory_managing.entity.Student;
import com.utc.dormitory_managing.repository.StaffRepo;
import com.utc.dormitory_managing.repository.StudentRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import com.utc.dormitory_managing.apis.error.BadRequestAlertException;
import com.utc.dormitory_managing.configuration.ApplicationProperties;
import com.utc.dormitory_managing.dto.ResponseDTO;
import com.utc.dormitory_managing.dto.SearchDTO;
import com.utc.dormitory_managing.dto.UserDTO;
import com.utc.dormitory_managing.entity.Role;
import com.utc.dormitory_managing.entity.User;
import com.utc.dormitory_managing.repository.RoleRepo;
import com.utc.dormitory_managing.repository.UserRepo;

import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;

public interface UserService {
	UserDTO create(UserDTO userDTO);

	Boolean delete(String id);

	Boolean deleteAll(List<String> ids);
	
	List<UserDTO> getAll();

	UserDTO get(String id);

	UserDTO updatePassword(UserDTO userDTO);
	
	UserDTO update(UserDTO userDTO);
	
	ResponseDTO<List<UserDTO>> search(SearchDTO searchDTO);
	void processForgotPassword(String email);
	boolean validateOtp(String email, String otp);
	void resetPassword(NewPasswordDTO request);
	
}

@Service
class UserServiceImpl implements UserService {

	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private ApplicationProperties props;
	
	@Autowired
	private RoleRepo roleRepo;
	@Autowired
	private StudentRepo studentRepo;

	@Autowired
	private StaffRepo staffRepo;

	@Autowired
	private MailService mailService;

	private Map<String, String> otpStorage = new HashMap<>(); // In production use Redis/Cache
	@Override
	@Transactional
	public UserDTO create(UserDTO userDTO) {
		try {
			ModelMapper mapper = new ModelMapper();
			// creatte user
			
			if(userDTO.getExpired()<= 0 || userDTO.getExpired()==null) {
				userDTO.setExpired(Long.parseLong(props.getExpiredTime())*12);
			}
			else {
				userDTO.setExpired(Long.parseLong(props.getExpiredTime())* userDTO.getExpired());
			}
			User user = mapper.map(userDTO, User.class);
			
			if(userRepo.findByUsername(userDTO.getUsername()).isPresent()) throw new BadRequestAlertException("user is available", "user", "missing");
			
			
			String user_id = UUID.randomUUID().toString().replaceAll("-", "");
			user.setUserId(user_id);
			user.setPassword(new BCryptPasswordEncoder().encode(userDTO.getPassword()));
			
			if(userDTO.getRole() == null) {
				Role role = roleRepo.findByRoleName("USER").orElseThrow(NoResultException::new);
				user.setRole(role);
			}else {
				System.err.println("Not Null");
				Role role = roleRepo.findById(userDTO.getRole().getRoleId()).orElseThrow(NoResultException::new);
				user.setRole(role);
			}
			// commit save
			userRepo.save(user);
			return mapper.map(user, UserDTO.class);

		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	@Override
	@Transactional
	public Boolean deleteAll(List<String> ids) {
		if(ids.isEmpty()) return false;
		userRepo.deleteAllById(ids);
		return true;
	}

	@Override
	@Transactional
	public UserDTO get(String id) {
		try {
			User user = userRepo.findByUserId(id).orElseThrow(NoResultException::new);
			UserDTO userDTO = new ModelMapper().map(user, UserDTO.class);
			return userDTO;

		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}

	}

	@Override
	@Transactional
	public UserDTO updatePassword(UserDTO userDTO) {
		try {
			User user = userRepo.findByUserId(userDTO.getUserId()).orElseThrow(NoResultException::new);
			user.setPassword(new BCryptPasswordEncoder().encode(userDTO.getPassword()));
			userRepo.save(user);
			UserDTO userResponse = new ModelMapper().map(user, UserDTO.class);
			return userResponse;

		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}

	}

	@Override
	public List<UserDTO> getAll() {
		ModelMapper mapper = new ModelMapper();
		List<User> users  = userRepo.findAll();
		return users
				  .stream()
				  .map(user -> mapper.map(user, UserDTO.class))
				  .collect(Collectors.toList());
	}

	@Override
	public UserDTO update(UserDTO userDTO) {
		try {
			User user = userRepo.findByUserId(userDTO.getUserId()).orElseThrow(NoResultException::new);
			ModelMapper model = new ModelMapper();
			user = model.map(userDTO, User.class);
			user.setPassword(new BCryptPasswordEncoder().encode(userDTO.getPassword()));
			userRepo.save(user);
			return userDTO;

		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	@Override
	public Boolean delete(String id) {
		try {
		Optional<User> userOptional = userRepo.findById(id);
		if(userOptional.isEmpty()) return false;
		userRepo.deleteById(id);
		return true;
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	@Override
	public ResponseDTO<List<UserDTO>> search(SearchDTO searchDTO) {
		try {
			return null;
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	//forgot
	public void processForgotPassword(String email) {
		// Check in student table
		Optional<Student> student = studentRepo.findByStudentEmail(email);
		if (student.isPresent()) {
			sendOtp(email, student.get().getUser());
			return;
		}

		// Check in staff table
		Optional<Staff> staff = staffRepo.findByStaffEmail(email);
		if (staff.isPresent()) {
			sendOtp(email, staff.get().getUser());
			return;
		}

		throw new BadRequestAlertException("Email not found", "User", "email_not_found");
	}

	private void sendOtp(String email, User user) {
		String otp = generateOtp();
		otpStorage.put(email, otp);

		// Use new mail method
		mailService.sendOtpEmail(
				email,
				"Password Reset OTP",
				"Your OTP for password reset is: " + otp
		);
	}

	private String generateOtp() {
		return String.format("%06d", new Random().nextInt(999999));
	}

	public boolean validateOtp(String email, String otp) {
		String storedOtp = otpStorage.get(email);
		return storedOtp != null && storedOtp.equals(otp);
	}

	@Transactional
	public void resetPassword(NewPasswordDTO request) {
		if (!validateOtp(request.getEmail(), request.getOtp())) {
			throw new BadRequestAlertException("Invalid OTP", "User", "invalid_otp");
		}

		// Find user by email through student or staff
		User user = findUserByEmail(request.getEmail());
		if (user == null) {
			throw new BadRequestAlertException("User not found", "User", "user_not_found");
		}

		// Update password
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		user.setPassword(encoder.encode(request.getNewPassword()));
		userRepo.save(user);

		// Clear OTP
		otpStorage.remove(request.getEmail());
	}

	private User findUserByEmail(String email) {
		Optional<Student> student = studentRepo.findByStudentEmail(email);
		if (student.isPresent()) {
			return student.get().getUser();
		}

		Optional<Staff> staff = staffRepo.findByStaffEmail(email);
		if (staff.isPresent()) {
			return staff.get().getUser();
		}

		return null;
	}
}
