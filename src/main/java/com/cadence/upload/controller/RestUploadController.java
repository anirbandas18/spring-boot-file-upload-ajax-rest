package com.cadence.upload.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.cadence.upload.model.FileModel;
import com.cadence.upload.model.UploadMetadataModel;
import com.cadence.upload.service.UploadService;

@RestController
public class RestUploadController {

	private static final Logger logger = LoggerFactory.getLogger(RestUploadController.class);

	@Autowired
	private UploadService uploadService;
	
	@Value("${file.chunk.size}")
	private Integer fileChunkSize;
	
	@GetMapping("/upload/metadata")
	public UploadMetadataModel getUploadMetadata(HttpServletRequest request) throws IOException {
		HttpSession session = request.getSession();
		String baseDir = (String) session.getAttribute("baseDir");
		String client = "";
		client = request.getHeader("X-FORWARDED-FOR");
	    client = client == null || "".equals(client) ? request.getRemoteAddr() : client;
		logger.info("Upload metadata requested by " + client + " for baseDir " + baseDir);
		List<FileModel> filesInBaseDir = uploadService.getCompletelyUploadedFiles(baseDir);
		UploadMetadataModel uploadMetadata = new UploadMetadataModel();
		uploadMetadata.setFilesInBaseDir(filesInBaseDir);
		uploadMetadata.setFileChunkSize(fileChunkSize);
		return uploadMetadata;
	}
	
	@GetMapping("upload/status/{fileName}")
	public void getFileStatus(@PathVariable String fileName) {
		logger.info("File status of " + fileName);
		
	}

	/*@PostMapping("/upload/fileChunk/{fileName}")
	// If not @RestController, uncomment this
	// @ResponseBody
	public ResponseEntity<?> postFileChunk(@RequestParam("file") MultipartFile uploadfile,
			@PathVariable String fileName) throws IOException {

		logger.info("Single file upload!");

		if (uploadfile.isEmpty()) {
			return new ResponseEntity<>("please select a file!", HttpStatus.OK);
		} else {
			logger.info("Selected file : " + fileName);
		}
		
		String paths = uploadService.saveUploadedFileChunk(uploadfile, fileName);

		return new ResponseEntity<>("Successfully uploaded - " + paths, HttpStatus.OK);

	}*/

	/*@PostMapping("/upload/multi")
	public ResponseEntity<?> uploadFileMulti(@RequestParam("files") MultipartFile[] uploadfiles,
			@RequestParam("customField") String customField, HttpServletRequest request) throws IOException {

		logger.info("Multiple file upload!");

		HttpSession session = request.getSession();
		String baseDir = (String) session.getAttribute("baseDir");
		String id = session.getId();

		logger.info(id + " " + baseDir + " " + customField);

		String uploadedFileName = Arrays.stream(uploadfiles).map(x -> x.getOriginalFilename())
				.filter(x -> !StringUtils.isEmpty(x)).collect(Collectors.joining(" , "));

		if (StringUtils.isEmpty(uploadedFileName)) {
			return new ResponseEntity<>("please select a file!", HttpStatus.OK);
		}
		String paths = uploadService.saveUploadedFiles(baseDir, Arrays.asList(uploadfiles));

		return new ResponseEntity<>("Successfully uploaded - " + paths, HttpStatus.OK);

	}*/

	// maps html form to a Model
	/*@PostMapping("/upload/multi/model")
	public ResponseEntity<?> multiUploadFileModel(@ModelAttribute UploadModel model, HttpServletRequest request)
			throws IOException {

		logger.debug("Multiple file upload! With UploadModel");
		HttpSession session = request.getSession();
		String baseDir = (String) session.getAttribute("baseDir");

		String paths = uploadService.saveUploadedFiles(baseDir, Arrays.asList(model.getFiles()));
		return new ResponseEntity<>("Successfully uploaded - " + paths, HttpStatus.OK);

	}*/

}
