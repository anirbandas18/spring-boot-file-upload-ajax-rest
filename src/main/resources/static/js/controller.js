/**
 * 
 */
app.controller('operationscontroller', function($scope, $location){
	$scope.operation = 'fileupload';
	$scope.go = function(path) {
		sessionStorage.setItem('baseDir', $scope.baseDir);
		console.log(sessionStorage.getItem('baseDir'));
		console.log($scope.operation);
		$location.path($scope.operation);
	};
});

app.controller('fileuploadcontroller',
				function($scope) {
					var file1 = {
						name : "Introduction to R programming.mp4",
						progress : 25,
						status : "InProgress",
						size : 4868729,
						lastModified : 1499376984778
					};
					var file2 = {
						name : "Introduction to machine learning_ Alex Simola.pdf",
						progress : 100,
						status : "Completed",
						size : 2898689878,
						lastModified : 1498376984778
					};
					var file3 = {
						name : "Machine Learning Resource Guide 2014.pdf",
						progress : 0,
						status : "TBD",
						size : 48728,
						lastModified : 1497376984778
					};
					$scope.uploadedFileList = [ file1, file2, file3 ];
					$scope.propertyName = 'name';
					$scope.reverse = true;
					$scope.orderByField = function(propertyName) {
						$scope.reverse = ($scope.propertyName === propertyName) ? !$scope.reverse
								: false;
						$scope.propertyName = propertyName;
					}
					$scope.selectRow = function(file) {
						console.log(file.name);
					};
					$scope.baseDir = sessionStorage.getItem('baseDir');
				});