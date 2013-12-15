<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Stageous: Get Your Events Online</title>

<link type="text/css" rel="stylesheet" media="screen" href="../style.css" />
<link type="text/css" rel="stylesheet" rel="Stylesheet" href="../css/loginbox.css" />
<link href='http://fonts.googleapis.com/css?family=Bubblegum+Sans' rel='stylesheet' type='text/css'>
<script type="text/javascript">
/* window.onload(function() {
	var link = alert(document.URL);
	boolean error = alert(link.indexOf("loginfailed") !== -1);
	if(error || {$login_error})
	if({$login_error}=='true')
	{
		$('body').append('<h1>FUUUUUUUUUCK</h1>');
		$('.login-error').append('<p style="color:#c33">Oops... Given username and password don\'t match.</p>').append(' ');
	} 
}); */
</script>
</head>

<body>

<div class="containerBig">
  <div class="header"> 
       <ul class="nav" >
       	<li><a href="#" id="home" >stageous</a></li>
        <li><a href="#" id="logo" >stageous</a></li>

       </ul>
  </div><!-- end .header -->
  
<div class="content">
	<div class="welcome">
    	<h6>WELCOME</h6>
    </div>
   <div class="intro">
     <p>Meet Stageous to have a fast access to a vast Musical Event database and get personalised event recommendations based 
on your musical interests. </p>

<p>
There are several ways you can make use of Stageous:
<br></br>
<br>Search events according to artist, genre, time or location</br>
<br>Share photos, videos and comments about events</br>
<br>Communicate with your friends to see what they've been up to.</br>
<br></br>
<br>Sign up to Stageous and start having fun right now!</br>
</p>
	</div>
</div> <!-- end .content -->
       
   <div class="rightsidebarwide">
   <section id="loginbox">
		<form action="welcome" method="POST">
			<h1>Login Form</h1>
            <div class="login-error"><p style="color:#c33">Oops... Given username and password don't match.</p></div>
			<div>
				<input type="text" placeholder="Username" required id="username" name="username" />
			</div>
			<div>
				<input type="password" placeholder="Password" required id="password" name="password" />
			</div>
			<div>
				<input type="submit" value="Log in" />
				<a href="#">Lost your password?</a>
				<a href="register.html">Register</a>
			</div>
		</form><!-- form -->
	</section>
   </div> <!-- end .rightsidebar -->
   
</div> <!-- end .container -->

</body>
</html>