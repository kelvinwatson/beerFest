var express = require('express');
var app = express();
var path = require("path");
var mysql = require("mysql");
var winston = require('winston');
var con;

function addReview(reqData, callback) {
	var addReview = "INSERT INTO reviews (user_id, beer_id, rating, comment) VALUES ?;";
	con.query(addReview, [[reqData]], function(err, results) {
		if(err) {
			callback(false);
		} else {
			callback(true, results);
		}
	});
}

function updateReview(reqData, callback) {
	var updateReview = "UPDATE reviews SET rating=?, comment=? WHERE user_id=? AND beer_id=?;";
	con.query(updateReview, reqData, function(err, results) {
		if(err) {
			callback(false);
		} else {
			callback(true, results);
		}
	});
}

function getAllCommentsForBeers(userId, callback) {
	var selectCommentsForBeers = "SELECT b.id AS beerId, comment " +
	"FROM beers b INNER JOIN reviews re ON re.beer_id=b.id "+
	"INNER JOIN users u ON u.id=re.user_id " +
	"WHERE u.id <> " + userId + " " +
	"ORDER BY b.id ASC;";
	con.query(selectCommentsForBeers, function(err, results) {
		if(err) {
			callback(false);
		} else {
			callback(true, results);
		}
	});
}

function getAllMyCommentsForBeers(userId, callback) {
	var selectCommentsForBeers = "SELECT b.id AS beerId, rating, comment " +
	"FROM beers b INNER JOIN reviews re ON re.beer_id=b.id "+
	"INNER JOIN users u ON u.id=re.user_id " +
	"WHERE u.id = " + userId + " " +
	"ORDER BY b.id ASC;";
	con.query(selectCommentsForBeers, function(err, results) {
		if(err) {
			callback(false);
		} else {
			callback(true, results);
		}
	});
}

function getAverageRatingAllBeers(callback){
	var selectAverageRating = "SELECT b.id AS beerId, AVG(rating) AS averageRating "+
	"FROM beers b INNER JOIN reviews re ON re.beer_id=b.id "+
	"GROUP BY b.id ORDER BY b.id ASC;"; 
	con.query(selectAverageRating, function(err, results) {
		if(err) {
			callback(false);
		} else {
			callback(true, results);
		}
	});
}

function getFavoriteBeers(eventId, userId, callback) {
	var selectBeersForEvent = "SELECT DISTINCT 'true' AS favorited, br.logo_url AS logoUrl, br.name AS breweryName, e.name AS eventName,DATE_FORMAT(e.event_date, '%W, %M %d, %Y') AS eventDate, b.id as beerID, b.name AS beerName, b.type AS beerType, b.ABV AS beerABV, b.IBU AS beerIBU, b.description AS beerDescription " +
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
		if(err) {
			callback(false);
		} else {
			callback(true, results);
		}
	});
}

function getAllEvents(callback) {
	var selectAllEvents = "SELECT e.id, e.name AS eventName, DATE_FORMAT(event_date,'%W, %M %d, %Y') AS eventDate, logo_url AS logoUrl, COUNT(b.id) AS beerCount FROM events e"+
	" LEFT JOIN features f ON f.event_id = e.id" +
	" LEFT JOIN beers b ON b.id = f.beer_id" +
	" WHERE event_date>CURDATE() GROUP BY e.id ORDER BY eventDate ASC;";
	con.query(selectAllEvents, function(err, rows, fields) {
		if (err) {
			callback(false);
		}
		callback(true, rows);
	});
}

function getDefaultEvent(callback) {
	var selectDefaultEvent = "SELECT id, name, DATE_FORMAT(event_date, '%W, %M %d, %Y') AS eventDate FROM events WHERE event_date>CURDATE() LIMIT 1;";
	con.query(selectDefaultEvent, function(err, rows, fields) {
		if (err) {
			callback(false);
		}
		callback(true, rows);
	});
}

function getBeersForEvent(eventId, callback) {
	var selectBeersForEvent = "SELECT br.logo_url AS logoUrl, br.name AS breweryName, e.name AS eventName,DATE_FORMAT(e.event_date, '%W, %M %d, %Y') AS eventDate, b.id as beerID, b.name AS beerName, b.type AS beerType, b.ABV AS beerABV, b.IBU AS beerIBU, b.description AS beerDescription " +
	"FROM events e INNER JOIN features ON event_id=e.id INNER JOIN beers b ON beer_id=b.id  INNER JOIN breweries br ON br.id=b.brewery_id WHERE e.id="+eventId+" ORDER BY b.name ASC;";
	con.query(selectBeersForEvent, function(err, rows, fields) {
		if (err) {
			callback(false);
		}
		callback(true, rows);
	});
}

function matchFavoriteBeers(beers, favorites) {
	for(var i=0; i<favorites.length; i++) {
		for(var x=0; x<beers.length; x++) {
			if(favorites[i].beerID === beers[x].beerID) {
				beers[x].favorited = true;
				favorites[i].favorited = true;
				//merge other factors
				favorites[i].comments = beers[x].comments;
				favorites[i].averageRating = beers[x].averageRating;
				favorites[i].myComment = beers[x].myComment;
				favorites[i].myRating = beers[x].myRating;
			}
		}
	}
	return {beers: beers, favorites: favorites};
}

app.get('/addReview/:userId/:beerId/:rating/:comment', function (req, res) {
	var userId = req.params.userId;
	var beerId = req.params.beerId;
	var rating = req.params.rating;
	var comment = decodeURI(req.params.comment);
	winston.info('addReview called with', {userId: userId, beerId: beerId, rating: rating, comment: comment});
	var reqData = [userId, beerId, rating, comment];
	addReview(reqData, function(success, rows) {
		var response = {};
		if(success) {
			response.success = success;
			winston.info('addReview returning - new review', {success: success});
			res.setHeader('Content-Type', 'application/json');
			res.send(JSON.stringify(response));
			return;
		} else {
			//Try to update instead.
			updateReview([rating, comment, userId, beerId], function(success, rows) {
				response.success = success;
				winston.info('addReview returning - updated review', {success: success});
				res.setHeader('Content-Type', 'application/json');
				res.send(JSON.stringify(response));
				return;
			});
		}
	});
});

app.get('/startUp/:firstName/:lastName/:facebookCredential', function (req, res) {
	var firstName = req.params.firstName;
	var lastName = req.params.lastName;
	var facebookCredential = req.params.facebookCredential;
	var eventId = req.query.eventId;
	winston.info('!!!!!!!!!!!eventId!!!!!!!!!!!!!! '+eventId);
	winston.info('startUp called with params', {firstName: firstName, lastName: lastName, facebookCredential: facebookCredential});
	var reqData = [firstName, lastName, facebookCredential];
	addUser(reqData, function(success, rows) {
		var response = {};
		var userId;
		//Get user ID
		getUser([facebookCredential], function(success, rows) {
			if(success) {
				userId = rows[0].id;
				response.user = rows[0];
				getAllEvents(function(success, rows) {
					if(!eventId) {
						eventId = rows[0].id;
					}
					response.events = rows;
					getBeersForEvent(eventId, function(success, rows) {
						if(success) {
							var beers = rows;
							getFavoriteBeers(eventId, userId, function(success, rows) {
								var favoriteBeers = rows;
								response.beers = beers;
								response.favorites = favoriteBeers;
								response.success = success;
								getAllCommentsForBeers(userId, function(success, rows) {
									var commentsForBeers = rows;//all comments for all beers except my own
									getAverageRatingAllBeers(function(success, rows) {
										var averageRatingsForBeers = rows;
										getAllMyCommentsForBeers(userId, function(success, rows) {
											var myCommentsForBeers = rows;
											//Add details to all beers
											for(var i=0,len=response.beers.length; i<len; i++) {
												response.beers[i].comments = [];
												for(var x=0,lenx=commentsForBeers.length; x<lenx; x++) {
													if(commentsForBeers[x].beerId === response.beers[i].beerID) {
														response.beers[i].comments.push(commentsForBeers[x].comment);
													}
												}
												response.beers[i].averageRating = 0.0;
												for(var x=0,lenx=averageRatingsForBeers.length; x<lenx; x++) {
													if(averageRatingsForBeers[x].beerId === response.beers[i].beerID) {
														response.beers[i].averageRating = averageRatingsForBeers[x].averageRating;
													}
												}
												response.beers[i].myComment = "";
												for(var x=0,lenx=myCommentsForBeers.length; x<lenx; x++) {
													if(myCommentsForBeers[x].beerId === response.beers[i].beerID) {
														response.beers[i].myComment = myCommentsForBeers[x].comment;
														response.beers[i].myRating = myCommentsForBeers[x].rating;
													}
												}
											}
											var beersAndFavBeers = matchFavoriteBeers(beers, favoriteBeers);
											response.beers = beersAndFavBeers.beers;
											response.favorites = beersAndFavBeers.favorites;
											winston.info('startUp returning', {response: response});
											res.setHeader('Content-Type', 'application/json');
											res.send(JSON.stringify(response));
										});
									})
								})
							});
						}
					});
				});
			} else {
				winston.error('getUser database call failed');
			}
		});
	});
});

app.get('/removeFavorites/:userId/:beerId', function (req, res) {
	var userId = req.params.userId;
	var beerId = req.params.beerId;
	winston.info('removeFavorites called with request', {userId: userIds, beerId: beerIds});
	var reqData = [];
	reqData.push(parseInt(userId));
	reqData.push(parseInt(beerId));
	console.log(reqData);
	removeFavorites(reqData, function(success, rows) {
		var response = {};
		response.success = success;
		winston.info('removeFavorites returning', {reqData: reqData, success: success});
		res.setHeader('Content-Type', 'application/json');
		res.send(JSON.stringify(response));
	});
});

app.get('/addFavorites/:userIds/:beerIds', function (req, res) {
	var userIds = req.params.userIds;
	var beerIds = req.params.beerIds;
	winston.info('addFavorites called with request', {userIds: userIds, beerIds: beerIds});
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
	winston.info('addFavorites returning', reqData);
	setFavorites(reqData, function(success, rows) {
		var response = {};
		response.success = success;
		res.setHeader('Content-Type', 'application/json');
		res.send(JSON.stringify(response));
	});
});

app.get('/', function (req, res) {
	winston.info('default endpoint called');
});

var server = app.listen(80, function () {

	var host = server.address().address;
	var port = server.address().port;
	winston.add(winston.transports.File, { filename: 'beerFest.log' });

	winston.info('Beerfest API listening at http://%s:%s', host, port);
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
		winston.info('Database connection established');
	});

});
