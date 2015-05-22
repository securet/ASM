<!DOCTYPE html>
<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#assign decorator = JspTaglibs["http://www.opensymphony.com/sitemesh/decorator"]/>
<#setting number_format="0.##">
<@decorator.usePage id="page"/>
<html>
	<head>
	    <meta charset="utf-8">
	    <meta http-equiv="X-UA-Compatible" content="IE=edge">
	    <meta name="viewport" content="width=device-width, initial-scale=1">
		<title><@decorator.title/></title>
		<base href="http://${serverName}<#if port!=80>:${port}</#if>${springMacroRequestContext.contextPath}/"/>
		<script type="text/javascript">
			var contextPath="${springMacroRequestContext.contextPath}";
		</script>
		<!-- Latest compiled and minified JavaScript -->
		<script src="assets/js/jquery-2.0.0.min.js"></script>
		<script src="assets/js/bootstrap.min.js"></script>
		<script src="assets/js/fileinput.min.js"></script>
		<!-- Latest compiled and minified CSS -->
		<link rel="stylesheet" href="assets/css/bootstrap.min.css">
		
		<!-- Optional theme -->
		<link rel="stylesheet" href="assets/css/bootstrap-theme.min.css">
		<link rel="stylesheet" href="assets/css/fileinput.min.css">
		
		<script src="assets/js/bootstrap-multiselect-0.9.8.min.js"></script>
		<link rel="stylesheet" href="assets/css/bootstrap-multiselect.min.css">
		<@decorator.head/>
		<script id="imgTmpl" type="text/x-jsrender">
		  <img class="{{:imgclass}}" src='{{:path}}'/></option>
		</script>		
		<link rel="stylesheet" href="assets/css/ssm.css">
		<script src="assets/js/jquery.jsrender.min.js"></script>
		<script src="assets/js/ssm.js"></script>
		<script src="assets/js/ticket.js"></script>
	</head>
	<body>
		<#include "topnavigation.ftl">
		<div id="content" class="container">
			<div class="row row-offcanvas row-offcanvas-left">
			    <div class="col-xs-6 col-sm-3 col-md-2 sidebar-offcanvas" id="sidebar" role="navigation">
					<div class="list-group">
						<@security.authorize access="hasAnyRole('CLIENT_USER','CLIENT_CONTROLLER')">
							<a href="<@spring.url relativeUrl="/user/viewVendorAssetMapping"/>" class="list-group-item "> <i class="glyphicon glyphicon-wrench"></i> Manage Vendors</a>
						</@security.authorize>
						<a href="<@spring.url relativeUrl="/tickets/listTickets?filterStatus=OPEN"/>" class="list-group-item ">Open <span class="badge badge-danger">${openTicketsCount!}</span></a>
						<a href="<@spring.url relativeUrl="/tickets/listTickets?filterStatus=WORK_IN_PROGRESS"/>" class="list-group-item ">Work in Progress <span class="badge badge-info">${work_in_progressTicketsCount!}</span></a>
						<a href="<@spring.url relativeUrl="/tickets/listTickets?filterStatus=RESOLVED"/>" class="list-group-item ">Resolved <span class="badge badge-warning">${resolvedTicketsCount!}</span></a>
						<a href="<@spring.url relativeUrl="/tickets/listTickets?filterStatus=CLOSED"/>" class="list-group-item ">Closed <span class="badge badge-success">${closedTicketsCount!}</span></a>
						<a href="<@spring.url relativeUrl="/tickets/listTickets"/>" class="list-group-item ">All Tickets <span class="badge badge-primary">${openTicketsCount?default(0)+work_in_progressTicketsCount?default(0)+resolvedTicketsCount?default(0)+closedTicketsCount?default(0)}</span></a>
						<@security.authorize access="hasAnyRole('ADMIN','CLIENT_CONTROLLER')">
							<a href="<@spring.url relativeUrl="/reports/dashboard"/>" class="list-group-item "> <i class="glyphicon glyphicon-dashboard"></i> Dashboard</a>
						</@security.authorize>
						<@security.authorize access="hasAnyRole('ADMIN')">
							<a href="<@spring.url relativeUrl="/admin/"/>" class="list-group-item "> <i class="glyphicon glyphicon-circle-arrow-left"></i> Go to Admin</a>
						</@security.authorize>
					</div>
			    </div><!--/.sidebar-offcanvas-->
			    <div class="col-xs-12 col-sm-9 col-md-10">
			    	<p class="pull-left visible-xs">
			        	<button type="button" class="toggleleftmenu btn glyphicon glyphicon-th-list" data-toggle="offcanvas"></button>
			    	</p>
					<div class="jumbotron">
			      		<div>
							<@decorator.body/>
						</div>
					</div><!--/row-->
			    </div><!--/.col-xs-12.col-sm-9-->			
			</div><!--/row-->
		</div>
		<div class="securelogo"> <div>Powered by :</div><a href="http://www.securet.in" target="_blank"><img  src="assets/images/logo.png"/></a></div>				
		<div id="footer">Copyright &copy; SecureT ${now?string("yyyy")}</div>
	</body>
</html>
