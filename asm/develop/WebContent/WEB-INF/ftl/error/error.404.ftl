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
		<title>Page Not Found</title>
		<!-- Latest compiled and minified JavaScript -->
		<script src="<@spring.url relativeUrl="/assets/js/jquery-2.0.0.min.js"/>" ></script>
		<script src="<@spring.url relativeUrl="/assets/js/bootstrap.min.js"/>"></script>
		<!-- Latest compiled and minified CSS -->
		<link rel="stylesheet" href="<@spring.url relativeUrl="/assets/css/bootstrap.min.css"/>"></link>
		
		<!-- Optional theme -->
		<link rel="stylesheet" href="<@spring.url relativeUrl="/assets/css/bootstrap-theme.min.css"/>" ></link>
		<link rel="stylesheet" href="<@spring.url relativeUrl="/assets/css/ssm.css"/>" ></link>
	</head>
	<body>
		<#include "../decorator/topnavigation.ftl">
		<div id="contentsingle" class="container">
			<div class="row row-offcanvas row-offcanvas-left">
			    <div class="col-xs-12 col-sm-12">
			    	<p class="pull-left visible-xs">
			        	<button type="button" class="toggleleftmenu btn glyphicon glyphicon-th-list" data-toggle="offcanvas"></button>
			    	</p>
					<div class="jumbotron">
			      		<div>
							Oops! nothing found.. go back to <a href="<@spring.url relativeUrl="/"/>" title="Home">Home</a> 
						</div>
					</div><!--/row-->
			    </div><!--/.col-xs-12.col-sm-9-->			
			</div><!--/row-->
		</div>
		<div class="securelogo"> <div>Powered by :</div><a href="http://www.securet.in" target="_blank"><img width="100%" src="<@spring.url relativeUrl="/assets/images/logo.png"/>" ></a></div>				
		<div id="footer">Copyright &copy; SecureT ${.now?string("yyyy")}</div>
		<div id='ajax_loader' style="position: fixed; left: 50%; top: 50%; display: none;">
			<img src="<@spring.url relativeUrl="/assets/images/loading-b.gif"/>" id="loading-indicator"  />
		</div>
	</body>
</html>
