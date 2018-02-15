<#setting number_format="0.##">
<#assign statusColor ={"OPEN":"#d9534f","WORK_IN_PROGRESS":"#5bc0de","RESOLVED":"#f0ad4e","CLOSED":"#5cb85c"}>
<#include "../formMacros.ftl">
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
	</head>	
	<body>
		<div class="panel panel-primary">
			<div class="panel-heading">
				<h1 class="panel-title" > PO Request </h1>
			</div>
			<div class="panel-body reports" id="dashboard">
				<div class="row  margintop10">
					<div class="col-md-12">
						<div class="col-md-12  roundbordersmall">
							<h4>Filter Reports</h4>
							<form method="POST" action="<@spring.url relativeUrl="/reports/porequests"/>">
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
									</div>
									<div class='col-md-3 margintop10'><div class="form-group"><button class="btn btn-primary" id="dashboardReport">Show Report</button></div></div><br/>
									<script type="text/javascript">
										if($("#dashboard").size()>0){
											initMultiSelect("circleIds");
										}
									</script>
								</div>												
							</form>	
						</div>		
					</div>
				</div>
				<div class="row margintop10">
					<div class="col-md-12">
						<div class="col-md-12  roundbordersmall">
							<#if poRequestReports?exists>
								<table id="poRequestsReport" width="100%" style="word-wrap:break-word" class="display table table-striped table-hover responsive"  cellspacing="0">
							        <thead>
							            <tr>
							                <th>Asset</th>
							                <th>Vendor</th>
							                <th>PO Status</th>
							                <th>No of PO Requests</th>
							                <th>Total Cost</th>
							            </tr>
							        </thead>
							        <tfoot>
							            <tr>
							                <th>Asset</th>
							                <th>Vendor</th>
							                <th>PO Status</th>
							                <th>No of PO Requests</th>
							                <th>Total Cost</th>
							            </tr>
							        </tfoot>
							        <tbody>
							        	<#list poRequestReports as poRequestReportDetails>
								            <tr>
								                <td>${poRequestReportDetails[0]}</td>
								                <td>${poRequestReportDetails[1]}</td>
								                <td>${poRequestReportDetails[2]}</td>
								                <td>${poRequestReportDetails[3]}</td>
								                <td>Rs. ${poRequestReportDetails[4]}</td>
											</tr>
										</#list>
									</tbody>
								</table>									            
							</#if>
						</div>	
					</div>	
				</div>
			</div>	
		</div>
	</body>
</html>				