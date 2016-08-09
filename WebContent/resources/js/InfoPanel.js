function InfoPanel() {};

InfoPanel.displayCurrentTable = function() {
  // reset all details of table
  TableAction.resetDetails();
  
	if(Table.instance == null) {
		console.log("No table to be displayed");
	}
	else {
		// empty tables before insert new data
		InfoPanel.clearData();
		
		for(var i = 0; i < Table.instance.childs.length; i++) {
			var col = Table.instance.childs[i];
			
			// Show all columns
			if(col.json == "pk" || col.json == "column") {
				$(".table-info").append("<tr>" +
						"<td>" + col.columnName + "</td>" +
						"<td>" + col.dataType + "</td>" +
						"<td>" + "<input id='nn-" + col.tempId + "' type='checkbox' disabled='disabled'/></td>" +
						"<td>" + "<input id='pk-" + col.tempId + "' type='checkbox' disabled='disabled'/></td>" +
						"<td>" + "<input id='ai-" + col.tempId + "' type='checkbox' disabled='disabled'/></td>" +
						"<td>" +
							"<input type='hidden' value='" + col.tempId + "'/>" + 
							"<span class='rmv-col glyphicon glyphicon-trash'></span></td>" +
						"</tr>"
						);
				
				// Checked a checkbox if attribute of col is true
				if(col.notNull == true) $("#nn-" + col.tempId).prop('checked', true);
				else $("#nn-" + col.tempId).prop('checked', false);
				if(col.primaryKey == true) $("#pk-" + col.tempId).prop('checked', true);
				else $("#fk-" + col.tempId).prop('checked', false);
				if(col.autoIncrement == true) $("#ai-" + i).prop('checked', true);
				else $("#ai-" + col.tempId).prop('checked', false);
			}
			// Show all relations
			else if (col.json == "mto" || col.json == "otm"){
				var fkColName = (col.foreignKey != null) ? col.foreignKey.columnName : "";
				var rfTableName = (col.referTable != null) ? col.referTable.tableName : "";
				var rfColName = (col.referColumn != null) ? col.referColumn.columnName : "";
	
				$(".relationship-info").append("<tr class='rela-info'>" +
						"<td>" + col.type + "</td>" +
						"<td>" + fkColName + "</td>" +
						"<td>" + rfTableName + "</td>" +
						"<td>" + rfColName + "</td>" +
						"<td>" +
							"<input type='hidden' value='" + col.tempId + "'/>" + 
							"<span class='rmv-rela glyphicon glyphicon-trash'></span></td>" +
						"</tr>"
						);
			}
		};
	}
};

InfoPanel.deleteColumn = function(table, szColumnId, relatedList) {
	Table.deleteColumn(table, szColumnId, relatedList);
};

InfoPanel.deleteRela = function(table, szColumnId, relatedList) {
	Table.deleteRela(table, szColumnId, relatedList);
}

InfoPanel.showColDetail = function(szColName) {
	for(var i = 0; i < Table.instance.childs.length; i++) {
		var col = Table.instance.childs[i];
		if(szColName == col.columnName) {
		  $("#btn-save").text("Update");
		  
		  $("#col-tempid-detail").val(col.tempId);
			$("#col-name-detail").val(col.columnName);
			$("#col-type-detail").val(col.dataType);
			$("#col-length-detail").val(col.length);
				
			if(col.notNull == true) $("#col-nn-detail").prop('checked', true);
			else $("#col-nn-detail").prop('checked', false);
				
			if(col.primaryKey == true) $("#col-pk-detail").prop('checked', true);
			else $("#col-pk-detail").prop('checked', false);
				
			if(col.foreignKey == true) $("#col-fk-detail").prop('checked', true);
			else $("#col-fk-detail").prop('checked', false);
				
			if(col.autoIncrement == null) {
				$("#col-ai-detail").prop('checked', false);
				$("#col-ai-detail").prop("disabled", true);
			}
			else {
				$("#col-ai-detail").prop("disabled", false);
				if(col.autoIncrement == true) $("#col-ai-detail").prop('checked', true);
				else $("#col-ai-detail").prop('checked', false);
			}
				
			break;
		}
	}
};

InfoPanel.addColumn = function(table, simpleCol) {
	var result = Table.addColumn(table, simpleCol);
	if(result == true) $('#addColModal').modal("hide");
}

InfoPanel.showRelaDetail = function(szColName) {
	for(var i = 0; i < Table.instance.childs.length; i++) {
		var element = Table.instance.childs[i];
		var fkColName = (element.foreignKey != null) ? element.foreignKey.columnName : "";
		var rfTableName = (element.referTable != null) ? element.referTable.tableName : "";
		var rfColName = (element.referColumn != null) ? element.referColumn.columnName : "";
			
		if(szColName == fkColName) {
		  $("#btn-save").text("Update");

		  $("#rela-tempid-detail").val(element.tempId);
		  $("#rela-tableid-detail").val(Table.instance.tempId);
			$("#rela-type-detail").val(element.type);
			$("#rela-col-detail").val(fkColName);
			
			var refTblEl = document.getElementById('rela-rftable-detail');
			for (var i = 0; i < FakeTable.list.length; i++) {
				var tbl = FakeTable.list[i];
				refTblEl.options[i] = new Option(tbl.tblName, tbl.tblName);
	      
				if (tbl.tblName == rfTableName) {
					refTblEl.selectedIndex = i;
					document.getElementById('rela-rfcol-detail').options[0] = new Option(tbl.pkName, tbl.pkName);
				}
			}
	    
			break;
		}
	}
}

InfoPanel.clearData = function() {
	$(".table-info").html("");
	$(".relationship-info").html("");
}