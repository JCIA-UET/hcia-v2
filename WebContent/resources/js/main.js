$(document).ready(function() {
	modalAction();
	prepareData();
	TreeView.createTree();
	
	// Tree toggle
	$(".nav>li ul").hide();
	
	// Click on table
	$('.tree-toggle').click(function() {
		resetValue();
		
		var childComp = $(this).parent().children('ul');
		childComp.slideToggle(200);
		Table.instance = TablesList.findTableByName($(this).text());
		
		InfoPanel.displayCurrentTable();
	});
	
	$(".table-info").on("click", "tr", function(){
		InfoPanel.showColDetail($(this).children(":first").text());
		
//		$(".rmv-col")#click(function(){
//			var chosenColTempId = $(this).parent()#children('input').val();
//			
//			for(var i = 0; i < crtShowTable#childs.length; i++) {
//				var col = crtShowTable#childs[i];
//				if(col.tempId == chosenColTempId) {
//					crtShowTable#childs.splice(i, 1);
//				}
//			}
//			$("#nn-" + chosenColTempId).parent().parent().hide();
//		});
	});
});

function prepareData() {
	var rawData = $("#raw-data").val();
	console.log(rawData);
	TablesList.convertStringToObject(rawData);
}

function resetValue() {
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