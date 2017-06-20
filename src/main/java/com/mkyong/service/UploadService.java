package com.mkyong.service;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface UploadService {
	
	public String saveUploadedFileChunks(MultipartFile fileChunk,String fileName) throws IOException ;
	
	public String mergeUploadedFile(String baseDir, String fileName) throws IOException ;
	
}
