<#assign homeUrl ="/">
<@security.authorize access="hasRole('ADMIN')">
	<#assign homeUrl ="/admin/">
</@security.authorize>
<@security.authorize access="hasRole('CLIENT_CONTROLLER')">
	<#assign homeUrl ="/reports/"> 
</@security.authorize>
<@security.authorize access="hasAnyRole('CLIENT_USER','RESOLVER')">
	<#assign homeUrl ="/tickets/">
</@security.authorize>
<nav class="navbar navbar-default navbar-static-top" role="navigation">
	<div class="navbar-inner">
		<img id="sitelogo" src="<@spring.url relativeUrl="/assets/images/sbi.png"/>" alt="Logo"/>
		<ul class="nav navbar-nav navbar-right">
			<li class="dropdown">
				<a href="<@spring.url relativeUrl=homeUrl/>"  role="button" aria-expanded="false"><i class="glyphicon glyphicon-home"></i> Home</a>
			</li>	
			<li class="dropdown">
				<a href="<@spring.url relativeUrl="/content/help"/>"  role="button" aria-expanded="false"><i class="glyphicon glyphicon-question-sign"></i> Help</a>
			</li>	
			<li class="dropdown">
				<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false"><i class="glyphicon glyphicon-user"></i> <b> Hi <@security.authentication property="principal.username" /> !</b> <i class="caret"></i></span></a>
				<ul class="dropdown-menu" role="menu">
					<li><a href="<@spring.url relativeUrl="/user/profile"/>"> <i class="glyphicon glyphicon-edit"> </i> &nbsp;&nbsp; Profile</a></li>
					<li><a href="<@spring.url relativeUrl="/logout"/>"> <i class="glyphicon glyphicon-log-out"> </i> &nbsp;&nbsp; Logout</a></li>
				</ul>
			</li>
		</ul>		
	</div>
</nav>		
