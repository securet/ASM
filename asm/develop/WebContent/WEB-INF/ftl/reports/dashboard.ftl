<#assign statusColor ={"OPEN":"#d9534f","WORK_IN_PROGRESS":"#5bc0de","RESOLVED":"#f0ad4e","CLOSED":"#5cb85c"}>
<#include "../formMacros.ftl">
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
      		var TICKET_DOWNLOAD_URL = "<@spring.url relativeUrl="/reports/getTicketsByTimePeriod"/>?${_csrf.parameterName}=${_csrf.token}";
      		var VENDOR_USER_COUNT_URL = "<@spring.url relativeUrl="/reports/vendorUserTicketCount"/>?${_csrf.parameterName}=${_csrf.token}";
      		var CLIENT_USER_COUNT_URL = "<@spring.url relativeUrl="/reports/clientUserTicketCount"/>?${_csrf.parameterName}=${_csrf.token}";
		</script>
	</head>	
	<body>
		<div class="panel panel-primary">
			<div class="panel-heading">
				<h1 class="panel-title" > Dashboard </h1>
			</div>
			<div class="panel-body reports" id="dashboard">
				<div class="row  margintop10">
					<div class="col-md-12">
						<div class="col-md-12  roundbordersmall">
							<h4>Filter Reports</h4>
							<form method="POST" action="<@spring.url relativeUrl="/reports/dashboard"/>">
								<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
								<div class="margintop10">
									<div class='col-md-4'>
										<#if dashboardFilter.dashboardEndDate??>
											<#assign maxDate=dashboardFilter.dashboardEndDate?string("dd-MM-yyyy")>
										</#if>	
					                	<@formInputSSM  path="dashboardFilter.dashboardStartDate" field={"fieldName":"","maxDate":maxDate?default("")} fieldType="date" />
										<div id="dashboardStartDateAlert" class="alert alert-danger hide" role="alert">Please select start date</div>
									</div>
									<div class='col-md-1 dashboardarrowGlyph'><i class="glyphicon glyphicon-arrow-right"></i></div>							
									<div class='col-md-4'>
										<#if dashboardFilter.dashboardStartDate??>
											<#assign minDate=dashboardFilter.dashboardStartDate?string("dd-MM-yyyy")>
										</#if>	
					                	<@formInputSSM  path="dashboardFilter.dashboardEndDate" field={"fieldName":"","minDate":minDate?default("")} fieldType="date" />
										<div id="dashboardEndDateAlert" class="alert alert-danger hide" role="alert">Please select end date</div>
									</div>
									<div class="row">
										<div class='col-md-4'>
											<#if circles?exists && (circles?size>0) >
												<#assign circleoptions>{<#list circles as uiObject>"${uiObject.geoId}":"${uiObject.name}"<#if uiObject_has_next>,</#if></#list>}</#assign>
											</#if>
											<@formMultiSelectSSM path="dashboardFilter.circleIds" field={"fieldName":"circles","label":"Select Circles"} options=circleoptions?default("{}")?eval  includeLabelInline=false/>
										</div>	
										<div class='col-md-4'>
											<#if modules?exists && (modules?size>0) >
												<#assign moduleoptions>{<#list modules as uiObject>"${uiObject.moduleId}":"${uiObject.name}"<#if uiObject_has_next>,</#if></#list>}</#assign>
											</#if>
											<@formMultiSelectSSM path="dashboardFilter.moduleIds" field={"fieldName":"modules","label":"Select Modules"} options=moduleoptions?default("{}")?eval  includeLabelInline=false/>
										</div>	
										<div class='col-md-4'>
											<#if clientUsers?exists && (clientUsers?size>0) >
												<#assign useroptions>{<#list clientUsers as uiObject>"${uiObject.userId}":"${uiObject.userId?lower_case} (${uiObject.fullName})"<#if uiObject_has_next>,</#if></#list>}</#assign>
											</#if>
											<@formMultiSelectSSM path="dashboardFilter.clientUserIds" field={"fieldName":"clientUsers","label":"Select Client User"} options=useroptions?default("{}")?eval  includeLabelInline=false/>
										</div>
									</div>
									<div class='col-md-3 margintop10'><div class="form-group"><button class="btn btn-primary" id="dashboardReport">Show Report</button></div></div><br/>
								</div>
							</form>	
						</div>		
					</div>						
				</div>
				<div class="row margintop10">
					<div id="displayTicketStatusCount" class="col-md-4 minichart">
					<div class="col-md-12 minichart roundbordersmall">
						<h4>Ticket by Status</h4>
						<#if ticketStatusSummary?exists>
							<#assign totalCount = ticketStatusSummary.totalCount>
							<#if totalCount==0>
								<#assign totalCount = 1>
							</#if>	
							<#assign openCountPercentage = ((ticketStatusSummary.openCount/totalCount)*100)>
							<@ticketStatusHolder status={"key":"OPEN","value":"Open"} text="${ticketStatusSummary.openCount} \\n ${openCountPercentage?round}%"/>
							<#assign wipCountPercentage = ((ticketStatusSummary.workInProgressCount/totalCount)*100)>
							<@ticketStatusHolder status={"key":"WORK_IN_PROGRESS","value":"Work In Progress"} text="${ticketStatusSummary.workInProgressCount} \\n ${wipCountPercentage?round}%"/>
  							<div class="clearfix visible-sm-block"></div>
							<#assign resolvedCountPercentage = ((ticketStatusSummary.resolvedCount/totalCount)*100)>
							<@ticketStatusHolder status={"key":"RESOLVED","value":"Resolved"} text="${ticketStatusSummary.resolvedCount} \\n ${resolvedCountPercentage?round}%"/>
							<#assign closedCountPercentage = ((ticketStatusSummary.closedCount/totalCount)*100)>
							<@ticketStatusHolder status={"key":"CLOSED","value":"Closed"} text="${ticketStatusSummary.closedCount} \\n ${closedCountPercentage?round}%"/>
  							<div class="clearfix visible-sm-block"></div>
						</#if>	
						</div>
					</div>	
					<div  class="col-md-8 minichart">
						<div class="col-md-12  roundbordersmall">
							<h4>Tickets Status by Service Type</h4>
							<#assign  countByServiceTypeAndStatusString>[<#list ticketCountByServiceTypeAndStatus as countByServiceTypeAndStatus>["${countByServiceTypeAndStatus[1]}","${countByServiceTypeAndStatus[2]!}",${countByServiceTypeAndStatus[3]?default(0)}]<#if countByServiceTypeAndStatus_has_next>,</#if></#list>]</#assign>
							<div id="ticketCountByStatusAndServiceType"  style="height:458px;" data-value='<@compress single_line=true>${countByServiceTypeAndStatusString}</@compress>'> </div>
						</div>
					</div>
				</div>
				<div class="row  margintop10">
					<div id="vendorUserTicketCount" class="col-md-6 " >
						<div class="col-md-12  col-sm-08 col-xs-08  roundbordersmall">
							<h4>Open Tickets with Vendor User</h4>
							<div class="tbldwnldbtn"><a href="javascript:void(0)" data-url="<@spring.url relativeUrl="/reports/downloadVendorUserTicketCount"/>?${_csrf.parameterName}=${_csrf.token}" id="vendorTicketCountDownload" class="dashboardDownload btn btn-default"><i class="glyphicon glyphicon-download-alt"></i> Download</a></div> 
							<table id="vendorUserTicketCountTable" width="100%" style="word-wrap:break-word" class="display table dt-responsive no-wrap"  cellspacing="0">
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
					<div id="clientUserTicketCount" class="col-md-6 " >
						<div class="col-md-12 col-sm-08 col-xs-08 roundbordersmall">
							<h4>Total Tickets by Client User</h4>
							<div class="tbldwnldbtn"><a  href="javascript:void(0)" data-url="<@spring.url relativeUrl="/reports/downloadClientUserTicketCount"/>?${_csrf.parameterName}=${_csrf.token}" id="clientTicketCountDownload" class="dashboardDownload btn btn-default"><i class="glyphicon glyphicon-download-alt"></i> Download</a></div> 
							<table id="clientUserTicketCountTable" width="100%" style="word-wrap:break-word" class="display table dt-responsive"  cellspacing="0">
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
							<h4>Download ALL Tickets</h4>
							<div class="margintop10">
								<div class='col-md-4'>
						            <div class="form-group">
						                <div class='input-group date' id='reportStartDate'>
						                    <input data-date-format="DD-MM-YYYY"  type='text' class="form-control" />
						                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span>
						                    </span>
						                </div>
						            </div>
									<div id="startDateAlert" class="alert alert-danger hide" role="alert">Please select start date</div>
								</div>
								<div class='col-md-1 dashboardarrowGlyph'><i class="glyphicon glyphicon-arrow-right"></i></div>							
								<div class='col-md-4'>
									<div class="alert alert-danger hide" role="alert">Please select end date</div>
								    <div class="form-group">
								        <div class='input-group date' id='reportEndDate'>
								            <input data-date-format="DD-MM-YYYY"  type='text' class="form-control" />
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
