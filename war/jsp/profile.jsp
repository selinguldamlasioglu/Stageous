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
<!-- Include theme -->
<link type="text/css" href="/css/jquery-ui-1.8.5.custom.css" rel="Stylesheet" />

<!-- Include jQuery and UI -->
<script type="text/javascript" src="/js/jquery-1.6.2.min.js"></script>
<script type="text/javascript" src="/js/jquery-ui-1.8.9.custom.min.js"></script>

    
<!-- Include jQuery CoverFlow widget -->
<script type="text/javascript" src="/js/ui.coverflow.js"></script>

<link type="text/css" href="/css/demos.css" rel="stylesheet" />

<!-- Transformie is a plugin that makes webkit's CSS transforms work in Internet Explorer -->
<script src="/js/sylvester.js" type="text/javascript"></script>
<script src="/js/transformie.js" type="text/javascript"></script>

<!-- Include mousewheel dependancies and app files -->
<script src="/js/jquery.mousewheel.min.js" type="text/javascript"></script>
<script src="/js/app.js" type="text/javascript"></script>

<script src="/js/ajaxupload.js" type="text/javascript"></script>
<link type="text/css" rel="stylesheet" media="screen" href="/style.css" />

<script type="text/javascript">
$(function(){	
    var demo = {

        yScroll: function(wrapper, scrollable) {

            var wrapper = $(wrapper), scrollable = $(scrollable),
                loading = $('<div class="loading">Loading...</div>').appendTo(wrapper),
                internal = null,
                images	= null;
                scrollable.hide();
                images = scrollable.find('img');
                completed = 0;
                
                images.each(function(){
                    if (this.complete) completed++;	
                });
                
                if (completed == images.length){
                    setTimeout(function(){							
                        loading.hide();
                        wrapper.css({overflow: 'hidden'});						
                        scrollable.slideDown('slow', function(){
                            enable();	
                        });					
                    }, 0);	
                }
        
            
            function enable(){
                var inactiveMargin = 99,
                    wrapperWidth = wrapper.width(),
                    wrapperHeight = wrapper.height(),
                    scrollableHeight = scrollable.outerHeight() + 2*inactiveMargin,
                    wrapperOffset = 0,
                    top = 0,
                    lastTarget = null;

                
                wrapper.mousemove(function(e){
                    lastTarget = e.target;
                    wrapperOffset = wrapper.offset();		
                    top = (e.pageY -  wrapperOffset.top) * (scrollableHeight - wrapperHeight) / wrapperHeight - inactiveMargin;
                    if (top < 0){
                        top = 0;
                    }			
                    wrapper.scrollTop(top);
                });	
            }			
        }
    }

    
    demo.yScroll('#scroll-pane', 'ul#sortable');

        
});
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
	        <li><a href="/profile/${sessuser.username}" id="profile" >stageous</a></li>
        </c:if>
       	<li><a href="/home" id="home" >stageous</a></li>
        <li><a href="/home" id="logo" >stageous</a></li>
       </ul>
  </div><!-- end .header -->
  
  <div class="leftsidebar">
  	 <jsp:useBean id="profile" class="gigs.engine.event.user.User" scope="session"/> <!-- DOES THIS WORK???? OR NECESSARY? -->
     <h2>${profile.username}</h2>
	     <form name="follow" method="post" action="/profile/${profile.username}" >
	     <c:choose>
		    <c:when test="${ userstate eq 'notfollowed'}">
			   		<input type="hidden" name="follow" value="1"/>
			   		<button type="button" value="Follow" class="followbutton" onclick="this.form.submit();">Follow</button>
			   		<!-- <input class="followbutton" value="follow" name="follow" type="button" onclick="this.form.submit();" /> -->
			   		<!-- <input type="hidden" name="follow" value="follow"/>
			   		<input class="followbutton" value="follow" name="follow" type="submit"/> -->
		     		<!-- <a class="followbutton" onclick="document.follow.submit()" href="#follow">Follow</a> -->
	     	</c:when>
	     	<c:when test="${ userstate eq 'followed'}">
		     		<input type="hidden" name="follow" value="0"/>
		     		<button type="button" value="Unfollow" class="unfollowbutton" onclick="this.form.submit();" >Unfollow</button>
		     		<!-- <input class="unfollowbutton" value="unfollow" name="unfollow" type="button" onclick="this.form.submit();" /> -->
		     	<!-- 	<input type="hidden" name="unfollow" value="unfollow"/> -->
		     		<!-- <a class="unfollowbutton" onclick="document.unfollow.submit()" href="#unfollow">Unfollow</a> -->
	     	</c:when>
	     	<c:otherwise>
	     	</c:otherwise>
	     </c:choose>
	     </form>
     <img src="${profile.imageURL}" id="profilepic" />
     <fieldset id="user">
        <legend><b>Description</b></legend>
		<p>${profile.description}</p>
	</fieldset>
	<div class="followbox"><b>Followers</b>
    <div class="followscrollbox">
        <table id="avatartable">
    		<tr>	
    			<jsp:useBean id="f1" class="gigs.engine.event.user.User" scope="session"/> 
		        <c:forEach items="${following}" var="f1" varStatus="loop">
		            <c:if test="${not loop.first and loop.index % 6 == 0}">
		                </tr><tr>
		            </c:if>
		           <!--  <td><img src="http://assets.tumblr.com/images/default_avatar_24.png" id="user" /></td> -->
		           <td><a href="/profile/${f1.username}"><img src="${f1.imageURL}" id="user" width="24" height="24"/></a></td>
		        </c:forEach>
		    </tr>
		</table>
<!--         <tr><td><img src="http://assets.tumblr.com/images/default_avatar_24.png" id="user" /></td>
        <td><img src="http://assets.tumblr.com/images/default_avatar_24.png" id="user" /></td>
        <td><img src="http://assets.tumblr.com/images/default_avatar_24.png" id="user" /></td>
        <td><img src="http://assets.tumblr.com/images/default_avatar_24.png" id="user" /></td>
        <td><img src="http://assets.tumblr.com/images/default_avatar_24.png" id="user" /></td>
        <td><img src="http://assets.tumblr.com/images/default_avatar_24.png" id="user" /></td></tr>
        
        <tr><td><img src="http://assets.tumblr.com/images/default_avatar_24.png" id="user" /></td>
        <td><img src="http://assets.tumblr.com/images/default_avatar_24.png" id="user" /></td>
        <td><img src="http://assets.tumblr.com/images/default_avatar_24.png" id="user" /></td>
        <td><img src="http://assets.tumblr.com/images/default_avatar_24.png" id="user" /></td>
        <td><img src="http://assets.tumblr.com/images/default_avatar_24.png" id="user" /></td>
        <td><img src="http://assets.tumblr.com/images/default_avatar_24.png" id="user" /></td></tr>
        
        <tr><td><img src="http://assets.tumblr.com/images/default_avatar_24.png" id="user" /></td>
        <td><img src="http://assets.tumblr.com/images/default_avatar_24.png" id="user" /></td>
        <td><img src="http://assets.tumblr.com/images/default_avatar_24.png" id="user" /></td></tr></table> -->
        </div>
        <br /><b>Following</b>
    	<div class="followscrollbox">
	        <table id="avatartable">
	    		<tr>	
	    			<jsp:useBean id="f2" class="gigs.engine.event.user.User" scope="session"/>  
			        <c:forEach items="${followed}" var="f2" varStatus="loop">
			            <c:if test="${not loop.first and loop.index % 6 == 0}">
			                </tr><tr>
			            </c:if>
			           <!--  <td><img src="http://assets.tumblr.com/images/default_avatar_24.png" id="user" /></td> -->
			           <td><a href="/profile/${f2.username}"><img src="${f2.imageURL}" id="user"  width="24" height="24"/></a></td>
			        </c:forEach>
			    </tr>
			</table>
        </div>
     </div>
  </div> <!-- end .leftsidebar -->
  
  <div class="content">
    <jsp:useBean id="latevent" class="gigs.engine.event.user.Event" scope="session"/> 
    <c:choose>
		<c:when test = "${not empty userlatevents}">
			<h2>Last 10 Events</h2>
		    <div class="demo">
				<div class="wrapper">
					<div id="coverflow">
						<c:forEach var="latevent" items="${userlatevents}">
					       	<img src="${my:getEventPoster(latevent)}" data-artist="${latevent.title}"/>
				        </c:forEach><%--<a href= "/event/${latevent.eventID}"><img src="${my:getEventPoster(latevent)}" data-artist="${latevent.title}"/></a> --%>
					</div>
				</div>
				<div id="imageCaption">Last 10 Events</div><div id='slider'></div>
				<div id="slider-wrap"><div id="slider-vertical"></div></div>
		   </div>
		</c:when>
		<%-- <c:otherwise>
		<p>Oops... This user haven't attended any events yet.</p><br />
		</c:otherwise>  --%>
	</c:choose>      
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
			        			<td><a href="/profile/${my:getFollowerName(post)}">${my:getFollowerName(post)}</a> is now following <a href="/event/${my:getFollowingName(post)}">${my:getFollowingName(post)}</a>.</td></tr>
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
		<p>This user hasn't posted anything recently.</p><br />
		</c:otherwise> 
	</c:choose>  
   </div><!-- end .content -->
       
   <div class="rightsidebar">
       <div class="searchEvent">
           <form action="" method="get">
          	 <input type="searchEvent" onkeypress="return runEventSearch(event)" id="eventKey" value="Search by tags, artists..." onblur="if (this.value == '') {this.value = this.defaultValue;}" onfocus="if(this.value=this.defaultValue){this.value=''}; return false;" />
           </form>
            <a href="/advancedSearch" id="advanced">Advanced</a>
       </div>
       <c:if test = "${not empty recevents}">
	       <h2>Suggested Events</h2>
	       <c:forEach var="recevent" items="${recevents}">
		     	<div class="eventlist">
		       		<div class="poster"><img src="${my:getEventPoster(recevent)}" /></div>
		            <p><a href="/event/${recevent.eventID}">${recevent.title}</a><br>${my:getTime(recevent.time)}<br />${my:getVenueName(recevent)}</p>
		       </div>
	       </c:forEach>
       </c:if>
   </div> <!-- end .rightsidebar -->
   
</div> <!-- end .container -->

</body>
</html>
