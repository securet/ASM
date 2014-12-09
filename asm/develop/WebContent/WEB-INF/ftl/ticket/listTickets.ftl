<#assign fieldTypeMapping = {"int":"text","string":"text","list":"select","file":"file","double":"text","datetime":"datetime","date":"date","boolean","text"}>
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
					<#assign userParams = "&columns[0][name]=&columns[0][orderable]=true&columns[0][searchRegex]=false&columns[0][searchable]=true&columns[0][searchValue]=11"+userName>
					<#if isReporter>
						<#assign userParams= "columns[0][data]=reporter.userId" + userParams>
					<#else>
						<#assign userParams= "columns[0][data]=resolver.userId" + userParams>	
					</#if>	
					dataUrl="<@spring.url relativeUrl="/tickets/listUserTickets?entityName=Ticket&operator=or"/>";
					columnsToDisplay=[{ "data": "ticketId" },{ "data": "serviceType.name" },{ "data": "description"},{ "data": "status.enumDescription" },{ "data": "resolver.organization.name","defaultContent":"None" },{ "data": "resolver.userId", "defaultContent":"None" },{ "data": "issueType.name","defaultContent":"None" },{ "data": "ticketType.enumDescription" }];
					function makeEditLink(row,data){
						var cellToModify = $(row).find("td:eq(0)");
						var text = $(cellToModify).html();
						$(cellToModify).html('<a href="<@spring.url relativeUrl="/tickets/modifyTicket?id="/>'+data.ticketId+'">'+text+'</a>');
					}				
				</script>
				<#--
				<#assign bindingResult = .data_model["org.springframework.validation.BindingResult.formObject"]>
				<#if formObject.name?exists && bindingResult?exists && !bindingResult.hasErrors()>
					<div class="alert alert-info asmnotification" role="alert"><#if createNew>Created<#else>Saved</#if> ${entityName} successfully : ${formObject.name!}</div>
				</#if>
				-->
				<table id="asmdatatable" class="display table table-striped table-hover dt-responsive" width="100%" cellspacing="0">
			        <thead>
			            <tr>
			                <th>Ticket Id</th>
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
	</body>
</html>