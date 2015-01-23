<#macro formInputSSM path field attributes="" fieldType="text" >
	<@spring.bind path/>
	<#if fieldType="hidden">
		<input type="hidden" id="${spring.status.expression}" name="${spring.status.expression}" value="${spring.stringStatusValue}"<@spring.closeTag/>
	<#else>
		<#if spring.status.errorCodes?exists && (spring.status.errorCodes?size >0) >
			<div class="alert alert-danger" role="alert">
			  <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
			  <span class="sr-only">Error:</span>
			  <@spring.showErrors "spring.status.expression"/>
			</div>
		</#if>	
		<div class="form-group">
			<label class="col-sm-3 control-label" for="${field.fieldName}"><#if field.label?exists>${field.label}<#else>${field.fieldName?cap_first}</#if></label>
			<div class="col-sm-9<#if fieldType=='checkbox'> checkbox</#if><#if fieldType="datetime" || fieldType="date"> input-group  date</#if>" id="${spring.status.expression}Wrap">
		    	<input id="${spring.status.expression}" <#if fieldType="date">data-date-format="DD-MM-YYYY"</#if> class="<#if fieldType!='checkbox'>form-control</#if> <#if fieldType="file">asminputfile</#if>" type="<#if fieldType="datetime" || fieldType="date">text<#else>${fieldType}</#if>" name="${spring.status.expression}<#if fieldType="file">File</#if>" value="<#if fieldType!="password">${spring.stringStatusValue}</#if>" ${attributes} <@spring.closeTag/>
				<#if fieldType=="file" && spring.stringStatusValue?exists && spring.stringStatusValue?has_content><input id="${spring.status.expression}-hidden" type="hidden" name="${spring.status.expression}" value="${spring.stringStatusValue}"/></#if>
				<#if fieldType="datetime" || fieldType="date"><span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span></#if>
			</div>
		</div>
		<#if fieldType="datetime" || fieldType="date">
			<script type="text/javascript">
				$(document).ready(function(){
					$("#${spring.status.expression}").datetimepicker({
						<#if fieldType="date">pickTime: false</#if>
					});
				});
			</script>
		</#if>	
	</#if> 
</#macro>

<#macro formSingleSelectSSM path field options attributes="" includeLabelInline=true>
	<@spring.bind path/>
	<#if includeLabelInline && spring.status.errorCodes?exists && (spring.status.errorCodes?size >0) >
		<div class="alert alert-danger" role="alert">
		  <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
		  <span class="sr-only">Error:</span>
		  <@spring.showErrors "spring.status.expression"/>
		</div>
	</#if>	
	<div class="form-group">
		<#if includeLabelInline>
			<label class="col-sm-3 control-label" for="${spring.status.expression}"><#if field.label?exists>${field.label}<#else>${field.fieldName?cap_first}</#if></label>
		</#if>
		<div class="col-sm-9">
			<#if !includeLabelInline>
				<#if spring.status.errorCodes?exists && (spring.status.errorCodes?size >0) >
					<div class="alert alert-danger" role="alert">
					  <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
					  <span class="sr-only">Error:</span>
					  <@spring.showErrors "spring.status.expression"/>
					</div>
				</#if>	
				<div><label class="control-label" for="${spring.status.expression}"><#if field.label?exists>${field.label}<#else>${field.fieldName?cap_first}</#if></label></div>
			</#if>
			<#assign attributesStr> class="form-control" ${attributes} data-default="${spring.stringStatusValue}" </#assign>
			<@spring.formSingleSelect path=path options=options attributes=attributesStr/>
		</div>
	</div>
</#macro>

<#macro formMultiSelectSSM path field options attributes="" includeLabelInline=true>
	<div class="form-group">
		<#if includeLabelInline>
			<label class="col-sm-3 control-label" for="${field.fieldName}.${field.fieldName}Id">${field.fieldName?cap_first}</label>
		</#if>	
		<div class="col-sm-9">
			<#if !includeLabelInline>
				<label class="control-label" for="${field.fieldName}">${field.label}</label>
			</#if>
			<#assign attributesStr> class="form-control" ${attributes}</#assign>	
			<@spring.formMultiSelect path=path options=options attributes=attributesStr/>
		</div>
	</div>
</#macro>

<#macro formCheckboxSSM path field attributes="">
	<@spring.bind path />
	<#if spring.status.errorCodes?exists && (spring.status.errorCodes?size >0) >
		<div class="alert alert-danger" role="alert">
		  <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
		  <span class="sr-only">Error:</span>
		  <@spring.showErrors "spring.status.expression"/>
		</div>
	</#if>
	<div class="form-group">
		<label class="col-sm-3 control-label" for="${field.fieldName}">${field.fieldName?cap_first}</label>
		<div class="col-sm-9 checkbox" id="${spring.status.expression}">
			<@spring.formCheckbox path />
		</div>
	</div>	
</#macro>

<#macro formTextAreaSSM path field attributes="" empty=false>
	<@spring.bind path />
	<#if spring.status.errorCodes?exists && (spring.status.errorCodes?size >0) >
		<div class="alert alert-danger" role="alert">
		  <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
		  <span class="sr-only">Error:</span>
		  <@spring.showErrors "spring.status.expression"/>
		</div>
	</#if>
	<div class="form-group">
		<label class="col-sm-3 control-label" for="${field.fieldName}">${field.fieldName?cap_first}</label>
		<div class="col-sm-9 checkbox" id="${spring.status.expression}">
			 <textarea id="${spring.status.expression?replace('[','')?replace(']','')}" name="${spring.status.expression}" ${attributes}><#if empty><#else>${spring.stringStatusValue}</#if></textarea>
		</div>
	</div>	
</#macro>


<#macro showErrorsSSM path>
	<@spring.bind path />
	<#if spring.status.errorCodes?exists && (spring.status.errorCodes?size >0) >
		<div class="alert alert-danger" role="alert">
		  <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
		  <span class="sr-only">Error:</span>
		  <@spring.showErrors "spring.status.expression"/>
		</div>
	</#if>	
</#macro>

<#macro simpleInputFieldSSM field  attributes="">
	<div class="form-group">
		<label for="${field.fieldName}" class="col-sm-3 control-label">${field.label}</label>
		<div class="col-sm-9">
			<input id="${field.fieldName}" name="${field.fieldName}" value="${field.fieldValue!}" type="${field.fieldType}" ${attributes}/>
		</div>	
	</div>	
</#macro>

<#macro autoSuggestField path field  attributes="">
	<@formInputSSM path=path?replace(field.fieldName+"Id","name") field=field attributes=attributes fieldType="text" />
	<@formInputSSM path=path field=field attributes=attributes fieldType="hidden" />
</#macro>


<#macro ticketTimeLine ticket>
	<#if ticket?exists>
		<li class="timeline-inverted">
			<div class="timeline-badge"><i class="glyphicon <#if ticket.modifiedBy.organization.organizationType=='VENDOR'>glyphicon-wrench<#else>glyphicon-user</#if>"></i></div>
			<div class="timeline-panel">
				<div class="timeline-heading">
		  			<p><small class="text-muted"><i class="glyphicon glyphicon-time"></i> posted by ${ticket.modifiedBy.userId!} @  ${ticket.lastUpdatedTimestamp?string!} </small></p>
		    	</div>
			    <div class="timeline-body">
					<p>${ticket.description!}</p>
			    </div>
			</div> 
		</li>
	</#if>	
</#macro>
