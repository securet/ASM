<#setting number_format="0.##">
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
										<div class='col-md-4'>
											<#assign months>{"1":"January","2":"February","3":"March","4":"April","5":"May","6":"June","7":"July","8":"August","9":"September","10":"October","11":"November","12":"December"}</#assign>
											<@formMultiSelectSSM path="dashboardFilter.month" field={"fieldName":"months","label":"Select Month"} options=months?default("{}")?eval  includeLabelInline=false/>
										</div>	
									</div>
									<div class='col-md-3 margintop10'><div class="form-group"><button class="btn btn-primary" id="dashboardReport">Show Report</button></div></div><br/>
								</div>
							</form>	
						</div>		
					</div>
				</div>
				<div class="row margintop10">
				</div>
				<div class="row  margintop10">
				</div>
				<div class="row  margintop10">
				</div>
			</div>
		</div>		
	</body>
</html>	