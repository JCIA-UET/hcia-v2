$(document).ready(function() {
  
//  console.log($("#raw-data-ip").val());

  // notify when user tries to refresh 
  window.onbeforeunload = function() {
    return "All data will be lost if you refresh, are you sure?";
  };
  
	modalAction();
	prepareData();
	TreeView.createTree();
	
	// Click on table
	$('.hcia-treepanel').on("click", ".tree-toggle", function() {
		resetPanelValue();
		
		var childComp = $(this).parent().children('ul');
		childComp.slideToggle(200);
		Table.instance = TablesList.findTableByName($(this).text());
		
		InfoPanel.displayCurrentTable();
	});
	
	$(".table-info").on("click", "tr", function(){
		InfoPanel.showColDetail($(this).children(":first").text());
		
		$(".rmv-col").on("click", function(){
			if(confirm("Are you sure?") == true)
				InfoPanel.deleteColumn($(this).parent().children('input').val());
			else return false;
		});
	});
	
	
	
	$(".relationship-info").on("click", "tr", function(){
		console.log("show detail: " + $(this).children().eq(1).text());
		InfoPanel.showRelaDetail($(this).children().eq(1).text());
		
		$(".rmv-rela").on("click", function(){
			if(confirm("Are you sure?") == true)
				InfoPanel.deleteRela($(this).parent().children('input').val());
			else return false;
		});
	});
	
//	$(".download-btn").click(function(){
//		var rawData = JSON.stringify(TablesList.instances);
//		console.log("Raw Data: " + rawData);
//		$(".rawdata-op-area input[type=hidden]").val(rawData);
//	});
});

function prepareData() {
	TablesList.convertStringToObject($("#raw-data-ip").val());
}

function resetPanelValue() {
	// refresh details view
	$("#col-name-detail").val("");
	$("#col-length-detail").val("");
	$("#col-nn-detail").val("");
	$("#col-pk-detail").val("");
	$("#col-fk-detail").val("");
	$("#col-ai-detail").val("");
	
	// refresh tree and table
	$(".nav>li ul").hide();
	$(".table-info").empty();
	$(".relation-info").empty();
}
function modalAction() {
	$('#upload-trigger').click(function() {
		$('#uploadModal').modal("show");
	});
}
function getJsonData() {
	RootNode.instance.childs = TablesList.instances;
	var rawData = JSON.stringify(RootNode.instance);
	console.log("Raw Data: " + rawData);
	$(".rawdata-op-area input[type=hidden]").val(rawData);
}