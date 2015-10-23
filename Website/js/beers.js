$( document ).ready(function() {
    console.log( "ready!" );
    //Bind listeners
	$('.favorite-img').click(function(event){
		debugger;
		toggleFavorite($(this).data('favorite'), $(this).data('userid'), $(this).data('beerid'));
	});

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
