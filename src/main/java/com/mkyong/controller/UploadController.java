package com.mkyong.controller;

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

import com.mkyong.exception.UploadException;
import com.mkyong.service.UploadService;
import com.mkyong.upload.model.FileModel;
import com.mkyong.upload.model.UploadMetadataModel;
import com.mkyong.upload.model.UploadStatusModel;

@RestController
public class UploadController {

	private static final Logger logger = LoggerFactory.getLogger(RestUploadController.class);

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

	@PostMapping("/upload/fileChunk/{baseDir}/{fileName}/{totalChunks}")
	public UploadStatusModel postFileChunk(@RequestParam("fileChunk") MultipartFile fileChunk, @PathVariable String baseDir, 
			@PathVariable String fileName, @PathVariable Integer totalChunks) throws IOException, UploadException, NoSuchAlgorithmException {
		String chunkNumber = fileChunk.getOriginalFilename();
		logger.info("Uploading chunk number " + chunkNumber + " of file " + fileName);
		if (fileChunk.isEmpty()) {
			throw new UploadException("Chunk number " + chunkNumber + " of file " + fileName + " is empty!");
		} else {
			UploadStatusModel uploadStatus = new UploadStatusModel(); 
			Boolean status = uploadService.saveUploadedFileChunk(fileChunk, fileName, totalChunks);
			uploadStatus.setMerged(status);
			if(status) {
				String md5 = uploadService.mergeUploadedFile(baseDir, fileName);
				uploadStatus.setMd5(md5);
			}
			return uploadStatus;
		}
	}
	
}
