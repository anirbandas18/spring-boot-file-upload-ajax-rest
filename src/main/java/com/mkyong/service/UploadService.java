package com.mkyong.service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mkyong.upload.model.FileModel;

@Service
public interface UploadService {
	
	public String uploadStatusOfFile(String fileName) throws IOException;
	
	public List<FileModel> getCompletelyUploadedFiles(String baseDir) throws IOException;
	
	public Boolean saveUploadedFileChunk(MultipartFile fileChunk, String fileName, Integer totalChunks) throws IOException ;
	
	public String mergeUploadedFile(String baseDir, String fileName) throws IOException, NoSuchAlgorithmException ;
	
}
