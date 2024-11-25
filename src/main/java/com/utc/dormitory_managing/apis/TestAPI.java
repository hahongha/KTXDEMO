//package com.utc.dormitory_managing.apis;
//
//import java.io.IOException;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatusCode;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.utc.dormitory_managing.service.ImageUploadService;
//
//
//@RestController
//@RequestMapping("/test")
//public class TestAPI {
//	@Autowired
//	private ImageUploadService service;
//
//	@PostMapping
//	public ResponseEntity<?> uploadImage(@RequestParam("image")MultipartFile file) throws IOException {
//		String uploadImage = service.uploadImage(file);
//		return ResponseEntity.status(HttpStatusCode.valueOf(200))
//				.body(uploadImage);
//	}
//
//	@GetMapping("/{fileName}")
//	public ResponseEntity<?> downloadImage(@PathVariable String fileName){
//		byte[] imageData=service.downloadImage(fileName);
//		return ResponseEntity.status(HttpStatusCode.valueOf(200))
//				.contentType(MediaType.valueOf("image/png"))
//				.body(imageData);
//	}
//
//}
