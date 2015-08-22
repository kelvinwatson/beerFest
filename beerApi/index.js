var express = require('express');
var app = express();
var path = require("path");
var mysql = require("mysql");
var con;

function getFavoriteBeers(eventId, userId, callback) {
	var selectBeersForEvent = "SELECT br.logo_url AS logoUrl, br.name AS breweryName, e.name AS eventName,DATE_FORMAT(e.event_date, '%W, %M %D, %Y') AS eventDate, b.id as beerID, b.name AS beerName, b.type AS beerType, b.ABV AS beerABV, b.IBU AS beerIBU, b.description AS beerDescription " +
	"FROM users u "+
	"INNER JOIN favorites f ON f.user_id=u.id "+
	"INNER JOIN beers b ON beer_id=b.id  "+
	"INNER JOIN breweries br ON br.id=b.brewery_id "+
	"INNER JOIN features fe ON fe.beer_id=b.id "+
	"INNER JOIN events e ON e.id=fe.event_id "+
	"WHERE e.id="+eventId+" AND u.id="+userId+" ORDER BY b.name ASC;";
	con.query(selectBeersForEvent, function(err, rows, fields) {
		if (err) {
			callback(false);
		}
		callback(true, rows);
	});
}

function getUser(reqData, callback) {
	var getUser = "SELECT * FROM users WHERE facebook_credential = ?;";
	con.query(getUser, reqData, function(err, rows, fields) {
		if (err) { 
			callback(false);
		}
		callback(true, rows);
	});
}

function addUser(reqData, callback) {
	var addUser = "INSERT INTO users (first_name, last_name, facebook_credential) VALUES ?;";
	con.query(addUser, [[reqData]], function(err, results) {
		console.log(err);
		console.log(results);
		if(err) {
			callback(false);
		} else {
			callback(true, results);
		}
	});
}

function removeFavorites(reqData, callback) {
	var removeFavorites = "DELETE FROM favorites WHERE user_id = ? AND beer_id = ?;";
	con.query(removeFavorites, reqData, function(err, results) {
		console.log(err);
		console.log(results);
		if(err) {
			callback(false);
		} else {
			callback(true, results);
		}
	});
}

function setFavorites(reqData, callback) {
	var insertFavorites = "INSERT INTO favorites (user_id, beer_id) VALUES ?;";
	con.query(insertFavorites, [reqData], function(err, results) {
		console.log(err);
		console.log(results);
		if(err) {
			callback(false);
		} else {
			callback(true, results);
		}
	});
}

function getAllEvents(callback) {
	var selectAllEvents = "SELECT id, name AS eventName, DATE_FORMAT(event_date,'%W, %M %D, %Y') AS eventDate, logo_url AS logoUrl FROM events WHERE event_date>CURDATE();";
	con.query(selectAllEvents, function(err, rows, fields) {
		if (err) {
			callback(false);
		}
		callback(true, rows);
	});
}

function getDefaultEvent(callback) {
	var selectDefaultEvent = "SELECT id, name, DATE_FORMAT(event_date, '%W, %M %D, %Y') AS eventDate FROM events WHERE event_date>CURDATE() LIMIT 1;";
	con.query(selectDefaultEvent, function(err, rows, fields) {
		if (err) {
			callback(false);
		}
		callback(true, rows);
	});
}

function getBeersForEvent(eventId, callback) {
	var selectBeersForEvent = "SELECT br.logo_url AS logoUrl, br.name AS breweryName, e.name AS eventName,DATE_FORMAT(e.event_date, '%W, %M %D, %Y') AS eventDate, b.id as beerID, b.name AS beerName, b.type AS beerType, b.ABV AS beerABV, b.IBU AS beerIBU, b.description AS beerDescription " +
	"FROM events e INNER JOIN features ON event_id=e.id INNER JOIN beers b ON beer_id=b.id  INNER JOIN breweries br ON br.id=b.brewery_id WHERE e.id="+eventId+" ORDER BY b.name ASC;";
	con.query(selectBeersForEvent, function(err, rows, fields) {
		if (err) {
			callback(false);
		}
		callback(true, rows);
	});
}

app.get('/startUp/:firstName/:lastName/:facebookCredential', function (req, res) {
	var firstName = req.params.firstName;
	var lastName = req.params.lastName;
	var facebookCredential = req.params.facebookCredential;
	var reqData = [firstName, lastName, facebookCredential];
	addUser(reqData, function(success, rows) {
		var response = {};
		var userId;
		//Get user ID
		getUser([facebookCredential], function(success, rows) {
			if(success) {
				console.log(rows);
				userId = rows[0].id;
				var beers = [];
				var events = [];
				response.user = rows[0];
				getDefaultEvent(function(success, rows) {
					var eventId = rows[0].id;
					response.event = rows[0];
					getBeersForEvent(eventId, function(success, rows) {
						if(success) {
							response.beers = rows;
							getFavoriteBeers(eventId, userId, function(success, rows) {
								response.favoriteBeers = rows;
								response.success = success;
								res.setHeader('Content-Type', 'application/json');
								res.send(JSON.stringify(response));
							});
						}
					});
				});
			}
		});
	});
});

app.get('/removeFavorites/:userId/:beerId', function (req, res) {
	var userId = req.params.userId;
	var beerId = req.params.beerId;
	var reqData = [];
	reqData.push(parseInt(userId));
	reqData.push(parseInt(beerId));
	console.log(reqData);
	removeFavorites(reqData, function(success, rows) {
		var response = {};
		response.success = success;
		res.setHeader('Content-Type', 'application/json');
		res.send(JSON.stringify(response));
	});
});

app.get('/addFavorites/:userIds/:beerIds', function (req, res) {
	var userIds = req.params.userIds;
	var beerIds = req.params.beerIds;
	if(userIds && beerIds) {
		userIds = userIds.split(',');
		beerIds = beerIds.split(',');
	}
	var reqData = [];
	for(var i=0; i<userIds.length; i++) {
		var fav = [];
		fav.push(parseInt(userIds[i]));
		fav.push(parseInt(beerIds[i]));
		reqData.push(fav);
	}
	console.log(reqData);
	setFavorites(reqData, function(success, rows) {
		var response = {};
		response.success = success;
		res.setHeader('Content-Type', 'application/json');
		res.send(JSON.stringify(response));
	});
});

app.get('/getStartupData', function (req, res) {
	var eventId;
	var beers = [];
	var events = [];
	var response = {};
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

app.get('/getDefaultBeers', function (req, res) {
	var eventId;
	var beers = [];
	var response = {};
	getDefaultEvent(function(success, rows) {
		var eventId = rows[0].id;
		getBeersForEvent(eventId, function(success, rows) {
			if(success) {
				response.beers = rows;
				getAllEvents(function(success, rows) {
					response.events = rows;
					res.setHeader('Content-Type', 'application/json');
					res.send(JSON.stringify(response));
				});
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
	var passwords = require('./dbpw.js');
	con = mysql.createConnection({
		host: "localhost",
		user: "root",
		password: passwords.password,
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
