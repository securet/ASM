var userOptionTemplate = null;
var siteOptionTemplate = null;
var serviceTypeOptionTemplate = null;
var assetOptionTemplate = null;
var selectBoxTemplate = null;
var imgTemplate = null;
var geoTemplate = null;
var logoPreviewTemplate = null;
var allSites = null;
var allUnAssignedAssets = null;
var userSites = new Array();
var vendorServiceAsset = new Array();
var orderIndex=0;
var stateCitiesList = new Array();
var TYPE_AHEAD_LIMIT = 10;


$(document).ready(function() {
	$(document).ajaxSend(function(event, request, settings) {
	    $('#ajax_loader').show();
	});

	$(document).ajaxComplete(function(event, request, settings) {
	    $('#ajax_loader').hide();
	});
	// $.fn.dataTableExt.sErrMode = 'throw';
	if ($("form[name='loginForm']").size() > 0) {
		$("form[name='loginForm'] input[name='username']").focus();
	}

	if ($("#userOptionTmpl").size() > 0) {
		userOptionTemplate = $.templates("#userOptionTmpl");
	}
	if ($("#geoTmpl").size() > 0) {
		geoTemplate = $.templates("#geoTmpl");
	}
	if ($("#siteOptionTmpl").size() > 0) {
		siteOptionTemplate = $.templates("#siteOptionTmpl");
	}
	if ($("#serviceTypeOptionTmpl").size() > 0) {
		serviceTypeOptionTemplate = $.templates("#serviceTypeOptionTmpl");
	}
	if ($("#assetOptionTmpl").size() > 0) {
		assetOptionTemplate = $.templates("#assetOptionTmpl");
	}
	if ($("#imgTmpl").size() > 0) {
		imgTemplate = $.templates("#imgTmpl");
	}

	if ($("#selectBoxTmpl").size() > 0) {
		selectBoxTemplate = $.templates("#selectBoxTmpl");
	}
	if ($("#logoPreviewTemplate").size() > 0) {
		logoPreviewTemplate = $.templates("#logoPreviewTemplate");
	}
	$('[data-toggle="offcanvas"]').click(function() {
		$('.row-offcanvas').toggleClass('active')
	});

	if ($(".asminputfile").size() > 0) {
		$(".asminputfile").fileinput({
			'showUpload' : false,
			'previewFileType' : 'any',
			previewSettings : {
				'image' : {
					width : "100%",
					height : "auto"
				}
			},
			allowedFileExtensions : [ "jpg", "gif", "png" ]
		});
	}

	if ($("#asmdatatable").size() > 0) {
		$('#asmdatatable').dataTable({
			"processing" : true,
			"scrollX": true,
			"responsive":true,
			"language" : {
				"processing" : "<img src='assets/images/loading-b.gif' alt='loading'/>"
			},
			"order": [[ orderIndex, "desc" ]],
			"serverSide" : true,
			ajax : {
				"url" : dataUrl,
				"type" : "POST",
				"data" : function(data) {
					planify(data);
				}
			},
			"columns" : columnsToDisplay,
			"rowCallback" : function(row, data) {
				makeEditLink(row, data);
			}
		});
	}
	if ($("#clientUserSiteMapId").size() > 0) {
		initMultiSelect("userId");
		initClientSiteMapping();
	}
	if ($("#vendorAssetMapId").size() > 0) {
		initMultiSelect("userId");
		initVendorAssetMapping();
	}
	if ($("#state\\.geoId").size() > 0) {
		if ($("#SiteForm #city\\.geoId").size() == 0) {
			// initialize the city option
			$("#state\\.geoId").parents(".form-group").after(selectBoxTemplate.render({
				elementId : "circle.geoId",
				elementLabel : "Circle",
				options : []
			}));
			$("#circle\\.geoId").parents(".form-group").after(selectBoxTemplate.render({
				elementId : "city.geoId",
				elementLabel : "City",
				options : []
			}));
		}
		$("#state\\.geoId").change(function(eventData) {
			initCircleAndCity();
		});
		initMultiSelect("state\\.geoId");
	}
	if ($("#city\\.geoId").size() > 0) {
		initCircleAndCity();
		initMultiSelect("city\\.geoId");
		initMultiSelect("circle\\.geoId");
		initMultiSelect("module\\.moduleId");
	}
	
	if ($("input[name='logoFile']").size() > 0) {
		var imagePath = $("input[name='logoFile']").attr("value");
		if (imagePath != "") {
			$("#logo .file-preview").show();
			if (typeof imagePath != 'undefined' && imagePath != "") {
				$(".file-preview-thumbnails").append(logoPreviewTemplate.render({
					path : imagePath
				}));
			}
		}
	}
	
	if(typeof Bloodhound !='undefined'){
		$("#AssetForm #site\\.name").attr("placeholder","Start typing area or site tag to select site");
		var siteSearch = new Bloodhound({
			datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
			queryTokenizer: Bloodhound.tokenizers.whitespace,
			limit:TYPE_AHEAD_LIMIT,
			remote: contextPath+'/admin/searchSites?searchString=%QUERY&resultsSize='+TYPE_AHEAD_LIMIT
		});
		siteSearch.initialize();
		$('#AssetForm #site\\.name').typeahead(null, {
			name: 'siteName',
			displayKey: 'name',
			items: TYPE_AHEAD_LIMIT,
			source: siteSearch.ttAdapter()
		}); 
		$("#AssetForm #site\\.name").on("typeahead:opened",function(tpObj,selectedObj,fieldName){
			$("#site\\.siteId").val(0);
		});
		$("#AssetForm #site\\.name").on("typeahead:selected",function(tpObj,selectedObj,fieldName){
			$("#site\\.siteId").val(selectedObj.siteId);
		});
	}
	
	if($("#transferFromUserId").size()>0){
		initMultiSelect("transferFromUserId");
		initMultiSelect("transferToUserId");
	}
	
});

function initCircleAndCity(){
	var cityElementId=[];
	if ($("#circle\\.geoId").size() > 0) {
		cityElementId.push("circle\\.geoId");
		//getCitiesForState("state\\.geoId", "circle\\.geoId");
	}
	cityElementId.push("city\\.geoId");
	getCitiesForState("state\\.geoId", cityElementId);
}

function initClientSiteMapping() {
/*	$("#organizationId").change(function(eventData) {
		if ($(this).val() == "") {
			alert("Please select an organization");
			return false;
		}
		$.ajax({
			url : contextPath + "/admin/getUsersForOrganization?organizationId=" + $(this).val(),
			success : function(data) {
				data.unshift({"userId":"","fullName":"Select User ","emailId":"","mobile":"","organizationName":""});
				addMultiSelectOptions("userId", userOptionTemplate, data);
//				$("#userId").html("<option value=''>Select User</option>");
//				$("#userId").append(userOptionTemplate.render(data));
			},
			error : function() {
				alert("No Users found")
			}
		});
	});
*/
	if ($("#clientUserSiteMapId").size() > 0) {
		$("#clientUserSiteMapId #cityGeoId, #userId").change(function(eventData) {
			fetchClientUserSiteMappings();
		});
	}
	if ($("#siteId").size() > 0) {
		initMultiSelect("siteId");
		if ($("#userId").val() != null && $("#userId").val() != "" && $("#cityGeoId").val() != null && $("#cityGeoId").val() != "") {
			fetchClientUserSiteMappings();
		}
	}
}

function initVendorAssetMapping() {
	$("#organizationId").change(function(eventData) {
		if ($(this).val() == "") {
			alert("Please select an organization");
			return false;
		}
		$.ajax({
			url : VENDOR_FOR_ORG_URL+ "?organizationId=" + $(this).val(),
			success : function(data) {
				data.unshift({"userId":"","fullName":"Select User ","emailId":"","mobile":"","organizationName":""});
				addMultiSelectOptions("userId", userOptionTemplate, data);
				//$("#userId").html("<option value=''>Select User</option>");
				//$("#userId").append(userOptionTemplate.render(data));
				resetVendorMappingAssets();
			},
			error : function() {
				alert("No Users found")
			}
		});
	});

	if ($("#userId").size() > 0) {
		$("#userId,#serviceTypeId,#cityGeoId,#assetTypeId").change(function(eventData) {
			fetchAndMapVendorAssets(false);
		});
	}

	if ($("#assets").size() > 0) {
		initMultiSelect("assets");
		fetchAndMapVendorAssets(false);
	}
}

function initMultiSelect(elementId) {
	$("#" + elementId).multiselect({
		enableFiltering : true,
		filterBehavior : 'text',
		enableCaseInsensitiveFiltering : true,
		includeSelectAllOption : true,
		buttonWidth : "100%",
		buttonText: function(options, select) {
			var textToDisplay = "";
			if (options.length === 0) {
				textToDisplay= "None selected";
			}else if (options.length === 1) {
				/*if(options.val()==""){
					textToDisplay = options[0].text;
				}else{
					textToDisplay = options.val();
				}*/
				var max = options[0].text.length>30?30:options[0].text.length;
				textToDisplay = options[0].text.substring(0,max);
			}else{
				textToDisplay = options.length + " selected";
			}
			return textToDisplay+' <b class="caret"></b>';
		},
		maxHeight : 300,
		numberDisplayed : 1
	});
}
function planify(data) {
	for (var i = 0; i < data.columns.length; i++) {
		column = data.columns[i];
		column.searchRegex = column.search.regex;
		column.searchValue = column.search.value;
		delete (column.search);
	}
}

function makeSiteSelectOptions() {
	var sitesToShow = [];
	if (typeof userSites != 'undefined' && typeof allSites != 'undefined'){
		sitesToShow= sitesToShow.concat(allSites);
		sitesToShow = sitesToShow.concat(userSites);
	}
	addMultiSelectOptions("siteId", siteOptionTemplate, sitesToShow);
}

function addMultiSelectOptions(elementId, templateObj, dataObj) {
	$("#" + elementId).html("");
	$("#" + elementId).append(templateObj.render(dataObj));
	$("#" + elementId).multiselect('rebuild');
}

function getCitiesForState(stateElementId, cityElementIds) {
	if ($("#" + stateElementId).val() != "") {
		var stateId = $("#" + stateElementId).val();
		$.ajax({
			url : contextPath + "/admin/getCitiesForState?stateGeoId=" + stateId,
			success : function(data) {
				if(!$.isArray(cityElementIds)){
					cityElementIds=[cityElementIds];
				}
				for(var i=0;i<cityElementIds.length;i++){
					var cityElementId = cityElementIds[i];
					addMultiSelectOptions(cityElementId, geoTemplate, data);
					$("#default-" + cityElementId).data("default");
					if ($("#default-" + cityElementId).size() > 0 && $("#default-" + cityElementId).data("default") != "") {
						// keep the default selected..
						$("#" + cityElementId + " option[value='" + $("#default-" + cityElementId).data("default") + "']").attr("selected", "selected");
						$("#" + cityElementId).multiselect("rebuild");
					}
				}
			},
			error : function() {
				alert("No cities found");
			}
		});
	}
}

function fetchClientUserSiteMappings(){
	if ($("#cityGeoId").val() == "" || $("#userId").val() == "") {
		resetClientUserMappingSites();
		//alert("Please select a city");
		return false;
	}
	$.ajax({
		url : contextPath + "/admin/getUserAssignedAndUnAssignedSites?cityGeoId=" + $("#cityGeoId").val() +"&userId="+ $("#userId").val(),
		success : function(data) {
			if(typeof data!='undefined'){
				allSites = data.allRegionSites;
				userSites=data.userAssignedSites;
				for(var i=0; i< userSites.length; i++){
					userSites[i].siteSelected=true;
				}
			}else{
				allSites=[];
				userSites=[];
			}
			makeSiteSelectOptions();
		},
		error : function() {
			alert("No Sites found");
		}
	});
}

function fetchAndMapVendorAssets(fetchUnassigned) {
	if ($("#userId").val() == "" || $("#serviceTypeId").val() == "" || $("#cityGeoId").val() == "" || $("#assetTypeId").val() == "") {
		//reset assets if a change event got triggered 
		resetVendorMappingAssets();
		return false;
	}
	$.ajax({
		url : GET_VENDOR_ASSIGNED_UNASSIGNED_URL +"?userId=" + $("#userId").val() + "&serviceTypeId=" + $("#serviceTypeId").val() 
		+ "&assetTypeId=" + $("#assetTypeId").val() + "&cityGeoId=" + $("#cityGeoId").val(),
		success : function(data) {
			if (typeof data != 'undefined') {
				allUnAssignedAssets = data.unassignedAssets;
				vendorServiceAsset=data.assignedAssets;
				for(i=0;i<vendorServiceAsset.length;i++){
					vendorServiceAsset[i].assetSelected=true;
				}
			}else{
				allUnAssignedAssets=[]
				vendorServiceAsset=[];
			}
			makeAssetsOptions();
		},
		error : function() {
			alert("No Asset mapped for user");
		}
	});
}

function resetClientUserMappingSites(){
	allSites=[];
	userSites=[];
	makeSiteSelectOptions();
}

function resetVendorMappingAssets(){
	allUnAssignedAssets=[]
	vendorServiceAsset=[];
	makeAssetsOptions();
}

function makeAssetsOptions() {
	var assetsToShow = [];
	if (typeof vendorServiceAsset != 'undefined' && typeof allUnAssignedAssets != 'undefined'){
		assetsToShow= assetsToShow.concat(allUnAssignedAssets);
		assetsToShow = assetsToShow.concat(vendorServiceAsset);
	}
	addMultiSelectOptions("assets", assetOptionTemplate, assetsToShow);
}

function logoDisplay(data, type, full, meta) {
	return imgTemplate.render({
		imgclass : "tablelogo",
		path : data
	});
}

function formatTimeinHrsMins( data, type, full, meta){
	// get total seconds between the times
	var delta = data;
	// calculate (and subtract) whole days
	var days = Math.floor(delta / 86400);
	delta -= days * 86400;
	// calculate (and subtract) whole hours
	var hours = Math.floor(delta / 3600) % 24;
	delta -= hours * 3600;
	// calculate (and subtract) whole minutes
	var minutes = Math.floor(delta / 60) % 60;
	delta -= minutes * 60;
	// what's left is seconds
	var seconds = delta % 60;  // in theory the modulus is not required
	var formatStrParts = [];
	if(days>0){
		formatStrParts.push(days+(days>1?" days ":" day "));
	}
	if(hours >0){
		formatStrParts.push(hours+(hours>1?" hrs ":" hr "));
	}
	if(formatStrParts.length<2){
		formatStrParts.push(minutes+(minutes>1?" mins ":" min "));
	}
	if(formatStrParts.length<2){
		formatStrParts.push(seconds+(seconds>1?" secs ":" sec "));
	}
	return formatStrParts.join();
}