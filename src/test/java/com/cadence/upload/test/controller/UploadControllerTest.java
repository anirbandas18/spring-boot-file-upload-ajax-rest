package com.cadence.upload.test.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.cadence.upload.controller.UploadController;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UploadControllerTest {

	@LocalServerPort
    private int port;
	
	@Autowired
	private UploadController uploadController;
	
	 @Autowired
	 private TestRestTemplate testRestTemplate;
	 
	 @Test
	 public void hj() {
		 
	 }
	
}
