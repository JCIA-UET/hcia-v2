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

Table.deleteColumn = function(table, colId, relatedList) {
	var colIdList = [];
	for(var i = 0; i < relatedList.length; i++) {
		colIdList.push(TablesList.instances[parseInt(relatedList[i][0])].childs[parseInt(relatedList[i][1])].tempId);
	}
	
	for(var i = 0; i < colIdList.length; i++) {
		TablesList.deleteColumnById(colIdList[i].toString());
	}
	
	for(var i = 0; i < table.childs.length; i++) {
		if(table.childs[i].tempId == colId) {
			TreeView.hideElementById(table, colId);
			table.childs.splice(i, 1);
			TablesList.updateTable(table);
			break;
		}
	}
	InfoPanel.displayCurrentTable();
};

Table.deleteRela = function(table, colId, relatedList) {
	var colIdList = [];
	for(var i = 0; i < relatedList.length; i++) {
		colIdList.push(TablesList.instances[parseInt(relatedList[i][0])].childs[parseInt(relatedList[i][1])].tempId);
	}
	
	for(var i = 0; i < colIdList.length; i++) {
		TablesList.deleteColumnById(colIdList[i].toString());
	}
	
	for(var i = 0; i < table.childs.length; i++) {
		if(table.childs[i].tempId == colId) {
			table.childs.splice(i, 1);
			TablesList.updateTable(table);
			break;
		}
	}
	
	InfoPanel.displayCurrentTable();
}
Table.addColumn = function(column) {};