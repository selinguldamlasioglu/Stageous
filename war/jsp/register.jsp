<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="my" uri="/WEB-INF/taglib.tld" %>

<html xmlns="http://www.w3.org/1999/xhtml"
	  xmlns:my="/WEB-INF/taglib.tld"
	  xmlns:c="http://java.sun.com/jsp/jstl/core">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>Welcome to Stageous! </title>
    
    <link type="text/css" rel="stylesheet" media="screen" href="style.css" />
    
    <link href='http://fonts.googleapis.com/css?family=Bubblegum+Sans' rel='stylesheet' type='text/css'>
    <link rel="stylesheet" href="css/register.css" />
    <link rel="stylesheet" href="accountry/jquery.autocomplete.css" />
    <script src="http://code.jquery.com/jquery-latest.js" type="text/javascript" charset="utf-8"></script>
    <script type="text/javascript" src="js/jquery.tagcloud.js"></script>
    <script type="text/javascript" charset="utf-8">
         $(document).ready(function(){
           $("#tags_left a").tagcloud({
             size: {
               start: parseInt("13"), 
               end: parseInt("28"), 
               unit: "px"
             }, 
             color: {
               start: "#333", 
               end: "#F52"
             }
           })
           return false;
       })
 	</script>
    <script language="javascript">
		$(document).ready(function () {
			$('.ctagleft').click(function() {
				var tag_clone = $(this).clone(true);
				
				if ($(tag_clone).hasClass('ctagleft')) {
					$(tag_clone).removeClass('ctagleft').addClass('ctagright');
					$('#tags_right').append(tag_clone).append(' ');
				}
				else if ($(tag_clone).hasClass('ctagright')) {
					$(tag_clone).removeClass('ctagright').addClass('ctagleft');
					$('#tags_left').append(tag_clone).append(' ');
				}
				$(this).remove();
			});
			
			$('.ctagright').click(function() {
				var tag_clone = $(this).clone(true);
				
				if ($(tag_clone).hasClass('ctagleft')) {
					$(tag_clone).removeClass('ctagleft').addClass('ctagright');
					$('#tags_right').append(tag_clone).append(' ');
				}
				else if ($(tag_clone).hasClass('ctagright')) {
					$(tag_clone).removeClass('ctagright').addClass('ctagleft');
					$('#tags_left').append(tag_clone).append(' ');
				}
				$(this).remove();
			});
			
			$('#submit').click(function() {
				var tags = '';
				$('#tags_right .ctagright').each(function() {
					var tag_id = $(this).attr('tagid');
					tags += tag_id + ',';
				});
				
				if (tags == '') { tags = 'No tag selected yet.'; }
				alert('Selected tags ID are: ' + tags);
			});
		});
	</script>
    
    <script language="javascript">
	    $('#register-submit').click(function() {
	    	var tags = 'hello';
			$('.ctagright').each(function() {
				var tag_id = $(this).text();
				tags += tag_id + ',';
			});
			
			if (tags == '') { tags = 'No tag selected yet.'; }
			
			$('#form-field-tags').prop('value', tags);
			/* alert(tags); */
			document.forms['register-form'].submit();
	    });
	    
	    function doTags() {
	    	var tags = '';
			$('.ctagright').each(function() {
				var tag_id = $(this).text();
				tags += tag_id + ',';
			});
			
			if (tags == '') { tags = 'No tag selected yet.'; }
			
			$('#form-field-tags').attr('value', tags);
			/* alert(tags); */
			document.forms['register-form'].submit();
	    }
    </script>
    
    <script src="js/register.js"></script>
	<script src="js/jquery-color.js"></script>
    <script src="js/raphael.js"></script>
    <script src="accountry/jquery.autocomplete.js"></script>
    <script src="accountry/countries.en.js"></script>
    <script src="http://maps.google.com/maps/api/js?sensor=false"></script>
</head>

<body>

<div class="containerBig">
  <div class="header"> 
       <ul class="nav" >
       	<li><a href="/welcome" id="home" >stageous</a></li>
        <li><a href="/welcome" id="logo" >stageous</a></li>
       </ul>
  </div><!-- end .header -->
   
<div class="content">
	<div id="register">
		<section>
    		<h1 style="text-decoration:none;" >Register</h1>
			<div id="progress"><!-- "Progress" element is not yet recognized by Webkit browsers, div used. -->
				<p class="expletive">Pick your favourites!</p>
			</div>
    	</section>
       
	<div id="tags_left">
    <br />
    
   <%@ page import = "gigs.engine.event.user.*" %>
   <jsp:useBean id="aTag" class="gigs.engine.event.user.Tag" scope="session"/>
   <c:forEach var="aTag" items="${alltags}">
        <a href="javascript:void(0);" class="ctagleft" rel="${aTag.interest}">${aTag.name}</a>
   </c:forEach>
        </div>
        <div id="tagcloud">
            <!-- <form action="" method="post" id="add-tags">
                <fieldset>
                    <label for="form-field-name" style="font-size:14px">Another tag?</label>
                    <input type="text" class="field" name="tag" value="" id="form-field-tag" />
                    <div class="form-error form-error-blank">
                        <p>We couldn't find that tag?</p>
                    </div>
                </fieldset>
            </form> -->
            <br />
            <div id="tags_right" class="seltags">You have selected: </div>
        </div>
   </div>
</div> <!-- end .content -->
       
   <div class="rightsidebarwide">
   <section>
    		<h1><br /></h1>
			<div id="progress"><!-- "Progress" element is not yet recognized by Webkit browsers, div used. -->
				<p class="expletive">Start filling out the form:</p>
			</div>
    	</section>
   		<section id="form-container">
		<form action="/register" method="post" id="register-form">
			<fieldset>
				<input type="hidden" name="tags" value="" id="form-field-tags" />
				<table>
					<tr>
						<th>
							<label for="form-field-name">Name</label>
						</th>
						<td>
							<input type="text" class="field" name="name" value="" id="form-field-name" />
						</td>
						<td class="form-alert">
							<div class="vee-container form-success">
								<div id="circle-1"></div>
							</div>
							<div class="form-error form-error-blank">
								<p>Most people have a name. Do you?</p>
							</div>
						</td>
					</tr>
					
					<tr>
						<th>
							<label for="form-field-email">Email</label>
						</th>
						<td>
							<input type="email" class="field" name="email" value="" id="form-field-email" />
						</td>
						<td class="form-alert">
							<div class="vee-container form-success">
								<div id="circle-2"></div>
							</div>
							<div class="form-error form-error-email">
								<p>Hmm... are you sure that's a valid email?</p>
							</div>
						</td>
					</tr>
					
					<tr>
						<th>
							<label for="form-field-password">Password</label>
						</th>
						<td>
							<input type="password" class="field" name="password" value="" id="form-field-password" />
						</td>
						<td class="form-alert">
							<div class="vee-container form-success">
								<div id="circle-3"></div>
							</div>
							<div class="form-error form-error-password">
								<p>Must be more than 6 characters long</p>
							</div>
						</td>
					</tr>
					
					<tr id="location-wrapper">
							
						<th>
							<span>Location</span>
						</th>
						
						<td colspan="2">
							<input type="hidden" name="location" value="" id="form-field-location" />
							
							<!-- In case Geolocation worked, display question -->
							<section id="geolocation-worked">
								<p id="location-detection">We detected you're from sunny <span class="location-name" id="location-name"></span>, is that right?</p>
								<div id="location-answers-switch">
									<div class="switch">
										<a href="#" title="" class="left-switch" id="switch-yes">Yes</a>
										<a href="#" title="" class="right-switch" id="switch-no">No</a>
									</div>
								</div>
							</section>
							
							<section id="geolocation-fix">
								<!-- In case Geolocation didn't work -->
								<label class="location-type-correction"  for="country-type" id="no-geolocation">We couldn't detect your location.</label>
							
								<!-- In case the user clicked No and in case Geolocation didn't work -->
								<label class="location-type-correction" for="country-type" id="other-location">Do you mind typing in your country's name?</label>
							
								<input type="text" id='country-type' class="field" name='country' value='' />
								<p id="geolocation-fix-approval"></p>
							</section>
															
							<script>
								var geolocation_fix_height = $("#geolocation-fix").height();
								$("#location-wrapper").css('display', 'none');
							</script>
						</td>
					</tr>
					
					<tr id="submit-wrapper">
						<td colspan="3">
							<script>
								$("#submit-wrapper").hide();
							</script>
							<p id="submit-form-expletive">Thanks for filling out the form!</p>
							<div id="submit-form"><input type="submit" value="Register!" id="register-submit" onclick="doTags()"/></div>
						</td>
					</tr>
					
				</table>
			</fieldset>
		</form>
	</section>
   </div> <!-- end .rightsidebar -->
   
</div> <!-- end .container -->

</body>
</html>
