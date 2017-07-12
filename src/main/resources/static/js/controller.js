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
				function($scope, FileModel) {
					var f1 = {name : "Introduction to R programming.mp4", size : 4868729, lastModified : 1499376984778, progress : 25, status : "InProgress"};
					var f2 = {name : "Introduction to machine learning_ Alex Simola.pdf", size : 2898689878, lastModified : 1498376984778, progress : 100, status : "Completed"};
					var f3 = {name : "Machine Learning Resource Guide 2014.pdf", size : 48728, lastModified : 1497376984778, progress : 0, status : "TBD"};
					$scope.uploadedFileList = [ new FileModel(f1), new FileModel(f2), new FileModel(f3)  ];
					$scope.propertyName = 'name';
					$scope.reverse = true;
					$scope.orderByField = function(propertyName) {
						$scope.reverse = ($scope.propertyName === propertyName) ? !$scope.reverse
								: false;
						$scope.propertyName = propertyName;
					}
					$scope.onFileSelection = function () {
						for(var i = 0 ; i < $scope.selectedFileList.length ; i++ ) {
							var fileModel = new FileModel($scope.selectedFileList[i]);
							console.log(fileModel);
							$scope.uploadedFileList.push(fileModel);
						}
					};
					$scope.selectRow = function(file) {
						$scope.selectedFile = file;
						console.log($scope.selectedFile);
					};
					$scope.baseDir = sessionStorage.getItem('baseDir');
				});