package com.cadence.upload.model;

import java.util.List;

public class FileUploadMetadataModel {

	private Long fileChunkSize;

	private List<FileModel> filesInBaseDir;

	public Long getFileChunkSize() {
		return fileChunkSize;
	}

	public void setFileChunkSize(Long fileChunkSize) {
		this.fileChunkSize = fileChunkSize;
	}

	public List<FileModel> getFilesInBaseDir() {
		return filesInBaseDir;
	}

	public void setFilesInBaseDir(List<FileModel> filesInBaseDir) {
		this.filesInBaseDir = filesInBaseDir;
	}

	
}
