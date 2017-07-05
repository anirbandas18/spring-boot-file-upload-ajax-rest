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