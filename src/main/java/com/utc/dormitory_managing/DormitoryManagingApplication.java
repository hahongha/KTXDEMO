package com.utc.dormitory_managing;

import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.utc.dormitory_managing.service.FileService;

import jakarta.annotation.Resource;

@SpringBootApplication
@EnableJpaRepositories
public class DormitoryManagingApplication {

//	 @Resource
//	  FileService storageService;
	
	public static void main(String[] args) {
		SpringApplication.run(DormitoryManagingApplication.class, args);
	}
	
//	@Bean
//    public FirebaseApp firebaseApp() throws IOException {
//        FileInputStream serviceAccount = new FileInputStream("src/main/resources/firebase.json");
//
//        // Use the new method for building FirebaseOptions
//        FirebaseOptions options = FirebaseOptions.builder()
//                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
////                .setDatabaseUrl("https://<your-database-name>.firebaseio.com/") // Optional, if using Realtime Database
//                .build();
//
//        return FirebaseApp.initializeApp(options);
//    }
//
//	 public void run(String... arg) throws Exception {
////	    storageService.deleteAll();
//	    storageService.init();
//	  }
}
