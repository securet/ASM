<#assign fieldTypeMapping = {"int":"text","string":"text","list":"select","file":"file","double":"text","datetime":"datetime","date":"date","boolean","text"}>
<html>
	<head>
		<title>View ${entityName}</title>
		<script src="assets/js/jquery.dataTables.min.js"></script>
		<script src="assets/js/dataTables.responsive.min.js"></script>
		<script src="assets/js/dataTables.bootstrap.js"></script>
		<link rel="stylesheet" href="assets/css/jquery.dataTables.min.css">
		<link rel="stylesheet" href="assets/css/dataTables.bootstrap.css">
		<link rel="stylesheet" href="assets/css/dataTables.responsive.css">
	</head>
	<body>
		<h1> View ${entityName}'s </h1>
		<script type="text/javascript">
			dataUrl="<@spring.url relativeUrl="/admin/listSimpleObjects?entityName=${entityName}&operator=or"/>";
			columnsToDisplay=[<#list formField as field><#assign fieldType = fieldTypeMapping[field.fieldType]?default(field.fieldType)><#if field.canDisplay>{ "data": "<#if fieldType!='text' &&  fieldType!='file' &&  fieldType!='date'>${field.fieldName}.name<#else>${field.fieldName}</#if>" },</#if></#list>];
			function makeEditLink(row,data){
				var cellToModify = $(row).find("td:eq(0)");
				var text = $(cellToModify).html();
				<#assign editPath><#if entityName=="User">createEditUser<#else>createEditObject</#if></#assign>
				$(cellToModify).html('<a href="<@spring.url relativeUrl="/admin/${editPath}?entityName=${entityName}&id="/>'+data.${entityName?uncap_first}Id+'">'+text+'</a>');
				console.log(data);
			}				
		</script>
		<#assign bindingResult = .data_model["org.springframework.validation.BindingResult.formObject"]>
		<#if formObject.name?exists && bindingResult?exists && !bindingResult.hasErrors()>
			<div class="alert alert-info asmnotification" role="alert"><#if createNew>Created<#else>Saved</#if> ${entityName} successfully : ${formObject.name!}</div>
		</#if>
		<div class="pageoptions">
			<div class="btn-group">
				<a href="<@spring.url relativeUrl="/admin/${editPath}?entityName=${entityName}"/>" class="btn btn-default DTTT_button_text" tabindex="0" aria-controls="example">
					<span>New</span>
				</a>
			</div>
		</div>			
		<table id="asmdatatable" class="display table table-striped table-hover dt-responsive" width="100%" cellspacing="0">
	        <thead>
	            <tr>
				<#list formField as field>
					<#if field.canDisplay>
		                <th>${field.fieldName?cap_first}</th>
		            </#if>
				</#list>    
	            </tr>
	        </thead>
	        <tfoot>
	            <tr>
				<#list formField as field>
					<#if field.canDisplay>
		                <th>${field.fieldName?cap_first}</th>
		            </#if>
				</#list>    
	            </tr>
	        </tfoot>
	    </table>
	</body>
</html>