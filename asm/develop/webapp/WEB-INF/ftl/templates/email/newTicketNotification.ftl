<html>
	<head></head>
	<body>
		<div>
			<div>A new Call Log  No : ${ticket.ticketId} has been logged for SBI ATM site ${ticket.site.name!} at ${ticket.site.area!}, ${ticket.site.city.name!}, <#if ticket.site.module?exists && ticket.site.circle?exists>${ticket.site.module.name!} in ${ticket.site.circle.name!} circle</#if>  <br/></div>
			<div>Channel Manager : ${ticket.reporter.fullName!}</div> 
			<div>Channel Manager : ${ticket.reporter.emailId!}</div> 
			<div>Channel Manager : ${ticket.reporter.mobile!}<br/></div> 
			<div>Please look into the matter and update your action on <a href="http://asm.securet.in/tickets/modifyTicket?id=${ticket.ticketId}">asm.securet.in</a><br/><br/></div>
			<div>Regards,</div>
			<div>SBI - ATM Services</div>
			<div>This is an auto generated mail. Please do not reply to it.</div>
		</div>
	</body>
</html>