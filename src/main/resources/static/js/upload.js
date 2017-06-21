onmessage = function(e) {
	var file = e.data.fileObject;
	var fileChunkSize = e.data.chunkSize;
	console.log('Worker Input : ' + JSON.stringify(e.data));
	var action = '/upload/single';
	var method = 'POST';
	var ajax = new XMLHttpRequest();// For non IE browsers
	ajax.open(method, action, true);
	for (var offset = 0, partNo = 0; offset < file.size; offset += fileChunkSize, partNo++) {
		var blob = file.slice(offset, offset + fileChunkSize, "{type: 'text/plain'}");
		var formData = new FormData();
		formData.append("fileName", file.name);
		formData.append("file", blob, partNo);
		console.log(method + ' to ' + action + ' with ' + file.name + ' -> ' + partNo);
		ajax.send(formData);
		ajax.onreadystatechange = function() {
	        if (this.readyState == 4) {
	        	console.log(this.responseText + ' on ' + this.status + '-' + this.statusText)
	        	postMessage({responseText : this.responseText, status : this.statusText});
	       } 
	    };
	}
};