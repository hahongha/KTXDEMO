package com.utc.dormitory_managing.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import com.utc.dormitory_managing.apis.error.BadRequestAlertException;
import com.utc.dormitory_managing.configuration.ApplicationProperties;
import com.utc.dormitory_managing.dto.ResponseDTO;
import com.utc.dormitory_managing.dto.RoomDTO;
import com.utc.dormitory_managing.dto.SearchDTO;
import com.utc.dormitory_managing.dto.Student2DTO;
import com.utc.dormitory_managing.dto.StudentDTO;
import com.utc.dormitory_managing.dto.UserDTO;
import com.utc.dormitory_managing.dto.UserResponse;
import com.utc.dormitory_managing.entity.Role;
import com.utc.dormitory_managing.entity.Room;
import com.utc.dormitory_managing.entity.Student;
import com.utc.dormitory_managing.entity.User;
import com.utc.dormitory_managing.repository.RoleRepo;
import com.utc.dormitory_managing.repository.RoomRepo;
import com.utc.dormitory_managing.repository.StudentRepo;
import com.utc.dormitory_managing.repository.UserRepo;
import com.utc.dormitory_managing.utils.Utils;

import jakarta.persistence.NoResultException;


public interface StudentService {
	StudentDTO create(StudentDTO studentDTO);
	StudentDTO update(StudentDTO studentDTO);
	Boolean delete(String id);
	StudentDTO get(String id);
	List<StudentDTO> getAll();
	List<Student2DTO> getAll2();
	StudentDTO updateStatus(StudentDTO studentDTO);
	ResponseDTO<List<Student>> search(SearchDTO searchDTO);
	List<StudentDTO> findbyRoom(RoomDTO room);
}
@Service
class StudentServiceImpl implements StudentService {
	
	@Autowired
	private StudentRepo studentRepo;
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private ApplicationProperties props;
	
	@Autowired
	private RoomRepo roomRepo;
	
	@Autowired
	private MailService mailService;
	
	@Autowired
	private RoleRepo roleRepo;
	
	@Override
	@Transactional
	public StudentDTO create(StudentDTO studentDTO) {
		try {
			ModelMapper mapper = new ModelMapper();
			Optional<Student> studentOptional = studentRepo.findById(studentDTO.getStudentId());
			if(studentOptional.isPresent()) throw new BadRequestAlertException("Student exists", "Student", "Exist");
			Student student = mapper.map(studentDTO, Student.class);
			UserDTO userDTO = new UserDTO();
			userDTO.setUserId(UUID.randomUUID().toString().replaceAll("-", ""));
			userDTO.setUsername(studentDTO.getStudentId());
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			String encodedPassword = encoder.encode(student.getStudentId());
			userDTO.setPassword(encodedPassword);
			userDTO.setExpired(Long.parseLong(props.getExpiredTime())*12);
			Role role = roleRepo.findByRoleName("USER").orElseThrow(NoResultException::new);
			User user = mapper.map(userDTO, User.class);
			user.setRole(role);
			userRepo.save(user);
			student.setUser(user);
			student.setRoom(null);
			studentRepo.save(student);
			return studentDTO;
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	@Override
	@Transactional
	public StudentDTO update(StudentDTO studentDTO) {
		try {
			ModelMapper mapper = new ModelMapper();
			Optional<Student> studentOptional = studentRepo.findById(studentDTO.getStudentId());
			if(studentOptional.isEmpty()) throw new BadRequestAlertException("Not Found Student", "student", "missing");
			Student student = mapper.map(studentDTO, Student.class);
			User user = studentOptional.get().getUser();
			student.setUser(user);
			if(studentDTO.getRoomName()== null) {
				student.setRoom(null);
			}else {
				Optional<Room> roomOP = roomRepo.findByRoomName(studentDTO.getRoomName());
				if(roomOP.isPresent()) {
					student.setRoom(roomOP.get());
					Long count = studentRepo.getStudentNumber(roomOP.get().getRoomId());
					roomOP.get().setRoomNumber(Integer.valueOf(count.toString()));
					roomRepo.save(roomOP.get());
				}
				else student.setRoom(null);
			}
			UserResponse userResponse = mapper.map(user, UserResponse.class);
			studentDTO.setUserResponse(userResponse);
			studentRepo.save(student);
			return studentDTO;
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	@Override
	@Transactional
	public Boolean delete(String id) {
		try {
			Optional<Student> studentOptional = studentRepo.findById(id);
			if(studentOptional.isEmpty()) return false;
			studentRepo.deleteById(id);
			return true;
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	@Override
	@Transactional
	public StudentDTO get(String id) {
		try {
			StudentDTO student = new StudentDTO();
			ModelMapper mapper = new ModelMapper();
			Optional<Student> studentOptional = studentRepo.findById(id);
			if(studentOptional.isEmpty()) throw new BadRequestAlertException("Not Found Student", "student", "missing");
			student = mapper.map(studentOptional.get(), StudentDTO.class);
			if(studentOptional.get().getRoom()!= null) {
				student.setRoomName(studentOptional.get().getRoom().getRoomName());
			}
			UserResponse userResponse = mapper.map(studentOptional.get().getUser(), UserResponse.class);
			student.setUserResponse(userResponse);
			return student;
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	
	@Override
	@Transactional
	public List<StudentDTO> getAll() {
		
		try {
			ModelMapper mapper = new ModelMapper();
			List<Student> students = studentRepo.findAll();
			List<StudentDTO> studentDTOs = new ArrayList<StudentDTO>();
			for (Student student : students) {
				StudentDTO studentDTO = get(student.getStudentId());
				studentDTOs.add(studentDTO);
			}
			return studentDTOs;
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	@Override
	@Transactional
	public ResponseDTO<List<Student>> search(SearchDTO searchDTO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public StudentDTO updateStatus(StudentDTO studentDTO) {
		return null;
	}

	@Override
	@Transactional
	public List<Student2DTO> getAll2() {
		try {
			List<Student2DTO> s = new ArrayList<Student2DTO>();
			ModelMapper mapper = new ModelMapper();
			
			List<Student> students = studentRepo.findAll();
			System.err.println(students.size());
			if(students.size()==0) return s;
			for (Student student : students) {
				StudentDTO studentDTO = new StudentDTO();
				UserDTO userDTO = new UserDTO();
				RoomDTO roomDTO = new RoomDTO();
				studentDTO = mapper.map(student, StudentDTO.class);
				
				if(student.getUser()!= null)
					userDTO = mapper.map(student.getUser(), UserDTO.class);
				if(student.getRoom()!= null)
					roomDTO = mapper.map(student.getRoom(), RoomDTO.class);
				Student2DTO s2 = new Student2DTO(studentDTO, roomDTO, userDTO);
				s.add(s2);
			}
			return s;
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	@Override
	public List<StudentDTO> findbyRoom(RoomDTO room) {
		try {
			List<Student> students = studentRepo.findByRoom(room.getRoomId());
			if (students.size()==0) {
				return new ArrayList<StudentDTO>();
			}
			return students.stream().map(s -> new ModelMapper().map(s, StudentDTO.class)).collect(Collectors.toList());
	} catch (ResourceAccessException e) {
		throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
	} catch (HttpServerErrorException | HttpClientErrorException e) {
		throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
	}
	}

}