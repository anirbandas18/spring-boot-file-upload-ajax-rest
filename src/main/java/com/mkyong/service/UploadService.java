package com.mkyong.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface UploadService {
	
	public String saveUploadedFiles(List<MultipartFile> files) throws IOException ;
	
}
