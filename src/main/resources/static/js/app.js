/**
 * 
 */
var app = angular.module('app', [ 'ngRoute' ]);

app.config(function($locationProvider, $routeProvider) {
	$locationProvider.html5Mode({
		enabled : true,
		requireBase : false
	});
	$routeProvider.when("/fileupload", {
		title : 'File Upload',
		templateUrl : "view/fileupload.html",
		controller : 'fileuploadcontroller'
	}).when("/", {
		title : 'Index',
		templateUrl : 'view/operations.html',
		controller : 'operationscontroller'
	});
});

// dynamic page title change
app.run(function($rootScope, $route) {
	$rootScope.$on('$routeChangeSuccess', function() {
		document.title = $route.current.title;
	});
});