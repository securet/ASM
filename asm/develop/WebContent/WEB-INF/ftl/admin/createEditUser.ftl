<#include "../formMacros.ftl">
<html>
	<head>
		<title>Create/Edit Profile</title>
	</head>
	<body>
		<h1>Create/Edit Profile </h1>	
		<#assign mode=mode?default("new")>
		<#if mode="edit">
			<div class="pageoptions">
				<div class="btn-group">
					<a href="<@spring.url relativeUrl="/admin/changePassword?userId=${formObject.userId}"/>" class="btn btn-default DTTT_button_text" tabindex="0" aria-controls="example">
						<span>Change Password</span>
					</a>
				</div>
			</div>			
		</#if>
		<form class="form-horizontal" role="form" method="POST" action="<@spring.url relativeUrl="/admin/saveUser"/>">
			<#setting number_format="0.##">
			<@formInputSSM path="formObject.fullName" field={"fieldName":"Full Name"} fieldType="text"/>
			<@formInputSSM path="formObject.userId" field={"fieldName":"user Id"} fieldType="text"/>
			<#if mode!="edit">
				<@formInputSSM path="formObject.userLogin.password" field={"fieldName":"Password"} fieldType="password"/>
				<@formInputSSM path="formObject.userLogin.verifyPassword" field={"fieldName":"Verify Password"} fieldType="password"/>
				<@formCheckboxSSM path="formObject.userLogin.enabled" field={"fieldName":"enabled"}  attributes="checked"/>
			</#if>
			<@formInputSSM path="formObject.emailId" field={"fieldName":"Email"} fieldType="text"/>
			<@formInputSSM path="formObject.mobile" field={"fieldName":"Mobile"} fieldType="text"/>
			<@formInputSSM path="formObject.enableNotifications" field={"fieldName":"Enable Notification"} fieldType="checkbox"/>
			<#assign organizationsList = .data_model["getOrganizationForView"]>
			<#assign options>{<#list organizationsList as uiObject>"${uiObject.organizationId}":"${uiObject.name}"<#if uiObject_has_next>,</#if></#list>}</#assign>
			<@formSingleSelectSSM path="formObject.organization.organizationId" field={"fieldName":"Organization"} options=options?eval/>
			<#assign roleTypeList = .data_model["getRoleTypeForView"]>
			<#assign options>{<#list roleTypeList as uiObject>"${uiObject.roleTypeId}":"<#if uiObject.roleType="CLIENT_USER">Channel Manager<#else>${uiObject.roleType?replace("_"," ")?capitalize}</#if>"<#if uiObject_has_next>,</#if></#list>}</#assign>
			<@formMultiSelectSSM path="formObject.rolesList" field={"fieldName":"Role"} options=options?eval attributes="multiple"/>
			<div style="text-align:right">
				<input  class="btn btn-primary right" name="submit" type="submit"  value="Save" />
			</div>	              	
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
		</form>
	</body>
</html>