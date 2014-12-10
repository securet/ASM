<#assign formEncodingType={"Organization":"multipart"}><#-- ensure this is configured using field type -->
<html>
	<head>
		<title>Create/Edit ${entityName}</title>
		<script id="logoPreviewTemplate" type="text/x-jsrender">
			<div id="preview-1-0" class="file-preview-frame">
			   <img style="width:100%;height:auto;" alt="logo" title="logo" src="{{:path}}" class="file-preview-image"/>
			</div>
		</script>		
		<script id="geoTmpl" type="text/x-jsrender">
		 	<option value='{{:geoId}}' {{if geoSelected }} selected='selected' {{/if}} >{{:name}}</option>
		</script>		
		<script id="selectBoxTmpl" type="text/x-jsrender">
			<div class="form-group">
				<label for="{{:elementId}}" class="col-sm-3 control-label">Select {{:elementLabel}}</label>
				<div class="col-sm-9">
					<select class="form-control" name="{{:elementId}}" id="{{:elementId}}">
						<option value="">Select {{:elementLabel}}</option>
						{{for options}}
							<option value="{{:value}}" {{if geoSelected }} selected='selected' {{/if}} >{{:text}}</option>
						{{/for}}	
					</select>
				</div>	
			</div>	
		</script>		
	</head>
	<body>
		<div class="panel panel-primary">
			<div class="panel-heading">
				<h1 class="panel-title" >Create/Edit ${entityName} </h1>
			</div>
			<div class="panel-body">
				<form id="${entityName}Form" class="form-horizontal" role="form" method="POST" action="<@spring.url relativeUrl="/admin/save${entityName}"/><#if formEncodingType[entityName]?default("")=="multipart">?${_csrf.parameterName}=${_csrf.token}</#if>" <#if formEncodingType[entityName]?default("")=="multipart">enctype="multipart/form-data"</#if> >
					<#include "../dynamicForm.ftl">
					<div style="text-align:right">
						<input  class="btn btn-primary right" name="submit" type="submit"  value="Save" />
					</div>	              	
				</form>
			</div>
	    </div>
	</body>
</html>