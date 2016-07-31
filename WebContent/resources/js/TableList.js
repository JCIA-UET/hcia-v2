
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
			if(TablesList.instances[i].tableName == szTableName) {
				return TablesList.instances[i];
			}
			else continue;
		}
		return null;
	}
}

TablesList.updateTable = function(table) {
	for(var i = 0; i < TablesList.instances.length; i++) {
		if(TablesList.instances[i].tempId != table.tempId) {
			if(i == (TablesList.instances.length - 1))
				console.log("Can't update Table with ID: " + table.tempId);
			else
				continue;
		}
		else {
			TablesList.instances[i] = table;
		}
	}
};

TablesList.deleteTable = function(table) {
	for(var i = 0; i < TablesList.instances.length; i++) {
		if(TablesList.instances[i].tempId != table.tempId) {
			if(i == (TablesList.instances.length - 1))
				console.log("Can't delete Table with ID: " + table.tempId);
			else
				continue;
		}
		else {
			TablesList.instances.splice(i, 1);
			console.log("Delete successfully");
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

Table.deleteColumn = function(columnId) {
	for(var i = 0; i < Table.instance.childs.length; i++) {
		if(Table.instance.childs[i].tempId != columnId) {
			if(i == (Table.instance.childs.length - 1))
				console.log("Can't remove column with ID: " + columnId);
			else
				continue;
		}
		else {
			Table.instance.childs.splice(i, 1);
			console.log("Remove successfully");
		}
	}
};

Table.addColumn = function(column) {};