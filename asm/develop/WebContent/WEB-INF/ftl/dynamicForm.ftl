<#assign fieldTypeMapping = {"int":"text","string":"text","list":"select","file":"file","double":"text","datetime":"datetime","date":"date"}>
<#include "formMacros.ftl">
<#assign includeDateScripts = false>
<#setting number_format="0.##">
<#list formField as field>
	<#if !field.canDisplay && !field.fieldName?contains("Timestamp")>
		<@formInputSSM path="formObject.${field.fieldName}" field=field fieldType="hidden"/>
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
		<#assign options>{<#list uiObjects as uiObject>"${fieldStr?eval}":"${uiObject.name}"<#if uiObject_has_next>,</#if></#list>}</#assign>
		<@formSingleSelectSSM path="formObject.${field.fieldName}.${fieldIdPrefix}Id" field=field options=options?eval/>
	</#if>
	<#if !includeDateScripts && (field.fieldType="datetime" || field.fieldType="date")>
		<#assign includeDateScripts = false>
	</#if>
</#list>
<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

<script src="assets/js/moment.min.js"></script>
<script src="assets/js/bootstrap-datetime.min.js"></script>
<link rel="stylesheet"  href="assets/css/bootstrap-datetimepicker.min.css"/>