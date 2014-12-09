<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#setting number_format="0.##">
<#include "../formMacros.ftl">
<html>
	<head>
		<title>Modify Ticket</title>
		<script id="errorMessageTmpl" type="text/x-jsrender">
			<div id="{{:elementId}}" class="alert alert-danger" role="alert">
			  <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
			  <span class="sr-only">Error:</span>
			  <span class="messagebox">{{:message}}</div>
			</div>
		</script>		
		<script id="selectBoxTmpl" type="text/x-jsrender">
			<div class="form-group">
				<label for="{{:elementId}}" class="col-sm-3 control-label">Select {{:elementLabel}}</label>
				<div class="col-sm-9">
					<select class="form-control" name="{{:elementId}}" id="{{:elementId}}">
						<option value="">Select {{:elementLabel}}</option>
						{{for options}}
							<option value="{{:value}}">{{:text}}</option>
						{{/for}}	
					</select>
				</div>	
			</div>	
		</script>
		<link rel="stylesheet" href="assets/css/timeline.bootstrap.css">
		<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAuqrxOXFfGiaLBrEljQ_ioHQVvE6X1z34">
		</script>
	</head>
	<body>
		<div class="panel panel-primary">
			<div class="panel-heading">
				<h1 class="panel-title" >Modify Ticket</h1>
			</div>
			<div  id="editTicket" class="panel-body leftcolumns" >
				<div class="col-md-8">
					<form class="form-horizontal" role="form" method="POST" action="<@spring.url relativeUrl="/tickets/updateTicket"/>?${_csrf.parameterName}=${_csrf.token}" enctype="multipart/form-data" >
						<div class="form-group labelblock">
							<label for="ticketId" class="col-sm-3 control-label">Ticket Id</label>
						    <div id="ticketId"  class="col-sm-9">
								<p class="form-control-static normaltext">${formObject.ticketId!}</p>
						    </div>
							<input type="hidden" name="ticketId" value="${formObject.ticketId!}"/>	
						</div>
						<div class="form-group labelblock">
							<label for="siteId" class="col-sm-3 control-label">Site</label>
						    <div id="siteId"  class="col-sm-9">
								<p class="form-control-static normaltext">${formObject.site.name!}</p>
						    </div>
						</div>
						<div class="form-group labelblock">
							<label for="areaId" class="col-sm-3 control-label">Area</label>
						    <div id="areaId"  class="col-sm-9">
								<p class="form-control-static normaltext">${formObject.site.area!}</p>
						    </div>
						</div>
						<div class="form-group labelblock">
							<label for="assetId" class="col-sm-3 control-label">Asset</label>
						    <div id="assetId"  class="col-sm-9">
								<p class="form-control-static normaltext">${formObject.asset.name!}</p>
						    </div>
						</div>
						<div class="form-group labelblock">
							<label for="serviceTypeId" class="col-sm-3 control-label">Service Type</label>
						    <div id="serviceTypeId"  class="col-sm-9">
								<p class="form-control-static normaltext">${formObject.serviceType.name!}</p>
						    </div>
						</div>
						<div class="form-group labelblock">
							<label for="issueTypeId" class="col-sm-3 control-label">Issue Type</label>
						    <div id="issueTypeId"  class="col-sm-9">
								<p class="form-control-static normaltext">${formObject.issueType.name!}</p>
						    </div>
						</div>
						<div class="form-group labelblock">
							<label for="severity" class="col-sm-3 control-label">Severity</label>
						    <div id="severity"  class="col-sm-9">
								<p class="form-control-static normaltext">${formObject.severity.enumDescription!}</p>
						    </div>
						</div>
						<div class="form-group labelblock">
							<label for="currentStatus" class="col-sm-3 control-label">Current Status</label>
						    <div id="currentStatus"  class="col-sm-9">
								<p class="form-control-static normaltext">${formObject.status.enumDescription!}</p>
						    </div>
						</div>
						<#assign statusOptions>{"":"You cannot change status check your role"}</#assign>
						<@security.authorize access="hasRole('RESOLVER')">
							<#if formObject.status.enumerationId='WORK_IN_PROGRESS'>
								<#assign statusOptions>{"":"Select Status","WORK_IN_PROGRESS":"Work in Progress","RESOLVED":"Resolved"}</#assign>
							</#if>
						</@security.authorize>	
						<@security.authorize access="hasAnyRole('CLIENT_USER','CLIENT_CONTROLLER','ADMIN')">
							<#if formObject.status.enumerationId='WORK_IN_PROGRESS'>
								<#assign statusOptions>{"Test":"Select Status","WORK_IN_PROGRESS":"Work in Progress","CLOSED":"Closed"}</#assign>
							<#else>
								<#assign statusOptions>{"Test":"Select Status","CLOSED":"Closed"}</#assign>
							</#if>
						</@security.authorize>	
						<@formSingleSelectSSM path="formObject.status.enumerationId" field={"fieldName":"formObject.status.enumerationId","label":"Select Status"} options=statusOptions?default("{}")?eval />
						<@simpleInputFieldSSM  field={"fieldName":"ticketAttachments","label":"Attachment","fieldType":"file"}  attributes="multiple"/>
						<ul class="timeline">
							<li class="timeline-inverted">
								<div class="timeline-badge"><i class="glyphicon glyphicon-user"></i></div>
								<div class="timeline-panel">
									<div class="timeline-heading">
						      			<p><small class="text-muted"><i class="glyphicon glyphicon-time"></i></small></p>
							    	</div>
								    <div class="timeline-body">
										<p>${formObject.description!}</p>
								    </div>
								</div>
							</li>
						</ul>							
						<@formTextAreaSSM path="formObject.description" field={"fieldName":"description","label":"Description"} attributes="class=\"form-control\"" empty=true/>
						<span class="hide" id="lat-long" data-value='{"lat":${formObject.latitude!},"lng": ${formObject.longitude!}}' data-title="Ticket Posted by ${formObject.createdBy.userId}"></span>
					</form>	
				</div>
				<div class="col-md-4">
					<div id="map-canvas"></div>		
					<div>
						<h4>Attachments (${formObject.attachments?size})</h4>	
						<#list formObject.attachments as attachment>
							<a target="_blank" href="<@spring.url relativeUrl="/"+attachment.attachmentPath />" title=${attachment.attachmentName}>${attachment.attachmentName}</a>  @ ${attachment.createdTimestamp?string}
						</#list> 	
					</div>										
				</div>
			</div>
		</div>
	</body>
</html>