var userOptionTemplate = null;
var siteOptionTemplate = null;
var serviceTypeOptionTemplate = null;
var assetOptionTemplate = null;
var allSites=null;
var allAssets=null;
var userSites=new Array();
var vendorServiceAsset=new Array();

$(document).ready(function () {
	if($("#userOptionTmpl").size()>0){
		userOptionTemplate = $.templates("#userOptionTmpl");
	}
	if($("#siteOptionTmpl").size()>0){
		siteOptionTemplate = $.templates("#siteOptionTmpl");
	}
	if($("#serviceTypeOptionTmpl").size()>0){
		serviceTypeOptionTemplate = $.templates("#serviceTypeOptionTmpl");
	}
	if($("#assetOptionTmpl").size()>0){
		assetOptionTemplate = $.templates("#assetOptionTmpl");
	}

	$('[data-toggle="offcanvas"]').click(function () {
		$('.row-offcanvas').toggleClass('active')
	});
	if($(".asminputfile").size()>0){
		$(".asminputfile").fileinput({'showUpload':false, 'previewFileType':'any',allowedFileExtensions: ["jpg", "gif", "png"]});
	}

	if($("#asmdatatable").size()>0){
		$('#asmdatatable').dataTable( {
			"processing": true,
			"serverSide": true,
			ajax: {
				"url": dataUrl,
				"data": function(data) {
					planify(data);
			}},
			"columns": columnsToDisplay,
			"rowCallback": function(row,data) {
				makeEditLink(row,data);
			}
		});
	}
	if($("#clientUserSiteMapId").size()>0){
		initClientSiteMapping();
	}
	if($("#vendorAssetMapId").size()>0){
		initVendorAssetMapping();
	}
});


function initClientSiteMapping(){
	$("#organizationId").change(function(eventData){
		if($(this).val()==""){
			alert("Please select an organization");
			return false;
		}
		$.ajax({
			 url:contextPath+"/admin/getUsersForOrganization?organizationId="+$(this).val(),
			 success:function(data){
				 $("#userId").html("<option value=''>Select User</option>");
				 $("#userId").append(userOptionTemplate.render(data));
			 },
			 error: function(){
				 alert("No Users found")
			 }
		});	
	});
	if($("#cityGeoId").size()>0){
		$("#cityGeoId").change(function(eventData){
			if($(this).val()==""){
				alert("Please select a city");
				return false;
			}
			$.ajax({
					 url:contextPath+"/admin/getSitesForCity?cityGeoId="+$(this).val(),
					 success:function(data){
						 allSites = data;
						 makeSiteSelectOptions();
					 },
					 error: function(){
						 alert("No Sites found");
					 }
				});	
		});
	}
	if($("#userId").size()>0){
		$("#userId").change(function(eventData){
			if($(this).val()==""){
				alert("Please select a user");
				return false;
			}
			$.ajax({
					 url:contextPath+"/admin/getUserAssignedSites?userId="+$(this).val(),
					 success:function(data){
						 if(typeof data!='undefined' && data.length>0){
							 for(var i=0;i<data.length;i++){
								 userSites[data[i].siteId]=data[i].name;	
							 }
							 makeSiteSelectOptions();
						 }	
					 },
					 error: function(){
						 alert("No Sites found");
					 }
				});	
		});
	}
	if($("#userId").val()!=null &&  $("#userId").val()!="" && $("#cityGeoId").val()!=null &&  $("#cityGeoId").val()!="") {
		$("#cityGeoId").trigger("change");
	}
	if($("#siteId").size()>0){
		initMultiSelect("siteId");
	}
}

function initVendorAssetMapping(){
	$("#organizationId").change(function(eventData){
		if($(this).val()==""){
			alert("Please select an organization");
			return false;
		}
		$.ajax({
			 url:contextPath+"/admin/getVendorsForOrganization?organizationId="+$(this).val(),
			 success:function(data){
				 $("#userId").html("<option value=''>Select User</option>");
				 $("#userId").append(userOptionTemplate.render(data));
			 },
			 error: function(){
				 alert("No Users found")
			 }
		});	
	});
	if($("#cityGeoId").size()>0){
		$("#cityGeoId").change(function(eventData){
			if($(this).val()==""){
				alert("Please select a city");
				return false;
			}
			$.ajax({
					 url:contextPath+"/admin/getSitesForCity?cityGeoId="+$(this).val(),
					 success:function(data){
						 $("#siteId").html("<option value=''>Select Sites</option>");
						 $("#siteId").append(siteOptionTemplate.render(data));
					 },
					 error: function(){
						 alert("No Sites found");
					 }
				});	
		});
	}
	
	if($("#siteId").size()>0){
		$("#siteId").change(function(eventData){
			if($(this).val()==""){
				alert("Please select a site");
				return false;
			}
			$.ajax({
					 url:contextPath+"/admin/getAssetsForSite?siteId="+$(this).val(),
					 success:function(data){
						 allAssets = data;
						 makeAssetsOptions();
					 },
					 error: function(){
						 alert("No Assets found");
					 }
				});	
		});
	}
	
	if($("#userId").size()>0){
		$("#userId").change(function(eventData){
			fetchAndMapVendorAssets();
			makeAssetsOptions();
		});
	}

	if($("#serviceTypeId").size()>0){
		$("#serviceTypeId").change(function(eventData){
			fetchAndMapVendorAssets();
			 makeAssetsOptions();
		});
	}

	if($("#assets").size()>0){
		initMultiSelect("assets");
	}
	if($("#siteId").val()!=null && $("#siteId").val()!=""){
		fetchAndMapVendorAssets();
		$("#siteId").trigger("change");
	}
}

function initMultiSelect(elementId){
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

function planify(data) {
	for (var i = 0; i < data.columns.length; i++) {
		column = data.columns[i];
		column.searchRegex = column.search.regex;
		column.searchValue = column.search.value;
		delete(column.search);
	}
}

function makeSiteSelectOptions(){
	 if($("#userId").val()!=null && $("#userId").val()!="" && userSites.length==0){
			$("#userId").trigger("change");
	 }
	 if(typeof allSites!='undefined' && allSites!=null && allSites.length>0){
		 for(var j=0; j<allSites.length; j++){
			 if(typeof userSites[allSites[j].siteId]!='undefined'){
				 allSites[j].siteSelected=true;
			 }else{
				 allSites[j].siteSelected=false;
			 }
		 }
		 addMultiSelectOptions("siteId",siteOptionTemplate,allSites);
	 }
}

function addMultiSelectOptions(elementId,templateObj,dataObj){
	 $("#"+elementId).html("");
	 $("#"+elementId).append(templateObj.render(dataObj));
	 $("#"+elementId).multiselect('rebuild'); 
}

function fetchAndMapVendorAssets(){
	if($("#userId").val()=="" || $("#serviceTypeId").val()==""){
		//alert("Please select a user");
		return false;
	}
	$.ajax({
			 url:contextPath+"/admin/getUserAssignedAssets?userId="+$("#userId").val()+"&serviceTypeId="+$("#serviceTypeId").val(),
			 success:function(data){
				 if(typeof data!='undefined' && data.length>0){
					 for(var i=0;i<data.length;i++){
						 vendorServiceAsset[$("#userId").val()+"_"+$("#serviceTypeId").val()+"_"+data[i].assetId]=data[i].name;	
					 }
				 }	
			 },
			 error: function(){
				 alert("No Asset mapped for user");
			 }
		});	
}

function makeAssetsOptions(){
	if(typeof allAssets !='undefined' && allAssets!=null && allAssets.length>0){
		for(var j=0; j<allAssets.length; j++){
			if(typeof vendorServiceAsset[$("#userId").val()+"_"+$("#serviceTypeId").val()+"_"+allAssets[j].assetId] !='undefined'){
				allAssets[j].assetSelected=true
			}else{
				allAssets[j].assetSelected=false;
			}
		}
	}
	addMultiSelectOptions("assets",assetOptionTemplate,allAssets);
} 