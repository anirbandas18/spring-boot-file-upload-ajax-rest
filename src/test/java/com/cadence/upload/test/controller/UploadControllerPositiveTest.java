package com.cadence.upload.test.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.cadence.upload.controller.UploadController;
import com.cadence.upload.model.FileChunkUploadStatusModel;
import com.cadence.upload.model.FileModel;
import com.cadence.upload.model.FileUploadMetadataModel;
import com.cadence.upload.service.UploadService;
import com.cadence.upload.test.model.UploadTestProperties;
import com.cadence.upload.test.model.UploadTestProperties.FileUpload;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@WebMvcTest(UploadController.class)
@ComponentScan(basePackages = {"com.cadence.upload.test"})
public class UploadControllerPositiveTest {

	@MockBean
	private UploadService uploadService;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UploadTestProperties testModel;

	@Test
	public void shouldReturnFileUploadMetadataWhenBaseDirIsGiven() throws Exception {
		List<FileModel> filesInBaseDir = fileModelDataset();
		FileUploadMetadataModel uploadMetadata = new FileUploadMetadataModel();
		uploadMetadata.setFilesInBaseDir(filesInBaseDir);
		uploadMetadata.setFileChunkSize(testModel.getFileChunkSize());
		String jsonString = objectMapper.writeValueAsString(uploadMetadata);
		when(uploadService.getCompletelyUploadedFiles(testModel.getBaseDir())).thenReturn(filesInBaseDir);
		mockMvc.perform(get("/upload/metadata/" + testModel.getBaseDir())).andDo(print()).andExpect(status().isOk())
				.andExpect(content().json(jsonString));
	}

	//@Test
	public void shouldReturnFileStatusWhenFileUploadYetTBD() throws Exception {
		FileUpload fu = testModel.getFiles().get(0);
		FileChunkUploadStatusModel uploadStatus = fileUploadStatus(fu);
		String jsonString = objectMapper.writeValueAsString(uploadStatus);
		when(uploadService.uploadStatusOfFile(fu.getName())).thenReturn(uploadStatus);
		mockMvc.perform(get("/upload/status/" + fu.getName())).andDo(print()).andExpect(status().isOk())
				.andExpect(content().json(jsonString));
	}

	//@Test
	public void shouldReturnFileStatusWhenFileUploadIsCompleted() throws Exception {
		FileUpload fu = testModel.getFiles().get(2);
		FileChunkUploadStatusModel uploadStatus = fileUploadStatus(fu);
		String jsonString = objectMapper.writeValueAsString(uploadStatus);
		when(uploadService.uploadStatusOfFile(fu.getName())).thenReturn(uploadStatus);
		mockMvc.perform(get("/upload/status/" + fu.getName())).andDo(print()).andExpect(status().isOk())
				.andExpect(content().json(jsonString));
	}

	//@Test
	public void shouldReturnFileStatusWhenFileUploadInProgress() throws Exception {
		FileUpload fu = testModel.getFiles().get(1);
		FileChunkUploadStatusModel uploadStatus = fileUploadStatus(fu);
		String jsonString = objectMapper.writeValueAsString(uploadStatus);
		when(uploadService.uploadStatusOfFile(fu.getName())).thenReturn(uploadStatus);
		mockMvc.perform(get("/upload/status/" + fu.getName())).andDo(print()).andExpect(status().isOk())
				.andExpect(content().json(jsonString));
	}

	private List<FileModel> fileModelDataset() {
		List<FileModel> filesInBaseDir = new ArrayList<>();
		for (int i = 0; filesInBaseDir.size() != 2; i++) {
			Long n = ThreadLocalRandom.current().nextLong(testModel.getLower(), testModel.getUpper());
			if (n != null) {
				FileModel fm = new FileModel();
				fm.setLastModified(System.currentTimeMillis());
				fm.setName("file" + i);
				fm.setSize(n);
				filesInBaseDir.add(fm);
			}
		}
		return filesInBaseDir;
	}

	private FileChunkUploadStatusModel fileUploadStatus(FileUpload fu) {
		FileChunkUploadStatusModel fcusm = new FileChunkUploadStatusModel();
		fcusm.setBytesUploaded(fu.getBytes());
		fcusm.setStatus(fu.getStatus());
		return fcusm;
	}

}
