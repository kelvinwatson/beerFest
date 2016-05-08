var Client = require('node-rest-client').Client;
 
client = new Client();

var template = require('./template.marko');
module.exports = function (req, res) {
	console.log('beer profile page called');
	getBeers(req, function(data) {
		template.render(data, res);
	});	
};

function getBeers(req, callback) {
	client.get('http://www.iamhoppy.com:8080/startUp/'+req.params.fName+'/'+req.params.lName+'/'+req.params.id, function(data, response){
	    // parsed response body as js object 
	    //console.log(data.toString('utf-8'));
	    var responseData = JSON.parse(data.toString('utf-8'));
	    var beers = responseData.beers;
	    var selectedBeer;
	    for(var i=0; i<beers.length;i++) {
	    	console.log(beers[i]);
	    	if(beers[i].beerID == req.params.beerId) {
	    		selectedBeer = beers[i];
	    		break;
	    	}
	    }
	    console.log('selectedBeer: ' + selectedBeer);
	    selectedBeer.user = responseData.user;
	    callback(selectedBeer);
	});
} 

