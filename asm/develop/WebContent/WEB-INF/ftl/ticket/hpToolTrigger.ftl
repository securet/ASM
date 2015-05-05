<#setting number_format="0.##">
<#include "../formMacros.ftl">
<html>
	<head>
		<title>HP Toool Trigger</title>
		<script src="assets/js/typeahead.min.js"></script>
	</head>
	<body>
		<form class="form-horizontal" role="form" method="POST" action="<@spring.url relativeUrl="/tickets/hpToolTriggerSubmit"/>">
			<#if saved??>
				<div class="alert alert-success" role="alert">${saved}</div>
			</#if>	
			<#if error??>
			<div class="alert alert-danger" role="alert">
			  <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
			  <span class="sr-only">Error:</span> Please check  your input!
			</div>
			</#if>
			<#assign field = {"label":"Site", "fieldName":"terminalId","fieldName":"terminalId"}>
			<@autoSuggestField  path="hpInput.terminalID" field=field/>
			<div style="text-align:center">
				<input  class="btn btn-primary right" name="trigger" type="submit"  value="start cashout" />
				<input  class="btn btn-primary right" name="trigger" type="submit"  value="end cashout" />
			</div>	       
			<input type="hidden" name="fault" value="cash out"/>
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
		</form>
	</body>
</html>	