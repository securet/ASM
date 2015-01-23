<#--
<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#assign c=JspTaglibs["http://java.sun.com/jstl/core"]>
-->

<html>
<head>
<title>Login Page</title>
</head>
<body onload='document.loginForm.username.focus();'>
<div>
 	<div class="row">
		<div class="col-sm-4 login-row center-block">
			<img class="box-start" src="assets/images/wrapper_top.png" width="100%"/>			
			<div id="login-box">				
				<h1 class="login">SecureT Service Management</h1>		
				<#if RequestParameters.error?exists>
					<div class="alert alert-danger" role="alert">
					  <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
					  <span class="sr-only">Error:</span>
					  Please check your credentials
					</div>
				</#if>
				<#if msg?exists>
					<div class="error">${msg}</div>
				</#if>
				<div class="col-sm-4 login-form center-block">
					<form role="form" name='loginForm' action="<@spring.url relativeUrl='/j_spring_security_check' />" method='POST'>				
				              	<div class="form-group input-group">
				              		<span class="input-group-addon"><span class="glyphicon glyphicon-user"></span></span>
									<input type="text" class="form-control" name="username" placeholder="Username">
								</div>			
				              	<div class="form-group input-group">
				              		<span class="input-group-addon"><span class="glyphicon glyphicon-lock"></span></span>
									<input type="password" class="form-control" name="password" placeholder="Password">
								</div>
								<div style="text-align:right">
									<input  class="btn btn-primary right" name="submit" type="submit"  value="Log In" />
								</div>	              	
							  	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
					</form>
				</div>
			</div>
			<img class="box-end" src="assets/images/wrapper_bottom.png" width="100%"/>			
		</div>	
	</div>
</div> 
</body>
</html>