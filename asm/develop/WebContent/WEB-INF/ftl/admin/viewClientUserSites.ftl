<#include "../formMacros.ftl">
<#setting number_format="0.##">
<html>
	<head>
		<title>Assign Sites to clients</title>
		<script src="assets/js/bootstrap-multiselect-0.9.8.min.js"></script>
		<link rel="stylesheet" href="assets/css/bootstrap-multiselect.min.css">
		<script id="userOptionTmpl" type="text/x-jsrender">
		  <option value='{{:userId}}'>{{:fullName}}</option>
		</script>		
		<script id="siteOptionTmpl" type="text/x-jsrender">
		  <option value='{{:siteId}}' {{if siteSelected }} selected='selected' {{/if}} >{{:name}}</option>
		</script>		
	</head>
	<body>
		<h1>Assign Sites</h1>
		<#if saved?exists && saved?has_content>
			<div class="alert alert-info asmnotification" role="alert">${saved}</div>
		</#if>	
		<form id="clientUserSiteMapId" class="form-horizontal" role="form" method="POST" action="<@spring.url relativeUrl="/admin/saveClientUserSiteMapping"/>">
			<div class="row">
				<div class="col-xs-6  col-lg-5">
					<#assign organizationsList = .data_model["getOrganizationWithUsersForView"]>
					<#assign options>{"":"Select Organization",<#list organizationsList as uiObject>"${uiObject.organizationId}":"${uiObject.name}"<#if uiObject_has_next>,</#if></#list>}</#assign>
					<@formSingleSelectSSM path="formObject.organizationId" field={"fieldName":"organizationId","label":"Select Organization"} options=options?eval  includeLabelInline=false/>

					<#assign usersList = .data_model["getUsersForOrganization"]?default([])>
					<#if usersList?exists && (usersList?size>0) >
						<#assign useroptions>{"":"Select User",<#list usersList as uiObject>"${uiObject.userId}":"${uiObject.fullName}"<#if uiObject_has_next>,</#if></#list>}</#assign>
					</#if>
					<@formSingleSelectSSM path="formObject.userId" field={"fieldName":"userId","label":"Select User"} options=useroptions?default("{}")?eval  includeLabelInline=false/>

	        	</div>
		        <div class="col-xs-12 col-sm-6 col-lg-7">
					<#assign citiesList = .data_model["getCityWithSitesForView"]>
					<#assign options>{"":"Select Region",<#list citiesList as uiObject>"${uiObject.geoId}":"${uiObject.name}"<#if uiObject_has_next>,</#if></#list>}</#assign>
					<@formSingleSelectSSM path="formObject.cityGeoId" field={"fieldName":"cityGeoId","label":"Select Region"} options=options?eval  includeLabelInline=false/>

					<div class="form-group">
						<div class="col-sm-9">
							<@showErrorsSSM "formObject.sites"/>							
							<div><label class="control-label" for="siteId">Select Sites</label></div>
							<select class="form-control" name="sites" id="siteId" multiple="multiple">			
							</select>
						</div>
					</div>
		        </div>
			</div>
      		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
			<div style="text-align:right">
				<input  class="btn btn-primary right" name="submit" type="submit"  value="Save" />
			</div>	              	
		</form>
	</body>
</html>