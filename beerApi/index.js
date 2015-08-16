var express = require('express');
var app = express();
var path = require("path");
var mysql = require("mysql");
var con;

function getDefaultEvent(callback) {
	var selectDefaultEvent = "SELECT id FROM events WHERE event_date>CURDATE() LIMIT 1;";
	con.query(selectDefaultEvent, function(err, rows, fields) {
		if (err) {
			callback(false);
		}
		callback(true, rows);
	});
}

function getBeersForEvent(eventId, callback) {
	var selectBeersForEvent = "SELECT e.name AS eventName,e.event_date AS eventDate, b.name AS beerName, b.type AS beerType, b.ABV AS beerABV, b.IBU AS beerIBU, b.description AS beerDescription FROM events e INNER JOIN features ON event_id=e.id INNER JOIN beers b ON beer_id=b.id WHERE e.id="+eventId+" ORDER BY b.name ASC;";
	con.query(selectBeersForEvent, function(err, rows, fields) {
		if (err) {
			callback(false);
		}
		callback(true, rows);
	});
}

app.get('/getDefaultBeers', function (req, res) {
	var eventId;
	var beers = [];
	getDefaultEvent(function(success, rows) {
		var eventId = rows[0].id;
		getBeersForEvent(eventId, function(success, rows) {
			if(success) {
				res.setHeader('Content-Type', 'application/json');
				res.send(JSON.stringify(rows));
			}
		});
	});
});

app.get('/getBeers/:eventId', function (req, res) {
	var eventId = req.params.eventId;
	var beers = [];
	if(!eventId) {
		//TODO: Handle no eventId
		res.setHeader('Content-Type', 'application/json');
		res.send(JSON.stringify({success: false, errorMsg: 'Missing eventId'}));
		return;
	} 
	getBeersForEvent(eventId, function(success, rows) {
		if(success) {
			res.setHeader('Content-Type', 'application/json');
			res.send(JSON.stringify(rows));
			return;
		}
	});
});

app.get('/', function (req, res) {
	console.log('default endpoint');
});

var server = app.listen(80, function () {

	var host = server.address().address;
	var port = server.address().port;

	console.log('Example app listening at http://%s:%s', host, port);

	con = mysql.createConnection({
		host: "localhost",
		user: "root",
		password: "********",
		database: 'beer_app'
	});

	con.connect(function(err){
		if(err){
			console.log(err);
			return;
		}
		console.log('Connection established');
	});

});
