/**
 * 
 */
var app = angular.module('app', ['ngRoute']);

app.config(function($locationProvider, $routeProvider) {
	$locationProvider.html5Mode({
		enabled : true,
		requireBase : false
	});
	$routeProvider.when("/fileUpload/:baseDir", {
        templateUrl : "view/fileupload.html",
        controller : 'uploadcontroller'
    }).otherwise({
        redirectTo:'index.html'
    }); 
});

/*app.run(function($rootScope, $location, $window, ajax) {
	var map = $location.search();
	sessionStorage.setItem('baseDir', map.baseDir);
	var url = "/upload/metadata/" + sessionStorage.getItem('baseDir');
	var promise = ajax.get(url);
	promise.then(function(response) {
		console.log(response.data); // "Stuff worked!"
	}, function(err) {
		console.log(err); // Error: "It broke"
	});
});*/