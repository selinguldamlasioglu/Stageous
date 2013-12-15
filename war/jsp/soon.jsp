<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="my" uri="/WEB-INF/taglib.tld" %>

<html xmlns="http://www.w3.org/1999/xhtml"
	  xmlns:my="/WEB-INF/taglib.tld"
	  xmlns:c="http://java.sun.com/jsp/jstl/core">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Stageous: Get Your Events Online</title>

<style type="text/css">
@import url(http://fonts.googleapis.com/css?family=Open+Sans|Open+Sans+Condensed:300|Lobster);
	@font-face
	{
	font-family: Frutiger Linotype;
	src: url("fonts/frutiger.ttf");
		/* url('Sansation_Light.eot'); /* IE9 */
	}
	
	@font-face
	{
	font-family: Bebas Neue;
	src: url('fonts/BebasNeue-webfont.eot');
    src: url('fonts/BebasNeue-webfont.eot?#iefix') format('embedded-opentype'),
         url('fonts/BebasNeue-webfont.woff') format('woff'),
         url('fonts/BebasNeue-webfont.ttf') format('truetype'),
         url('fonts/BebasNeue-webfont.svg#BebasNeueRegular') format('svg');
    font-weight: normal;
    font-style: normal;
	}
	
	@font-face
	{
	font-family: 'Open Sans';
	src: url("fonts/TrebuchetMS.ttf");
	}
	
/*	@font-face { 
		font-family: 'Museo Sans'; 
		src: url("fonts/MuseoSans.otf"); 
	}
	@font-face { font-family: 'font-face chunkfive'; src: url("fonts/Chunkfive.otf"); }
	@font-face { font-family: 'chunkfive'; src: url("fonts/Chunkfive.otf"); }
*/
	@font-face { font-family: 'font-face chunkfive'; src: url(../fonts/Chunkfive.otf); }
	@font-face { font-family: 'font-face museo sans'; src: url(../fonts/MuseoSans.otf); }
	
	html,body {min-height:100%;}
/*	body{
		font-size:14px;
		font-family:"UniversLTStd47LightCondensed", Helvetica, Arial, sans-serif;
		color:#66615e;
		background: url("http://static.tumblr.com/tokpufl/ZuJlt3upi/tile.png") repeat scroll 0 0 transparent;
		line-height:20px;
		padding:0;
		text-align:center;
		text-transform:uppercase;
	}
*/	
	body {
		font-family: Frutiger Linotype, Bebas Neue, 'Open Sans', sans-serif;
		font-size: 13px;
		margin: 0;
		padding: 0;
		color:#111;
		background: #222 url("http://static.tumblr.com/tokpufl/ZuJlt3upi/tile.png") repeat scroll 0 0 transparent;
		height: 100%;
		word-wrap: break-word;
	}
	
	.layout-bg {
		background: transparent url("http://static.tumblr.com/tokpufl/pJ0lt3usx/layout.png") repeat-y scroll 50% -155px;
		overflow:hidden;
		margin:0 auto 0;
	}
	
	.layout {
		width:940px;
/*		padding:45px 0px;*/
		margin:0 auto 0;
		text-align:left;
		position:relative;
	}
	.layout-veneer {
		height:45px;
		left:0;
		bottom:0;
		width:100%;
		position:absolute;
	}
	
	@media screen and (max-device-width: 480px) { 
		.layout-decor-top-center,.layout-decor-bottom-center {
			width:922px;
			left:-1px;
			top:0px;
		}
		.layout-decor-top-center {
			height:12px;
			background-color:#fff;
		}
		.layout-content {
			border-bottom:1px solid #fff;
		}
	}
	
	::-webkit-scrollbar-thumb:vertical {
		height: 100px;
		background: #222;
	}
	::-webkit-scrollbar {
		width: 10px;
		height: 10px;
		background: #fafafa;
	}
	
/* ::-webkit-scrollbar { width: 12px; } /* Track */ 							/*::-webkit-scrollbar-track { -webkit-box-shadow: inset 0 0 6px rgba(0,0,0,0.3); -webkit-border-radius: 10px; border-radius: 10px; } /* Handle */ 
/*::-webkit-scrollbar-thumb { -webkit-border-radius: 10px; border-radius: 10px; background: rgba(255,0,0,0.8); -webkit-box-shadow: inset 0 0 6px rgba(0,0,0,0.5); } ::-webkit-scrollbar-thumb:window-inactive { background: rgba(255,0,0,0.4); } 
*/	
	/* ~~ Element/tag selectors ~~ */
	ol, dl { 
		list-style-type:none;
		padding: 0;
		margin: 0;
	}
	h1, h2, h3, h4, h5, h6, p {
		margin-top: 0;	
		padding-right: 15px;
		padding-left: 15px;
	}
	
	h1 {
		font-family: Frutiger Linotype, sans-serif;
		font-size:20px;
	}
	
	h2 {
		font-family: Frutiger Linotype, sans-serif;
		font-size:18px;
		padding-top:14px;
		text-decoration:underline;
		font-weight:normal;
	}
	
	h3 {
		font-family: Frutiger Linotype, sans-serif;
		font-size:14px;
		text-decoration:underline;
		font-weight:normal;
	}
	
	h4 { 
		font-family: Bebas Neue, sans-serif;
		font-size:18px;
		color:black;
		text-decoration:underline;
		font-weight:normal;
	}
	
	h5{
		font-family: 'Bubblegum Sans';
		font-size:18px;
		font-weight:normal;
	}	
	
	h6 {
		margin-top:20px;
		padding: 0px 10px;
		display: block;
		color:#292929;
	/* 	text-align: center; */
		font-weight:normal;
		letter-spacing: -1px;
		font-size: 30pt;
		font-family: "chunkfive", "font-face chunkfive", "museo slab", "romeral", sans-serif;
		text-shadow: 0px 2px 2px #EFEBE7, 0px 4px 2px #CFC8C2;  
		/* font-family: Bebas Neue, sans-serif;
		font-size:50px;
		color:black;
		font-weight:normal;
		padding-bottom:0px; */
	}
	
	hr
	{
		width:90%;
	}

	h7 {
		margin-top: -80px;
		padding: 10px 10px;
		display: block;
		color:#292929;
	/* 	text-align: center; */
		font-weight:normal;
		letter-spacing: -1px;
		font-size: 20pt;
		font-family: "chunkfive", "font-face chunkfive", "museo slab", "romeral", sans-serif;
		text-shadow: 0px 2px 2px #EFEBE7, 0px 4px 2px #CFC8C2;  
		/* font-family: Bebas Neue, sans-serif;
		font-size:50px;
		color:black;
		font-weight:normal;
		padding-bottom:0px; */
	}
	
	a img { /* this selector removes the default blue border displayed in some browsers around an image when it is surrounded by a link */
		border: none;
	}
	
	/* ~~ Styling for your site's links must remain in this order - including the group of selectors that create the hover effect. ~~ */
	a:link {
		color:#414958;
		 /* unless you style your links to look extremely unique, it's best to provide underlines for quick visual identification */
	}
	a:visited {
		color: #4E5869;
	}
	a:hover, a:active, a:focus { /* this group of selectors will give a keyboard navigator the same hover experience as the person using a mouse. */
		text-decoration: underline;
	}
	
	/* ~~ this container surrounds all other divs giving them their percentage-based width ~~ */
	.container {
		width: 1100px;
		background-image: url("images/containbg.gif");
		background-repeat: repeat;
		margin: 0 auto; /* the auto value on the sides, coupled with the width, centers the layout. It is not needed if you set the .container's width to 100%. */
		align:center;
		position:relative;
		overflow:hidden;
	}
	
	/* ~~ the header is not given a width. It will extend the full width of your layout. It contains an image placeholder that should be replaced with your own linked logo ~~ */
	.header {
		background-color: #292929;
		height: 50px;
		margin: 0 auto;	
	}
	
	.header ul, .header ol { 
		list-style-type:none;
		padding: 0;
		margin: 0;
	}
	
	ul.nav {
		display:block;
		width: 100%;
		height:50px;
		list-style:none;
		position:absolute;
		right: 0px;
		top: 0px;
	}
	
	ul.nav li, .nav li a, .nav li a span{
		height:50px;
		display:inline;
	}
	
	ul.nav li a{
		position:relative;
		text-align:center;
		text-indent:-9999px;
		overflow:hidden;
	/*    text-indent:100%;*/
		white-space:nowrap;
	}
	
	ul.nav li a span{
		top:0;
		left:0;
		/*background:url('images/BG-global-nav.jpg') no-repeat left top; */
	}
	
	ul.nav li a#profile, ul.nav li a#profile span{
		float:right;
		width:50px;
		height:50px;
		background:url("images/profile.png") no-repeat left top;
	}
	
	ul.nav li a#messages, ul.nav li a#messages span{
		float:right;
		width:50px;
		height:50px;
		background:url("images/messages.png") no-repeat left top;
	}
	
	ul.nav li a#setting, ul.nav li a#setting span{
		float:right;
		width:50px;
		height:50px;
		background:url("images/setting.png") no-repeat left top;
	}
	
	ul.nav li a#logout, ul.nav li a#logout span{
		float:right;
		width:50px;
		height:50px;
		background:url("images/logout.png") no-repeat left top;
	}
	
	ul.nav li a#home, ul.nav li a#home span{
		position:absolute;
		width:50px;
		height:50px;
		left:160px;
		background:url("images/home.png") no-repeat left top;
	}
	
	ul.nav li a#logo, ul.nav li a#logo span{
		position:absolute;
		width:150px;
		height:50px;
		left:10px;
		background:url("images/logo.png") no-repeat left top;
	}

	.leftsidebar {
		font-family: 'Open Sans', sans-serif;
		float: left;
		width: 20%;
		height: 100%;
		min-height: 600px;
		padding: 0 auto;
		margin-top:20px;
		position: relative;
		overlow:hidden;
		
	}
	
	.content {
		font-family: Frutiger Linotype, sans-serif;
		padding: 20px 0;
		width: 60%;
		float: left;
		position:relative;
		
	}
	
	.rightsidebar {
		font-family: 'Open Sans', sans-serif;
		float: left;
		width: 20%;
		height: 100%;
		min-height: 600px;
		padding: 20px 0;
		position: relative fixed;
		overlow:hidden;
	}
	
	.sooncontainer {
		
		background: url("http://static.tumblr.com/tokpufl/ZuJlt3upi/tile.png") repeat scroll 0 0 transparent;
		margin: 0 auto; /* the auto value on the sides, coupled with the width, centers the layout. It is not needed if you set the .container's width to 100%. */
		align:center;
		position:relative;
		overflow:hidden;
		
	}

</style>

<link type="text/css" rel="stylesheet" rel="Stylesheet" href="/css/loginbox.css" />
<link href='http://fonts.googleapis.com/css?family=Bubblegum+Sans' rel='stylesheet' type='text/css'>
</head>

<body>
  
<div class="sooncontainer">
	<div class="header"> 
       <ul class="nav" >
       	<li><a href="#" id="home" >stageous</a></li>
        <li><a href="#" id="logo" >stageous</a></li>

       </ul>
  </div><!-- end .header -->
	<section id="loginbox" style="margin-top:50px; width:700px; margin-bottom:100px;">
		<form action="soon" method="POST">
			<h1>Staff</h1>
            <br /><br />
            <h6>Stageous Alpha</h6><h7> is sadly over! <br />But wait, we're just starting. <br />Sign up to our mailing list for early access to </h7><h6>Stageous Beta!</h6>
            <hr  />
            <br /><br />
            <h1>Mailing List</h1>
            <c:if test = "${signedup eq 'true'}">
            	<div class="login-error"><p style="margin-bottom:120px;">Thanks for signing up! You will hear from us again until Fall 2013.</p></div>
			</c:if>
			<c:if test = "${empty signedup}">
				<div style="width:80%; padding: 0 0 0 10%  ;">
					<input type="text" placeholder="E-mail" required id="username" name="email" />
				</div>
				<div style="width:75%; padding: 0 0 20% 12%  ;">
					<input type="submit" value="Submit" />
					<a href="http://www.youtube.com/watch?v=aFmjRa9uhv4" target="_blank">Missed the alpha?</a>
	                <a href="mailto:staff@stageous.com?subject=Contact%20Stageous" target="_blank">Contact us</a>
				</div>
			</c:if>
		</form><!-- form -->
	</section>

</div> <!-- end .content -->
</body>
</html>