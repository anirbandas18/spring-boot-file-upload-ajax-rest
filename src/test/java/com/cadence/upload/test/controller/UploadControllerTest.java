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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.cadence.upload.controller.UploadController;
import com.cadence.upload.model.FileModel;
import com.cadence.upload.model.FileUploadMetadataModel;
import com.cadence.upload.service.UploadService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@WebMvcTest(UploadController.class)
@TestPropertySource("classpath:test.properties")
public class UploadControllerTest {
	
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UploadService uploadService;
	
	@Autowired
	private ObjectMapper objectMapper; 

	@Value("${file.chunk.size}")
	private Integer fileChunkSize;

	@Value("${baseDir}")
	private String baseDir;
	
	@Value("#{new java.lang.Long('${random.range.lower}')}")
	private Long lower;
	
	@Value("#{new java.lang.Long('${random.range.upper}')}")
	private Long upper;
	
	@Test
	public void shouldReturnFileUploadMetadataWhenBaseDirIsGiven() throws Exception {
		FileModel fm1 = new FileModel();
		fm1.setLastModified(System.currentTimeMillis());
		fm1.setName("file1");
		fm1.setSize(ThreadLocalRandom.current().nextLong(lower, upper));
		FileModel fm2 = new FileModel();
		fm2.setLastModified(System.currentTimeMillis());
		fm2.setName("file2");
		fm1.setSize(ThreadLocalRandom.current().nextLong(lower, upper));
		List<FileModel> filesInBaseDir = new ArrayList<>();
		filesInBaseDir.add(fm2);
		filesInBaseDir.add(fm1);
		FileUploadMetadataModel uploadMetadata = new FileUploadMetadataModel();
		uploadMetadata.setFilesInBaseDir(filesInBaseDir);
		uploadMetadata.setFileChunkSize(fileChunkSize);
		String jsonString = objectMapper.writeValueAsString(uploadMetadata);
		when(uploadService.getCompletelyUploadedFiles(baseDir)).thenReturn(filesInBaseDir);
		mockMvc.perform(get("/upload/metadata/" + baseDir)).andDo(print()).andExpect(status().isOk())
				.andExpect(content().json(jsonString));
	}

}
