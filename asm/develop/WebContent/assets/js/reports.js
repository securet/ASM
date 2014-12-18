var TICKET_STATUS = ["OPEN","WORK_IN_PROGRESS","RESOLVED","CLOSED"];
var TICKET_STATUS_DESC = ["Open","Work in Progress","Resolved","Closed"];
var allTicketsDataTable = null;
$(document).ready(function(){
	if($("#ticketCountByStatusAndServiceType").size()>0){
		drawServiceTypeChart();
	}
	if($('#vendorUserTicketCount')!=null){
		dashboardSimpleTable("vendorUserTicketCountTable","vendorUserTicketCount",1);
	}
	if($('#clientUserTicketCount')!=null){
		dashboardSimpleTable("clientUserTicketCountTable","clientUserTicketCount",1);
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
    	var downloadUrl = TICKET_DOWNLOAD_URL+"?startDate="+startDate+"&endDate="+endDate;
    	window.location = downloadUrl;
    })
});

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