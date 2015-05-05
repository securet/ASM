<#setting number_format="0.##">
<#assign slaStatusConditions={"cashout":2,"availability":7,"all":0,"caretaker":0}>
<#assign slaPenaltyValues={"cashout":5000,"availability":7,"all":0,"caretaker":0}>

<#include "../formMacros.ftl">
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
	</head>	
	<body>
		<div class="panel panel-primary">
			<div class="panel-heading">
				<h1 class="panel-title" > SLA Status </h1>
			</div>
			<div class="panel-body reports" id="dashboard">
				<div class="row  margintop10">
					<div class="col-md-12">
						<div class="col-md-12  roundbordersmall">
							<h4>SELECT Circle and Month</h4>
							<form method="POST" action="<@spring.url relativeUrl="/reports/sla/fetchStatus"/>">
								<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
								<div class="margintop10">
									<div class="row">
										<div class='col-md-4'>
											<#if circles?exists && (circles?size>0) >
												<#assign circleoptions>{<#list circles as uiObject>"${uiObject.geoId}":"${uiObject.name}"<#if uiObject_has_next>,</#if></#list>}</#assign>
											</#if>
											<@formMultiSelectSSM path="dashboardFilter.circleIds" field={"fieldName":"circles","label":"Select Circles"} options=circleoptions?default("{}")?eval  includeLabelInline=false/>
										</div>	
										<script type="text/javascript">
											initMultiSelect("circleIds");
										</script>	
										<div class='col-md-4'>
											<#assign months>{"1":"January","2":"February","3":"March","4":"April","5":"May","6":"June","7":"July","8":"August","9":"September","10":"October","11":"November","12":"December"}</#assign>
											<@formSingleSelectSSM path="dashboardFilter.month" field={"fieldName":"months","label":"Select Month"} options=months?default("{}")?eval  includeLabelInline=false/>
										</div>	
										<div class='col-md-4'>
											<#assign issueGroups>{"cashout":"Cash out","all":"TAT(FLM)","caretaker":"House Keeping"}</#assign>
											<@formSingleSelectSSM path="dashboardFilter.issueGroup" field={"fieldName":"SLA Type","label":"Select Type"} options=issueGroups?default("{}")?eval  includeLabelInline=false/>
										</div>	
									</div>
									<div class='col-md-3 margintop10'><div class="form-group"><button class="btn btn-primary" id="dashboardReport">Show Report</button></div></div><br/>
								</div>
							</form>	
						</div>		
					</div>
				</div>
				<div class="row margintop10">
					<div class="col-md-12">
						<div class="col-md-12  roundbordersmall">
							<#if slaPenaltyStats?exists>
								<table id="slaPenaltyStats" width="100%" style="word-wrap:break-word" class="display table dt-responsive no-wrap"  cellspacing="0">
							        <thead>
							            <tr>
							                <th>Vendor</th>
							                <th>Sites</th>
							                <th>No of Issues</th>
							                <th>SLA STATUS</th>
							                <th>Total Penalty</th>
							            </tr>
							        </thead>
							        <tfoot>
							            <tr>
							                <th>Vendor</th>
							                <th>Sites</th>
							                <th>No of Issues</th>
							                <th>SLA STATUS</th>
							                <th>Total  Penalty</th>
							            </tr>
							        </tfoot>
							        <tbody>
							        	<#list slaPenaltyStats as slaPenaltyStat>
							            <tr>
							                <td>${slaPenaltyStat.vendorOrganization}</td>
							                <td>${slaPenaltyStat.noOfSites}</td>
							                <td>${slaPenaltyStat.noOfIssues}</td>
											<td>
												<#assign slaMet = false>
												<#assign slaPenaltyStatus = (slaPenaltyStat.noOfIssues/slaPenaltyStat.noOfSites)*100>
												${slaPenaltyStatus?round}% <#if slaPenaltyStatus<=slaStatusConditions[dashboardFilter.issueGroup]><#assign slaMet = true>  <i style="color:green;" class="glyphicon glyphicon-thumbs-up"></i><#else><i style="color:red;" class="glyphicon glyphicon-thumbs-down"></i></#if>
											</td>			
							                <td>Rs. <#if slaMet>0<#else><#if dashboardFilter.issueGroup='cashout'>${slaPenaltyStat.totalPenalty*5000}<#elseif dashboardFilter.issueGroup='all'>${slaPenaltyStat.noOfIssues*1000}<#elseif dashboardFilter.issueGroup='caretaker'>${slaPenaltyStat.noOfIssues*2000}<#else>0</#if></#if></td>
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