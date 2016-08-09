$(document).ready(function() {
  console.log($("#raw-data-ip").val());

  // notify when user tries to refresh 
  window.onbeforeunload = function() {
    return "All your changes will be lost if you refresh, are you sure?";
  };
  
	modalAction();
	prepareData();
	TreeView.createTree();
	createTableSelectList(document.getElementById("rftable-new-col"));
	showPKListByChosenTableName($("#rftable-new-col").val(), document.getElementById("rfcolumn-new-col"));
	
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
	
	$("#type-new-col").change(function(){
		var colType = $("#type-new-col").val();
		
		if(colType == "nm" || colType == "pk") {
			$("#col-input").css({'height':'325px'});
			$("#rftable-nc-container").hide();
			$("#rfcolumn-nc-container").hide();
				
			if(colType == "nm") $("#ai-nc-checkbox").hide();
			else $("#ai-nc-checkbox").show();
		}
		else if(colType == "fk") {
			$("#col-input").css({'height':'425px'});
			$("#ai-nc-checkbox").hide();
			$("#rftable-nc-container").show();
			$("#rfcolumn-nc-container").show();
		}
	});
	
	$("#create-new-col").click(function(){
		var result = validateName();
		if(result == false) {
			return;
		}
		else {
			var simpleCol = {
				type: 			$("#type-new-col").val(),
				columnName: 	$("#name-new-col").val(),
				dataType:		$("#datatype-new-col").val(),
				notNull: 		$("#nn-new-col").is(":checked") ? true : false,
				autoIncrement:	$("#ai-new-col").is(":checked") ? true : false,
				rfTableName:	$("#rftable-new-col").val(),
				rfColName:		$("#rfcolumn-new-col").val()
			};
			
			InfoPanel.addColumn(Table.instance, simpleCol);
			
			TreeView.recreateTree();
			//TreeView.expanseElement(Table.instance.tableName);
			
			console.log(TablesList.instances);
			
			InfoPanel.displayCurrentTable();
		}
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

function createTableSelectList(element) {
	if(TablesList.instances != null) {
		for(var i = 0; i < TablesList.instances.length; i++) {
			element.options[element.options.length]= new Option(TablesList.instances[i].tableName, TablesList.instances[i].tableName);
		} 
	}
}

function onChangeSelectedTableNC() {
	var tableName = $("#rftable-new-col").val();
	showPKListByChosenTableName(tableName, document.getElementById("rfcolumn-new-col"));
}

function showPKListByChosenTableName(tableName, colElement) {
	var table = TablesList.findTableByName(tableName);
	
	colElement.options.length = 0;
			
	for(var i = 0; i < table.childs.length; i++) {
		if(table.childs[i].json == "pk")
			colElement.options[colElement.options.length]= new Option(table.childs[i].columnName, table.childs[i].columnName)
	} 
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

function resetCreatedColPanelValue() {
	$("#new-col-tablename").text("Table: " + Table.instance.tableName);
	
	$("#type-new-col").val("nm");
	$("#col-input").css({'height':'325px'});
	
	$("#ai-nc-checkbox").hide();
	$("#rftable-nc-container").hide();
	$("#rfcolumn-nc-container").hide();
	
	$("#name-new-col").val("");
	$("#nn-nc-checkbox").prop('checked', false);
	
	var parentElement = $("#name-new-col").parent();
	parentElement.removeClass();

	var spanElement = parentElement.children("span");
	if(spanElement != null) spanElement.remove();
	
	$("#validate-notice-col").text("");
}
function modalAction() {
	$('#upload-trigger').click(function() {
		$('#uploadModal').modal("show");
	});	
	
	$("#add-col-trigger").click(function() {
		$('#addColModal').modal("show");
	})
}

function validateName() {
	var tempColName = $("#name-new-col").val();
	var colFound = Table.findColumnByName(Table.instance, tempColName);

	var parentElement = $("#name-new-col").parent();
	parentElement.removeClass();

	var spanElement = parentElement.children("span");
	if(spanElement != null) spanElement.remove();
	
	if(tempColName == "") {
		$("#validate-notice-col").css({'color':'#a94442'});
		$("#validate-notice-col").text("Column name is required.");
		parentElement.addClass("has-error has-feedback");
		parentElement.append("<span class='glyphicon glyphicon-remove form-control-feedback'></span>");
		return false;
	}
	else if(colFound == null) {
		$("#validate-notice-col").css({'color':'#3c763d'});
		$("#validate-notice-col").text("You can use this name.");
		parentElement.addClass("has-success has-feedback");
		parentElement.append("<span class='glyphicon glyphicon-ok form-control-feedback'></span>");
		return true;
	}
	else {
		$("#validate-notice-col").css({'color':'#a94442'});
		$("#validate-notice-col").text("This name is used by another column.");
		parentElement.addClass("has-error has-feedback");
		parentElement.append("<span class='glyphicon glyphicon-remove form-control-feedback'></span>")
		return false;
	}
}

function getJsonData() {
	RootNode.instance.childs = TablesList.instances;
	var rawData = JSON.stringify(RootNode.instance);
	console.log("Raw Data: " + rawData);
	$(".rawdata-op-area input[type=hidden]").val(rawData);
}
function tranferMode(){
	var element = document.getElementById('tableMode'),
    style = window.getComputedStyle(element);
    display = style.getPropertyValue('display');
	if(display == "none"){
		$("#present-mode").text("Table Mode");
		document.getElementById('tableMode').style.display ="block";
		document.getElementById('ERDMode').style.display ="none";
	}else{
		$("#present-mode").text("ERD Mode");
		document.getElementById('tableMode').style.display ="none";
		document.getElementById('ERDMode').style.display ="block";
	}
}