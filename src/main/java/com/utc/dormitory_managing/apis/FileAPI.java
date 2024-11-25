//package com.utc.dormitory_managing.apis;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.utc.dormitory_managing.service.FileService;
//
//import java.io.File;
//import java.io.IOException;
//
//
////    @PostMapping("/upload")
////    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
////        if (file.isEmpty()) {
////            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please select a file to upload.");
////        }
////
////        try {
////            // Create the directory if it doesn't exist
////            File directory = new File(uploadDirectory);
////            if (!directory.exists()) {
////                directory.mkdirs();
////            }
////
////            // Save the file
////            File serverFile = new File(directory, file.getOriginalFilename());
////            file.transferTo(serverFile);
////
////            return ResponseEntity.ok("File uploaded successfully: " + serverFile.getPath());
////        } catch (IOException e) {
////            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file: " + e.getMessage());
////        }
////    }
//
//@RestController
//@RequestMapping("/api/files")
//public class FileAPI {
//
//	@Autowired 
//	private FileService fileService;
//	
//	// Path to the resources directory (This may vary based on your IDE/project
//	// setup)
//	private final String uploadDir = "src/main/resources/uploads/";
//
////	@PostMapping("/upload")
////	public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
////		if (file.isEmpty()) {
////			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please select a file to upload.");
////		}
////
////		try {
////			// Create the upload directory if it doesn't exist
////			File directory = new File(uploadDir);
////			if (!directory.exists()) {
////				directory.mkdirs();
////			}
////
////			// Save the file
////			File serverFile = new File(directory, file.getOriginalFilename());
////			file.transferTo(serverFile);
////
////			return ResponseEntity.ok("File uploaded successfully: " + serverFile.getAbsolutePath());
////		} catch (IOException e) {
////			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
////					.body("Failed to upload file: " + e.getMessage());
////		}
////	}
//	
//	@PostMapping("/upload")
//    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
//        if (file.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please select a file to upload.");
//        }
//
//        try {
//            String message = fileService.uploadFile(file);
//            return ResponseEntity.ok(message);
//        } catch (Exception  e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Failed to upload file: " + e.getMessage());
//        }
//    }
//}
