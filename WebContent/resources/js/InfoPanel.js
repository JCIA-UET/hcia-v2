function InfoPanel() {};

InfoPanel.displayCurrentTable = function() {
	if(Table.instance == null) {
		console.log("No table to be displayed");
	}
	else {
		for(var i = 0; i < Table.instance.childs.length; i++) {
			var col = Table.instance.childs[i];
			
			if(col.json == "pk" || col.json == "column") {
				$(".table-info").append("<tr>" +
						"<td>" + col.columnName + "</td>" +
						"<td>" + col.dataType + "</td>" +
						"<td>" + "<input id='nn-" + col.tempId + "' type='checkbox' disabled='disabled'/></td>" +
						"<td>" + "<input id='pk-" + col.tempId + "' type='checkbox' disabled='disabled'/></td>" +
						"<td>" + "<input id='fk-" + col.tempId + "' type='checkbox' disabled='disabled'/></td>" +
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
				else $("#pk-" + col.tempId).prop('checked', false);
				if(col.foreignKey == true) $("#fk-" + col.tempId).prop('checked', true);
				else $("#fk-" + col.tempId).prop('checked', false);
				if(col.autoIncrement == true) $("#ai-" + i).prop('checked', true);
				else $("#ai-" + col.tempId).prop('checked', false);
				
				console.log("Table Added: " + col.columnName);
			}
			else {
				var fkColName = (col.foreignKey != null) ? col.foreignKey.columnName : "";
				var rfTableName = (col.referTable != null) ? col.referTable.tableName : "";
				var rfColName = (col.referColumn != null) ? col.referColumn.columnName : "";
	
				$(".relationship-info").append("<tr class='rela-info'>" +
						"<td>" + col.type + "</td>" +
						"<td>" + fkColName + "</td>" +
						"<td>" + rfTableName + "</td>" +
						"<td>" + rfColName + "</td>" +
						"</tr>"
						);
				
				console.log("Relation Added: " + col.columnName);
			}
		};	
	}
};

InfoPanel.deleteColumn = function() {
	for(var i = 0; i < Table.instance.childs.length; i++) {
		
	}
};

InfoPanel.showColDetail = function(szColName) {
	for(var i = 0; i < Table.instance.childs.length; i++) {
		var col = Table.instance.childs[i];
		if(szColName == col.columnName) {
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

InfoPanel.showRelaDetail = function(szColName) {
	for(var i = 0; i < Table.instance.childs.length; i++) {
		var element = Table.instance.childs[i];
		var fkColName = (element.foreignKey != null) ? element.foreignKey.columnName : "";
		var rfTableName = (element.referTable != null) ? element.referTable.tableName : "";
		var rfColName = (element.referColumn != null) ? element.referColumn.columnName : "";
			
		if(szColName == fkColName) {
			$("#rela-table-detail").val(crtTable.tableName);
			$("#rela-col-detail").val(fkCol.columnName);
			$("#rela-rftable-detail option[value=" + rfTable.tempId + "]").attr("selected","selected");
			$("#rela-rfcol-detail option[value=" + rfCol.tempId + "]").attr("selected","selected");
				
			break;
		}
	}
}