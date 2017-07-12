/**
 * http://usejsdoc.org/
 */
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

app.filter('capitalizeFirstCharacter', function() {
	return function(phrase) {
		phrase = phrase.replace(/([A-Z])/g, ' $1').replace(/^./, function(str){ return str.toUpperCase(); });
		var tokens = phrase.split(" ");
		var formattedStr = "";
		for(var i = 0 ; i < tokens.length ; i++) {
			var word = tokens[i];
			var ascii = word.charCodeAt(0);
			if(ascii >= 65 && ascii <= 90 || ascii >= 97 && ascii <= 122) {
				if(ascii >= 97 && ascii <= 122) {
					ascii = ascii - 32;
				}
			} 
			word = String.fromCharCode(ascii) + word.substring(1);
			formattedStr = formattedStr + word + " ";
		}
		formattedStr = formattedStr.trim();
		return formattedStr;
	}
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
		case '':
			translation = "";
			break;
		default:
			translation = "Stopped";
			break;
		}
		return translation;
	};

});