<#include "../formMacros.ftl">
<#setting number_format="0.##">
<html>
	<head>
		<title>Assign Sites to clients</title>
		<script id="userOptionTmpl" type="text/x-jsrender">
		  <option value='{{:userId}}'>{{:userId}} ({{:fullName}})</option>
		</script>		
		<script id="siteOptionTmpl" type="text/x-jsrender">
		  <option value='{{:siteId}}' {{if siteSelected }} selected='selected' {{/if}} >{{:name}}</option>
		</script>		
	</head>
	<body>
		<div class="panel panel-primary">
			<div class="panel-heading">
				<h1 class="panel-title" >Assign Sites</h1>
			</div>
			<div class="panel-body">
				<#if saved?exists && saved?has_content>
					<div class="alert alert-info asmnotification" role="alert">${saved}</div>
				</#if>	
				<form id="clientUserSiteMapId" class="form-horizontal" role="form" method="POST" action="<@spring.url relativeUrl="/admin/saveClientUserSiteMapping"/>">
					<div class="row">
						<div class="col-xs-12  col-lg-5">
<#--
							<#assign organizationsList = .data_model["getClientOrganizationForView"]>
							<#if organizationsList?exists && (organizationsList?size>0)>
								<#assign options>{"":"Select Organization",<#list organizationsList as uiObject>"${uiObject.organizationId}":"${uiObject.name}"<#if uiObject_has_next>,</#if></#list>}</#assign>
							<#else>
								<#assign options>{"":"Select Organization"}</#assign>
							</#if>
							<@formSingleSelectSSM path="formObject.organizationId" field={"fieldName":"organizationId","label":"Select Organization"} options=options?eval  includeLabelInline=false/>
-->		

							<#assign usersList = .data_model["getAllClientUsers"]?default([])>
							<#if usersList?exists && (usersList?size>0) >
								<#assign useroptions>{"":"Select User",<#list usersList as uiObject>"${uiObject.userId}":"${uiObject.userId?lower_case} (${uiObject.fullName})"<#if uiObject_has_next>,</#if></#list>}</#assign>
							</#if>
							<@formSingleSelectSSM path="formObject.userId" field={"fieldName":"userId","label":"Select User"} options=useroptions?default("{}")?eval  includeLabelInline=false/>
		
			        	</div>
				        <div class="col-xs-12 col-sm-6 col-lg-7">
							<#assign citiesList = .data_model["getCityWithSitesForView"]>
							<#if citiesList?exists && (citiesList?size>0)>
								<#assign options>{"":"Select Region",<#list citiesList as uiObject>"${uiObject.geoId}":"${uiObject.name}"<#if uiObject_has_next>,</#if></#list>}</#assign>
							<#else>	
								<#assign options>{"":"Select Region"}</#assign>
							</#if>
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
			</div>
		</div>		
		<div class="panel panel-primary">
			<div class="panel-heading">
				<h1 class="panel-title" >Transfer Sites</h1>
			</div>
			<div class="panel-body">
				<form id="transferClientUserSite" class="form-horizontal" role="form" method="POST" action="<@spring.url relativeUrl="/admin/transferClientUserSiteMapping"/>">
					<#if transferError??>
						<div class="alert alert-danger" role="alert">
						  <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
						  <span class="sr-only">Error:</span>
							${transferError.defaultMessage!}
						</div>	
						<script type="text/javascript">
							$(document).ready(function(){
								window.location=window.location+"#transferClientUserSite";
							});
						</script>	
					</#if>

					<@formSimpleSingleSelect field={"fieldName":"transferFromUserId","label":"From User"} selectedValue=transferFromUserId?default("")  options=useroptions?default("{}")?eval  />
					<@formSimpleSingleSelect field={"fieldName":"transferToUserId","label":"To User"} selectedValue=transferToUserId?default("") options=useroptions?default("{}")?eval />
					<#if transferError??>
						<#assign selectedValue = RequestParameters.replicate?default("")>
					</#if>
					<@simpleCheckboxSSM field={"fieldName":"replicate","label":"Replicate","fieldValue":"true"} selectedValue=selectedValue?default("")  />
		      		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
					<div style="text-align:right">
						<input  class="btn btn-primary right" name="submit" type="submit"  value="Transfer" />
					</div>	              	
				</form>
			</div>			
		</div>			
	</body>
</html>