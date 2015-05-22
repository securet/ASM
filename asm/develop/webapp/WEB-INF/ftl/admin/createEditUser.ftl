<#include "../formMacros.ftl">
<#assign moduleName="admin">
<#assign isProfileEdit=isProfileEdit?default(false)>
<#if isProfileEdit>
	<#assign moduleName="user">
</#if>
<html>
	<head>
		<title>Create/Edit Profile</title>
	</head>
	<body>
		<div class="panel panel-primary">
			<div class="panel-heading">
				<h1 class="panel-title" ><#if !isProfileEdit>Create/</#if>Edit Profile </h1>
			</div>
			<div class="panel-body">
					<#assign mode=mode?default("new")>
				<#if mode="edit">
					<div class="pageoptions">
						<div class="btn-group">
							<a href="<@spring.url relativeUrl="/${moduleName}/changePassword"/><#if !isProfileEdit>?userId=${formObject.userId}</#if>" class="btn btn-default DTTT_button_text" tabindex="0" aria-controls="example">
								<span>Change Password</span>
							</a>
						</div>
					</div>			
				</#if>
				<#if saved?exists>
					<div class="alert alert-info asmnotification" role="alert">Successfully updated your details!</div>
				</#if>
				<form class="form-horizontal" role="form" method="POST" action="<@spring.url relativeUrl="/${moduleName}/saveUser"/>">
					<#setting number_format="0.##">
					<@formInputSSM path="formObject.fullName" field={"fieldName":"Full Name"} fieldType="text"/>
					<#if mode!="edit">
						<@formInputSSM path="formObject.userId" field={"fieldName":"user Id"} fieldType="text"/>
						<@formInputSSM path="formObject.userLogin.password" field={"fieldName":"Password"} fieldType="password"/>
						<@formInputSSM path="formObject.userLogin.verifyPassword" field={"fieldName":"Verify Password"} fieldType="password"/>
						<@formCheckboxSSM path="formObject.userLogin.enabled" field={"fieldName":"enabled"}  attributes="checked"/>
					<#else>
						<div class="form-group labelblock">
							<label for="serviceTypeId" class="col-sm-3 control-label">User Id</label>
						    <div id="serviceTypeId"  class="col-sm-9">
								<p class="form-control-static normaltext">${formObject.userId!}</p>
								<input type="hidden" name="userId" value="${formObject.userId!}"/>
						    </div>
						</div>
					</#if>
					<@formInputSSM path="formObject.emailId" field={"fieldName":"Email"} fieldType="text"/>
					<@formInputSSM path="formObject.mobile" field={"fieldName":"Mobile"} fieldType="text"/>
					<@formCheckboxSSM path="formObject.enableNotifications" field={"fieldName":"Enable Notifications"}/>
					<#if isProfileEdit?exists && isProfileEdit>
						<div class="form-group labelblock">
							<label for="organizationName" class="col-sm-3 control-label">Organization</label>
						    <div id="organizationName"  class="col-sm-9">
								<p class="form-control-static normaltext">${formObject.organization.name!}</p>
						    </div>
						</div>
					<#else>
						<#assign organizationsList = .data_model["getOrganizationForView"]>
						<#assign options>{<#list organizationsList as uiObject>"${uiObject.organizationId}":"${uiObject.name}"<#if uiObject_has_next>,</#if></#list>}</#assign>
						<@formSingleSelectSSM path="formObject.organization.organizationId" field={"fieldName":"Organization"} options=options?eval/>
						<#assign roleTypeList = .data_model["getRoleTypeForView"]>
						<#assign options>{<#list roleTypeList as uiObject>"${uiObject.roleTypeId}":"<#if uiObject.roleType="CLIENT_USER">Channel Manager<#else>${uiObject.roleType?replace("_"," ")?capitalize}</#if>"<#if uiObject_has_next>,</#if></#list>}</#assign>
						<@formMultiSelectSSM path="formObject.rolesList" field={"fieldName":"Role"} options=options?eval attributes="multiple"/>
					</#if>
					<div style="text-align:right">
						<input  class="btn btn-primary right" name="submit" type="submit"  value="Save" />
					</div>	              	
					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
				</form>
			</div>
		</div>		
	</body>
</html>