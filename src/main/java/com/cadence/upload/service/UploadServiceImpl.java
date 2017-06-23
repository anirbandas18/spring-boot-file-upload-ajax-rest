package com.cadence.upload.service;

import java.io.ByteArrayOutputStream;
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
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import com.cadence.upload.model.FileChunkModel;
import com.cadence.upload.model.FileChunkUploadStatusModel;
import com.cadence.upload.model.FileModel;

@Component
public class UploadServiceImpl implements UploadService {
	
	private static final Logger logger = LoggerFactory.getLogger(UploadServiceImpl.class);

	@Value("${file.chunk.size}")
	private Integer fileChunkSize;
	
	@Value("${file.storage.location}")
	private String uploadLocation;
	
	@Value("${working.location}")
	private String workingDirectory;
	
	@Value("${file.merge.marker}")
	private String fileMergeMarker;
	
	@Value("#{'${upload.status}'.split(',')}")
	private List<String> uploadStatus;
	
	@Override
	public Boolean saveUploadedFileChunk(FileChunkModel chunk) throws IOException {
		// TODO Auto-generated method stub
		Path dirPath = Paths.get(uploadLocation, workingDirectory, chunk.getFileName());
		Path result = Files.createDirectories(dirPath);
		Path filePath = Paths.get(result.toString(), chunk.getNumber());
		Path written = Files.write(filePath, chunk.getBytes());
		logger.info("Saved file chunk " + written.toString());
		//https://www.quora.com/What-are-some-mistakes-you-can-make-as-a-programmer-that-will-get-you-fired-immediately
		result = Files.createDirectories(dirPath);
		Boolean status = result.getNameCount() == chunk.getTotalChunks();
		return status;
	}

	@Override
	public String mergeUploadedFile(String baseDir, String fileName) throws IOException, NoSuchAlgorithmException {
		// TODO Auto-generated method stub
		Path target = Paths.get(uploadLocation, baseDir, fileName);
		Path source = Paths.get(uploadLocation, workingDirectory, fileName);
		Path newDirPath = Paths.get(source.toString() + fileMergeMarker);
		DirectoryStream<Path> fileChunkPaths = Files.newDirectoryStream(source);
		OutputStream file = new FileOutputStream(target.toString());
		int off = 0;
		ByteArrayOutputStream memory = new ByteArrayOutputStream();
		for(Path filePath : fileChunkPaths) {
			byte[] b = Files.readAllBytes(filePath);
			memory.write(b, off, b.length);
			off = b.length;
		}
		file.write(memory.toByteArray());
		file.close();
		Path renamedPath = Files.move(source, source.resolveSibling(newDirPath));
		logger.info("Uploaded file to " + renamedPath.toString());
		String md5 = DigestUtils.md5DigestAsHex(memory.toByteArray());
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
	public FileChunkUploadStatusModel uploadStatusOfFile(String fileName) throws IOException {
		// TODO Auto-generated method stub
		FileChunkUploadStatusModel fcusm = new FileChunkUploadStatusModel();
		Path filePathInProgress = Paths.get(uploadLocation, workingDirectory, fileName);
		Path filePathCompleted = Paths.get(filePathInProgress.toString(), fileName + fileMergeMarker);
		if(Files.exists(filePathInProgress)) {
			fcusm.setStatus(uploadStatus.get(2));
			int numberOfChunksCompleted = filePathInProgress.getNameCount();
			Long bytesUploaded = new Long(numberOfChunksCompleted * fileChunkSize);
			fcusm.setBytesUploaded(bytesUploaded);
		} else if(Files.exists(filePathCompleted)) {
			String lastModifiedDate = lastModifiedDateOfEntity(filePathCompleted);
			logger.info(fileName + " uploaded successfully on " + lastModifiedDate);
			fcusm.setStatus(uploadStatus.get(2));
			int numberOfChunksCompleted = filePathCompleted.getNameCount();
			Long bytesUploaded = new Long((numberOfChunksCompleted - 1) * fileChunkSize);
			Stream<Path> uploadedFileChunks = Files.list(filePathCompleted);
			bytesUploaded += uploadedFileChunks.findFirst().get().toFile().length();
			uploadedFileChunks.close();
			fcusm.setBytesUploaded(bytesUploaded);
		} else {
			logger.info(fileName+ " not yet uploaded");
			fcusm.setStatus(uploadStatus.get(0));
		}
		return fcusm;
	}
	
	private String lastModifiedDateOfEntity(Path entity) {
		File fileOrDir = entity.toFile();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Date date = new Date();
		date.setTime(fileOrDir.lastModified());
		String formattedDate = sdf.format(date);
		return formattedDate;
	}

}
