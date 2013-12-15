<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="my" uri="/WEB-INF/taglib.tld" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html xmlns="http://www.w3.org/1999/xhtml"
	  xmlns:my="/WEB-INF/taglib.tld"
	  xmlns:fn="http://java.sun.com/jsp/jstl/functions"
	  xmlns:c="http://java.sun.com/jsp/jstl/core">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Stageous: Get Your Events</title>
<link type="text/css" rel="stylesheet" media="screen" href="/style.css" />
<script language="JavaScript" type="text/JavaScript">
<!--
function uncheck() {
	
}
//-->
</script>
</head>

<body>
<%@ page import = "gigs.engine.event.user.*" %>
<div class="container">
  <div class="header"> 
       <ul class="nav" >
        <c:if test = "${sess}">
	        <li><a href="/logout" id="logout" >stageous</a></li>
	       	<li><a href="#" id="setting" >stageous</a></li>
	        <li><a href="#" id="messages" >stageous</a></li>
	        <li><a href="/profile/${username}" id="profile" >stageous</a></li>
        </c:if>
       	<li><a href="/home" id="home" >stageous</a></li>
        <li><a href="/home" id="logo" >stageous</a></li>     
       </ul>
  </div><!-- end .header -->
  <div class="eventbanner">
  <div id="hidden">
 	<div class="posterframe"><div class="eventposter"><img src="${my:getEventPoster(event)}" /></div></div>
        <div class="eventinfo"><h4>${event.title}</h4><p>${my:getTime(event.time)}</p>
	        <p>
	        	<c:forEach items="${eventartists}" var="artist" >
		           ${fn:toUpperCase(artist)}&nbsp;&middot;&nbsp;
		        </c:forEach>
	        </p>
	        <p>${event.description}</p><br /><br />
	        <p><b>@</b> ${my:getVenue(event)}</p>
        </div>
        <jsp:useBean id="event" class="gigs.engine.event.user.Event" scope="session"/>  
	     <c:if test = "${sess}">
		    <form action="/event/${event.eventID}" method="post" >
			    <fieldset id="goingradio">
			       <legend>Are you going?</legend>
			       <c:choose>
				       <c:when test = "${userstate eq 'attending'}" >
				       		<input id="going" value="GOING" name="going" type="radio" onclick="this.form.submit();" checked />
				       </c:when>
				       <c:otherwise>
				       		<input id="going" value="GOING" name="going" type="radio" onclick="this.form.submit();" unchecked />
				       </c:otherwise>
			       </c:choose>
			       <label for="going">I'm going</label>
			       <c:choose>
				       <c:when test = "${userstate eq 'interested'}">
				       		<input id="interested" value="INTERESTED" name="going" type="radio" onclick="this.form.submit();" checked />
				       </c:when>
				       <c:otherwise>
				       		<input id="interested" value="INTERESTED" name="going" type="radio" onclick="this.form.submit();" unchecked />
				       </c:otherwise>
			       </c:choose>
			       <label for="interested">I'm interested</label>
			       <span></span>
			   </fieldset>
		   </form>
	    </c:if>
    </div>
  </div><!-- end .eventbanner -->
  <div class="leftsidebar">
     <a href="${event.ticketLink}" class="goingbutton" style="margin:35%; color=#fff;"">Buy tickets</a>
     <br /><br />
     <div class="followbox"><b>Attending</b>
    	<div class="followscrollbox">
        	<table id="avatartable">
	    		<tr>	
			        <c:forEach items="${attending}" var="f1" varStatus="loop">
			            <c:if test="${not loop.first and loop.index % 6 == 0}">
			                </tr><tr>
			            </c:if>
			           <!--  <td><img src="http://assets.tumblr.com/images/default_avatar_24.png" id="user" /></td> -->
			           <td><a href="/profile/${f1.username}"><img src="${f1.imageURL}" id="user" width="24" height="24"/></a></td>
			        </c:forEach>
			    </tr>
			</table>
	 	</div>
        <br /><b>Interested</b>
    	<div class="followscrollbox">
		    <table id="avatartable">
		   		<tr>	
			        <c:forEach items="${interested}" var="f2" varStatus="loop">
			            <c:if test="${not loop.first and loop.index % 6 == 0}">
			                </tr><tr>
			            </c:if>
			           <!--  <td><img src="http://assets.tumblr.com/images/default_avatar_24.png" id="user" /></td> -->
			           <td><a href="/profile/${f2.username}"><img src="${f2.imageURL}" id="user" width="24" height="24"/></a></td>
			        </c:forEach>
			    </tr>
			</table>
        </div>
     </div>
  </div> <!-- end .leftsidebar -->
  
  <div class="content">
	    <h2>Posts </h2>
	    <c:if test = "${sess}">
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
		          	 	</form>
		          	 	<button class="goingbutton" type="submit" onclick="document.getElementById('imageupForm').submit();">Send Post</button>
					</div> 
		       </div> 
		       <div class="newPostTab">
		           <input type="radio" id="texttab" name="posttabs" checked />
		           <label for="texttab" id="text"></label>
		           <div class="tabcontent">
		               <form action="/event/${event.eventID}" id="textpostForm" method="post">
		                    <input type="textpost" id="textpost" name="textpost" value="Enter Text..." onfocus="if(this.value=this.defaultValue){this.value=''}; return false;" onblur="if (this.value == '') {this.value = this.defaultValue;}"  onkeypress="return postText(event);" />
		               </form>
		               <button class="goingbutton" type="submit" onclick="document.getElementById('textpostForm').submit();">Send Post</button>
		           </div> 
		       </div>   
		  	</div> <!-- end posttabs -->
	  	</c:if>
	  	
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
			        	<c:when test="${my:isFollowPost(post)}"><!-- Some unnecessary follow post  --></c:when>
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
				<p>No one has posted about this event yet. How about you?</p><br />
			</c:otherwise>
		</c:choose>     
	   	
   </div><!-- end .content -->
       
   <div class="rightsidebar">

       <h2>Related Events</h2>
       <jsp:useBean id="recevent" class="gigs.engine.event.user.Event" scope="session"/>  
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

