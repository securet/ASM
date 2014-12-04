<#setting number_format="0.##">
<#include "../formMacros.ftl">
<html>
	<head>
		<title>Submit New Ticket</title>
	</head>
	<body>
		<div class="panel panel-primary">
			<div class="panel-heading">
				<h1 class="panel-title" >Submit New Ticket</h1>
			</div>
			<div class="panel-body">
				<#assign mode=mode?default("new")>
				<form class="form-horizontal" role="form" method="POST" action="<@spring.url relativeUrl="/tickets/saveTicket"/>">
					<#assign userAssignedSites = .data_model["userAssignedSites"]>
					<#assign options>{<#list userAssignedSites as uiObject>"${uiObject.Id}":"${uiObject.name}"<#if uiObject_has_next>,</#if></#list>}</#assign>
					<@formSingleSelectSSM path="formObject." field={"fieldName":"Organization"} options=options?eval/>
										
					<div style="text-align:right">
						<input  class="btn btn-primary right" name="submit" type="submit"  value="Save" />
					</div>	              	
					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
				</form>
			</div>
		</div>		
	</body>
</html>