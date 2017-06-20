package com.mkyong.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class UploadServiceImpl implements UploadService {
	
	private static final Logger logger = LoggerFactory.getLogger(UploadServiceImpl.class);

	@Value("${file.storage.location}")
	private String uploadLocation;
	
	@Value("${working.location}")
	private String workingDirectory;
	
	@Value("${file.merge.marker}")
	private String fileMergeMarker;
	
	@Override
	public String saveUploadedFileChunks(MultipartFile fileChunk, String fileName) throws IOException {
		// TODO Auto-generated method stub
		byte[] bytes = fileChunk.getBytes();
		Path dirPath = Paths.get(uploadLocation, workingDirectory, fileName);
		Path result = Files.createDirectories(dirPath);
		Path filePath = Paths.get(result.toString(), fileChunk.getOriginalFilename());
		Path written = Files.write(filePath, bytes);
		logger.info(written.toString());
		return written.toString();
	}

	@Override
	public String mergeUploadedFile(String baseDir, String fileName) throws IOException {
		// TODO Auto-generated method stub
		Path target = Paths.get(uploadLocation, baseDir);
		Path source = Paths.get(uploadLocation, workingDirectory, fileName);
		Path newDirPath = Paths.get(source.toString() + fileMergeMarker);
		DirectoryStream<Path> fileChunks = Files.newDirectoryStream(source);
		OutputStream os = new FileOutputStream(target.toString());
		int off = 0;
		for(Path filePath : fileChunks) {
			byte[] b = Files.readAllBytes(filePath);
			os.write(b, off, b.length);
			off = b.length;
		}
		os.flush();
		os.close();
		Path renamedPath = Files.move(source, source.resolveSibling(newDirPath));
		logger.info(renamedPath.toString());
		return target.toString();
	}

}
