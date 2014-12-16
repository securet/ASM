<html>
	<head></head>
	<body>
		<div>
			<#if previousStatus?exists && previousStatus=="OPEN" && ticket.status.enumerationId=="WORK_IN_PROGRESS">
				<div> Call Log No ${ticket.ticketId} for SBI ATM ${ticket.site.name!} at ${ticket.site.area!} has been assigned and moved to ${ticket.status.enumDescription!}.</br></div> 
				<div> You will be updated on the progress of this call.<br/><br/></div>
			<#elseif previousStatus?exists && previousStatus=="WORK_IN_PROGRESS" && ticket.status.enumerationId=="RESOLVED">
				<div> Call Log No ${ticket.ticketId} for SBI ATM ${ticket.site.name!} at ${ticket.site.area!} has been Resolved. Please confirm resolution and update ticket details.</br/><br/></div>
			<#elseif previousStatus?exists && previousStatus=="RESOLVED" && ticket.status.enumerationId=="WORK_IN_PROGRESS">
				<div> Call Log  No :${ticket.ticketId} for SBI ATM ${ticket.site.name!} at ${ticket.site.area!}   has resent for your action <br/></div>
				<div>Please look into the matter and update your action on <a href="http://asm.securet.in/tickets/modifyTicket?id=${ticket.ticketId}">asm.securet.in</a><br/><br/></div>
			</#if>	
			<div>Regards,</div>
			<div>SBI - ATM Services</div>
			<div>This is an auto generated mail. Please do not reply to it.</div>
		</div>
	</body>
</html>