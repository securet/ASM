<!DOCTYPE html>
<#assign decorator = JspTaglibs["http://www.opensymphony.com/sitemesh/decorator"]/>
<@decorator.usePage id="page"/>
<html>
	<head>
	    <meta charset="utf-8">
	    <meta http-equiv="X-UA-Compatible" content="IE=edge">
	    <meta name="viewport" content="width=device-width, initial-scale=1">
		<title><@decorator.title/></title>
		<base href="http://192.168.0.53:8080${springMacroRequestContext.contextPath}/"/>
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
		
		<@decorator.head/>
		<link rel="stylesheet" href="assets/css/ssm.css">
		<script src="assets/js/jquery.jsrender.min.js"></script>
		<script src="assets/js/ssm.js"></script>

	</head>
	<body>
		<header>
			Header will be here
		</header>	
		<div id="content" class="container">
			<div class="row row-offcanvas row-offcanvas-left">
			    <div class="col-xs-6 col-sm-3 sidebar-offcanvas" id="sidebar" role="navigation">
					<div class="list-group">
						<a href="<@spring.url relativeUrl="/admin/viewObjects?entityName=Organization"/>" class="list-group-item ">Organization</a>
						<a href="<@spring.url relativeUrl="/admin/viewObjects?entityName=Site"/>" class="list-group-item ">Site</a>
						<a href="<@spring.url relativeUrl="/admin/viewObjects?entityName=ServiceType"/>" class="list-group-item ">Service Type</a>
						<a href="<@spring.url relativeUrl="/admin/viewObjects?entityName=IssueType"/>" class="list-group-item ">Issue Type</a>
						<a href="<@spring.url relativeUrl="/admin/viewObjects?entityName=AssetType"/>" class="list-group-item ">AssetType</a>
						<a href="<@spring.url relativeUrl="/admin/viewObjects?entityName=Asset"/>" class="list-group-item ">Asset</a>
						<a href="<@spring.url relativeUrl="/admin/viewObjects?entityName=User"/>" class="list-group-item ">User</a>
						<a href="<@spring.url relativeUrl="/admin/viewClientUserSites"/>" class="list-group-item ">Assign Client Sites</a>
						<a href="<@spring.url relativeUrl="/admin/viewVendorAssetMapping"/>" class="list-group-item ">Map Vendor Assets</a>
					</div>
			    </div><!--/.sidebar-offcanvas-->
			    <div class="col-xs-12 col-sm-9">
			    	<p class="pull-left visible-xs">
			        	<button type="button" class="btn glyphicon glyphicon-th-list" data-toggle="offcanvas"></button>
			    	</p>
					<div class="jumbotron">
			      		<div>
							<@decorator.body/>
						</div>
					</div><!--/row-->
			    </div><!--/.col-xs-12.col-sm-9-->			
			</div><!--/row-->
		</div>
		<div class="securelogo"> <div>Powered by :</div><a href="http://www.securet.in" target="_blank"><img width="100%" src="assets/images/logo.png"/></a></div>				
		<div id="footer">Copyright &copy; SecureT ${now?string("yyyy")}</div>
	</body>
</html>
		
 
