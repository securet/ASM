<#include "../formMacros.ftl">
<html>
	<head>
		<title>Change Password</title>
	</head>
	<body>
		<h1>Change Password</h1>	
		<#if saved?default(false)>
			<div class="alert alert-info asmnotification" role="alert">Changed Password Successfully</div>
		</#if>
		<form class="form-horizontal" role="form" method="POST" action="<@spring.url relativeUrl="/admin/savePassword"/>">
			<@formInputSSM path="formObject.userId" field={"fieldName":"user Id"} fieldType="text"/>
			<@formInputSSM path="formObject.password" field={"fieldName":"Password"} fieldType="password"/>
			<@formInputSSM path="formObject.verifyPassword" field={"fieldName":"Verify Password"} fieldType="password"/>
			<@formCheckboxSSM path="formObject.enabled" field={"fieldName":"enabled"} />
			<div style="text-align:right">
				<input  class="btn btn-primary right" name="submit" type="submit"  value="Save" />
			</div>	              	
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
		</form>
	</body>
</html>