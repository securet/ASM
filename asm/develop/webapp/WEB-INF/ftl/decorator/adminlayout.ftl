<!DOCTYPE html>
<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#assign decorator = JspTaglibs["http://www.opensymphony.com/sitemesh/decorator"]/>
<@decorator.usePage id="page"/>
<#setting number_format="0.##">
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
			    <div class="col-xs-6 col-sm-2 sidebar-offcanvas" id="sidebar" role="navigation">
					<div class="list-group">
						<a href="<@spring.url relativeUrl="/admin/viewObjects?entityName=Organization"/>" class="list-group-item ">Organization <i class="glyphicon glyphicon-chevron-right"></i></a>
						<a href="<@spring.url relativeUrl="/admin/viewObjects?entityName=Module"/>" class="list-group-item ">Module   <i class="glyphicon glyphicon-chevron-right"></i></a>
						<a href="<@spring.url relativeUrl="/admin/viewObjects?entityName=Site"/>" class="list-group-item ">Site   <i class="glyphicon glyphicon-chevron-right"></i></a>
						<a href="<@spring.url relativeUrl="/admin/viewObjects?entityName=ServiceType"/>" class="list-group-item ">Service Type  <i class="glyphicon glyphicon-chevron-right"></i></a>
						<a href="<@spring.url relativeUrl="/admin/viewObjects?entityName=IssueType"/>" class="list-group-item ">Issue Type  <i class="glyphicon glyphicon-chevron-right"></i></a>
						<a href="<@spring.url relativeUrl="/admin/viewObjects?entityName=AssetType"/>" class="list-group-item ">AssetType  <i class="glyphicon glyphicon-chevron-right"></i></a>
						<a href="<@spring.url relativeUrl="/admin/viewObjects?entityName=Asset"/>" class="list-group-item ">Asset  <i class="glyphicon glyphicon-chevron-right"></i></a>
						<a href="<@spring.url relativeUrl="/admin/viewObjects?entityName=ServiceSparePart"/>" class="list-group-item ">Service Spare Part  <i class="glyphicon glyphicon-chevron-right"></i></a>
						<a href="<@spring.url relativeUrl="/admin/viewObjects?entityName=User"/>" class="list-group-item ">User  <i class="glyphicon glyphicon-chevron-right"></i></a>
						<a href="<@spring.url relativeUrl="/admin/viewClientUserSites"/>" class="list-group-item ">Assign Client Sites  <i class="glyphicon glyphicon-chevron-right"></i></a>
						<a href="<@spring.url relativeUrl="/admin/viewVendorAssetMapping"/>" class="list-group-item ">Map Vendor Assets  <i class="glyphicon glyphicon-chevron-right"></i></a>
						<a href="<@spring.url relativeUrl="/tickets/listTickets"/>" class="list-group-item ">Tickets<i class="glyphicon glyphicon-chevron-right"></i></a>
					</div>
			    </div><!--/.sidebar-offcanvas-->
			    <div class="col-xs-12 col-sm-10">
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
		<div id='ajax_loader' style="position: fixed; left: 50%; top: 50%; display: none;">
			<img src="<@spring.url relativeUrl="/assets/images/loading-b.gif"/>" id="loading-indicator"  />
		</div>
	</body>
</html>
		
 
