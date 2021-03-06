window.onbeforeunload = function(e) {
	return message = "If you changed your data, please click Save button to save all your changes. Or your data will be lost!";
};

$(document).ready(function() {
	modalAction();
	console.log($("#raw-data-ip").val());
	console.log(TablesList.instances);
	console.log(RootNode.instance);
	
	// Prepare all need element
	prepareData();
	
	// Hide unnecessary action can make server crash
	if(TablesList.instances == null) {
		$(".download-btn").hide();
		$(".gensql-btn").hide();
		$("#save-form\\:save-btn").hide();
	}

	Table.instance = null;
	$("#add-col-trigger").hide();
	$(".hcia-contentpanel").children("ul").hide();
	$(".tab-content").hide();
	$('.transfer-nav').hide();
	
	TreeView.createTree();
	
	createSelectedListOfTable(document.getElementById("rftable-new-col"));
	showPKListByChosenTableName($("#rftable-new-col").val(), document.getElementById("rfcolumn-new-col"));
	
	/** Nav Events **/
	// Toogle Click Event
	$('.toggle-nav').click(function() {
		$('.navbar').slideToggle("200");
		$('.transfer-nav').slideToggle("200");
	});
	
	/** Tree Events **/
	// Table Click Event
	$('.hcia-treepanel').on("click", ".tree-toggle", function() {
		resetPanelValue();
		
		var childComp = $(this).parent().children('ul');
		childComp.slideToggle(200);
		Table.instance = TablesList.findTableByName($(this).text());
		
		InfoPanel.displayCurrentTable();
		
		$("#add-col-trigger").show();
	});
	
	// Column Click Event
	$('.hcia-treepanel').on("click", ".tree-node", function() {
		var columnName = $(this).text();
		$(".detail-wrap").show();
		InfoPanel.showColDetail(columnName);
	});
	
	$("#col-tab").click(function(){$(".detail-wrap").hide();});
	$("#fk-tab").click(function(){$(".detail-wrap").hide();});
	
	/** Table Click Events **/
	// Row Click Event
	$(".table-info").on("click", "tr", function(){
		var parent = $(this).parent();
		TableAction.setFocus(parent, this);
		$(".detail-wrap").show();
		InfoPanel.showColDetail($(this).children(":first").text());
	});
	
	// Remove Icon Click Event
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
				
				// Recreate tree (remove deleted elements)
				TreeView.recreateTree();
				TreeView.expanseElement(Table.instance.tableName);
		});
	});
	
	/** Relationship Table Click Events **/
	// Row Click Event
	$(".relationship-info").on("click", "tr", function(){
		var parent = $(this).parent();
		TableAction.setFocus(parent, this);
		InfoPanel.showRelaDetail($(this).children().eq(1).text());
		$(".detail-wrap").show();
	});
	
	// Remove Icon Click Event
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
				
				// Recreate tree (remove deleted elements)
				TreeView.recreateTree();
				TreeView.expanseElement(Table.instance.tableName);
		});
	});
	
	/** Add Column Dialog Event **/
	$("#type-new-col").change(function(){
		var colType = $("#type-new-col").val();
		resetCreatedColFormValue();
		$("#col-input").css({'height':'300px'});
		
		if(colType == "nm" || colType == "pk") {	
			
			$("#rftable-nc-container").hide();
			$("#rfcolumn-nc-container").hide();
			
			$("#datatype-nc-container").show();
			$("#length-nc-container").show();
				
			if(colType == "nm") $("#ai-nc-checkbox").hide();
			else $("#ai-nc-checkbox").show();
		}
		else if(colType == "fk") {
			$("#ai-nc-checkbox").hide();
			$("#datatype-nc-container").hide();
			$("#length-nc-container").hide();
			
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
			TreeView.expanseElement(Table.instance.tableName);
			
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

function createSelectedListOfTable(element) {
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
	$("#type-new-col").val("nm");
	resetCreatedColFormValue();
}

function resetCreatedColFormValue() {
	$("#new-col-tablename").text("Table: " + Table.instance.tableName);
	
	$("#col-input").css({'height':'325px'});
	
	$("#ai-nc-checkbox").hide();
	$("#rftable-nc-container").hide();
	$("#rfcolumn-nc-container").hide();
	
	$("#name-new-col").val("");
	$("#length-new-col").val("");
	$("#nn-nc-checkbox").prop('checked', false);
	
	var parentElement = $("#name-new-col").parent();
	parentElement.removeClass();

//	var spanElement = parentElement.children("span");
//	if(spanElement != null) spanElement.remove();
	
	var parentElement = $("#length-new-col").parent();
	parentElement.removeClass();

//	var spanElement = parentElement.children("span");
//	if(spanElement != null) spanElement.remove();
	
	$("#validate-name-notice").text("");
	$("#validate-length-notice").text("");
}
function modalAction() {
	$('#upload-trigger').click(function() {
		$('#uploadModal').modal("show");
	});	
	
	$("#add-col-trigger").click(function() {
		$('#addColModal').modal("show");
	})
}

function navMouseOver() {
	
}

function navMouseOut() {
	console.log("out");
	$('.navbar').slideUp("200");
	$('.toogle-nav').slideDown("200");
}

function validateName() {
	var reg = new RegExp("^[0-9]");
	var tempColName = $("#name-new-col").val();
	var colFound = Table.findColumnByName(Table.instance, tempColName);

	var parentElement = $("#name-new-col").parent();
	parentElement.removeClass();

	var spanElement = parentElement.children("span");
	if(spanElement != null) spanElement.remove();
	
	if(tempColName == "") {
		$("#validate-name-notice").css({'color':'#a94442'});
		$("#col-input").css({'height':'325px'});
		$("#validate-name-notice").text("This field is required.");
		parentElement.addClass("has-error has-feedback");
		//parentElement.append("<span id='name-icon' class='glyphicon glyphicon-remove form-control-feedback'></span>");
		return false;
	}
	else if(reg.test(tempColName)) {
		$("#validate-name-notice").css({'color':'#a94442'});
		$("#col-input").css({'height':'325px'});
		$("#validate-name-notice").text("Column Name cannot begin with numbers.");
		parentElement.addClass("has-error has-feedback");
		return false;
	}
	else if(colFound == null) {
		$("#validate-name-notice").css({'color':'#3c763d'});
		$("#col-input").css({'height':'325px'});
		$("#validate-name-notice").text("You can use this name.");
		parentElement.addClass("has-success has-feedback");
		//parentElement.append("<span id='name-icon' class='glyphicon glyphicon-ok form-control-feedback'></span>");
		return true;
	}
	else {
		$("#validate-name-notice").css({'color':'#a94442'});
		$("#col-input").css({'height':'325px'});
		$("#validate-name-notice").text("This name is used by another column.");
		parentElement.addClass("has-error has-feedback");
		//parentElement.append("<span id='name-icon' class='glyphicon glyphicon-remove form-control-feedback'></span>")
		return false;
	}
	
	//$("#name-icon").css({'line-height': '75px'});
}

function validateLength() {
	var reg = new RegExp("^[0-9]+$");
	var lengthCol = $("#length-new-col").val();
	
	var parentElement = $("#length-new-col").parent();
	parentElement.removeClass();

	var spanElement = parentElement.children("span");
	if(spanElement != null) spanElement.remove();
	
	if(reg.test(lengthCol)) {
		$("#validate-length-notice").css({'color':'#3c763d'});
		$("#validate-length-notice").text("");
		parentElement.addClass("has-success has-feedback");
		//parentElement.append("<span id='length-icon' class='glyphicon glyphicon-ok form-control-feedback'></span>");
		//$("#length-icon").css({'line-height': '34px'});
		return true;
	}
	else {
		$("#validate-length-notice").css({'color':'#a94442'});
		$("#col-input").css({'height':'325px'});
		$("#validate-length-notice").text("Please enter a valid number.");
		parentElement.addClass("has-error has-feedback");
		//parentElement.append("<span id='length-icon' class='glyphicon glyphicon-remove form-control-feedback'></span>");
		//$("#length-new-col").css({'line-height': '75px'});
		return false;
	}
}

function getJsonDataToDL() {
	//console.log(TablesList.instances);
	if(TablesList.instances == null) {
		alert("Cannot download: No data found!");
		return false;
	}
	
	RootNode.instance.childs = TablesList.instances;
	var rawData = JSON.stringify(RootNode.instance);
	$(".rawdata-dl-area input[type=hidden]").val(rawData);
	return true;
}

function getJsonDataToGenSQL() {
	//console.log(TablesList.instances);
	if(TablesList.instances == null) {
		alert("Cannot download: No data found!");
		return false;
	}
	
	RootNode.instance.childs = TablesList.instances;
	var rawData = JSON.stringify(RootNode.instance);
	$(".rawdata-sql-area input[type=hidden]").val(rawData);
	return true;
}

function getJsonDataToSave() {
	//console.log(TablesList.instances);
	if(TablesList.instances == null) {
		alert("Cannot download: No data found!");
		return false;
	}
	
	RootNode.instance.childs = TablesList.instances;
	var rawData = JSON.stringify(RootNode.instance);
	$(".rawdata-save-area input[type=hidden]").val(rawData);
	alert("Saved!");
	return true;
}

function tranferMode(){
	var element = document.getElementById('tableMode'),
    style = window.getComputedStyle(element);
    display = style.getPropertyValue('display');
	if(display == "none"){
		$("#mode").text("Table Mode");
		$(".transfer-nav").attr('title', 'To ERD Mode');
		document.getElementById('tableMode').style.display ="block";
		document.getElementById('ERDMode').style.display ="none";
	}else{
		$("#mode").text("ERD Mode");
		$(".transfer-nav").attr('title', 'To Table Mode');
		document.getElementById('tableMode').style.display ="none";
		document.getElementById('ERDMode').style.display ="block";
	}
}