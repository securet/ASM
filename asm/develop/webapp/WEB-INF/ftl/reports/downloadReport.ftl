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
			<#assign colCount = columnNames?size>	
			<#if ticketSummary?exists>
				<#list ticketSummary as ticket>		
					<tr>
						<#list ticket as fieldValue>
							<!-- going by index is not good approach - we will customize the download reports later to have summary object --> 
							<td width="120"><#if fieldValue_index=(colCount-1) && columnNames?seq_contains("TAT")><#if (fieldValue<0) ><#assign fieldValue=0></#if>${statics["com.securet.ssm.utils.SecureTUtils"].formatTimeInHrsMins(fieldValue!0)}<#else>${fieldValue!}</#if></td>
						</#list>	
					</tr>				
			</#list>
		</#if>
		</table>
	</body>
</html>