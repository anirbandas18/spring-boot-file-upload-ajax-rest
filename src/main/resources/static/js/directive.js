/**
 * 
 */
app.directive("fileInput", function() {
	return {
		require: "ngModel",
		link: function postLink(scope,elem,attrs,ngModel) {
			elem.on("change", function(e) {
				var files = elem[0].files;
				for(var i = 0 ; i < files.length ; i++ ) {
					console.log(files[i]);
				}
		        ngModel.$setViewValue(files);
		    })
		 }
	}
});