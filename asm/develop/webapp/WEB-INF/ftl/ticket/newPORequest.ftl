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
				<h1 class="panel-title" >Part Order Request</h1>
			</div>
			<div  id="editTicket" class="panel-body leftcolumns" >
				<div class="col-md-8">
					<form class="form-horizontal" role="form" method="POST" action="<@spring.url relativeUrl="/tickets/initiatePORequest"/>?${_csrf.parameterName}=${_csrf.token}" enctype="multipart/form-data" >
						<div class="form-group labelblock">
							<label for="siteId" class="col-sm-3 control-label">Ticket</label>
						    <div id="siteId"  class="col-sm-9">
								<p class="form-control-static normaltext">${partOrderRequest.ticket.ticketId!}</p>
						    </div>
						</div>
						<#if vendorServiceSpareParts?exists && (vendorServiceSpareParts?size>0)>
							<#if vendorServiceSpareParts?exists && (vendorServiceSpareParts?size>0) >
								<#assign vendorServiceSparePartOptions>{"0":"Select Spare Part",<#list vendorServiceSpareParts as serviceSparePart>"${serviceSparePart.sparePartId}":"${serviceSparePart.partName +" - " + serviceSparePart.cost}"<#if serviceSparePart_has_next>,</#if></#list>}</#assign>
							</#if>
							<@formSingleSelectSSM path="partOrderRequest.serviceSparePart.sparePartId" field={"fieldName":"partOrderRequest.serviceSparePart.sparePartId","label":"Select Spare Part"} options=vendorServiceSparePartOptions?default("{}")?eval />
						<#else>
							<div class="form-group labelblock">
								<label for="siteId" class="col-sm-3 control-label"></label>
							    <div id="siteId"  class="col-sm-9">
									<p class="form-control-static normaltext">No Spares available for your organization, please contact administrator to add them!</p>
							    </div>
							</div>
						</#if>	
						<input type="hidden" name="ticket.ticketId" value="${partOrderRequest.ticket.ticketId!}" />
						<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
						<div style="text-align:right">
							<input  class="btn btn-primary right" name="submit" type="submit"  value="Initiate PO" />
						</div>	       
					</form>
				</div>
			</div>
		</div>
	</body>
</html>					