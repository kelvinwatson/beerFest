function init(){
  $('.dropdown-toggle').dropdown();
  /*$("#carousel-example-generic").swiperight(function() {
    $(this).carousel('prev');
  });
  $("#carousel-example-generic").swipeleft(function() {
    $(this).carousel('next');
  });*/
  $(".carousel-inner").swipe( {
      //Generic swipe handler for all directions
      swipeLeft:function(event, direction, distance, duration, fingerCount) {
        $(this).parent().carousel('next');
      },
      swipeRight: function() {
        $(this).parent().carousel('prev');
      },
      //Default is 75px, set to 0 for demo so any distance triggers swipe
      threshold:0,
    });
    $('#carousel-example-generic').carousel({
      interval:false
    });
}


function loadCarousel(loc) {
  console.log('LOAD CAROUSEL');
  window.location.href = loc;
}
