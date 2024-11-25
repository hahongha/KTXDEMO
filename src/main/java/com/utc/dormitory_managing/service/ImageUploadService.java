//package com.utc.dormitory_managing.service;
//
//import java.io.IOException;
//import java.util.Optional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.utc.dormitory_managing.entity.Test;
//import com.utc.dormitory_managing.repository.ImageRepo;
//import com.utc.dormitory_managing.utils.ImageUtils;
//
//public interface ImageUploadService {
//	String uploadImage(MultipartFile file);
//	byte[] downloadImage(String fileName);
//}
//@Service
//class ImageUploadServiceImpl implements ImageUploadService {
//	
//	@Autowired
//	private ImageRepo imageRepo;
//	
//	@Override
//	public String uploadImage(MultipartFile file) {
//		try {
//       Test imageData = imageRepo.save(Test.builder()
//                .name(file.getOriginalFilename())
//                .type(file.getContentType())
//                .imageData(ImageUtils.compressImage(file.getBytes())).build());
//        if (imageData != null) {
//            return "file uploaded successfully : " + file.getOriginalFilename();
//        }
//		}catch (IOException e) {
//			// TODO: handle exception
//			e.printStackTrace();
//			return null;
//		}
//		return null;
//        
//    }
//	@Override
//    public byte[] downloadImage(String fileName){
//        Optional<Test> dbImageData = imageRepo.findByName(fileName);
//        byte[] images=ImageUtils.decompressImage(dbImageData.get().getImageData());
//        return images;
//    }
//}