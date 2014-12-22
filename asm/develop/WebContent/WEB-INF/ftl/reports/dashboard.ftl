<#assign statusColor ={"OPEN":"#d9534f","WORK_IN_PROGRESS":"#5bc0de","RESOLVED":"#f0ad4e","CLOSED":"#5cb85c"}>
<#assign baseUrl = "/tickets/listAllTickets?${_csrf.parameterName}=${_csrf.token}&entityName=Ticket&operator=or">
<#macro ticketStatusHolder status text>
	<div class="col-md-6 col-sm-6 placeholder">
		<div class="bold" style="height:50px;font-size:13px;">${status.value}</div>
		<img data-src="holder.js/150x150/auto/${statusColor[status.key]}:#ffffff/size:20/text:${text}" class="img-responsive" alt="Generic placeholder thumbnail">
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
		<script src="assets/js/moment.min.js"></script>
		<script src="assets/js/bootstrap-datetime.min.js"></script>
		<link rel="stylesheet"  href="assets/css/bootstrap-datetimepicker.min.css"/>
		<script src="assets/js/reports.js"></script>
		<script type="text/javascript">
      		google.load("visualization", "1", {packages:["corechart"]});
      		var TICKET_DOWNLOAD_URL = "<@spring.url relativeUrl="/reports/getTicketsByTimePeriod"/>";
		</script>
	</head>	
	<body>
		<div class="panel panel-primary">
			<div class="panel-heading">
				<h1 class="panel-title" > Dashboard </h1>
			</div>
			<div class="panel-body reports">
				<div class="row ">
					<div id="displayTicketStatusCount" class="col-md-4 minichart">
					<div class="col-md-12 minichart roundbordersmall">
						<h4>Ticket by Status</h4>
						<#if ticketStatusSummary?exists>
							<#assign openCountPercentage = ((ticketStatusSummary.openCount/ticketStatusSummary.totalCount)*100)>
							<@ticketStatusHolder status={"key":"OPEN","value":"Open"} text="${ticketStatusSummary.openCount} \\n ${openCountPercentage?round}%"/>
							<#assign wipCountPercentage = ((ticketStatusSummary.workInProgressCount/ticketStatusSummary.totalCount)*100)>
							<@ticketStatusHolder status={"key":"WORK_IN_PROGRESS","value":"Work In Progress"} text="${ticketStatusSummary.workInProgressCount} \\n ${wipCountPercentage?round}%"/>
  							<div class="clearfix visible-sm-block"></div>
							<#assign resolvedCountPercentage = ((ticketStatusSummary.resolvedCount/ticketStatusSummary.totalCount)*100)>
							<@ticketStatusHolder status={"key":"RESOLVED","value":"Resolved"} text="${ticketStatusSummary.resolvedCount} \\n ${resolvedCountPercentage?round}%"/>
							<#assign closedCountPercentage = ((ticketStatusSummary.closedCount/ticketStatusSummary.totalCount)*100)>
							<@ticketStatusHolder status={"key":"CLOSED","value":"Closed"} text="${ticketStatusSummary.closedCount} \\n ${closedCountPercentage?round}%"/>
  							<div class="clearfix visible-sm-block"></div>
						</#if>	
						</div>
					</div>	
					<div  class="col-md-8 minichart">
						<div class="col-md-12  roundbordersmall">
							<h4>Tickets Status by Service Type</h4>
							<#assign  countByServiceTypeAndStatusString>[<#list ticketCountByServiceTypeAndStatus as countByServiceTypeAndStatus>["${countByServiceTypeAndStatus[2]}","${countByServiceTypeAndStatus[1]}",${countByServiceTypeAndStatus[3]}]<#if countByServiceTypeAndStatus_has_next>,</#if></#list>]</#assign>
							<div id="ticketCountByStatusAndServiceType"  style="height:458px;" data-value='<@compress single_line=true>${countByServiceTypeAndStatusString}</@compress>'> </div>
						</div>
					</div>
				</div>
				<div class="row  margintop10">
					<#assign  countByVendorString>[<#list ticketCountByVendorUser as countofUser>["${countofUser[0]}",${countofUser[1]}]<#if countofUser_has_next>,</#if></#list>]</#assign>
					<div id="vendorUserTicketCount" class="col-md-6 " data-value='${countByVendorString}'>
						<div class="col-md-12 col-sm-10  roundbordersmall">
							<h4>Open Tickets with Vendor User</h4>
							<table id="vendorUserTicketCountTable" class="display table table-striped table-hover dt-responsive display wrap"  cellspacing="0">
						        <thead>
						            <tr>
						                <th>User Id</th>
						                <th>Tickets</th>
						            </tr>
						        </thead>
						        <tfoot>
						            <tr>
						                <th>User Id</th>
						                <th>Tickets</th>
						            </tr>
						        </tfoot>
							</table>
						</div>	
					</div>					
					<#assign countByClientString>[<#list ticketCountByClientUser as countofClientUser>["${countofClientUser[0]}",${countofClientUser[1]}]<#if countofClientUser_has_next>,</#if></#list>]</#assign>
					<div id="clientUserTicketCount" class="col-md-6 " data-value='${countByClientString}'>
						<div class="col-md-12 col-sm-10 col-xs-10 roundbordersmall">
							<h4>Open Tickets with Vendor User</h4>
							<table id="clientUserTicketCountTable" width="100%" class="display table table-striped table-hover dt-responsive display wrap"  cellspacing="0">
						        <thead>
						            <tr>
						                <th>User Id</th>
						                <th>Tickets</th>
						            </tr>
						        </thead>
						        <tfoot>
						            <tr>
						                <th>User Id</th>
						                <th>Tickets</th>
						            </tr>
						        </tfoot>
							</table>
						</div>	
					</div>					
				</div>
				<div class="row  margintop10">
					<div class="col-md-10">
						<div class="col-md-12  roundbordersmall">
							<h4>Download tickets</h4>
							<div class="margintop10">
								<div class='col-md-4'>
						            <div class="form-group">
						                <div class='input-group date' id='reportStartDate'>
						                    <input data-date-format="DD/MM/YYYY" type='text' class="form-control" />
						                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span>
						                    </span>
						                </div>
						            </div>
									<div id="startDateAlert" class="alert alert-danger hide" role="alert">Please select start date</div>
								</div>
								<div class='col-md-1'><i class="glyphicon glyphicon-arrow-right"></i></div>							
								<div class='col-md-4'>
									<div class="alert alert-danger hide" role="alert">Please select end date</div>
								    <div class="form-group">
								        <div class='input-group date' id='reportEndDate'>
								            <input data-date-format="DD/MM/YYYY" type='text' class="form-control" />
								            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span>
								            </span>
								        </div>
								    </div>
									<div id="endDateAlert" class="alert alert-danger hide" role="alert">Please select end date</div>
								</div>
								<div class='col-md-3'><a href="javascript:void(0)" class="btn btn-primary" id="downloadReport"><i class="glyphicon glyphicon-download-alt"></i> Download</a></div><br/>
							</div>
						</div>		
					</div>						
				</div>
			</div>
		</div>		
	</body>
</html>	