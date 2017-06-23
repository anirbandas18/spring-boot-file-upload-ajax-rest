package com.mkyong.upload.model;

import java.util.List;

public class UploadMetadataModel {

	private Integer fileChunkSize;

	private List<FileModel> filesInBaseDir;

	public Integer getFileChunkSize() {
		return fileChunkSize;
	}

	public void setFileChunkSize(Integer fileChunkSize) {
		this.fileChunkSize = fileChunkSize;
	}

	public List<FileModel> getFilesInBaseDir() {
		return filesInBaseDir;
	}

	public void setFilesInBaseDir(List<FileModel> filesInBaseDir) {
		this.filesInBaseDir = filesInBaseDir;
	}

	
}
