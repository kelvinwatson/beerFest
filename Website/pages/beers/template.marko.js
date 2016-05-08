function create(__helpers) {
  var str = __helpers.s,
      empty = __helpers.e,
      notEmpty = __helpers.ne,
      attr = __helpers.a,
      escapeXml = __helpers.x,
      forEach = __helpers.f,
      escapeXmlAttr = __helpers.xa;

  return function render(data, out) {
    out.w('<!DOCTYPE html> <html><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1"><title>Hoppy - Rate Beers at Portland\'s Beer Events!</title><link rel="stylesheet" href="/css/bootstrap.css"><link href="http://fonts.googleapis.com/css?family=Open+Sans" rel="stylesheet" type="text/css"><link href="http://fonts.googleapis.com/css?family=PT+Sans" rel="stylesheet" type="text/css"></head><body style="padding-top: 50px;"><nav style="border-bottom: white;" class="navbar navbar-inverse navbar-fixed-top"><div class="container"><div style="background-color:orange;" class="navbar-header"><button style="border-color:white" type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse"><span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span></button><a style="height: 2em; padding: 0px; position: absolute;left:50%;margin-left:-25px;" class="navbar-brand" href="#"><img style="width:50px;height:50px" class="img-responsive" src="/images/logo2.png"></a></div><div style="background-color:orange; border-color:white" class="navbar-collapse collapse"><ul class="nav navbar-nav"><li><a style="color:white" href="https://play.google.com/store/apps/details?id=com.iamhoppy.hoppy">Hoppy for Android</a></li> </ul></div></div></nav><div style="background-image:url(/images/winterale.GIF);background-repeat:no-repeat;"><div class="dropdown list-group" style="margin-bottom:0px;"><a style="width:100%; color:#000" class="dropdown-toggle" data-toggle="dropdown" href="#"><div class="container" style="text-align:center;"><div class="row" style="text-align:left;margin-bottom:-10px;"><div style="padding-left:10px;vertical-align: middle;padding-right:10px;float: none;display: inline-block;text-align:center;" class="col-xs-3;padding-top:5px;"><img style="max-width:85%;"' +
      attr("src", data.events[0].logoUrl) +
      '></div><div style="color:white;vertical-align: middle;display: inline-block;float: none; width:50%" class="col-xs-9"><h4 class="list-group-item-heading">' +
      escapeXml(data.events[0].eventName) +
      '</h4>' +
      escapeXml(data.events[0].eventDate) +
      '<br></div></div><span style="color:white;" class="glyphicon glyphicon-chevron-down"></span></div></a>');

    if (data.events.length > 1) {
      out.w('<ul style="width:100%" class="dropdown-menu">');

      forEach(events, function(event) {
        out.w('<li data-toggle="collapse" data-target=".nav-collapse"><a href="#" class="list-group-item"><div class="container"><div class="row"><div style="padding-left:10px;padding-right:10px;" class="col-xs-3"></div><div style class="col-xs-7"><h4 class="list-group-item-heading">' +
          escapeXml(event.eventName) +
          '</h4>' +
          escapeXml(event.eventDate) +
          '</div></div></div></a> </li>');
      });

      out.w('</ul>');
    }
    else {
      out.w('<ul style="width:100%; background-color: orange;" class="dropdown-menu"><li data-toggle="collapse" data-target=".nav-collapse" style="background-color:orange;"><a href="#" class="list-group-item" style="border-color:orange; background-color:orange;"><div class="container"><div class="row"><div style class="col-xs-12"><h4 style="color:white;" class="list-group-item-heading">Stay tuned for upcoming events!</h4></div></div></div></a> </li></ul>');
    }

    out.w('</div></div><div class="container"> <div class="row"><div class="col-md-12"><div class="list-group">');

    if (notEmpty(data.beers)) {
      forEach(data.beers, function(beer) {
        out.w('<div class="list-group-item"><div class="container"><div class="row"><a href="/beer/' +
          escapeXmlAttr(data.user.first_name) +
          '/' +
          escapeXmlAttr(data.user.last_name) +
          '/' +
          escapeXmlAttr(data.user.facebook_credential) +
          '/' +
          escapeXmlAttr(beer.beerID) +
          '"><div style="padding-left:10px;padding-right:10px" class="col-xs-3"><img style="height:80px;width:80px;"' +
          attr("src", beer.logoUrl) +
          '></div><div style class="col-xs-7"><h4 class="list-group-item-heading">' +
          escapeXml(beer.beerName) +
          '</h4><span style="color:black;">' +
          escapeXml(beer.breweryName) +
          '<br>' +
          escapeXml(beer.beerType) +
          '<br>');

        if (notEmpty(beer.beerABV)) {
          out.w('ABV ' +
            escapeXml(beer.beerABV) +
            ', ');
        }

        if (notEmpty(beer.beerIBU)) {
          out.w('IBU ' +
            escapeXml(beer.beerIBU));
        }

        out.w('</span></div></a><div style="padding-left:5px; text-align:center" class="col-xs-2"><span style="color:orange; font-size:1.5em;">' +
          escapeXml(beer.averageRating?beer.averageRating:'3') +
          '</span><br>');

        if (beer.favorited) {
          out.w('<a id="img-link-' +
            escapeXmlAttr(beer.beerID) +
            '" href="javascript:void(0);" class="favorite-img" data-favorite="true"' +
            attr("data-beerid", beer.beerID) +
            attr("data-userid", data.user.id) +
            '><img id="img-check-' +
            escapeXmlAttr(beer.beerID) +
            '" style="height:50px;width:50px;" class="img-responsive on" src="/images/bg_check_on.png"></a>');
        }
        else {
          out.w('<a id="img-link-' +
            escapeXmlAttr(beer.beerID) +
            '" href="javascript:void(0);" class="favorite-img" data-favorite="false"' +
            attr("data-beerid", beer.beerID) +
            attr("data-userid", data.user.id) +
            '><img id="img-check-' +
            escapeXmlAttr(beer.beerID) +
            '" style="height:50px;width:50px;" class="img-responsive off" src="/images/bg_check_off.png"></a>');
        }

        out.w('</div></div></div></div>');
      });
    }
    else {
      out.w('<div class="list-group-item"><div class="container"><div class="row"><a href="/beers/' +
        escapeXmlAttr(data.user.first_name) +
        '/' +
        escapeXmlAttr(data.user.last_name) +
        '/' +
        escapeXmlAttr(data.user.facebook_credential) +
        '"><div style="padding-left:10px;padding-right:10px" class="col-xs-3"><img style="height:80px;width:80px;" src="/images/logo3.png"></div><div style class="col-xs-9"><h4 class="list-group-item-heading">Your bucket list is empty</h4><span style="color:black;">Add some!</span></div></a></div></div></div>');
    }

    out.w('</div></div></div></div><footer><div style="background-color: orange; border-color: transparent;" class="navbar navbar-inverse navbar-fixed-bottom"><div class="container"><div class="row" style="line-height:50px"><a href="/beers/' +
      escapeXmlAttr(data.user.first_name) +
      '/' +
      escapeXmlAttr(data.user.last_name) +
      '/' +
      escapeXmlAttr(data.user.facebook_credential) +
      '" style="color:white; text-align:center; border-right: 1px solid white" class="col-xs-6">All Beers</a><a href="/favorites/' +
      escapeXmlAttr(data.user.first_name) +
      '/' +
      escapeXmlAttr(data.user.last_name) +
      '/' +
      escapeXmlAttr(data.user.facebook_credential) +
      '" style="color:white; text-align:center" class="col-xs-6">My Bucket List</a></div></div> </div></footer><script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script><script src="/js/bootstrap.min.js"></script><script>\n\t\t$(document).ready(function () {\n\t\t\t$(\'.dropdown-toggle\').dropdown();\n\t\t});\n\t</script> <script type="text/javascript" src="/js/beers.js"></script> </body></html>');
  };
}
(module.exports = require("marko").c(__filename)).c(create);