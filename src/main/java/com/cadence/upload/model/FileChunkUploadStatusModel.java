package com.cadence.upload.model;

public class FileChunkUploadStatusModel {
	
	private String status;
	
	private Long bytesUploaded;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getBytesUploaded() {
		return bytesUploaded;
	}

	public void setBytesUploaded(Long bytesUploaded) {
		this.bytesUploaded = bytesUploaded;
	}
	
	

}
