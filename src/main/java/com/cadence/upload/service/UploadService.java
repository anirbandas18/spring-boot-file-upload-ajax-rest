package com.cadence.upload.service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cadence.upload.model.FileChunkModel;
import com.cadence.upload.model.FileChunkUploadStatusModel;
import com.cadence.upload.model.FileModel;

@Service
public interface UploadService {
	
	public FileChunkUploadStatusModel uploadStatusOfFile(String fileName) throws IOException;
	
	public List<FileModel> getCompletelyUploadedFiles(String baseDir) throws IOException;
	
	public Boolean saveUploadedFileChunk(FileChunkModel chunk) throws IOException ;
	
	public String mergeUploadedFile(String baseDir, String fileName) throws IOException, NoSuchAlgorithmException ;
	
}
