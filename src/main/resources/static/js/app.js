/**
 * 
 */
var app = angular.module('app', []);

app.config(function($locationProvider) {
	$locationProvider.html5Mode({
		enabled : true,
		requireBase : false
	});
});

app.filter('formatFileSize', function() {
	return function(bytes) {
		var sizes = [ 'Bytes', 'KB', 'MB', 'GB', 'TB' ];
		if (bytes == 0)
			return 'n/a';
		var i = parseInt(Math.floor(Math.log(bytes) / Math.log(1024)));
		if (i == 0)
			return bytes + ' ' + sizes[i];
		return (bytes / Math.pow(1024, i)).toFixed(1) + ' ' + sizes[i];
	};

});

app.filter('progressPercentage', function() {
	return function(progress) {
		var progressPercentage = progress.toString() + "%";
		return progressPercentage;
	};

});

app.filter('translateStatus', function() {
	return function(status) {
		var translation = "";
		switch (status) {
		case 'TBD':
			translation = "Starting";
			break;
		case 'InProgress':
			translation = "Uploading";
			break;
		case 'Completed':
			translation = "Finished";
			break;
		default:
			translation = "Stopped";
			break;
		}
		return translation;
	};

});