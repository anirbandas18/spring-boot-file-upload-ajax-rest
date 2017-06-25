package com.cadence.upload.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cadence.upload.exception.UploadException;
import com.cadence.upload.model.FileChunkModel;
import com.cadence.upload.model.FileChunkUploadStatusModel;
import com.cadence.upload.model.FileModel;
import com.cadence.upload.model.FileUploadMetadataModel;
import com.cadence.upload.model.FileUploadStatusModel;
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
	public FileUploadMetadataModel getUploadMetadataForBaseDir(HttpServletRequest request, @PathVariable String baseDir) throws IOException {
		String client = "";
		client = request.getHeader(clientIPHeader);
	    client = client == null || "".equals(client) ? request.getRemoteAddr() : client;
		logger.info("Upload metadata requested by " + client + " for baseDir = " + baseDir);
		List<FileModel> filesInBaseDir = uploadService.getCompletelyUploadedFiles(baseDir);
		FileUploadMetadataModel uploadMetadata = new FileUploadMetadataModel();
		uploadMetadata.setFilesInBaseDir(filesInBaseDir);
		uploadMetadata.setFileChunkSize(new Long(fileChunkSize));
		return uploadMetadata;
	}

	@GetMapping("/upload/status/{fileName}")
	public FileChunkUploadStatusModel getFileStatus(@PathVariable String fileName) throws IOException {
		logger.info("Upload status of " + fileName);
		FileChunkUploadStatusModel fcusm = uploadService.uploadStatusOfFile(fileName);
		return fcusm;
	}
	
	@PostMapping("/upload/fileChunk/{fileName}/{totalChunks}/{baseDir}")
	public FileUploadStatusModel postFileChunk(@RequestParam("fileChunk") MultipartFile fileChunk, @PathVariable String baseDir, 
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
			FileUploadStatusModel uploadStatus = new FileUploadStatusModel(); 
			uploadStatus.setMerged(status);
			if(status) {
				String md5 = uploadService.mergeUploadedFile(baseDir, fileName);
				uploadStatus.setMd5(md5);
			}
			return uploadStatus;
		}
	}
	
}
