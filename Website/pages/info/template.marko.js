function create(__helpers) {
  var str = __helpers.s,
      empty = __helpers.e,
      notEmpty = __helpers.ne;

  return function render(data, out) {
    out.w('<!DOCTYPE html> <html style="height:100%;"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1"><title>Hoppy - Rate Beers at Portland\'s Beer Events!</title><link rel="stylesheet" href="/css/bootstrap.css" type="text/css"><link href="http://fonts.googleapis.com/css?family=Open+Sans" rel="stylesheet" type="text/css"><link href="http://fonts.googleapis.com/css?family=PT+Sans" rel="stylesheet" type="text/css"></head><body id="front_page"><div class="container"> <div class="row"><div style="text-align:center" class="col-xs-12"><img style="display:inline;" class="img-responsive" src="/images/cover.png"></div></div></div><div class="container" style="max-width:35%;"><div class="row"><div class="col-xs-12"><h3 style="padding-top:15px; padding-bottom:15px">Download Hoppy for Android</h3></div> <div class="col-xs-6"> <p>Now you can rate and review beer at beer events in Portland, Oregon on your\nAndroid device! Track and rate your favorite beers\ntoday!</p></div><div style="text-align:center;" class="col-xs-6"><a href="https://play.google.com/store/apps/details?id=com.iamhoppy.hoppy"><img alt="Android app on Google Play" src="https://developer.android.com/images/brand/en_app_rgb_wo_60.png"></a></div></div></div><script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script><script src="js/bootstrap.min.js"></script></body></html>');
  };
}
(module.exports = require("marko").c(__filename)).c(create);