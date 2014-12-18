var TICKET_STATUS = ["OPEN","WORK_IN_PROGRESS","RESOLVED","CLOSED"];
var TICKET_STATUS_DESC = ["Open","Work in Progress","Resolved","Closed"];
var allTicketsDataTable = null;
$(document).ready(function(){
	if($("#ticketCountByStatusAndServiceType").size()>0){
		var ticketCountByStatusAndServiceType = $("#ticketCountByStatusAndServiceType").data("value");
		//console.log(ticketCountByStatusAndServiceType);
		var serviceTypeArray = [["Status"].concat(TICKET_STATUS_DESC).concat([{role: 'annotation'}])];
		var groupedAndTransposeOnIndex = groupByFirstAndTransposeOnSecond(ticketCountByStatusAndServiceType);
		serviceTypeArray = serviceTypeArray.concat(groupedAndTransposeOnIndex);
		var dataTable = google.visualization.arrayToDataTable(serviceTypeArray);
		var options = {
		        legend: 'none',
		        bar: { groupWidth: '50%' },
		        colors:['#d9534f','#5bc0de','#f0ad4e','#5cb85c'],
		        isStacked: true
		};
		var chart = new google.visualization.BarChart(document.getElementById("ticketCountByStatusAndServiceType"));
		chart.draw(dataTable, options);
	}
	
	if($('#vendorUserTicketCount')!=null){
		$('#vendorUserTicketCountTable').dataTable( {
	        "data": $("#vendorUserTicketCount").data("value"),
	        "paging": true,
	        "searching":true,
	        "info": false,
	        "order": [[ 1, "desc" ]]
	    });
	}
	
	if ($("#alltickettable").size() > 0) {
		allTicketsDataTable = $('#alltickettable').dataTable({
			"processing" : true,
			"language" : {
				"processing" : "<img src='assets/images/loading-b.gif' alt='loading'/>"
			},
			"serverSide" : true,
			ajax : {
				"url" : dataUrl,
				"type" : "POST",
				"data" : function(data) {
					planify(data);
				}
			},
			"columns" : columnsToDisplay			
		});
	}
	if($(".ticketStatusFilter").size()>0 && allTicketsDataTable!=null){
		$(".ticketStatusFilter").click(function(){
			var newUrl = $(this).data("href");
			allTicketsDataTable.api().ajax.url(newUrl).load();
			$("#filterNotification").html("Showing "+ $(this).data("filter") +" tickets");
			$("#filterNotification").removeClass("hide");
		});
	}
	
	$(window).resize(function(){
		
	})
});

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