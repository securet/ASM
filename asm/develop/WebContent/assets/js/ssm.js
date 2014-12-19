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
			"columns" : columnsToDisplay,
			"rowCallback" : function(row, data) {
				makeEditLink(row, data);
			}
		});
	}
	if ($("#clientUserSiteMapId").size() > 0) {
		initClientSiteMapping();
	}
	if ($("#vendorAssetMapId").size() > 0) {
		initVendorAssetMapping();
	}
	if ($("#state\\.geoId").size() > 0) {
		if ($("#SiteForm #city\\.geoId").size() == 0) {
			// initialize the city option
			$("#state\\.geoId").parents(".form-group").after(selectBoxTemplate.render({
				elementId : "city.geoId",
				elementLabel : "City",
				options : []
			}));
		}
		$("#state\\.geoId").change(function(eventData) {
			getCitiesForState("state\\.geoId", "city\\.geoId");
		});
		initMultiSelect("state\\.geoId");
	}
	if ($("#city\\.geoId").size() > 0) {
		getCitiesForState("state\\.geoId", "city\\.geoId");
		initMultiSelect("city\\.geoId");
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
});

function initClientSiteMapping() {
	$("#organizationId").change(function(eventData) {
		if ($(this).val() == "") {
			alert("Please select an organization");
			return false;
		}
		$.ajax({
			url : contextPath + "/admin/getUsersForOrganization?organizationId=" + $(this).val(),
			success : function(data) {
				$("#userId").html("<option value=''>Select User</option>");
				$("#userId").append(userOptionTemplate.render(data));
			},
			error : function() {
				alert("No Users found")
			}
		});
	});
	if ($("#clientUserSiteMapId #cityGeoId").size() > 0) {
		$("#clientUserSiteMapId #cityGeoId").change(function(eventData) {
			if ($(this).val() == "") {
				alert("Please select a city");
				return false;
			}
			$.ajax({
				url : contextPath + "/admin/getSitesForCity?cityGeoId=" + $(this).val(),
				success : function(data) {
					allSites = data;
					makeSiteSelectOptions();
				},
				error : function() {
					alert("No Sites found");
				}
			});
		});
	}
	if ($("#userId").size() > 0) {
		$("#userId").change(function(eventData) {
			if ($(this).val() == "") {
				alert("Please select a user");
				return false;
			}
			$.ajax({
				url : contextPath + "/admin/getUserAssignedSites?userId=" + $(this).val(),
				success : function(data) {
					if (typeof data != 'undefined' && data.length > 0) {
						for (var i = 0; i < data.length; i++) {
							userSites[data[i].siteId] = data[i].name;
						}
						makeSiteSelectOptions();
					}
				},
				error : function() {
					alert("No Sites found");
				}
			});
		});
	}
	if ($("#userId").val() != null && $("#userId").val() != "" && $("#cityGeoId").val() != null && $("#cityGeoId").val() != "") {
		$("#cityGeoId").trigger("change");
	}
	if ($("#siteId").size() > 0) {
		initMultiSelect("siteId");
	}

}

function initVendorAssetMapping() {
	$("#organizationId").change(function(eventData) {
		if ($(this).val() == "") {
			alert("Please select an organization");
			return false;
		}
		$.ajax({
			url : contextPath + "/admin/getVendorsForOrganization?organizationId=" + $(this).val(),
			success : function(data) {
				$("#userId").html("<option value=''>Select User</option>");
				$("#userId").append(userOptionTemplate.render(data));
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
	if ($("#userId").val() != null && $("#userId").val() != "" && userSites.length == 0) {
		$("#userId").trigger("change");
	}
	if (typeof allSites != 'undefined' && allSites != null && allSites.length > 0) {
		for (var j = 0; j < allSites.length; j++) {
			if (typeof userSites[allSites[j].siteId] != 'undefined') {
				allSites[j].siteSelected = true;
			} else {
				allSites[j].siteSelected = false;
			}
		}
		addMultiSelectOptions("siteId", siteOptionTemplate, allSites);
	}
}

function addMultiSelectOptions(elementId, templateObj, dataObj) {
	$("#" + elementId).html("");
	$("#" + elementId).append(templateObj.render(dataObj));
	$("#" + elementId).multiselect('rebuild');
}

function getCitiesForState(stateElementId, cityElementId) {
	if ($("#" + stateElementId).val() != "") {
		var stateId = $("#" + stateElementId).val();
		$.ajax({
			url : contextPath + "/admin/getCitiesForState?stateGeoId=" + stateId,
			success : function(data) {
				addMultiSelectOptions(cityElementId, geoTemplate, data);
				$("#default-" + cityElementId).data("default");
				if ($("#default-" + cityElementId).size() > 0 && $("#default-" + cityElementId).data("default") != "") {
					// keep the default selected..
					$("#" + cityElementId + " option[value='" + $("#default-" + cityElementId).data("default") + "']").attr("selected", "selected");
					$("#" + cityElementId).multiselect("rebuild");
				}
			},
			error : function() {
				alert("No cities found");
			}
		});
	}
}

function fetchAndMapVendorAssets(fetchUnassigned) {
	if ($("#userId").val() == "" || $("#serviceTypeId").val() == "" || $("#cityGeoId").val() == "" || $("#assetTypeId").val() == "") {
		//reset assets if a change event got triggered 
		resetVendorMappingAssets();
		return false;
	}
	$.ajax({
		url : contextPath + "/admin/getUserAssignedAndUnassignedAssets?userId=" + $("#userId").val() + "&serviceTypeId=" + $("#serviceTypeId").val() 
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
	/*	if (typeof vendorServiceAsset != 'undefined' && typeof allUnAssignedAssets != 'undefined') {
		
	}
	if (typeof allUnAssignedAssets != 'undefined' && assetsToShow != null && assetsToShow.length > 0) {
		for (var j = 0; j < assetsToShow.length; j++) {
			var key = $("#userId").val() + "_" + $("#serviceTypeId").val() + "_" + assetsToShow[j].assetId;
			if (typeof vendorServiceAsset[key] != 'undefined') {
				assetsToShow[j].assetSelected = true
			} else {
				assetsToShow[j].assetSelected = false;
			}
		}
	}
*/	
	addMultiSelectOptions("assets", assetOptionTemplate, assetsToShow);
}

function logoDisplay(data, type, full, meta) {
	return imgTemplate.render({
		imgclass : "tablelogo",
		path : data
	});
}