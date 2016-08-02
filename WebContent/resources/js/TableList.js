
function RootNode() {};
RootNode.instance = {};

// Constructor
function TablesList() {};

/** Static property **/
// Static instance for retrieving data
TablesList.instances = {};

/** Method **/
// Convert raw-type of TreeNode from backing bean to JS Object
TablesList.convertStringToObject = function(rawData) {
	if(rawData == null || rawData == "") {
		console.log("No data to display");
	}
	else {
		console.log(rawData);
		RootNode.instance = JSON.parse(rawData);
		TablesList.instances = RootNode.instance.childs;
		console.log("Convert successfully!");
		
	}
};

TablesList.findTableByName = function(szTableName) {
	if(szTableName == null || szTableName == "") {
		console.log("Can't find table with name: " + szTableName);
	}
	else {
		for(var i = 0; i < TablesList.instances.length; i++) {
			if(TablesList.instances[i].tableName == szTableName)
				return TablesList.instances[i];
		}
		return null;
	}
};

TablesList.getTableById = function (tempId) {
	for (var i = 0; i < TablesList.instances.length; i++) {
	    if (TablesList.instances[i].tempId == tempId) {
	    	return table;
	    }
	}
	return null;
};

TablesList.updateTable = function(table) {
	for(var i = 0; i < TablesList.instances.length; i++) {
		if(TablesList.instances[i].tempId == table.tempId) {
			TablesList.instances[i] = table;
			break;
		}
	}
};

TablesList.deleteTable = function(table) {
	for(var i = 0; i < TablesList.instances.length; i++) {
		if(TablesList.instances[i].tempId == table.tempId) {
			TablesList.instances.splice(i, 1);
			console.log("Delete successfully");
			break;
		}
	}
}

TablesList.findRelaPosRelatedToPK = function(pk) {
	var affectedList = [];
	for(var i = 0; i < TablesList.instances.length; i++) {
		for(var j = 0; j < TablesList.instances[i].childs.length; j++) {
			var tempTable = TablesList.instances[i];
			if(tempTable.childs[j].json == "mto") {
				if(tempTable.childs[j].referColumn.columnName == pk.columnName) {
					var index = [];
					index.push(i); index.push(j);
					console.log("Found column with tableIdx: " + i + ", colIdx: " + j);
					affectedList.push(index);
				}
			}
		}
	}
	return affectedList;
}
/****************************************
*********      Table Object     *********
*****************************************/
//Constructor
function Table() {};

/** Static property **/
// Static instance for retrieving data
Table.instance = {};

/** Method **/
Table.findColumnByName = function(table, szColumnName) {
	for(var i = 0; i < table.childs.length; i++) {
		if(table.childs[i].json == "column" || table.childs[i].json == "pk") {
			if(table.childs[i].columnName == szColumnName) {
				return table.childs[i];
			}
		}
	}
	return null;
}

Table.findColumnById = function(table, tempId) {
	for(var i = 0; i < table.childs.length; i++) {
		if(table.childs[i].json == "column" || table.childs[i].json == "pk") {
			if(table.childs[i].tempId == tempId) {
				return table.childs[i];
			}
		}
	}
}


Table.deleteColumn = function(table, colId) {
	for(var i = 0; i < table.childs.length; i++) {
		if(table.childs[i].tempId == colId) {
			var matchColumn = table.childs[i];
			if(matchColumn.json == "column") {
				if(matchColumn.foreignKey == "false") {
					// Remove
					table.childs.splice(i, 1);
					console.log("Remove successfully");
					// Update
					TablesList.updateTable(table);
				}
				else {
					for(var j = 0; j < table.childs.length; j++) {
						if(table.childs[j].json == "mto" 
							&& table.childs[j].foreignKey.columnName == matchColumn.columnName) 
						{
							// Delete foreign key
							table.childs.splice(i, 1);
							TablesList.updateTable(table);
							
							// Remove relation of this foreign key
							Table.deleteRela(table, table.childs[j].tempId);
						}
					}
				}
			}
			else if(matchColumn.json == "pk") {
				// Find all column
				var list = TablesList.findRelaPosRelatedToPK(matchColumn);
				
				for(var j = 0; j < list.length; j++) {
					console.log("Element: " + list[j]);
				}
				// Delete relation and the foreign key
				if(list.length != 0) {
					for(var j = 0; j < list.length; j++) {
						var tempTable = TablesList.instances[parseInt(list[j][0])];
						var colId = tempTable.childs[parseInt(list[j][1])].tempId;
						Table.deleteRela(tempTable, colId);
						console.log("Deleted relation at " + list[j][0] + ", " + list[j][1]);
					}
				}
				
				table.childs.splice(i, 1);
				TablesList.updateTable(table);
			}
		}
	}
	InfoPanel.displayCurrentTable();
};

Table.deleteRela = function(table, colId) {
	for(var i = 0; i < table.childs.length; i++) {
		if(table.childs[i].tempId == colId) {
			if(table.childs[i].json == "mto") {
				// Delete the foreign key of this relationship in current table
				var fkColumn = Table.findColumnByName(table, table.childs[i].foreignKey.columnName);
				if(fkColumn != null) {
					TreeView.hideElementById(table, fkColumn.tempId);
					Table.deleteColumn(table, fkColumn.tempId);
					console.log("Deleted: " + fkColumn.tempId);
				}
				
				// Find the referred table of this foreign key
				var rfTable = TablesList.findTableByName(table.childs[i].referTable.tableName);
				
				// Delete the relationship of current table
				table.childs.splice(i, 1);
				console.log("Deleted: mto");
				TablesList.updateTable(table);
				
				for(var j = 0; j < rfTable.childs.length; j++) {
					// Find mto relation of this foreign key
					if(rfTable.childs[j].json == "otm"
						&& rfTable.childs[j].referTable.tableName == table.tableName)
					{
						// Remove and update it
						rfTable.childs.splice(j, 1);
						console.log("Deleted: otm");
						TablesList.updateTable(rfTable);
					}
				}
			}
			else if(table.childs[i].json == "otm") {
				var delColName = table.childs[i].foreignKey.columnName;
				// Find the referred table of this relationship
				var rfTable = TablesList.findTableByName(table.childs[i].referTable.tableName);
				
				for(var j = 0; j < rfTable.childs.length; j++) {
					// Find the related column
					if(rfTable.childs[j].json == "mto"
						&& rfTable.childs[j].referColumn.columnName == delColName)
					{
						var fkCol = rfTable.childs[j].foreignKey;
						// Remove and update table
						rfTable.childs.splice(j, 1);
						console.log("Deleted: mto");
						TablesList.updateTable(rfTable);
						console.log("Table: " + rfTable.tableName);
						console.log("Column: " + fkCol.columnName);
						// Remove this column from tree
						TreeView.hideElementById(rfTable, fkCol.tempId);
						
						//Delete the foreign key
						Table.deleteColumn(rfTable, fkCol.tempId);
					}
				}
				
				// Delete the relationship of current table
				table.childs.splice(i, 1);
				TablesList.updateTable(table);
				console.log("Deleted: otm");
				
			}
			else console.log("Element with ID: " + tempId + " has not relationship");
		}
	}
	InfoPanel.displayCurrentTable();
}
Table.addColumn = function(column) {};