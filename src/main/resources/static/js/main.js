$(document).ready(function() {
	$("#files").change(function(event) {
			var files = event.target.files; // FileList object
			console.log(files);
			for(var i = 0 ; i < files.length ; i++ ) {
				var file = files[i];
				console.log('Starting worker');
				var worker = new Worker("js/upload.js");
				worker.onmessage = function(e) {
					$("#result").text(e.data.status + ' ' + e.data.responseText);
					//worker.terminate();
				};
       			worker.postMessage({fileObject : file, chunkSize : 1000002});
				//loadFile(file);
			}
		});
});

/*function loadFile(file) {
	var fileChunkSize = 1000002;
	for (var offset = 0, partNo = 0 ; offset < file.size ; offset += fileChunkSize, partNo++) {
		var blob = file.slice(offset, offset + fileChunkSize,  "{type: 'text/plain'}");
		var formData = new FormData();
		formData.append("fileName", file.name);
		formData.append("file", blob, partNo);
		uploadFile(formData);
	}
}

function uploadFile(formData) {
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
			$("#result").text(data);
			console.log("SUCCESS : ", data);
		},
		error : function(e) {
			$("#result").text(e.responseText);
			console.log("ERROR : ", e);
		}
	});
}*/