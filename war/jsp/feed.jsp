<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="my" uri="/WEB-INF/taglib.tld" %>

<html xmlns="http://www.w3.org/1999/xhtml"
	  xmlns:my="/WEB-INF/taglib.tld"
	  xmlns:c="http://java.sun.com/jsp/jstl/core">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Stageous: Get Your Events</title>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/2.0.0/jquery.min.js"></script>

<script type="text/javascript">
$('.searchEvent input[type=searchEvent]').keypress(function(e) {
    if(e.which == 13) {
    	$('#searchEventForm').submit();
        /* jQuery(this).blur();
        jQuery('#submit').focus().click(); */
    }
});
</script>
<link type="text/css" rel="stylesheet" media="screen" href="/style.css" />
<link rel="stylesheet" type="text/css" href="http://fonts.googleapis.com/css?family=Open+Sans">
</head>

<body>
<%@ page import = "gigs.engine.event.user.*" %>
<div class="container">
  <div class="header"> 
       <ul class="nav" >
        <li><a href="/logout" id="logout" >stageous</a></li>
       	<li><a href="#" id="setting" >stageous</a></li>
        <li><a href="#" id="messages" >stageous</a></li>
        <li><a href="/profile/${profile}" id="profile" >stageous</a></li>
       	<li><a href="/home" id="home" >stageous</a></li>
        <li><a href="/home" id="logo" >stageous</a></li>
       </ul>
  </div><!-- end .header -->
  
  <div class="leftsidebar">
     <h2>You follow</h2>
     <div class="searchFollow">
           <form action="?" method="get">
                <input type="searchFollow" onkeypress="return runFollowSearch(event)" id="followKey" value="Find people..." onblur="if (this.value == '') {this.value = this.defaultValue;}" onfocus="if(this.value=this.defaultValue){this.value=''}; return false;" />
            </form>
    </div>
           <table id="usertable">
            <c:forEach var="fuser" items="${followed}">
              <tr><td id="image"><a href="/profile/${fuser.username}"><img src="${fuser.imageURL}" /></a></td><td id="info"><p><b>${fuser.username}</b><br>${fuser.description}</p></td></tr>
            </c:forEach>
           </table>
  </div> <!-- end .leftsidebar -->
  
  <div class="content">
    <h2>Feed</h2>
    
    <div class="newPostTabs">
        <div class="newPostTab">
               <input type="radio" id="videotab" name="posttabs" />
               <label for="videotab" id="video"></label>
               <div class="tabcontent">
                   <p>Stuff for Tab Three</p>
               </div> 
        </div>  
		<div class="newPostTab">
            <input type="radio" id="phototab" name="posttabs" >
            <label for="phototab" id="photo"></label>
			<div class="tabcontent">
                 <%@ page import = "com.google.appengine.api.blobstore.*" %>
	             <% final BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService(); %>
                 <form action="${uploadURL}" method="post" id="imageupForm" enctype="multipart/form-data">
                   <input type="textpost" id="photopost" name="photopost" value="Enter Text..." onblur="if (this.value == '') {this.value = this.defaultValue;}" onfocus="if(this.value=this.defaultValue){this.value=''}; return false;" />
	               <div id="imageup">
		                <c:choose>
						    <c:when test="${empty preview}">
						        <div id="preview"><img src="/images/stamp.png"></div>
						    </c:when>
						    <c:otherwise>
						        <div id="preview"><img src="${preview}"></div>
						    </c:otherwise>
						</c:choose>
	                   	<label><b>Upload a Photo</b></label>
                       	<input type="file" size="20" id="imageupload" name="imageupload" />
          	 		</div>
          	 		<div class="ui-widget">
	                    <label style="border: 0; margin-left: -25px;">Select from your events: <br /></label>
	                    <select id="photocombobox" name="photocombobox" class="custom-combobox">
		                    <c:forEach var="latevent" items="${latevents}">
		                    	<option value="${latevent.eventID}">${latevent.title}</option>
				            </c:forEach>
	                    </select>
                	</div>
          	 	</form>
          	 	<button class="goingbutton" type="submit" onclick="document.getElementById('imageupForm').submit();">Send Post</button>
			</div> 
       </div> 
       <div class="newPostTab">
           <input type="radio" id="texttab" name="posttabs" checked />
           <label for="texttab" id="text"></label>
           <div class="tabcontent">
              <form action="/home" id="textpostForm" method="post">
	               <input type="textpost" id="textpost" name="textpost" value="Enter Text..." onfocus="if(this.value=this.defaultValue){this.value=''}; return false;" onblur="if (this.value == '') {this.value = this.defaultValue;}"  onkeypress="return postText(event);" />
	               <div style="padding:15px 20px;">
	               		<div class="ui-widget">
		                    <label style="border: 0; margin-left: -25px;">Select from your events: <br /></label>
		                    <select id="textcombobox" name="textcombobox" class="custom-combobox">
			                    <c:forEach var="latevent" items="${latevents}">
			                    	<option value="${latevent.eventID}">${latevent.title}</option>
					            </c:forEach>
		                    </select>
	                	</div>
                	</div>
              </form>
	          <button class="goingbutton" type="submit" onclick="document.getElementById('textpostForm').submit();">Send Post</button>
           </div> 
       </div>   
  	</div> <!-- end posttabs -->
   
   <jsp:useBean id="post" class="gigs.engine.event.user.Post" scope="session"/>  
    <c:choose>
		<c:when test = "${not empty posts}">
		   <c:forEach var="post" items="${posts}">
		        <c:choose>
		        	<c:when test="${my:isImagePost(post)}">
		        		<div class="entry">
		      				<table id="entrytable">
				        		<tr><td><a href="/profile/${my:getPosterName(post)}" ><img src="${my:getPosterProfileImage(post)}" id="user" width="24" height="24"/></a></td>
				        		<td><a href="/profile/${my:getPosterName(post)}" >${my:getPosterName(post)}</a> has posted a photo from <a href="/event/${my:getPostEventID(post)}">${my:getPostEventName(post)}</a>.</td></tr>
				       			<tr><td></td><td><div class="post">"${post.text}"</div></td></tr><tr><td></td><td><img src="${my:getPostImage(post)}" id="photo" /></td></tr>
		        			</table>
		  				</div>
		        	</c:when>
		        	<c:when test="${my:isFollowPost(post)}"><%-- 
		        		<div class="entry">
		      				<table id="entrytable">
			        			<tr><td><a href="/profile/${my:getFollowerName(post)}" ><img src="${my:getPosterProfileImage(post)}" id="user" width="24" height="24" /></a></td>
			        			<td><a href="/profile/${my:getFollowerName(post)}">${my:getFollowerName(post)}"</a> is now following <a href="/event/${my:getFollowingName(post)}">${my:getFollowingName(post)}</a>.</td></tr>
			       			</table>
		  				</div>
		  			 --%></c:when>
		        	<c:when test="${my:isAttendPost(post)}">
		        		<div class="entry">
		      				<table id="entrytable">
			        			<tr><td><a href="/profile/${my:getPosterName(post)}" ><img src="${my:getPosterProfileImage(post)}" id="user" width="24" height="24" /></a></td>
			        			<td><a href="/profile/${my:getAttenderName(post)}">${my:getAttenderName(post)}</a> is ${my:getAttendState(post)} <a href="/event/${my:getPostEventID(post)}">${my:getPostEventName(post)}</a>.</td></tr>
			       			</table>
		  				</div>
			       	</c:when>
		        	<c:otherwise>
		        		<div class="entry">
		      				<table id="entrytable">
			        		 	<tr><td><a href="/profile/${my:getPosterName(post)}" ><img src="${my:getPosterProfileImage(post)}" id="user" width="24" height="24" /></a></td>
			        		 	<td><a href="/profile/${my:getPosterName(post)}" >${my:getPosterName(post)}</a> has posted a comment on <a href="/event/${my:getPostEventID(post)}">${my:getPostEventName(post)}</a>.</td></tr>
			        		 	<tr><td></td><td><div class="post">"${post.text}"</div></td></tr>
			      			</table>
		  				</div>
			        </c:otherwise>
		        </c:choose>
			</c:forEach>
		    
		    <div class="entry" id="loading">
		        <img src="/images/loading.gif" />
		    </div>
		</c:when>
		<c:otherwise>
		<p>People you follow haven't posted anything recently. Follow more ot start posting?</p><br />
		</c:otherwise>
	</c:choose>
  
   </div><!-- end .content -->
       
   <div class="rightsidebar">
       <div class="searchEvent">
           <form action="/home" method="post" id=searchEventForm>
           		<input type="searchEvent" id="eventKey" name="eventKey" value="Search by tags, artists..." onblur="if (this.value == '') {this.value = this.defaultValue;}" onfocus="if(this.value=this.defaultValue){this.value=''}; return false;" />
           </form>
            <a href="/advancedSearch" id="advanced">Advanced</a>
       </div>
       <h3>Upcoming Events From Your Followers</h3>
       <c:forEach var="recevent" items="${recevents}">
	     	<div class="eventlist">
	       		<div class="poster"><img src="${my:getEventPoster(recevent)}" /></div>
	            <p><a href="/event/${recevent.eventID}">${recevent.title}</a><br>${my:getTime(recevent.time)}<br />${my:getVenueName(recevent)}</p>
	       </div>
       </c:forEach>
   </div> <!-- end .rightsidebar -->
   
</div> <!-- end .container -->

</body>
</html>
