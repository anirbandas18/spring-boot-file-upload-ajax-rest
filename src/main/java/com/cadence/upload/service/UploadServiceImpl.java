package com.cadence.upload.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cadence.upload.core.CheckSum;
import com.cadence.upload.model.FileChunkModel;
import com.cadence.upload.model.FileModel;

@Component
public class UploadServiceImpl implements UploadService {
	
	private static final Logger logger = LoggerFactory.getLogger(UploadServiceImpl.class);

	@Value("${file.storage.location}")
	private String uploadLocation;
	
	@Value("${working.location}")
	private String workingDirectory;
	
	@Value("${file.merge.marker}")
	private String fileMergeMarker;
	
	@Value("#{'${upload.status}'.split(',')}")
	private List<String> uploadStatus;
	
	@Autowired
	private CheckSum checkSum;
	
	@Override
	public Boolean saveUploadedFileChunk(FileChunkModel chunk) throws IOException {
		// TODO Auto-generated method stub
		Path dirPath = Paths.get(uploadLocation, workingDirectory, chunk.getFileName());
		Path result = Files.createDirectories(dirPath);
		Path filePath = Paths.get(result.toString(), chunk.getNumber());
		Path written = Files.write(filePath, chunk.getBytes());
		logger.info("Saved file chunk " + written.toString());
		Stream<Path> fileChunks = Files.list(result);
		Boolean status = fileChunks.count() == chunk.getTotalChunks();
		fileChunks.close();
		return status;
	}

	@Override
	public String mergeUploadedFile(String baseDir, String fileName) throws IOException, NoSuchAlgorithmException {
		// TODO Auto-generated method stub
		Path target = Paths.get(uploadLocation, baseDir, fileName);
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
		logger.info("Uploaded file to " + renamedPath.toString());
		String md5 = checkSum.generate(target.toString());
		return md5;
	}

	@Override
	public List<FileModel> getCompletelyUploadedFiles(String baseDir) throws IOException {
		// TODO Auto-generated method stub
		Path baseDirPath = Paths.get(uploadLocation, baseDir);
		DirectoryStream<Path> uploadedFiles = Files.newDirectoryStream(baseDirPath);
		List<FileModel> uploadedFilesInBaseDir = new ArrayList<>();
		for(Path uploadedFile : uploadedFiles) {
			File file = uploadedFile.toFile();
			FileModel fm = new FileModel();
			fm.setLastModified(file.lastModified());
			fm.setSize(file.length());
			fm.setName(file.getName());
			uploadedFilesInBaseDir.add(fm);
		}
		return uploadedFilesInBaseDir;
	}

	@Override
	public String uploadStatusOfFile(String fileName) throws IOException {
		// TODO Auto-generated method stub
		Path filePathInProgress = Paths.get(uploadLocation, workingDirectory, fileName);
		Path filePathCompleted = Paths.get(filePathInProgress.toString(), fileName + fileMergeMarker);
		String status = "";
		if(Files.exists(filePathInProgress)) {
			status = uploadStatus.get(1);
		} else if(Files.exists(filePathCompleted)) {
			String lastModifiedDate = lastModifieddateOfEntity(filePathCompleted);
			logger.info(fileName + " uploaded successfully on " + lastModifiedDate);
			status = uploadStatus.get(2);
		} else {
			logger.info(fileName + " not yet uploaded");
			status = uploadStatus.get(0);
		}
		return status;
	}

	@Override
	public String lastUploadedChunk(String fileName) throws IOException {
		// TODO Auto-generated method stub
		Path filePath = Paths.get(uploadLocation, workingDirectory, fileName);
		Stream<Path> uploadedFiles = Files.list(filePath);
		Optional<Path> lastUploaded = uploadedFiles.findFirst();
		uploadedFiles.close();
		Path fileChunk = lastUploaded.get();
		String lastModifiedDate = lastModifieddateOfEntity(fileChunk);
		logger.info("Last file chunk of " + fileName + " uploaded on " + lastModifiedDate);
		return fileChunk.getFileName().toString();
	}
	
	private String lastModifieddateOfEntity(Path entity) {
		File fileOrDir = entity.toFile();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Date date = new Date();
		date.setTime(fileOrDir.lastModified());
		String formattedDate = sdf.format(date);
		return formattedDate;
	}

}
