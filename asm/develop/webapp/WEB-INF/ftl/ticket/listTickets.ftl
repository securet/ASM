<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#assign fieldTypeMapping = {"int":"text","string":"text","list":"select","file":"file","double":"text","datetime":"datetime","date":"date","boolean","text","bigDecimal":"text"}>
<html>
	<head>
		<title>View Tickets</title>
		<script src="assets/js/jquery.dataTables.min.js"></script>
		<script src="assets/js/dataTables.responsive.min.js"></script>
		<script src="assets/js/dataTables.bootstrap.js"></script>
		<link rel="stylesheet" href="assets/css/jquery.dataTables.min.css">
		<link rel="stylesheet" href="assets/css/dataTables.bootstrap.css">
		<link rel="stylesheet" href="assets/css/dataTables.responsive.css">	
	</head>
	<body>
		<div class="panel panel-primary">
			<div class="panel-heading">
				<h1 class="panel-title" > View Tickets </h1>
			</div>
			<div class="panel-body">
				<script type="text/javascript">
					dataUrl="<@spring.url relativeUrl="/tickets/listUserTickets?${_csrf.parameterName}=${_csrf.token}&entityName=Ticket&operator=or"/><#if filterStatus?exists>&filterStatus=${filterStatus}</#if>";
					columnsToDisplay=[{ "data": "ticketId" },{ "data": "site.name" },{ "data": "lastUpdatedTimestamp",className: "never","visible":false},{ "data": "serviceType.name" },{"name":"Description", "data": "shortDesc"},{ "data": "statusId" },{"data":"actualTat","defaultContent":"0","render":function( data, type, full, meta ){return formatTimeinHrsMins( data, type, full, meta);}},{ "data": "vendorUser.userId","defaultContent":"None" },{ "data": "vendorUser.organizationName", "defaultContent":"None" },{ "data": "issueType.name","defaultContent":"None" },{ "data": "ticketType","defaultContent":"None" },{ "data": "site.circle","defaultContent":"None" }];
					orderIndex=2;
					function makeEditLink(row,data){
						var cellToModify = $(row).find("td:eq(0)");
						var text = $(cellToModify).html();
						$(cellToModify).html('<a href="<@spring.url relativeUrl="/tickets/modifyTicket?id="/>'+data.ticketId+'">'+text+'</a>');
					}
				</script>
				
				
				<#if saved?exists>
					<div class="alert alert-info asmnotification" role="alert">Saved Ticket with  : ${formObject.ticketId!}</div>
				</#if>
				
				<#if filterStatus?exists>
					<div class="alert alert-success" role="alert">Showing '${filterStatus?replace("_"," ")?capitalize}' tickets</div>
				</#if>	
				<@security.authorize access="hasAnyRole('CLIENT_USER','CLIENT_CONTROLLER','ADMIN')">
					<div class="pageoptions">
						<div class="btn-group">
							<a href="<@spring.url relativeUrl="/tickets/newTicket"/>" class="btn btn-default DTTT_button_text" tabindex="0" aria-controls="example">
								<span>New</span>
							</a>
						</div>
					</div>
				</@security.authorize>				
				<div> 
					<table id="asmdatatable" class="display table table-striped table-hover responsive " width="100%" cellspacing="0">
				        <thead>
				            <tr>
				                <th>Ticket Id</th>
				                <th>SiteId</th>
				                <th class="none">Last Modified Time</th>
				                <th>ServiceType</th>
				                <th style="width:50px">Description</th>
				                <th>Status</th>
				                <th>TAT</th>
				                <th>Vendor</th>
				                <th>Vendor Organization</th>
				                <th>Issue Type</th>
				                <th>Ticket Type</th>
				                <th>Circle</th>
				            </tr>
				        </thead>
				        <tfoot>
				            <tr>
				                <th>Ticket Id</th>
				                <th>SiteId</th>
				                <th class="none">Last Modified Time</th>
				                <th>ServiceType</th>
				                <th style="width:50px">Description</th>
				                <th>Status</th>
				                <th>TAT</th>
				                <th>Vendor</th>
				                <th>Vendor Organization</th>
				                <th>Issue Type</th>
				                <th>Ticket Type</th>
				                <th>Circle</th>
				            </tr>
				        </tfoot>
				    </table>
				 </div>   
			</div>
		</div>		   
	</body>
</html>