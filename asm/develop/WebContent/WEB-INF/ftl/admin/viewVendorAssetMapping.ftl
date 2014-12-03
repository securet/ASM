<#include "../formMacros.ftl">
<#setting number_format="0.##">
<html>
	<head>
		<title>Vendor Asset Mapping</title>
		<script src="assets/js/bootstrap-multiselect-0.9.8.min.js"></script>
		<link rel="stylesheet" href="assets/css/bootstrap-multiselect.min.css">
		<script id="userOptionTmpl" type="text/x-jsrender">
		  <option value='{{:userId}}'>{{:fullName}}</option>
		</script>		
		<script id="siteOptionTmpl" type="text/x-jsrender">
		  <option value='{{:siteId}}' {{if siteSelected }} selected='selected' {{/if}} >{{:name}}</option>
		</script>		
		<script id="serviceTypeOptionTmpl" type="text/x-jsrender">
		  <option value='{{:serviceTypeId}}' {{if serviceTypeSelected }} selected='selected' {{/if}} >{{:name}}</option>
		</script>		
		<script id="assetOptionTmpl" type="text/x-jsrender">
		  <option value='{{:assetId}}' {{if assetSelected }} selected='selected' {{/if}} >{{:name}}</option>
		</script>		
	</head>
	<body>
		<h1>Vendor Asset Mapping</h1>
		<#if saved?exists && saved?has_content>
			<div class="alert alert-info asmnotification" role="alert">${saved}</div>
		</#if>	
		<form id="vendorAssetMapId" class="form-horizontal mappingforms" role="form" method="POST" action="<@spring.url relativeUrl="/admin/saveVendorAssetMapping"/>">
			<div class="row">
				<div class="col-xs-6  col-lg-5">
					<#assign organizationsList = .data_model["getOrganizationWithUsersForView"]>
					<#assign options>{"":"Select Organization",<#list organizationsList as uiObject>"${uiObject.organizationId}":"${uiObject.name}"<#if uiObject_has_next>,</#if></#list>}</#assign>
					<@formSingleSelectSSM path="formObject.organizationId" field={"fieldName":"organizationId","label":"Select Organization"} options=options?eval  includeLabelInline=false/>

					<#assign usersList = .data_model["getVendorsForOrganization"]?default([])>
					<#if usersList?exists && (usersList?size>0) >
						<#assign useroptions>{"":"Select User",<#list usersList as uiObject>"${uiObject.userId}":"${uiObject.fullName}"<#if uiObject_has_next>,</#if></#list>}</#assign>
					</#if>
					<@formSingleSelectSSM path="formObject.userId" field={"fieldName":"userId","label":"Select User"} options=useroptions?default("{}")?eval  includeLabelInline=false/>
	        	</div>
		        <div class="col-xs-12 col-sm-6 col-lg-7">
					<#assign serviceTypeList = .data_model["getServiceTypeForView"]>
					<#if serviceTypeList?exists && (serviceTypeList?size>0) >
						<#assign serviceoptions>{"":"Select Service Type",<#list serviceTypeList as uiObject>"${uiObject.serviceTypeId}":"${uiObject.name}"<#if uiObject_has_next>,</#if></#list>}</#assign>
					</#if>
					<@formSingleSelectSSM path="formObject.serviceTypeId" field={"fieldName":"serviceTypeId","label":"Select Service Type"} options=serviceoptions?default("{}")?eval  includeLabelInline=false/>

					<#assign citiesList = .data_model["getCityWithSitesForView"]>
					<#if citiesList?exists && (citiesList?size>0) >
						<#assign citiesoptions>{"":"Select Region",<#list citiesList as uiObject>"${uiObject.geoId}":"${uiObject.name}"<#if uiObject_has_next>,</#if></#list>}</#assign>
					</#if>
					<@formSingleSelectSSM path="formObject.cityGeoId" field={"fieldName":"cityGeoId","label":"Select Region"} options=citiesoptions?default("{}")?eval  includeLabelInline=false/>

					<#assign sitesList = .data_model["getSitesForCity"]?default([])>
					<#if sitesList?exists && (sitesList?size>0) >
						<#assign sitesoptions>{"":"Select Site",<#list sitesList as uiObject>"${uiObject.siteId}":"${uiObject.name}"<#if uiObject_has_next>,</#if></#list>}</#assign>
					</#if>
					<@formSingleSelectSSM path="formObject.siteId" field={"fieldName":"siteId","label":"Select Site"} options=sitesoptions?default("{}")?eval  includeLabelInline=false/>
					<div class="form-group">
						<div class="col-sm-9">
							<@showErrorsSSM "formObject.assets"/>							
							<div><label class="control-label" for="assets">Select Assets</label></div>
							<select class="form-control" name="assets" id="assets" multiple="multiple">			
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