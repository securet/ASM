<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#setting number_format="0.##">
<#include "../formMacros.ftl">
<html>
	<head>
		<title>Part Order Request</title>
	</head>
	<body>
		<div class="panel panel-primary">
			<div class="panel-heading">
				<h1 class="panel-title" >Part Order Request Status</h1>
			</div>
			<div  id="editTicket" class="panel-body leftcolumns" >
				<div class="col-md-8">
					<form class="form-horizontal" role="form" method="POST" action="<@spring.url relativeUrl="/tickets/updatePORequestStatus"/>" >
						<div class="form-group labelblock">
							<label for="siteId" class="col-sm-3 control-label">Ticket</label>
						    <div id="siteId"  class="col-sm-9">
								<p class="form-control-static normaltext">${partOrderRequest.ticket.ticketId!}</p>
						    </div>
						</div>
						<div class="form-group labelblock">
							<label for="siteId" class="col-sm-3 control-label">Part Name</label>
						    <div id="siteId"  class="col-sm-9">
								<p class="form-control-static normaltext">${partOrderRequest.serviceSparePart.partName!}</p>
						    </div>
						</div>
						<div class="form-group labelblock">
							<label for="siteId" class="col-sm-3 control-label">Part Description</label>
						    <div id="siteId"  class="col-sm-9">
								<p class="form-control-static normaltext">${partOrderRequest.serviceSparePart.partDescription!}</p>
						    </div>
						</div>
						<div class="form-group labelblock">
							<label for="siteId" class="col-sm-3 control-label">Initiator</label>
						    <div id="siteId"  class="col-sm-9">
								<p class="form-control-static normaltext">${partOrderRequest.initiatedBy.userId!}</p>
						    </div>
						</div>
						<#if partOrderRequest.respondedBy?exists>
							<div class="form-group labelblock">
								<label for="siteId" class="col-sm-3 control-label">Approver</label>
							    <div id="siteId"  class="col-sm-9">
									<p class="form-control-static normaltext">${partOrderRequest.respondedBy.userId!}</p>
							    </div>
							</div>
						</#if>	
						<div class="form-group labelblock">
							<label for="siteId" class="col-sm-3 control-label">Cost</label>
						    <div id="siteId"  class="col-sm-9">
								<p class="form-control-static normaltext">${partOrderRequest.cost!}</p>
						    </div>
						</div>
						<div class="form-group labelblock">
							<label for="siteId" class="col-sm-3 control-label">Status</label>
						    <div id="siteId"  class="col-sm-9">
								<p class="form-control-static normaltext">${partOrderRequest.status.enumDescription!}</p>
						    </div>
						</div>
						<div class="btn-group pobutton">
							<a href="<@spring.url relativeUrl="/tickets/modifyTicket?id="+partOrderRequest.ticket.ticketId/>" class="btn btn-primary DTTT_button_text" tabindex="0">
								<span>Back</span>
							</a>
						</div>
						<@security.authorize access="hasAnyRole('ADMIN','CLIENT_USER','CLIENT_CONTROLLER')">
							<#if partOrderRequest.status.enumerationId=="PO_INITIATED">
							<div style="text-align:center">
								<input  class="btn btn-primary right" name="status.enumDescription" type="submit"  value="Authorize" />
								<input  class="btn btn-primary right" name="status.enumDescription" type="submit"  value="Reject" />
							</div>	       
							</#if>	
						</@security.authorize>						
						<@security.authorize access="hasAnyRole('RESOLVER')">
							<#if partOrderRequest.status.enumerationId=="PO_AUTHORIZED">
							<div style="text-align:right">
								<input  class="btn btn-primary right" name="status.enumDescription" type="submit"  value="Complete" />
							</div>	       
							</#if>	
						</@security.authorize>						
						<input type="hidden" name="partOrderRequestId" value="${partOrderRequest.partOrderRequestId!}" />
						<input type="hidden" name="ticket.ticketId" value="${partOrderRequest.ticket.ticketId!}" />
						<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
					</form>
				</div>
			</div>
		</div>
	</body>
</html>					