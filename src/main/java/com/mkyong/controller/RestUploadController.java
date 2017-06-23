package com.mkyong.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mkyong.service.UploadService;

@RestController
public class RestUploadController {

	private static final Logger logger = LoggerFactory.getLogger(RestUploadController.class);

	@Autowired
	private UploadService uploadService;

	@PostMapping("/upload/single/{fileName}")
	// If not @RestController, uncomment this
	// @ResponseBody
	public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile uploadfile,
			@PathVariable("fileName") String fileName) throws IOException {

		logger.info("Single file upload!");

		if (uploadfile.isEmpty()) {
			return new ResponseEntity<>("please select a file!", HttpStatus.OK);
		} else {
			logger.info("Selected file : " + fileName);
		}
		
		String paths = uploadService.saveUploadedFileChunks(uploadfile, fileName);

		return new ResponseEntity<>("Successfully uploaded - " + paths, HttpStatus.OK);

	}

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
