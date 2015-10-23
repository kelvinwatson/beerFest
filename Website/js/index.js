// This is called with the results from from FB.getLoginStatus().
  function statusChangeCallback(response) {
    console.log('statusChangeCallback');
    console.log(response);
    // The response object is returned with a status field that lets the
    // app know the current login status of the person.
    // Full docs on the response object can be found in the documentation
    // for FB.getLoginStatus().
    if (response.status === 'connected') {
      // Logged into your app and Facebook.
      authenticationComplete();
    } else if (response.status === 'not_authorized') {
      // The person is logged into Facebook, but not your app.
      console.log('not_authorized');
    } else {
      // The person is not logged into Facebook, so we're not sure if
      // they are logged into this app or not.
      console.log('unknown');
    }
  }

  // This function is called when someone finishes with the Login
  // Button.  See the onlogin handler attached to it in the sample
  // code below.
  function checkLoginState() {
    FB.getLoginStatus(function(response) {
      statusChangeCallback(response);
    });
  }

  // Now that we've initialized the JavaScript SDK, we call 
  // FB.getLoginStatus().  This function gets the state of the
  // person visiting this page and can return one of three states to
  // the callback you provide.  They can be:
  //
  // 1. Logged into your app ('connected')
  // 2. Logged into Facebook, but not your app ('not_authorized')
  // 3. Not logged into Facebook and can't tell if they are logged into
  //    your app or not.
  //
  // These three cases are handled in the callback function.

  // Load the SDK asynchronously
  (function(d, s, id) {
    var js, fjs = d.getElementsByTagName(s)[0];
    if (d.getElementById(id)) return;
    js = d.createElement(s); js.id = id;
    js.src="http://connect.facebook.net/en_US/all.js"
    fjs.parentNode.insertBefore(js, fjs);
  }(document, 'script', 'facebook-jssdk'));

  // Here we run a very simple test of the Graph API after login is
  // successful.  See statusChangeCallback() for when this call is made.
  function authenticationComplete() {
    console.log('Welcome!  Fetching your information.... ');
    FB.api('/me', function(loginResponse) {
      console.log('Successful login for: ' + loginResponse.name);
      console.log('');
      if(loginResponse.name) {
        var fullName = loginResponse.name.split(' ');
        var firstName = fullName[0];
        var lastName = fullName[fullName.length-1];
        if(!firstName) {
          firstName = 'Hoppy';
        }
        if(!lastName) {
          lastName = 'User';
        }
        console.log('Redirecting to beers for user ' + firstName + ' ' + lastName);
        window.location.href='/beers/'+firstName+'/'+lastName+'/'+loginResponse.id;
      }
    });
  }

  window.fbAsyncInit = function() {
  FB.init({
    appId      : '866256996757059',
    cookie     : false,  // enable cookies to allow the server to access 
                        // the session
    xfbml      : true,  // parse social plugins on this page
    version    : 'v2.5' // use version 2.2
  });
  FB.getLoginStatus(function(response) {
    statusChangeCallback(response);
  });
  FB.Event.subscribe('auth.login', function(response) {
    statusChangeCallback(response);
  });
};

  