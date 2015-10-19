require('marko/node-require').install();
var express = require('express');
var app = express();
var path = require("path");
var winston = require('winston');

app.use('/web-components', express.static(__dirname + '/web-components'));
app.use('/js', express.static(__dirname + '/js'));
app.use('/css', express.static(__dirname + '/css'));
app.use('/images', express.static(__dirname + '/images'));
app.use('/fonts', express.static(__dirname + '/fonts'));

app.get('/', function (req, res) {
	winston.info('default endpoint called');
    res.sendFile(path.join(__dirname+'/index.html'));
});

app.get('/beers/:fName/:lName/:id', require('./pages/beers/'));
app.get('/beer/:fName/:lName/:id/:beerId', require('./pages/beer/'));

var server = app.listen(80, function () {

	var host = server.address().address;
	var port = server.address().port;
	winston.add(winston.transports.File, { filename: 'website.log' });
	winston.info('Website listening at http://%s:%s', host, port);

});
