<#assign statusColor ={"OPEN":"#d9534f","WORK_IN_PROGRESS":"#5bc0de","RESOLVED":"#f0ad4e","CLOSED":"#5cb85c"}>
<#assign baseUrl = "/tickets/listAllTickets?${_csrf.parameterName}=${_csrf.token}&entityName=Ticket&operator=or">
<#macro ticketStatusHolder status text>
	<div class="col-md-6 placeholder">
		<div class="bold" style="height:50px;">${status.value}</div>
		<a class="ticketStatusFilter" href="javascript:void(0)" data-filter="${status.value}" data-href="<@spring.url relativeUrl=baseUrl+"&filterStatus="+status.key/>"> <img data-src="holder.js/150x150/auto/${statusColor[status.key]}:#ffffff/size:20/text:${text}" class="img-responsive" alt="Generic placeholder thumbnail"></a>
	</div>	
</#macro>
<html>
	<head>
		<title>ASM Dashboard</title>
		<link rel="stylesheet" href="assets/css/jquery.dataTables.min.css">
		<link rel="stylesheet" href="assets/css/dataTables.bootstrap.css">
		<link rel="stylesheet" href="assets/css/dataTables.responsive.css">	
		<script type="text/javascript" src="https://www.google.com/jsapi"></script>
		<script src="assets/js/holder.js"></script>
		<script src="assets/js/jquery.dataTables.min.js"></script>
		<script src="assets/js/dataTables.responsive.min.js"></script>
		<script src="assets/js/dataTables.bootstrap.js"></script>
		<script src="assets/js/reports.js"></script>
		<script type="text/javascript">
      		google.load("visualization", "1", {packages:["corechart"]});
		</script>
	</head>	
	<body>
		<div class="panel panel-primary">
			<div class="panel-heading">
				<h1 class="panel-title" > Dashboard </h1>
			</div>
			<div class="panel-body reports">
				<div class="row marginleft0">
					<div id="displayTicketStatusCount" class="col-md-4 roundbordersmall minichart">
					<#if ticketStatusSummary?exists>
						<#assign openCountPercentage = ((ticketStatusSummary.openCount/ticketStatusSummary.totalCount)*100)>
						<@ticketStatusHolder status={"key":"OPEN","value":"Open"} text="${ticketStatusSummary.openCount} \\n ${openCountPercentage?round}%"/>
						<#assign wipCountPercentage = ((ticketStatusSummary.workInProgressCount/ticketStatusSummary.totalCount)*100)>
						<@ticketStatusHolder status={"key":"WORK_IN_PROGRESS","value":"Work In Progress"} text="${ticketStatusSummary.workInProgressCount} \\n ${wipCountPercentage?round}%"/>
						<#assign resolvedCountPercentage = ((ticketStatusSummary.resolvedCount/ticketStatusSummary.totalCount)*100)>
						<@ticketStatusHolder status={"key":"RESOLVED","value":"Resolved"} text="${ticketStatusSummary.resolvedCount} \\n ${resolvedCountPercentage?round}%"/>
						<#assign closedCountPercentage = ((ticketStatusSummary.closedCount/ticketStatusSummary.totalCount)*100)>
						<@ticketStatusHolder status={"key":"CLOSED","value":"Closed"} text="${ticketStatusSummary.closedCount} \\n ${closedCountPercentage?round}%"/>
					</#if>	
					</div>
					<div  class="col-md-8">
						<div class="col-md-12 placeholders roundbordersmall">
							<div id="filterNotification" class="alert alert-success hide" role="alert"></div>
							<script type="text/javascript">
								dataUrl="<@spring.url relativeUrl="${baseUrl}"/>";
								columnsToDisplay=[{ "data": "ticketId" },{ "data": "site.name" },{ "data": "serviceType.name" },{"name":"Description", "data": "shortDesc"},{ "data": "status.enumDescription" },{ "data": "resolver.organization.name","defaultContent":"None" },{ "data": "resolver.userId", "defaultContent":"None" },{ "data": "issueType.name","defaultContent":"None" },{ "data": "ticketType.enumDescription","defaultContent":"None" }];
							</script>
							<table id="alltickettable" class="display table table-striped table-hover dt-responsive" width="100%" cellspacing="0">
						        <thead>
						            <tr>
						                <th>Ticket Id</th>
						                <th>SiteId</th>
						                <th>ServiceType</th>
						                <th>Description</th>
						                <th>Status</th>
						                <th>Vendor Organization</th>
						                <th>Vendor User</th>
						                <th>Issue Type</th>
						                <th>Ticket Type</th>
						            </tr>
						        </thead>
						        <tfoot>
						            <tr>
						                <th>Ticket Id</th>
						                <th>SiteId</th>
						                <th>ServiceType</th>
						                <th>Description</th>
						                <th>Status</th>
						                <th>Vendor Organization</th>
						                <th>Vendor User</th>
						                <th>Issue Type</th>
						                <th>Ticket Type</th>
						            </tr>
						        </tfoot>
						    </table>
						</div>
					</div>
				</div>
				<div class="row marginleft0 margintop10">
					<#assign  countByVendorString>[<#list ticketCountByVendorUser as countofUser>["${countofUser[0]}",${countofUser[1]}]<#if countofUser_has_next>,</#if></#list>]</#assign>
					<div  class="col-md-7 dashboardcol2">
						<div class="col-md-12 placeholders roundbordersmall">
							<#assign  countByServiceTypeAndStatusString>[<#list ticketCountByServiceTypeAndStatus as countByServiceTypeAndStatus>["${countByServiceTypeAndStatus[2]}","${countByServiceTypeAndStatus[1]}",${countByServiceTypeAndStatus[3]}]<#if countByServiceTypeAndStatus_has_next>,</#if></#list>]</#assign>
							<div id="ticketCountByStatusAndServiceType"  class="minichart" data-value='<@compress single_line=true>${countByServiceTypeAndStatusString}</@compress>'> </div>
						</div>
					</div>
					<div id="vendorUserTicketCount" class="col-md-5 dashboardcol2" data-value='${countByVendorString}'>
						<div class="col-md-12 placeholders roundbordersmall">
							<table id="vendorUserTicketCountTable" class="display table table-striped table-hover dt-responsive"  cellspacing="0">
						        <thead>
						            <tr>
						                <th>User Id</th>
						                <th>Tickets</th>
						            </tr>
						        </thead>
						        <tfoot>
						            <tr>
						                <th>Name</th>
						                <th>Tickets</th>
						            </tr>
						        </tfoot>
							</table>
						</div>	
					</div>					
				</div>
			</div>
		</div>		
	</body>
</html>	