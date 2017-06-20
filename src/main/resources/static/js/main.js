$(document).ready(function() {

	/* $("#btnSubmit").click(function (event) {

	     //stop submit the form, we will post it manually.
	     event.preventDefault();

	     fire_ajax_submit();

	 });*/

	$("#files").change(function(event) {
		var files = event.target.files; // FileList object
		console.log(files);
		for(var i = 0 ; i < files.length ; i++ ) {
			var file = files[i];
			loadFile(file);
		}
	});
});

function loadFile(file) {
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
}

function fire_ajax_submit() {

	// Get form
	var form = $('#fileUploadForm')[0];

	var data = new FormData(form);

	data.append("customField", "This is some extra data, testing");

	console.log(data);

	alert(data);

	$("#btnSubmit").prop("disabled", true);

	$.ajax({
		type : "POST",
		enctype : 'multipart/form-data',
		url : "/upload/multi",
		data : data,
		//http://api.jquery.com/jQuery.ajax/
		//https://developer.mozilla.org/en-US/docs/Web/API/FormData/Using_FormData_Objects
		processData : false, //prevent jQuery from automatically transforming the data into a query string
		contentType : false,
		cache : false,
		timeout : 600000,
		success : function(data) {

			$("#result").text(data);
			console.log("SUCCESS : ", data);
			$("#btnSubmit").prop("disabled", false);

		},
		error : function(e) {

			$("#result").text(e.responseText);
			console.log("ERROR : ", e);
			$("#btnSubmit").prop("disabled", false);

		}
	});

}