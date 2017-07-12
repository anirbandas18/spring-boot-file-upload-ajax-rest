/**
 * 
 */
app.factory('FileModel', function() {
	function FileModel() {
		this.name = new String();
		this.progress = new Number();
		this.status = new String();
		this.size = new Number();
		this.lastModified = new Number();
	};
	FileModel.prototype.toString = function () {
		return JSON.stringify(this);
	}
	return FileModel;
});