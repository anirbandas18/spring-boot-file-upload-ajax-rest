package com.cadence.upload.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cadence.upload.exception.UploadException;
import com.cadence.upload.model.FileChunkModel;
import com.cadence.upload.model.FileModel;
import com.cadence.upload.model.UploadMetadataModel;
import com.cadence.upload.model.UploadStatusModel;
import com.cadence.upload.service.UploadService;

@RestController
public class UploadController {

	private static final Logger logger = LoggerFactory.getLogger(UploadController.class);

	@Autowired
	private UploadService uploadService;
	
	@Value("${file.chunk.size}")
	private Integer fileChunkSize;
	
	@Value("${client.ip.header}")
	private String clientIPHeader;
	
	@GetMapping("/upload/metadata/{baseDir}")
	public UploadMetadataModel getUploadMetadataForBaseDir(HttpServletRequest request, @PathVariable String baseDir) throws IOException {
		String client = "";
		client = request.getHeader(clientIPHeader);
	    client = client == null || "".equals(client) ? request.getRemoteAddr() : client;
		logger.info("Upload metadata requested by " + client + " for baseDir = " + baseDir);
		List<FileModel> filesInBaseDir = uploadService.getCompletelyUploadedFiles(baseDir);
		UploadMetadataModel uploadMetadata = new UploadMetadataModel();
		uploadMetadata.setFilesInBaseDir(filesInBaseDir);
		uploadMetadata.setFileChunkSize(fileChunkSize);
		return uploadMetadata;
	}

	@GetMapping("upload/status/{fileName}")
	public ResponseEntity<String> getFileStatus(@PathVariable String fileName) throws IOException {
		logger.info("Upload status of " + fileName);
		String status = uploadService.uploadStatusOfFile(fileName);
		ResponseEntity<String> response = new ResponseEntity<>(status, HttpStatus.OK);
		return response;
	}
	
	@GetMapping("/upload/lastChunk/{fileName}")
	public ResponseEntity<String> getLastUploadedChunk(@PathVariable String fileName) throws IOException {
		logger.info("Last uploaded chunk of " + fileName);
		String fileChunkNumber = uploadService.lastUploadedChunk(fileName);
		ResponseEntity<String> response = new ResponseEntity<>(fileChunkNumber, HttpStatus.OK);
		return response;
	}

	@PostMapping("/upload/fileChunk/{fileName}/{totalChunks}/{baseDir}")
	public UploadStatusModel postFileChunk(@RequestParam("fileChunk") MultipartFile fileChunk, @PathVariable String baseDir, 
			@PathVariable String fileName, @PathVariable Integer totalChunks) throws IOException, UploadException, NoSuchAlgorithmException {
		String chunkNumber = fileChunk.getOriginalFilename();
		if (fileChunk.isEmpty()) {
			throw new UploadException("Chunk number " + chunkNumber + " of file " + fileName + " is empty!");
		} else {
			byte[] bytes = fileChunk.getBytes();
			logger.info("Uploading chunk number " + chunkNumber + " of file " + fileName);
			FileChunkModel chunk = new FileChunkModel();
			chunk.setBytes(bytes);
			chunk.setFileName(fileName);
			chunk.setNumber(chunkNumber);
			chunk.setTotalChunks(totalChunks);
			Boolean status = uploadService.saveUploadedFileChunk(chunk);
			UploadStatusModel uploadStatus = new UploadStatusModel(); 
			uploadStatus.setMerged(status);
			if(status) {
				String md5 = uploadService.mergeUploadedFile(baseDir, fileName);
				uploadStatus.setMd5(md5);
			}
			return uploadStatus;
		}
	}
	
}
