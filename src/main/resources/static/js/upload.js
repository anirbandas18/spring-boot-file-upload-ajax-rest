this.onmessage = function(e) {
	var file = e.data.fileObject;
	var fileChunkSize = e.data.chunkSize;
	for (var offset = 0, partNo = 0 ; offset < file.size ; offset += fileChunkSize, partNo++) {
		var blob = file.slice(offset, offset + fileChunkSize,  "{type: 'text/plain'}");
		var formData = new FormData();
		formData.append("fileName", file.name);
		formData.append("file", blob, partNo);
		$.ajax({
			type : "POST",
			enctype : 'multipart/form-data',
			url : "/upload/single",
			data : formData,
			processData : false, //prevent jQuery from automatically transforming the data into a query string
			contentType : false,
			cache : false,
			timeout : 0,
			success : function(data) {
				//$("#result").text(data);
				this.postMessage({responseText : data, status : 'SUCCESS'});
				console.log("SUCCESS : ", data);
			},
			error : function(e) {
				//$("#result").text(e.responseText);
				this.postMessage({responseText : e.responseText, status : 'ERROR'});
				console.log("ERROR : ", e);
			}
		});
	}
};