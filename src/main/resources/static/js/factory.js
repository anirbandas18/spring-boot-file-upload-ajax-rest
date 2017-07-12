/**
 * 
 */
app.factory('FileModel', function() {
	function FileModel(file) {
		this.name = file.name;
		this.size = file.size
		this.lastModified = file.lastModified;
		this.progress = typeof file.progress == "undefined" ? 0 : file.progress;// default = 0
		this.status = typeof file.progress == "undefined" ? "InProgress" : file.status;// default = "InProgress"
	};
	FileModel.prototype.toString = function () {
		return JSON.stringify(this);
	}
	return FileModel;
});