$(document).ready(function() {
  
  console.log($("#raw-data-ip").val());

  // notify when user tries to refresh 
  window.onbeforeunload = function() {
    return "All your changes will be lost if you refresh, are you sure?";
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
	});
	
	$(".table-info").on("click", ".rmv-col", function(){
		var colId = $(this).parent().children('input').val();
		$("#related-none").html("");
		$("#related-column").html("");
		$("#related-relation").html("");
		// Add value to modal
		var relatedList = TablesList.findRelatedElements(Table.instance, colId);
		if(relatedList.length == 0) {
			$("#related-none").append("<strong>No columns and relations are affected.</strong>")
		}
		else {
			$("#related-column").append("<u><h3>Column:</h3></u>");
			$("#related-relation").append("<u><h3>Relation:</h3></u>");
			
			for(var i = 0; i < relatedList.length; i++) {
				var relatedTable = TablesList.instances[parseInt(relatedList[i][0])];
				var relatedColumn = relatedTable.childs[parseInt(relatedList[i][1])];
				
				if(relatedColumn.json == "column" || relatedColumn.json == "pk") {
					$("#related-column").append("<div>" +
							"<strong>" + relatedColumn.columnName + "</strong> (in " +
							"<strong>" + relatedTable.tableName + "</strong>)" +
							"</div>");
				}
				else if(relatedColumn.json == "mto" || relatedColumn.json == "otm") {
					$("#related-relation").append("<div>" +
							"<strong>" + relatedColumn.type + "</strong> (in " +
							"<strong>" + relatedTable.tableName + "</strong>)" +
							"</div>");
				}
			}
		}
		// Show notice modal
		$("#noticeModal")
			.modal({ backdrop: 'static', keyboard: false })
			.one("click", "#delete", function(){
				InfoPanel.deleteColumn(Table.instance, colId, relatedList);
		});
	});
	
	$(".relationship-info").on("click", "tr", function(){
		console.log("show detail: " + $(this).children().eq(1).text());
		InfoPanel.showRelaDetail($(this).children().eq(1).text());
	});
	
	$(".relationship-info").on("click", ".rmv-rela", function(){
		var colId = $(this).parent().children('input').val();
		$("#related-none").html("");
		$("#related-column").html("");
		$("#related-relation").html("");
		// Add value to modal
		var relatedList = TablesList.findRelatedElements(Table.instance, colId);
		
		if(relatedList.length == 0) {
			$("#related-none").append("<strong>No column and relation are affected</strong>")
		}
		else {
			$("#related-column").append("<u><h3>Column:</h3></u>");
			$("#related-relation").append("<u><h3>Relation:</h3></u>");
			
			for(var i = 0; i < relatedList.length; i++) {
				var relatedTable = TablesList.instances[parseInt(relatedList[i][0])];
				var relatedColumn = relatedTable.childs[parseInt(relatedList[i][1])];
				
				if(relatedColumn.json == "column" || relatedColumn.json == "pk") {
					$("#related-column").append("<div>" +
							"<strong>" + relatedColumn.columnName + "</strong> (in " +
							"<strong>" + relatedTable.tableName + "</strong>)" +
							"</div>");
				}
				else if(relatedColumn.json == "mto" || relatedColumn.json == "otm") {
					$("#related-relation").append("<div>" +
							"<strong>" + relatedColumn.type + "</strong> (in " +
							"<strong>" + relatedTable.tableName + "</strong>)" +
							"</div>");
				}
			}
		}
		// Show notice modal
		$("#noticeModal")
			.modal({ backdrop: 'static', keyboard: false })
			.one("click", "#delete", function(){
				InfoPanel.deleteRela(Table.instance, colId, relatedList);
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
	Relationship.loadAll();
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
function tranferMode(){
	var element = document.getElementById('tableMode'),
    style = window.getComputedStyle(element),
    display = style.getPropertyValue('display');
	if(display == "none"){
		document.getElementById('tableMode').style.display ="block";
		document.getElementById('ERDMode').style.display ="none";
	}else{
		document.getElementById('tableMode').style.display ="none";
		document.getElementById('ERDMode').style.display ="block";
	}
}