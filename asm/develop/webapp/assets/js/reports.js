var TICKET_STATUS = ["OPEN","WORK_IN_PROGRESS","RESOLVED","CLOSED"];
var TICKET_STATUS_DESC = ["Open","Work in Progress","Resolved","Closed"];
var allTicketsDataTable = null;
$(document).ready(function(){
	if($("#ticketCountByStatusAndServiceType").size()>0){
		drawServiceTypeChart();
	}
	if($('#vendorUserTicketCountTable').size()>0){
		$('#vendorUserTicketCountTable').dataTable({
			"processing" : true,
			"scrollX": false,
			"responsive":true,
			"language" : {
				"processing" : "<img src='assets/images/loading-b.gif' alt='loading'/>"
			},
			"order": [[ 1, "desc" ]],
			"serverSide" : true,
			ajax : {
				"url" : VENDOR_USER_COUNT_URL,
				"type" : "POST",
				"data" : function(data) {
					return parsePostDataForDT(data);
				}
			},
			"columns" : [{ "name": "resolverUserId","defaultContent":"No Vendor Assigned" },{ "name": "noOfTickets" }]
		});
		//dashboardSimpleTable("vendorUserTicketCountTable","vendorUserTicketCount",1);
	}
	if($('#clientUserTicketCountTable').size()>0){
		$('#clientUserTicketCountTable').dataTable({
			"processing" : true,
			"scrollX": false,
			"responsive":true,
			"language" : {
				"processing" : "<img src='assets/images/loading-b.gif' alt='loading'/>"
			},
			"order": [[ 1, "desc" ]],
			"serverSide" : true,
			ajax : {
				"url" : CLIENT_USER_COUNT_URL,
				"type" : "POST",
				"data" : function(data) {
					return parsePostDataForDT(data);
				}
			},
			"columns" : [{ "name": "reporterUserId","defaultContent":"No Client Assigned" },{ "name": "noOfTickets" }]
		});
//		dashboardSimpleTable("clientUserTicketCountTable","clientUserTicketCount",1);
	}
	if($('#slaPenaltyStats')!=null){
		$('#slaPenaltyStats').dataTable({
			"processing" : true,
			"scrollX": false,
			"responsive":true,
			"language" : {
				"processing" : "<img src='assets/images/loading-b.gif' alt='loading'/>"
			},
			 "pageLength": 50,
			 "columns": [    { "width": "35%" },    null,    null,    null,    null  ],
			"order": [[ 1, "desc" ]]
		});
//		dashboardSimpleTable("clientUserTicketCountTable","clientUserTicketCount",1);
	}
	if($('#poRequestsReport')!=null){
		$('#poRequestsReport').dataTable({
			"processing" : true,
			"scrollX": false,
			"responsive":true,
			"language" : {
				"processing" : "<img src='assets/images/loading-b.gif' alt='loading'/>"
			},
			 "pageLength": 50,
			 "columns": [    { "width": "17.5%" },{ "width": "30%" },   { "width": "17.5%" },    { "width": "17.5%" },    { "width": "17.5%" }],
			"order": [[ 1, "desc" ]]
		});
//		dashboardSimpleTable("clientUserTicketCountTable","clientUserTicketCount",1);
	}

	if($(".ticketStatusFilter").size()>0 && allTicketsDataTable!=null){
		$(".ticketStatusFilter").click(function(){
			var newUrl = $(this).data("href");
			allTicketsDataTable.api().ajax.url(newUrl).load();
			$("#filterNotification").html("Showing "+ $(this).data("filter") +" tickets");
			$("#filterNotification").removeClass("hide");
		});
	}
	
    $('#reportStartDate').datetimepicker({pickTime:false});
    $('#reportEndDate').datetimepicker({pickTime:false});
    $("#reportStartDate").on("dp.change",function (e) {
       $('#reportEndDate').data("DateTimePicker").setMinDate(e.date);
    });
    $("#reportEndDate").on("dp.change",function (e) {
       $('#reportStartDate').data("DateTimePicker").setMaxDate(e.date);
	});
    
    $("#dashboardStartDate").on("dp.change",function (e) {
       $('#dashboardEndDate').data("DateTimePicker").setMinDate(e.date);
    });
    $("#dashboardEndDate").on("dp.change",function (e) {
       $('#dashboardStartDate').data("DateTimePicker").setMaxDate(e.date);
	});

    $("#downloadReport").click(function(){
    	var startDate = null;
    	var endDate = null;
    	if($('#reportStartDate input').val()!=""){
    		$("#startDateAlert").addClass("hide");
    		startDate=$('#reportStartDate input').val();
    	}else{
    		$("#startDateAlert").removeClass("hide");
    	}
    	if($('#reportEndDate input').val()!=""){
    		$("#endDateAlert").addClass("hide");
    		endDate=$('#reportEndDate input').val();
    	}else{
    		$("#endDateAlert").removeClass("hide");
    	}
    	if(startDate==null || endDate==null){
    		return false;
    	}
    	var downloadUrl = TICKET_DOWNLOAD_URL+"&dashboardStartDate="+startDate+"&dashboardEndDate="+endDate;
    	window.location = downloadUrl;
    })
    $(".dashboardDownload").click(function(){
    	//var downloadUrl = $(this).data("url")+"?"+addDashboardFilterQueryString();
    	addDashboardFiltertoHiddenFields("downloadDashboard");
    	$("#downloadDashboard").attr("action",$(this).data("url"));
    	$("#downloadDashboard").submit();
//    	window.location = downloadUrl;
    });
});

function parsePostDataForDT(data){
	var dF = {};
	addDashboardFields(dF);					
	planify(data);
	var parsedData = $.param(data);
	parsedData = parsedData+"&"+$.param(dF,true);
	data=parsedData
	return parsedData;
}


function addDashboardFields(data){
	data.dashboardStartDate=$("#dashboardStartDate").val();
	data.dashboardEndDate=$("#dashboardEndDate").val();
	data.circleIds=$("#circleIds").val();
	data.moduleIds=$("#moduleIds").val();
	data.clientUserIds=$("#clientUserIds").val();
}

function addDashboardFiltertoHiddenFields(formElement){
	$("#"+formElement).remove();
	$("body").append($("<form class='hiddenForm' method='POST' id='"+formElement+"' >"));
	cloneAppendFormField(formElement,"dashboardStartDate","#dashboardStartDate");
	cloneAppendFormField(formElement,"dashboardEndDate","#dashboardEndDate");
	cloneAppendFormField(formElement,"circleIds",$("#circleIds").siblings().find("input:checkbox:checked"));
	cloneAppendFormField(formElement,"moduleIds",$("#moduleIds").siblings().find("input:checkbox:checked"));
	cloneAppendFormField(formElement,"clientUserIds",$("#clientUserIds").siblings().find("input:checkbox:checked"));
}

function cloneAppendFormField(formElement,elementName,elementId){
	var elementClone=$(elementId).clone();
	elementClone.attr("name",elementName);
	elementClone.attr("id","");
	$("#"+formElement).append(elementClone);
}

function addDashboardFilterQueryString(){
	var queryString = "dashboardStartDate="+$("#dashboardStartDate").val()+"&dashboardEndDate="+$("#dashboardEndDate").val();
	queryString = queryString+"&circleIds="+$("#circleIds").val()+"&moduleIds="+$("#moduleIds").val()+"&clientUserIds="+$("#clientUserIds").val()
	return queryString;
}

function drawServiceTypeChart() {
	var ticketCountByStatusAndServiceType = $("#ticketCountByStatusAndServiceType").data("value");
	//console.log(ticketCountByStatusAndServiceType);
	var serviceTypeArray = [["Status"].concat(TICKET_STATUS_DESC).concat([{role: 'annotation'}])];
	var groupedAndTransposeOnIndex = groupByFirstAndTransposeOnSecond(ticketCountByStatusAndServiceType);
	serviceTypeArray = serviceTypeArray.concat(groupedAndTransposeOnIndex);
	var dataTable = google.visualization.arrayToDataTable(serviceTypeArray);
	var options = {
			//title:"Ticket Status by Vendor",
	        legend: 'none',
	        bar: { groupWidth: '50%' },
	        colors:['#d9534f','#5bc0de','#f0ad4e','#5cb85c'],
	        isStacked: true
	};
	var chart = new google.visualization.BarChart(document.getElementById("ticketCountByStatusAndServiceType"));
	chart.draw(dataTable, options);
	google.visualization.events.addListener(chart, 'select', selectHandler);

	// The select handler. Call the chart's getSelection() method
	function selectHandler() {
		var selectedItem = chart.getSelection()[0];
		if (selectedItem) {
			var value = serviceTypeArray[selectedItem.row+1];
	    	addDashboardFiltertoHiddenFields("downloadDashboard");
	    	$("#downloadDashboard").append("<input type='hidden' name='serviceType' value='"+value[0]+"' />");
	    	$("#downloadDashboard").append("<input type='hidden' name='statusId' value='"+TICKET_STATUS[selectedItem.column-1]+"' />");
	    	$("#downloadDashboard").attr("action",TICKET_DOWNLOAD_URL);
	    	$("#downloadDashboard").submit();
	    	//var downloadUrl = TICKET_DOWNLOAD_URL+"?dashboardStartDate="+$("#dashboardStartDate").val()+"&dashboardEndDate="+$("#dashboardEndDate").val();
	    	//downloadUrl=downloadUrl+"&serviceType="+encodeURI(value[0])+"&statusId="+TICKET_STATUS[selectedItem.column-1];
	    	//window.location = downloadUrl;
		}
	}
}

function dashboardSimpleTable(elementId,dataElementId,orderIndex){
	$('#'+elementId).dataTable( {
    "data": $("#"+dataElementId).data("value"),
    "paging": true,
    "searching":true,
    "info": false,
    "order": [[ orderIndex, "desc" ]]
});}

function groupByFirstAndTransposeOnSecond(data) {
	var groupTransposeOnValue = [];
	var groupedAndTransposeOnIndex = [];
	for(var i=0;i<data.length;i++){
		var eachRow = data[i];
		var groupFieldArray =  groupTransposeOnValue[eachRow[0]];
		if(typeof groupFieldArray =="undefined"){
			groupFieldArray = [eachRow[0]];
			groupFieldArray[5]="";
			groupTransposeOnValue[eachRow[0]]=groupFieldArray;
		}
		groupFieldArray[TICKET_STATUS.indexOf(eachRow[1])+1]=eachRow[2];
	}
	var index = 0;
	for(key in groupTransposeOnValue){
		groupedAndTransposeOnIndex[index]=groupTransposeOnValue[key];
		index=index+1;
	}
	return groupedAndTransposeOnIndex;
}