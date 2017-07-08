/**
 * http://usejsdoc.org/
 */
app.service('ajax', function($q, $http) {
	this.get = function(url) {
		var defer = $q.defer();
		//url = "/upload/metadata/{baseDir}"
		$http.get(url).then(function(response) {
			defer.resolve(response.data);
		});
		return defer.promise;
	};
	this.post = function(url, data) {
		var defer = $q.defer();
		//url = "/upload/metadata/{baseDir}"
		$http.post(url, data).then(function(response) {
			defer.resolve(response.data);
		});
		return defer.promise;
	};
});