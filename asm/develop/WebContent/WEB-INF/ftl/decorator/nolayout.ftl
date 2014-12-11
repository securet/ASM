<!DOCTYPE html>
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
		<!-- Latest compiled and minified CSS -->
		<link rel="stylesheet" href="assets/css/bootstrap.min.css">
		
		<!-- Optional theme -->
		<link rel="stylesheet" href="assets/css/bootstrap-theme.min.css">
		
		<!-- Latest compiled and minified JavaScript -->
		<script src="assets/js/jquery-2.0.0.min.js"></script>
		<script src="assets/js/bootstrap.min.js"></script>
		<script src="assets/js/ssm.js"></script>
		<link rel="stylesheet" href="assets/css/ssm.css">
		<@decorator.head/>
	</head>
	<body>
		<div id="content">
			<@decorator.body/>
		</div>
		<div class="securelogo"> <div>Powered by :</div><a href="http://www.securet.in" target="_blank"><img width="100%" src="assets/images/logo.png"/></a></div>				
		<div id="footer">Copyright &copy; SecureT ${now?string("yyyy")}</div>
	</body>
</html>
		
 
