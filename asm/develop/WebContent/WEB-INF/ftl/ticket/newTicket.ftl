<#setting number_format="0.##">
<#include "../formMacros.ftl">
<html>
	<head>
		<title>Submit New Ticket</title>
		<script id="errorMessageTmpl" type="text/x-jsrender">
			<div id="{{:elementId}}" class="alert alert-danger" role="alert">
			  <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
			  <span class="sr-only">Error:</span>
			  <span class="messagebox">{{:message}}</div>
			</div>
		</script>		
		<script id="selectBoxTmpl" type="text/x-jsrender">
			<div class="form-group">
				<label for="{{:elementId}}" class="col-sm-3 control-label">Select {{:elementLabel}}</label>
				<div class="col-sm-9">
					<select class="form-control" name="{{:elementId}}" id="{{:elementId}}">
						<option value="">Select {{:elementLabel}}</option>
						{{for options}}
							<option value="{{:value}}">{{:text}}</option>
						{{/for}}	
					</select>
				</div>	
			</div>	
		</script>		
	</head>
	<body>
		<div class="panel panel-primary">
			<div class="panel-heading">
				<h1 class="panel-title" >Submit New Ticket</h1>
			</div>
			<div  id="newTicket" class="panel-body" >
				<#assign mode=mode?default("new")>
				<form class="form-horizontal" role="form" method="POST" action="<@spring.url relativeUrl="/tickets/saveTicket"/>?${_csrf.parameterName}=${_csrf.token}" enctype="multipart/form-data" >
					<#assign contextData="">
					<#if userAssignedSites?exists && (userAssignedSites?size>0)>
						<#assign optionsText="{\"0\":\"Select Site\",">
						<#assign contextData="{">
						<#list userAssignedSites as assignedSite>			
							<#assign contextData = contextData +"\""+assignedSite.siteId+"\":{\"name\":\""+assignedSite.name+"\",\"area\":\""+ assignedSite.area+"\"}">
							<#assign optionsText = optionsText +"\""+assignedSite.siteId+"\":\""+assignedSite.name+"\"">
							<#if assignedSite_has_next>			
								<#assign optionsText = optionsText+",">
								<#assign contextData = contextData+",">
							</#if>
						</#list>
						<#assign optionsText=optionsText+"}">
						<#assign contextData=contextData+"}">
					<#else>
						<#assign optionsText>{"0":"You have not been assigned a site"}</#assign>
					</#if>
					<@formSingleSelectSSM path="formObject.site.siteId" field={"fieldName":"site.siteId","label":"Site"} options=optionsText?eval attributes="data-site='${contextData}'"/>
					<#assign hideSite="hide">
					<#assign area="">
					<#if selectedSite?exists>		
						<#assign hideSite="">
						<#assign area=selectedSite.area!>
					</#if>
					<div id="areablock" class="form-group labelblock ${hideSite}">
						<label for="area" class="col-sm-3 control-label">Area </label>
					    <div class="col-sm-9">
							<p id="area" class="form-control-static normaltext">${area}</p>
					    </div>
					</div>

					<#if serviceTypes?exists && (serviceTypes?size>0) >
						<#assign serviceOptions>{"0":"Select Service",<#list serviceTypes as serviceType>"${serviceType.serviceTypeId}":"${serviceType.name}"<#if serviceType_has_next>,</#if></#list>}</#assign>
					</#if>
					<#assign serviceDisabledAttr = "">
					<#if (formObject.site.siteId==0)>
						<#assign serviceDisabledAttr = "disabled='disabled'">
					</#if>
					<@formSingleSelectSSM path="formObject.serviceType.serviceTypeId" field={"fieldName":"serviceType.serviceTypeId","label":"Select Service"} options=serviceOptions?default("{}")?eval attributes=serviceDisabledAttr/>
					<#assign hideVendor="hide">	
					<#assign organizationName = "">	
					<#assign vendorName = "">	
					<#if vendors?exists>
						<#assign hideVendor = "">
						<#assign vendorName = vendors.userId>	
						<#assign organizationName = vendors.organization.name>	
					</#if>
					<div id="vendorOrgblock" class="form-group labelblock ${hideVendor}">
						<label for="vendorOrg" class="col-sm-3 control-label">Vendor Organization</label>
					    <div class="col-sm-9">
							<p id="vendorOrg" class="form-control-static normaltext">${organizationName!}</p>
					    </div>
					</div>
					<div id="vendorUserblock" class="form-group labelblock ${hideVendor}">
						<label for="vendorUser" class="col-sm-3 control-label">Vendor User</label>
					    <div class="col-sm-9">
							<p id="vendorUser" class="form-control-static normaltext">${vendorName!}</p>
					    </div>
					</div>
					<#if issueTypes?exists && (issueTypes?size>0)>
						<#if issueTypes?exists && (issueTypes?size>0) >
							<#assign issueTypeOptions>{"":"Select Issue Type",<#list issueTypes as type>"${type.issueTypeId}":"${type.name}"<#if type_has_next>,</#if></#list>}</#assign>
						</#if>
						<@formSingleSelectSSM path="formObject.issueType.issueTypeId" field={"fieldName":"formObject.issueType.issueTypeId","label":"Select Issue Type"} options=issueTypeOptions?default("{}")?eval />
					</#if>	
					<#if severity?exists && (severity?size>0) >
						<#assign severityOptions>{"":"Select Severity",<#list severity as type>"${type[0]}":"${type[1]}"<#if type_has_next>,</#if></#list>}</#assign>
					</#if>
					<@formSingleSelectSSM path="formObject.severity.enumerationId" field={"fieldName":"formObject.severity.enumerationId","label":"Select Severity"} options=severityOptions?default("{}")?eval />
					<@simpleInputFieldSSM  field={"fieldName":"ticketAttachments","label":"Attachment","fieldType":"file"}  attributes="multiple"/>
					<@formTextAreaSSM path="formObject.description" field={"fieldName":"description","label":"Description"} attributes="class=\"form-control\"" />
					<div style="text-align:right">
						<input  class="btn btn-primary right" name="submit" type="submit"  value="Save" />
					</div>	              	
					<input type="hidden" name="latitude" value="0.0" />
					<input type="hidden" name="longitude" value="0.0" />
					<input type="hidden" name="source" value="WEB" />
					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
				</form>
			</div>
		</div>		
	</body>
</html>