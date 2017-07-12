/**
 * 
 */
app.factory('FileModel', function() {
	function FileModel(file) {
		this.index = FileModel.index++;
		this.name = file.name;
		this.progress = typeof file.progress == "undefined" ? 0 : file.progress;// default = 0
		this.status = typeof file.progress == "undefined" ? "InProgress" : file.status;// default = "InProgress"
		this.size = file.size
		this.lastModified = file.lastModified;
	};
	FileModel.index = 1;
	FileModel.prototype.toString = function () {
		return JSON.stringify(this);
	}
	return FileModel;
});