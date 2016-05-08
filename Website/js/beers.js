function createPrettyDate() {
	var today = new Date();
	var yy = today.getFullYear();
    var dd = today.getDate();
    var mm = today.getMonth(); //January is 0!
    var hours = today.getHours();
    var minutes = today.getMinutes();
    var amOrPm = "AM";
    var prettyMonthArr = ["Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"];
    var prettyMonth = prettyMonthArr[mm];

    if(hours >= 12) {
    	amOrPm = "PM";
    }
    if(minutes < 10) {
    	minutes = '0'+minutes;
    }

    if (hours > 12) {
	   hours -= 12;
	} else if (hours === 0) {
	   hours = 12;
	}
    return prettyMonth + ' ' + dd + ', ' + yy + ' ' + hours + ':' + minutes + ' ' + amOrPm;
}

$( document ).ready(function() {
	$.valHooks.textarea = {
	  get: function( elem ) {
	      return elem.value.replace( /\r?\n/g, "%0A" );
	  } };
    console.log( "ready!" );
    //Bind listeners
	$('.favorite-img').click(function(event){
		toggleFavorite($(this).data('favorite'), $(this).data('userid'), $(this).data('beerid'));
	});

	$('.ratingLink').click(function(event){
		$('#my-comment-text').find('br').replaceWith('%0A');
		var currComment = $('#my-comment-text').text();
		if(currComment) {
			currComment = currComment.split('%0A').join('\n');
		} else {
			currComment = "NULL";
		}
		var firstName = $('#submit-comment-btn').data('firstname');
		var date = createPrettyDate();
		currComment = firstName + '%0A' + date + '%0A' + currComment;
		//Add name & date to comment string

		updateRating($(this).data('rating'), $(this).data('userid'), $(this).data('beerid'), currComment, $(this));
	});

	$('#submit-comment-btn').click(function(event){
		var comment = $('#commentInput').val();
		var firstName = $(this).data('firstname');
		var date = createPrettyDate();
		var newComment = firstName + '%0A' + date + '%0A' + comment;
		console.log('comment: '+newComment);
		updateRating($('.ratingOn').data('rating'), $('.ratingOn').data('userid'), $('.ratingOn').data('beerid'), newComment, $('.ratingOn'));
		updateMyComment(comment, firstName, date);
	});

	$('#my-comment-edit').click(function(event){
		$('#my-comment-text').find('br').replaceWith('%0A');
		var currComment = $('#my-comment-text').text();
		currComment = currComment.split('%0A').join('\n');
		$('#commentInput').val(currComment);
		$('#my-comment-parent').hide();
		$('#comment-txt-parent').show();
	});

	$('#submit-feedback-btn').click(function(event){
		var feedback = $('#feedback-text').val();
		var url = "/addFeedback/"+feedback;
		$.ajax({
			method: "GET",
			url: url
		});
		$('#feedback-close-btn').click();
		//TODO: Close the navbar menu.
		//$('#navbar-btn').collapse('hide');
	});

	function updateMyComment(comment, firstName, date) {
		//hide comment input
		$('#comment-txt-parent').hide();
		$('#my-comment-date').text(date);
		comment = comment.split('%0A').join('<br>');
		$('#my-comment-text').html(comment);
		$('#my-comment-parent').show();
	}

	function updateRating(rating, userId, beerId, comment, newRatingEl) {
		if(!comment) {
			comment = "NULL";
		}
		var url = "/addReview/"+userId+"/"+beerId+"/"+rating+"/"+comment;
		$.ajax({
			method: "GET",
			url: url
		});
		//change image
		var oldRatingEl = $('.ratingOn');
		var oldRatingImg = $('.ratingOn').children('img');
		var oldRatingNum = oldRatingEl.data('rating');
		var newRatingImg = newRatingEl.children('img');
		oldRatingEl.removeClass('ratingOn');
		oldRatingImg.attr('src', '/images/rate'+oldRatingNum+'dark.png');
		newRatingImg.attr('src', '/images/rate'+rating+'.png');
		newRatingEl.addClass('ratingOn');
	}

	function toggleFavorite(on, userId, beerId) {
		//make ajax call
		var callback, url;
		if(on) {
			url = '/removeFavorites/'+userId+'/'+beerId;
		} else {
			url = '/addFavorites/'+userId+'/'+beerId;
		}
		$.ajax({
			method: "GET",
			url: url
		});
		//change image
		if(on) {
			var imgEl = $('#img-check-'+beerId);
			imgEl.attr("src","/images/bg_check_off.png");
			imgEl.removeClass('on');
			imgEl.addClass('off');
			var link = $('#img-link-'+beerId);
			link.data('favorite', false);
		} else {
			var imgEl = $('#img-check-'+beerId);
			imgEl.attr("src","/images/bg_check_on.png");
			imgEl.removeClass('off');
			imgEl.addClass('on');
			var link = $('#img-link-'+beerId);
			link.data('favorite', true);

		}
	}
});
