<#assign fieldTypeMapping = {"int":"text","string":"text","list":"select","file":"file","double":"text","datetime":"datetime","date":"date","site":"suggestbox"}>
<#assign dataTypeMapping={"int":"number","double":"number","organization":"number","assetType":"number","module":"number"}>
<#include "formMacros.ftl">
<#assign includeDateScripts = false>
<#setting number_format="0.##">
<#list formField as field>
	<#if !field.canDisplay && field.fieldType=='multipartFile'>
		<#-- do nothing these field have a file binding -->
	<#elseif !field.canDisplay && !field.fieldName?contains("Timestamp")>
		<#if fieldTypeMapping[field.fieldType]?exists>
			<@formInputSSM path="formObject.${field.fieldName}" field=field fieldType="hidden"/>		
		<#else>
			<@formInputSSM path="formObject.${field.fieldName}.${field.fieldName}Id" field=field fieldType="hidden"/>
		</#if>
	<#elseif field.canDisplay && (fieldTypeMapping[field.fieldType]?exists && fieldTypeMapping[field.fieldType]='suggestbox')>
		<#assign fieldOne = {"label":(field.fieldName?cap_first), "fieldName":"formObject."}>
		<@autoSuggestField  path="formObject.${field.fieldName}.${field.fieldName}Id" field=field/> 
	<#elseif fieldTypeMapping[field.fieldType]?exists && (fieldTypeMapping[field.fieldType]='text'|| fieldTypeMapping[field.fieldType]='file' || fieldTypeMapping[field.fieldType]='datetime' || fieldTypeMapping[field.fieldType]='date')>
	    <@formInputSSM path="formObject.${field.fieldName}" field=field fieldType=fieldTypeMapping[field.fieldType]/>
	<#elseif .data_model["get"+field.fieldName?cap_first+"ForView"]?exists>
		<#assign uiObjects = .data_model["get"+field.fieldName?cap_first+"ForView"]>
		<#assign fieldStr = ("uiObject.${field.fieldName}Id")>
		<#assign fieldIdPrefix = field.fieldName>
		<#if field.fieldType='geo'>
			<#assign fieldIdPrefix = "geo">
			<#assign fieldStr = ("uiObject.geoId")>
		</#if>
		<#attempt>
			<#-- Check  if the options are evaluating  to any object if not it is a custom list -->
			<#assign options>{"<#if dataTypeMapping[field.fieldType]?default("")=='number'>0</#if>":"Select ${field.fieldName?cap_first}"<#if (uiObjects?size>0)>, </#if><#list uiObjects as uiObject>"${fieldStr?eval}":"${uiObject.name}"<#if uiObject_has_next>,</#if></#list>}</#assign>
			<#assign fieldIdPrefix = field.fieldName+"."+fieldIdPrefix+"Id">
		<#recover>
			<#-- this is array based key value -- i.e 0-1 index based -->
			<#assign options>{"<#if dataTypeMapping[field.fieldType]?default("")=='number'>0</#if>":"Select ${field.fieldName?cap_first}"<#if (uiObjects?size>0)>, </#if><#list uiObjects as uiObject>"${uiObject[0]}":"${uiObject[1]}"<#if uiObject_has_next>,</#if></#list>}</#assign>
		</#attempt>
		<@formSingleSelectSSM path="formObject.${fieldIdPrefix}" field=field options=options?eval attributes=dataAttributes/>
	<#elseif field.canDisplay>
		<#-- include a hidden span -->
		<#assign fieldStr = ("uiObject.${field.fieldName}Id")>
		<#assign fieldIdPrefix = field.fieldName>
		<#if field.fieldType='geo'>
			<#assign fieldIdPrefix = "geo">
			<#assign fieldStr = ("uiObject.geoId")>
		</#if>
		<#attempt>
			<#-- Check  if the options are evaluating  to any object if not it is a custom list -->
			<#assign fieldIdPrefix = field.fieldName+"."+fieldIdPrefix+"Id">
			<@spring.bind path="formObject.${fieldIdPrefix}"/>
			<span id="default-${fieldIdPrefix}" class="hide" data-default="${spring.stringStatusValue}"></span>
		<#recover>
		</#attempt>
		
	</#if>
	<#if !includeDateScripts && (field.fieldType="datetime" || field.fieldType="date")>
		<#assign includeDateScripts = true>
	</#if>
</#list>
<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
<#if includeDateScripts>
	<script src="assets/js/moment.min.js"></script>
	<script src="assets/js/bootstrap-datetime.min.js"></script>
	<link rel="stylesheet"  href="assets/css/bootstrap-datetimepicker.min.css"/>
</#if>