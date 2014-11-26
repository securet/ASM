<html>
	<head>
		<title>Create/Edit ${entityName}</title>
	</head>
	<body>
		<h1>Create/Edit ${entityName} </h1>
		<form class="form-horizontal" role="form" method="POST" action="<@spring.url relativeUrl="/admin/save${entityName}"/>">
			<#include "../dynamicForm.ftl">
			<div style="text-align:right">
				<input  class="btn btn-primary right" name="submit" type="submit"  value="Save" />
			</div>	              	
		</form>
	</body>
</html>