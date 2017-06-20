package com.mkyong.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class UploadServiceImpl implements UploadService {

	@Value("${file.storage.location}")
	private String uploadLocation;

	@Override
	public String saveUploadedFiles(String baseDir, List<MultipartFile> files) throws IOException {
		// TODO Auto-generated method stub
		List<String> paths = new ArrayList<>();
		for (MultipartFile file : files) {

            if (file.isEmpty()) {
                continue;
            }

            byte[] bytes = file.getBytes();
            Path dirPath = Paths.get(uploadLocation, baseDir);
            Files.createDirectories(dirPath);
            Path filePath = Paths.get(dirPath.toString(), file.getOriginalFilename());
            Path result = Files.write(filePath, bytes);
            paths.add(result.toString());
        }
		String filePaths = String.join(",", paths);
		return filePaths;
	}

}
