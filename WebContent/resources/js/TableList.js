
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
		//console.log(rawData);
		TablesList.instances = JSON.parse(rawData).childs;
		console.log("Convert successfully!");
		console.log(TablesList.instances);
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
/****************************************
*********      Table Object     *********
*****************************************/
//Constructor
function Table() {};

/** Static property **/
// Static instance for retrieving data
Table.instance = {};

/** Method **/

TablesList.getTableByName = function (szTableName) {
  for (var i = 0; i < TablesList.instances.length; i++) {
    var table = TablesList.instances[i];
    if (table.tableName == szTableName) {
      return table;
    }
  }
  return null;
}

TablesList.getTableById = function (tempId) {
  for (var i = 0; i < TablesList.instances.length; i++) {
    var table = TablesList.instances[i];
    if (table.tempId == tempId) {
      return table;
    }
  }
  return null;
}

Table.findColumnByName = function(szColumnName) {
	for(var i = 0; i < Table.instance.childs.length; i++) {
		if(Table.instance.childs[i].json == "column" || Table.instance.childs[i].json == "pk") {
			if(Table.instance.childs[i].columnName == szColumnName) {
				return Table.instance.childs[i];
			}
		}
	}
}

Table.deleteColumn = function(tempId) {
	for(var i = 0; i < Table.instance.childs.length; i++) {
		if(Table.instance.childs[i].json == "column" || Table.instance.childs[i].json == "pk") {
			if(Table.instance.childs[i].tempId == tempId) {
				// Remove
				Table.instance.childs.splice(i, 1);
				console.log("Remove successfully");
				// Update
				TablesList.updateTable(Table.instance);
				break;
			}
		}
	}
};

Table.deleteRela = function(tempId) {
	for(var i = 0; i < Table.instance.childs.length; i++) {
		if(Table.instance.childs[i].tempId == tempId) {
			if(Table.instance.childs[i].json == "mto") {
				// Delete the foreign key of this relationship in current table
				var fkColumn = Table.findColumnByName(Table.instance.childs[i].foreignKey.columnName);
				Table.deleteColumn(fkColumn.tempId);
				
				// Find the referred table of this foreign key
				var rfTable = TablesList.findTableByName(Table.instance.childs[i].referTable.tableName);
				
				// Delete the relationship of current table
				Table.instance.childs.splice(i, 1);
				
				for(var j = 0; j < rfTable.childs.length; j++) {
					// Find the column linked to this foreign key
					if(rfTable.childs[j].json == "otm"
						&& rfTable.childs[j].referTable.tableName == Table.instance.tableName)
					{
						// Remove and update it
						rfTable.childs.splice(j, 1);
						TablesList.updateTable(rfTable);
					}
				}
			}
			else if(Table.instance.childs[i].json == "otm") {
				var delColName = Table.instance.childs[i].foreignKey.columnName;
				// Find the referred table of this relationship
				var rfTable = TablesList.findTableByName(Table.instance.childs[i].referTable.tableName);
				
				// Delete the relationship of current table
				Table.instance.childs.splice(i, 1);
				
				for(var j = 0; j < rfTable.childs.length; j++) {
					// Find the related column
					if(rfTable.childs[j].json == "mto"
						&& rfTable.childs[j].referColumn.columnName == delColName)
					{
						console.log("Match");
						// Remove and update table
						rfTable.childs.splice(j, 1);
						TablesList.updateTable(rfTable);
					}
				}
			}
			else console.log("Element with ID: " + tempId + " has not relationship");
			
			// Everything done, update current table
			TablesList.updateTable(Table.instance);
		}
	}
	
}
Table.addColumn = function(column) {};