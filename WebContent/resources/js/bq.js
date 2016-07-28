$(document).ready(function() {
	var rawData = $(".raw-data").val();
	//console.log(rawData);
	if(rawData != "")
		var g_data = JSON.parse(rawData);
	
	var crtShowTable = new Object();
	$(".nav>li ul").hide();
	
	//Prepare value for dropdown in relation tab
	//Pure js (simple than jquery :D)
	if(g_data != null) {
		var selTbElement = document.getElementById("rela-rftable-detail");
		for(var i = 0; i < g_data.childs.length; i++) {
			//console.log("TableName: " + g_data.childs[i].tableName + " Table ID: " + g_data.childs[i].tempId);
			selTbElement.options[selTbElement.options.length]= new Option(g_data.childs[i].tableName, g_data.childs[i].tempId);
		} 
	}

	// Handle click event of label-tag (tree-root)
	$('.tree-toggle').click(function() {
		
		// refresh details view
		$(".col-name-detail").val("");
		$(".col-length-detail").val("");
		$(".col-nn-detail").val("");
		$(".col-pk-detail").val("");
		$(".col-fk-detail").val("");
		$(".col-ai-detail").val("");
		
		// refresh tree and table
		$(".nav>li ul").hide();
		$(".table-info").empty();
		renewColDetail();
		$(".relation-info").empty();
		renewRelaDetail();
		
		
		
		var childComp = $(this).parent().children('ul');
		
		// Toggles tree
		childComp.slideToggle(200);
		
		var tablesList = g_data.childs;
		
		for(var j = 0; j < tablesList.length; j++) {
			var labelText = $(this).text();
			
			if(tablesList[j].tableName == labelText) {
				crtShowTable = tablesList[j];
				
				// disable current table option in relation tab
				$("#rela-rftable-detail option").each(function(){
					if($(this).val() == crtShowTable.tempId) {
						$(this).attr("disabled","disabled");
					}
					else {
						$(this).removeAttr("disabled");
					}
				});
				
				// view at table tab
				var colsList = tablesList[j].childs;
				for(var i = 0; i < colsList.length; i++) {
					
					var col = colsList[i];
					console.log(col);
					if(col.referTable == null && col.referColumn == null) {
						$(".table-info").append("<tr>" +
								"<td>" + col.columnName + "</td>" +
								"<td>" + col.dataType + "</td>" +
								"<td>" + "<input id='nn-" + i + "' type='checkbox' disabled='disabled'/></td>" +
								"<td>" + "<input id='pk-" + i + "' type='checkbox' disabled='disabled'/></td>" +
								"<td>" + "<input id='fk-" + i + "' type='checkbox' disabled='disabled'/></td>" +
								"<td>" + "<input id='ai-" + i + "' type='checkbox' disabled='disabled'/></td>" +
								"<td><span class='glyphicon glyphicon-remove-sign'></span></td>" +
								"</tr>"
								);
						
						// Checked a checkbox if attribute of col is true
						if(col.notNull == true) $("#nn-" + i).prop('checked', true);
						else $("#nn-" + i).prop('checked', false);
						if(col.primaryKey == true) $("#pk-" + i).prop('checked', true);
						else $("#pk-" + i).prop('checked', false);
						if(col.foreignKey == true) $("#fk-" + i).prop('checked', true);
						else $("#fk-" + i).prop('checked', false);
						if(col.autoIncrement == true) $("#ai-" + i).prop('checked', true);
						else $("#ai-" + i).prop('checked', false);
					}
					else {
						var fkColName = (g_data.childs[j].childs[i].foreignKey != null) ? g_data.childs[j].childs[i].foreignKey.columnName : "";
						var rfTableName = (g_data.childs[j].childs[i].referTable != null) ? g_data.childs[j].childs[i].referTable.tableName : "";
						var rfColName = (g_data.childs[j].childs[i].referColumn != null) ? g_data.childs[j].childs[i].referColumn.columnName : "";

						$(".relation-info").append("<tr class='rela-info'>" +
								"<td>" + crtShowTable.tableName + "</td>" +
								"<td>" + fkColName + "</td>" +
								"<td>" + col.type + "</td>" +
								"<td>" + rfTableName + "</td>" +
								"<td>" + rfColName + "</td>" +
								"</tr>"
								);
					}
				};	
			}
		}
		
	});
	
	$("#rela-rftable-detail").change(function(){
		var optionVal = $("#rela-rftable-detail	 option:selected").val();
		showColOptionByTableName(optionVal, g_data);
	});
	
	$(".relation-info").on("click", "tr", function(){
		var colName = $(this).children(":first").next().text();
		showRelaDetail(crtShowTable, colName, crtShowTable.childs);
	});
	
	$(".table-info").on("click", "tr", function(){
		var colName = $(this).children(":first").text();
		showColDetail(colName, crtShowTable.childs);
	});
	
	$(".tree-node").click(function(){
		// Get name of chosen column
		var crtColName = $(this).text();
		//console.log("Clicked Node: " + crtColName);
		
		// Check holding-data variable
		//console.log("Table Info: " + g_tableInfo);
		
		// Set info
		showColDetail(crtColName, crtShowTable.childs);
	});
	
	$(".save-col").click(function(){
		var colTempId = $(".col-id-pane input[type=hidden]").val();
		
		// Visible properties
		var colName = $(".col-name-detail").val();
		var colType = $(".col-type-detail").val();
		var colLength = $(".col-length-detail").val();
		if ($('.col-nn-detail').is(":checked")) colNN = true;
		else colNN = false;
		if ($('.col-pk-detail').is(":checked")) colPK = true;
		else colPK = false;
		if ($('.col-fk-detail').is(":checked")) colFK = true;
		else colFK = false;
		if ($('.col-ai-detail').is(":checked")) colAI = true;
		else colAI = false;
		
		//console.log("Temp ID: " + colTempId + "Name: " + colName + "Type: " + colType + "Length: " + colLength + "NN: " + colNN + "PK: " + colPK + "FK: " + colFK +  "AI: " + colAI);
		
		var colsList = crtShowTable.childs;
		for(var i = 0; i < colsList.length; i++) {
			if(colsList[i].tempId == colTempId) {
				//console.log("Found Column With ID: " + colsList[i].tempId + " in ColList");
				colsList[i].columnName = colName;
				colsList[i].dataType = colType;
				colsList[i].length = colLength;
				colsList[i].length = colLength;
				colsList[i].length = colLength;
				colsList[i].length = colLength;
				
				break;
			}
			else continue;
		}
		
		crtShowTable.childs = colsList;
		//console.log("Column List after change: " + crtShowTable.childs);
		for(var j = 0; j < g_data.childs.length; j++) {
			if(crtShowTable.tempId = g_data.childs[j].tempId) {
				console.log("Found Table With ID: " + g_data.childs[j].tempId + " in Tables List");
				g_data.childs[j] = crtShowTable;
				
				break;
			}
			else continue;
		}
		
		rawData = JSON.stringify(crtShowTable);
		$(".json-pane input[type=hidden]").val(rawData);
	});

	$('#xml-trigger').click(function() {
		$('#xmlModal').modal('show');
		return false;
	});
	
	$('#upload-trigger').click(function() {
		$('#uploadModal').modal("show")
	});
	
	$('#zip-trigger').click(function() {
		$('#zipModal').modal('show');
		return false;
	});
	
	$('#reset-data-trigger').click(function() {
		$('#noticeModal').modal('show');
		return false;
	});

	$(".dropdown").hover(function() {
		$('.dropdown-menu', this).stop(true, true).fadeIn("fast");
	}, function() {
		$('.dropdown-menu', this).stop(true, true).fadeOut("fast");
	});
});

function showColDetail(elementName, colArray) {
	for(var i = 0; i < colArray.length; i++) {
		var col = colArray[i];
		if(col.referTable == null) {
			if(elementName == col.columnName) {
				
				// Hidden properties
				$(".col-id-pane input[type=hidden]").val(col.tempId);
		
				// Visible properties
				$(".col-name-detail").val(col.columnName);
				$(".col-type-detail").val(col.dataType);
				$(".col-length-detail").val(col.length);
				
				if(col.notNull == true) $(".col-nn-detail").prop('checked', true);
				else $(".col-nn-detail").prop('checked', false);
				
				if(col.primaryKey == true) $(".col-pk-detail").prop('checked', true);
				else $(".col-pk-detail").prop('checked', false);
				
				if(col.foreignKey == true) $(".col-fk-detail").prop('checked', true);
				else $(".col-fk-detail").prop('checked', false);
				
				if(col.autoIncrement == null) {
					$(".col-ai-detail").prop('checked', false);
					$(".col-ai-detail").prop("disabled", true);
				}
				else {
					$(".col-ai-detail").prop("disabled", false);
					if(col.autoIncrement == true) $(".col-ai-detail").prop('checked', true);
					else $(".col-ai-detail").prop('checked', false);
				}
				
				break;
			}
		}
		else continue;
	}
};

function renewColDetail() {
	// Hidden properties
	$(".col-id-pane input[type=hidden]").val("");
	
	// Visible properties
	$(".col-name-detail").val("");
	$(".col-type-detail").val("");
	$(".col-length-detail").val("");
	$(".col-nn-detail").prop('checked', false);
	$(".col-pk-detail").prop('checked', false);
	$(".col-fk-detail").prop('checked', false);
	$(".col-ai-detail").prop('checked', false);
}

function showRelaDetail(crtTable, elementName, relaArray) {
	for(var i = 0; i < relaArray.length; i++) {
		var rela = relaArray[i];
		if(rela.referTable != null) {
			var fkCol = rela.foreignKey;
			var rfTable = rela.referTable;
			var rfCol = rela.referColumn;
			
			if(elementName == fkCol.columnName) {
				$(".rela-table-detail").val(crtTable.tableName);
				$(".rela-col-detail").val(fkCol.columnName);
				$("#rela-rftable-detail option[value=" + rfTable.tempId + "]").attr("selected","selected");
				showColOptionByTableName(tfTable.tempId, g_data);
				$("#rela-rfcol-detail option[value=" + rfCol.tempId + "]").attr("selected","selected");
				
				break;
			}
		}
		else continue;
	}
}

function showColOptionByTableName(tableTempId, jsonData) {
	for(var i = 0; i < jsonData.childs.length; i++) {
		var table = g_data.childs[i];
		if(table.tempId != tableTempId)
			continue;
		else {
			var selColElement = document.getElementById("rela-rfcol-detail");
			selColElement.options.length = 0;
			
			for(var j = 0; j < table.childs.length; j++) {
				if(table.childs[j].referTable == null)
					selColElement.options[selColElement.options.length]= new Option(table.childs[j].columnName, table.childs[j].tempId);
				else continue;
			} 
		}
	}
}
function renewRelaDetail() {
	$(".rela-table-detail").val("");
	$(".rela-col-detail").val("");
	$(".rela-rftable-detail").val("");
	$(".rela-rfcol-detail").val("");
}