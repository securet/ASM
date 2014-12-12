var errorMessageTemplate = null;
var selectBoxTemplate = null;
var siteData={};
var currentPosition=null;
var ALL_OK = "ALL OK";
var previewSettings =     {
	    'image': {width: "auto", height:"auto"},
	    'html': {width: "auto", height:"auto"},
	    'text': {width: "auto", height:"50px"},
	    'video': {width: "auto", height:"auto"},
	    'audio': {width: "auto", height:"auto"},
	    'flash': {width: "auto", height:"auto"},
	    'object': {width: "auto", height:"auto"},
	    'other': {width: "auto", height:"auto"}
	    };

$(document).ready(function(){
	if($("#errorMessageTmpl").size()>0){
		errorMessageTemplate = $.templates("#errorMessageTmpl");
	}
	if($("#selectBoxTmpl").size()>0){
		selectBoxTemplate = $.templates("#selectBoxTmpl");
	}
	if($("#newTicket").size()>0){
		initTicketEvents();
	}
	if($("#editTicket").size()>0){
		attachBootstrapUpload("ticketAttachments");
	}
	if($("#map-canvas").size()>0){
		initializeMap();
	}
});

function initTicketEvents(){
	var siteElement = $("#newTicket #site\\.siteId");
	var serviceTypeElement = $("#newTicket #serviceType\\.serviceTypeId");
	var vendorOrgElement = $("#newTicket #vendorOrgblock");
	var vendorUserElement = $("#newTicket #vendorUserblock");
	if(siteElement.size()>0){
		initMultiSelectDropDown("site\\.siteId");
		bindSiteChange(siteElement,serviceTypeElement);;
	}
	attachBootstrapUpload("ticketAttachments");
	if(serviceTypeElement.size()>0){
		fetchVendorAndIssueType(serviceTypeElement,siteElement,vendorOrgElement,vendorUserElement);
	}
	getLocation(assignPosition);
}

function attachBootstrapUpload(elementId) {
	if($("#"+elementId).size()>0){
		$("#"+elementId).fileinput({'showUpload':false,maxFileSize:2000,maxFileCount:3, wrapTextLength:100,'previewFileType':'any',previewSettings:previewSettings});
	}
}

function bindSiteChange(siteElement,serviceTypeElement){
	siteData =siteElement.data("site");
	siteElement.change(function(){
		$("#newTicket #areablock").removeClass("hide");
		$("#newTicket #area").html(siteData[siteElement.val()].area);
		serviceTypeElement.prop("disabled",false);
	});}

function showTicketOrganization(vendorOrgElement,vendorUserElement,data){
	 $("#vendorNotAssignedError").remove();
	 vendorOrgElement.removeClass("hide");
	 var orgLabel = vendorOrgElement.find("#vendorOrg")
	 orgLabel.html(data.vendors.organization.name);
	 vendorUserElement.removeClass("hide");
	 var userLabel = vendorUserElement.find("#vendorUser")
	 userLabel.html(data.vendors.userId);
}

function makeIssueTypeOptions(vendorUserElement,data){
	 if(typeof data.issueTypes!='undefined' && data.issueTypes!=null && data.issueTypes.length>0){
		 var issueOptions = [];
		 $("#issueType\\.issueTypeId").remove();
		 for(var i=0; i<data.issueTypes.length;i++){
			 var option = new Object();
			 option.value=data.issueTypes[i].issueTypeId;
			 option.text=data.issueTypes[i].name;
			 issueOptions[i]=option;
		 }
		 vendorUserElement.after(selectBoxTemplate.render({elementId:"issueType.issueTypeId",elementLabel:"Issue Type",options:issueOptions}));
	 }
}

function resetNoVendorMapping(vendorOrgElement,vendorUserElement,serviceTypeElement,showError){
	 resetIssueType(vendorOrgElement, vendorUserElement);
	 $("#vendorNotAssignedError").remove();
	 if(showError){
		 serviceTypeElement.parents(".form-group").after(errorMessageTemplate.render({elementId:"vendorNotAssignedError",message:"No Vendor Assigned"}));
	 }
}

function resetIssueType(vendorOrgElement,vendorUserElement){
	$("#issueType\\.issueTypeId").parents(".form-group").remove();
	vendorOrgElement.addClass("hide");
	vendorUserElement.addClass("hide");
}

function fetchVendorAndIssueType(serviceTypeElement,siteElement,vendorOrgElement,vendorUserElement){
	serviceTypeElement.change(function(e){
		$("#siteSelectionError").hide();
		if(siteElement.val()==""){
			if($("#siteSelectionError").size()>0){
				$("#siteSelectionError").show();
			}else{
				siteElement.parents(".form-group").before(errorMessageTemplate.render({elementId:"siteSelectionError",message:"Please select a site"}));
			}
		}else if(serviceTypeElement.find("option:selected").text()!=ALL_OK){
			$.ajax({
				 url:contextPath+"/tickets/getVendorsAndIssueTypes?siteId="+siteElement.val()+"&serviceTypeId="+serviceTypeElement.val(),
				 success:function(data){
					 if(data!=null){
						 if(typeof data.vendors!='undefined' && data.vendors!=null){
							 resetIssueType(vendorOrgElement, vendorUserElement);
							 showTicketOrganization(vendorOrgElement,vendorUserElement,data);
							 makeIssueTypeOptions(vendorUserElement,data);	
						 }else{
							 resetNoVendorMapping(vendorOrgElement,vendorUserElement,serviceTypeElement,true);
						 }
					 }
				 },
				 error: function(){
					 $("#newTicket").append(errorMessageTemplate.render({elementId:"couldnotloaddata",message:"Something went wrong, try again or refresh the page"}));
				 }
			});
		}else{
			 resetNoVendorMapping(vendorOrgElement,vendorUserElement,serviceTypeElement,false);
		}
	});
}

function initMultiSelectDropDown(elementId){
	$("#"+elementId).multiselect({
		enableFiltering: true,
		filterBehavior: 'text',
		enableCaseInsensitiveFiltering:true,
		includeSelectAllOption: true,
		buttonWidth:"100%",
		maxHeight:300,
		numberDisplayed: 1
	});
}

function getLocation(assignPosition) {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(assignPosition);
    }
}

function assignPosition(position) {
    currentPosition = {latitude:position.coords.latitude,longitude:position.coords.longitude};
	if(currentPosition!=null){
		$("#newTicket input[name='latitude']").val(currentPosition.latitude);
		$("#newTicket input[name='longitude']").val(currentPosition.longitude);
	}
}

function initializeMap() {
    var mapOptions = {
      center: $("#lat-long").data("value"),
      zoom: 15
    };
    var marker = new google.maps.Marker({
        position: $("#lat-long").data("value"),
        title:$("#lat-long").data("title")
    });

    var map = new google.maps.Map(document.getElementById('map-canvas'),mapOptions);
    marker.setMap(map);
}
