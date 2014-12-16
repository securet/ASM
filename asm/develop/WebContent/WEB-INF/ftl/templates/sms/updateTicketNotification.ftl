<#if previousStatus?exists && previousStatus=="OPEN" && ticket.status.enumerationId=="WORK_IN_PROGRESS">
Call Log No ${ticket.ticketId} has been moved to Work In Progress
<#elseif previousStatus?exists && previousStatus=="WORK_IN_PROGRESS" && ticket.status.enumerationId=="RESOLVED">
Call Log No  ${ticket.ticketId} has been moved to Resolved. Pending your action.  
<#elseif previousStatus?exists && previousStatus=="RESOLVED" && ticket.status.enumerationId=="WORK_IN_PROGRESS">
Call Log No ${ticket.ticketId} has been moved to Work In Progress, Ticket Resent from Channel Manager
</#if>	
