function create(__helpers) {
  var str = __helpers.s,
      empty = __helpers.e,
      notEmpty = __helpers.ne,
      escapeXmlAttr = __helpers.xa,
      attr = __helpers.a,
      escapeXml = __helpers.x,
      forEachWithStatusVar = __helpers.fv,
      forEach = __helpers.f;

  return function render(data, out) {
    out.w('<!DOCTYPE html> <html><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1"><title>Hoppy - Rate Beers at Portland\'s Beer Events!</title><link rel="stylesheet" href="/css/bootstrap.css" type="text/css"><link href="http://fonts.googleapis.com/css?family=Open+Sans" rel="stylesheet" type="text/css"><link href="http://fonts.googleapis.com/css?family=PT+Sans" rel="stylesheet" type="text/css"></head><body><nav style="border-bottom: white;" class="navbar navbar-inverse navbar-fixed-top"><div class="container"><div style="background-color:orange;" class="navbar-header"><button id="navbar-btn" style="border-color:white" type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse"><span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span></button><a style="height: 2em; padding: 0px; position: absolute;left:50%;margin-left:-25px;" class="navbar-brand" href="/beers/' +
      escapeXmlAttr(data.user.first_name) +
      '/' +
      escapeXmlAttr(data.user.last_name) +
      '/' +
      escapeXmlAttr(data.user.facebook_credential) +
      '"><img style="width:50px;height:50px" class="img-responsive" src="/images/logo2.png"></a></div><div style="background-color:orange; border-color:white" class="navbar-collapse collapse"><ul class="nav navbar-nav"><li><a style="color:white" href="https://play.google.com/store/apps/details?id=com.iamhoppy.hoppy">Hoppy for Android</a></li></ul></div></div></nav><div id="feedbackModal" class="modal fade hide active" role="dialog"><div class="modal-dialog"><div class="modal-content"><div class="modal-header"><button type="button" class="close" data-dismiss="modal">&times;</button><h4 class="modal-title">Tell us what you think</h4></div><div class="modal-body"> <div class="form-group"><textarea id="feedback-text" class="form-control" rows="5"></textarea></div> </div><div class="modal-footer"><button id="submit-feedback-btn"' +
      attr("data-userid", data.user.id) +
      ' style="background-color:orange;color:white;" type="submit" class="btn btn-default">Submit</button> <button id="feedback-close-btn" type="button" class="btn btn-default" data-dismiss="modal">Close</button></div></div></div></div><div class="container"><div class="row"><div class="col-xs-5"><img class="img-responsive" style="height:110px;width:110px;"' +
      attr("src", data.logoUrl) +
      '></div><div class="col-xs-7"><h4>' +
      escapeXml(data.beerName) +
      '</h4>' +
      escapeXml(data.breweryName));

    if (notEmpty(data.beerABV)) {
      out.w('<br>' +
        escapeXml(notEmpty(data.beerABV)?('ABV '+data.beerABV):'') +
        ' ' +
        escapeXml(notEmpty(data.beerIBU)?(' IBU '+data.beerIBU):''));
    }

    out.w('</div></div><div class="row"><div style="text-align:center" class="col-xs-12">. . .</div><div style="text-align:center" class="col-xs-6">');

    if (data.favorited) {
      out.w('<a style="display: inline-block" id="img-link-' +
        escapeXmlAttr(data.beerID) +
        '" href="javascript:void(0);" class="favorite-img" data-favorite="true"' +
        attr("data-beerid", data.beerID) +
        attr("data-userid", data.user.id) +
        '><img id="img-check-' +
        escapeXmlAttr(data.beerID) +
        '" style="height:50px;width:50px;" class="img-responsive on" src="/images/bg_check_on.png"></a>');
    }
    else {
      out.w('<a style="display: inline-block" id="img-link-' +
        escapeXmlAttr(data.beerID) +
        '" href="javascript:void(0);" class="favorite-img" data-favorite="false"' +
        attr("data-beerid", data.beerID) +
        attr("data-userid", data.user.id) +
        '><img id="img-check-' +
        escapeXmlAttr(data.beerID) +
        '" style="height:50px;width:50px;" class="img-responsive off" src="/images/bg_check_off.png"></a>');
    }

    out.w('</div><div class="col-xs-6" style="text-align:center;"><h5>Average Rating</h5><span style="color:orange;font-size:1.3em;font-weight:bold;">' +
      escapeXml((empty(data.averageRating) || data.averageRating==0)?'3':(data.averageRating)) +
      '</span></div><div style="text-align:center" class="col-xs-12">. . .</div></div><div class="row"><div class="col-xs-12"><p>' +
      escapeXml(data.beerDescription) +
      '</p></div></div><div class="row"><div class="col-xs-12"><h4 style="border-bottom:1px solid orange">How was it?</h4> </div></div><div style="text-align:center" class="row"><div class="col-xs-1"></div><a class="ratingLink col-xs-2' +
      escapeXmlAttr(data.myRating==1?' ratingOn':'') +
      '" data-rating="1"' +
      attr("data-beerid", data.beerID) +
      attr("data-userid", data.user.id) +
      '><img style="width:100%"' +
      attr("src", data.myRating==1?'/images/rate1.png':'/images/rate1dark.png') +
      '><br>1</a><a class="ratingLink col-xs-2' +
      escapeXmlAttr(data.myRating==2?' ratingOn':'') +
      '" data-rating="2"' +
      attr("data-beerid", data.beerID) +
      attr("data-userid", data.user.id) +
      '><img style="width:100%"' +
      attr("src", data.myRating==2?'/images/rate2.png':'/images/rate2dark.png') +
      '><br>2</a><a class="ratingLink col-xs-2' +
      escapeXmlAttr((empty(data.myRating) || data.myRating==3)?' ratingOn':'') +
      '" data-rating="3"' +
      attr("data-beerid", data.beerID) +
      attr("data-userid", data.user.id) +
      '><img style="width:100%"' +
      attr("src", (empty(data.myRating) || data.myRating==3)?'/images/rate3.png':'/images/rate3dark.png') +
      '><br>3</a><a class="ratingLink col-xs-2' +
      escapeXmlAttr(data.myRating==4?' ratingOn':'') +
      '" data-rating="4"' +
      attr("data-beerid", data.beerID) +
      attr("data-userid", data.user.id) +
      '><img style="width:100%"' +
      attr("src", data.myRating==4?'/images/rate4.png':'/images/rate4dark.png') +
      '><br>4</a><a class="ratingLink col-xs-2' +
      escapeXmlAttr(data.myRating==5?' ratingOn':'') +
      '" data-rating="5"' +
      attr("data-beerid", data.beerID) +
      attr("data-userid", data.user.id) +
      '><img style="width:100%"' +
      attr("src", data.myRating==5?'/images/rate5.png':'/images/rate5dark.png') +
      '><br>5</a><div class="col-xs-1"></div></div><div class="row"><div class="col-xs-12"><h4 style="border-bottom:1px solid orange">What did you think?</h4> </div></div><div class="row"><div id="comment-txt-parent" class="col-xs-12"' +
      attr("style", (data.myComment && data.myComment != 'NULL')?'display:none':'') +
      '><div class="form-group"><label for="comment">Comment:</label><textarea class="form-control" rows="5" id="commentInput"></textarea></div> <button id="submit-comment-btn"' +
      attr("data-firstname", data.user.first_name) +
      ' style="background-color:orange;color:white;" type="submit" class="btn btn-default">Submit</button> </div><div id="my-comment-parent"' +
      attr("style", (data.myComment && data.myComment != 'NULL')?'':'display:none;') +
      '><div class="col-xs-7"><h6 style="font-weight:bold;color:orange">My Comment:</h6> </div><div style="text-align:right" class="col-xs-5"><h6 id="my-comment-date">');

    if (notEmpty(data.myComment)) {
      out.w(escapeXml(data.myComment.split('\n')[1]));
    }

    out.w('</h6> </div><div class="col-xs-12"><p id="my-comment-text">');

    if (notEmpty(data.myComment)) {
      var commentArray = data.myComment.split('\n');

      forEachWithStatusVar(commentArray, function(stringPart,loop) {
        if (loop.getIndex() > 1) {
          out.w(str(data.myComment.split('\n')[loop.getIndex()]));

          if (loop.getIndex() < commentArray.length - 1) {
            out.w('<br>');
          }
        }
      });
    }

    out.w('</p> </div> <div class="col-xs-12"><a id="my-comment-edit">Edit Comment</a> </div></div></div><div class="row"><div class="col-xs-12"><h4 style="border-bottom:1px solid orange">See what others think</h4> </div></div>');

    if (notEmpty(data.comments)) {
      forEach(data.comments, function(comment) {
        out.w('<div class="row"><div class="col-xs-7"><h6 style="font-weight:bold;color:orange">' +
          escapeXml(comment.split('\n')[0]) +
          '</h6> </div><div style="text-align:right" class="col-xs-5"><h6>' +
          escapeXml(comment.split('\n')[1]) +
          '</h6> </div><div class="col-xs-12"><p>' +
          str(comment.split('\n')[2]) +
          '</p> </div></div>');
      });
    }
    else {
      out.w('<div class="row"><div class="col-xs-12"><h6 style="font-weight:bold;color:orange">No Comments Yet!</h6> </div><div class="col-xs-12"><p>Be the first!</p> </div></div>');
    }

    out.w('</div><script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js" type="text/javascript">\n</script><script src="/js/bootstrap.min.js" type="text/javascript">\n</script><script type="text/javascript">\n        $(document).ready(function () {\n                        $(\'.dropdown-toggle\').dropdown();\n                });\n    </script><script type="text/javascript" src="/js/beers.js"></script> </body></html>');
  };
}
(module.exports = require("marko").c(__filename)).c(create);