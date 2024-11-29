package com.utc.dormitory_managing.apis;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.utc.dormitory_managing.apis.error.BadRequestAlertException;
import com.utc.dormitory_managing.entity.Role;
import com.utc.dormitory_managing.entity.Services;
import com.utc.dormitory_managing.entity.User;
import com.utc.dormitory_managing.repository.RoleRepo;
import com.utc.dormitory_managing.repository.ServiceRepo;
import com.utc.dormitory_managing.repository.UserRepo;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepo userRepository;
    
    @Autowired
    private RoleRepo roleRepo;
    
    @Autowired
    private ServiceRepo serviceRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;  // Cần để mã hóa mật khẩu

    @Override
    public void run(String... args) throws Exception {
        // Kiểm tra xem tài khoản root đã tồn tại chưa
    	if(roleRepo.findAll().size()==0) {
    		Role role = new Role();
    		role.setRoleId(UUID.randomUUID().toString());
    		role.setRoleName("ROOT");
    		roleRepo.save(role);
    		
    		role.setRoleId(UUID.randomUUID().toString());
    		role.setRoleName("USER");
    		roleRepo.save(role);
    	}
    	
    	if(roleRepo.findByRoleName("ROOT").isEmpty()) {
    		Role role = new Role();
    		role.setRoleId(UUID.randomUUID().toString());
    		role.setRoleName("ROOT");
    		roleRepo.save(role);
    	}
    	
        if (!userRepository.findByUsername("root").isPresent()) {
            // Nếu chưa, tạo tài khoản root với mật khẩu mã hóa
            User rootUser = new User();
            rootUser.setUserId(UUID.randomUUID().toString());
            rootUser.setUsername("root");
            rootUser.setPassword(passwordEncoder.encode("rootpassword")); // Đặt mật khẩu an toàn hơn
            Optional<Role> role= roleRepo.findByRoleName("ROOT");
            if(role.isEmpty()) throw new BadRequestAlertException("Not Found Root", "role", "missing");
            rootUser.setRole(role.get());  // Set role cho tài khoản root
            // Lưu tài khoản vào database
            userRepository.save(rootUser);
            System.out.println("Tài khoản root đã được tạo.");
        } else {
            System.out.println("Tài khoản root đã tồn tại.");
        }
        
        if (!userRepository.findByUsername("string0312").isPresent()) {
            // Nếu chưa, tạo tài khoản root với mật khẩu mã hóa
            User rootUser = new User();
            rootUser.setUserId(UUID.randomUUID().toString());
            rootUser.setUsername("string0312");
            rootUser.setPassword(passwordEncoder.encode("string")); // Đặt mật khẩu an toàn hơn
            Optional<Role> role= roleRepo.findByRoleName("ROOT");
            if(role.isEmpty()) throw new BadRequestAlertException("Not Found Root", "role", "missing");
            rootUser.setRole(role.get());  // Set role cho tài khoản root
            // Lưu tài khoản vào database
            userRepository.save(rootUser);
            System.out.println("Tài khoản root đã được tạo.");
        } else {
            System.out.println("Tài khoản root đã tồn tại.");
        }
        
        if(serviceRepo.findAll().size()==0) {
        	Services service = new Services();
        	service.setServiceId(UUID.randomUUID().toString());
        	service.setServiceName("WATER");
        	service.setServicePrice((long) 4000);
        	serviceRepo.save(service);
        	
        	service.setServiceId(UUID.randomUUID().toString());
        	service.setServiceName("ELECTRONIC");
        	service.setServicePrice((long) 4000);
        	serviceRepo.save(service);
        }
        
    }
}

