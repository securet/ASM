<html>
	<head></head>
	<body>
		<div>
			You got a new Ticket ${ticket.ticketId} in ${ticket.serviceType.name} which is assigned to 
			<#if ticket.resolver?exists>${ticket.resolver.userId} who is from ${ticket.resolver.organization.name}<#else>no vendor</#if>
		</div>
		<br/>
		<br/>
		<div>		
			Description: ${ticket.description}
		</div>
	</body>
</html>