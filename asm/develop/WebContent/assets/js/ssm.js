$(document).ready(function () {
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
	  } );
  }
});

function planify(data) {
    for (var i = 0; i < data.columns.length; i++) {
        column = data.columns[i];
        column.searchRegex = column.search.regex;
        column.searchValue = column.search.value;
        delete(column.search);
    }
}