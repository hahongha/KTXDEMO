package com.utc.dormitory_managing.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

public interface FileService {
	String uploadFile(MultipartFile file);
	
	public void init();

	  public void save(MultipartFile file);

	  public Resource load(String filename);

	  public void deleteAll();

	  public Stream<Path> loadAll();
}

@Service
class FileServiceImpl implements FileService {

    @Value("${firebase.storage.bucket}")
    private String bucketName; // Your Firebase storage bucket name

    public String uploadFile(MultipartFile file) {
    	try {
        // Get the Storage service
        Storage storage = StorageOptions.getDefaultInstance().getService();
        Bucket bucket = storage.get(bucketName);

        // Upload the file
        String fileName = file.getOriginalFilename();
        bucket.create(fileName, file.getBytes(), file.getContentType());

        return "File uploaded successfully: " + fileName;
    	}catch (Exception e) {
    		e.printStackTrace();
    		return null;
    	}
    }

    private final Path root = Paths.get("uploads");

    @Override
    public void init() {
      try {
        Files.createDirectories(root);
      } catch (IOException e) {
        throw new RuntimeException("Could not initialize folder for upload!");
      }
    }

    @Override
    public void save(MultipartFile file) {
      try {
        Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
      } catch (Exception e) {
        if (e instanceof FileAlreadyExistsException) {
          throw new RuntimeException("A file of that name already exists.");
        }

        throw new RuntimeException(e.getMessage());
      }
    }

    @Override
    public Resource load(String filename) {
      try {
        Path file = root.resolve(filename);
        Resource resource = new UrlResource(file.toUri());

        if (resource.exists() || resource.isReadable()) {
          return resource;
        } else {
          throw new RuntimeException("Could not read the file!");
        }
      } catch (MalformedURLException e) {
        throw new RuntimeException("Error: " + e.getMessage());
      }
    }

    @Override
    public void deleteAll() {
      FileSystemUtils.deleteRecursively(root.toFile());
    }

    @Override
    public Stream<Path> loadAll() {
      try {
        return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
      } catch (IOException e) {
        throw new RuntimeException("Could not load the files!");
      }
    }
    
}
