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