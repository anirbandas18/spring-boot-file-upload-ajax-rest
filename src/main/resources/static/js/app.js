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

app.run(function($rootScope, $location, $window, ajax) {
	var map = $location.search();
	sessionStorage.setItem('baseDir', map.baseDir);
	var url = "/upload/metadata/" + sessionStorage.getItem('baseDir');
	var promise = ajax.get(url);
	promise.then(function(response) {
		console.log(response.data); // "Stuff worked!"
	}, function(err) {
		console.log(err); // Error: "It broke"
	});
});