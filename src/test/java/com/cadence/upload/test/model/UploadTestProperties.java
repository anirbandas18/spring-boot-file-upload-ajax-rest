package com.cadence.upload.test.model;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:file-upload-test.properties")
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "fut")
public class UploadTestProperties {
	
	private Long fileChunkSize;
	
	private String baseDir;

	public static class FileUpload {
		
		private String name;
		
		private Long bytes;
		
		private String status;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Long getBytes() {
			return bytes;
		}

		public void setBytes(Long bytes) {
			this.bytes = bytes;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		@Override
		public String toString() {
			return "FileUpload [name=" + name + ", bytes=" + bytes + ", status=" + status + "]";
		}
		
	}
	
	private List<FileUpload> files;

	public Long getFileChunkSize() {
		return fileChunkSize;
	}

	public void setFileChunkSize(Long fileChunkSize) {
		this.fileChunkSize = fileChunkSize;
	}

	public String getBaseDir() {
		return baseDir;
	}

	public void setBaseDir(String baseDir) {
		this.baseDir = baseDir;
	}

	public List<FileUpload> getFiles() {
		return files;
	}

	public void setFiles(List<FileUpload> files) {
		this.files = files;
	}

	
}
