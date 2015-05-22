<html>
	<head>
	</head>
	<body>	
		<table border="1">
			<tr>
				<#list columnNames as columnName> 
					<th width="120"><b>${columnName}</b></th>
				</#list>	
			</tr>
			<#if ticketSummary?exists>
				<#list ticketSummary as ticket>		
					<tr>
						<#list ticket as fieldValue>
							<td width="120">${fieldValue!}</td>
						</#list>	
					</tr>				
			</#list>
		</#if>
		</table>
	</body>
</html>