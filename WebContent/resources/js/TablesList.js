/****************************************
*******      Root Node Object     *******
*****************************************/
// Constructor
function RootNode() {};

/** Static property **/
//Static instance for retrieving data
RootNode.instance = {};


/****************************************
******      Tables List Object     ******
*****************************************/
// Constructor
function TablesList() {};

/** Static property **/
// Static instance for retrieving data
TablesList.instances = {};

/** Method **/
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

TablesList.findColumnById = function(table, tempId) {
	for(var i = 0; i < table.childs.length; i++) {
		if(table.childs[i].json == "column" || table.childs[i].json == "pk") {
			if(table.childs[i].tempId == tempId) {
				return table.childs[i];
			}
		}
	}
	return null;
}

TablesList.deleteColumnById = function(colId) {
	for(var i = 0; i < TablesList.instances.length; i++) {
		for(var j = 0; j < TablesList.instances[i].childs.length; j++) {
			if(TablesList.instances[i].childs[j].tempId == colId) {
				TreeView.hideElementById(TablesList.instances[i], TablesList.instances[i].childs[j].tempId);	
				TablesList.instances[i].childs.splice(j, 1);
			}
		}
	}
}

TablesList.findMTORelaPosRelatedToPK = function(pk) {
	var affectedList = [];
	for(var i = 0; i < TablesList.instances.length; i++) {
		for(var j = 0; j < TablesList.instances[i].childs.length; j++) {
			var tempTable = TablesList.instances[i];
			if(tempTable.childs[j].json == "mto") {
				if(tempTable.childs[j].referColumn.columnName == pk.columnName) {
					var index = [];
					index.push(i); index.push(j);
					affectedList.push(index);
				}
			}
		}
	}
	return affectedList;
}

TablesList.findOppositeRelationPos = function(table, relaIdx) {
	var rfTable = {};
	var posCouple = [];
	var tableIdx, colIdx;
	
	// Finf refer table
	for(var i = 0; i < TablesList.instances.length; i++) {
		if(table.childs[parseInt(relaIdx)].referTable.tableName == TablesList.instances[i].tableName) {
			rfTable = TablesList.instances[i];
			tableIdx = i;
			break;
		}
	}
	
	if(table.childs[parseInt(relaIdx)].json == "mto") {
		for(var i = 0; i < rfTable.childs.length; i++) {
			if(rfTable.childs[i].json == "otm" 
				&& rfTable.childs[i].referTable.tableName == table.tableName)
			{
				colIdx = i;
				break;
			}
		}
		
		posCouple.push(tableIdx); posCouple.push(colIdx);
		return posCouple;
	}
	else if(table.childs[parseInt(relaIdx)].json == "otm") {
		for(var i = 0; i < rfTable.childs.length; i++) {
			if(rfTable.childs[i].json == "mto" 
				&& rfTable.childs[i].referTable.tableName == table.tableName)
			{
				colIdx = i;
				break;
			}
		}
		
		posCouple.push(tableIdx); posCouple.push(colIdx);
		return posCouple;
	}
	return null;
}

TablesList.findRelatedElements = function(table, tempId) {
	var relatedList = [];
	var colPos;
	var tableIdx, colIdx;
	
	// Find current table's index
	for(var i = 0; i < TablesList.instances.length; i++) {
		if(TablesList.instances[i].tableName == table.tableName) {
			tableIdx = i;
			break;
		}
	}
	
	// Find current column index
	for(var i = 0; i < table.childs.length; i++) {
		if(table.childs[i].tempId == tempId) {
			colPos = i;
			break;
		}
	}
	
	if(table.childs[parseInt(colPos)].json == "mto") {
		/** Find position of foreign key of mto relation in current table **/
		// Find foreign key's index
		for(var i = 0; i < table.childs.length; i++) {
			if(table.childs[i].json == "column" 
				&& table.childs[i].foreignKey == true
				&& table.childs[i].columnName == table.childs[parseInt(colPos)].foreignKey.columnName)
			{
				colIdx = i;
				break;
			}	
		}
		
		// Push position to array 
		var index = [];
		index.push(tableIdx); index.push(colIdx);
		relatedList.push(index);

		/** Find the position of otm in the refer table **/
		relatedList.push(TablesList.findOppositeRelationPos(table, colPos));
	}
	else if(table.childs[parseInt(colPos)].json == "column" 
		&& table.childs[parseInt(colPos)].foreignKey == true)
	{
		/** Find position of mto relation of foreign key in current table **/
		// Find mto relation's index
		for(var i = 0; i < table.childs.length; i++) {
			if(table.childs[i].json == "mto" 
				&& table.childs[i].foreignKey.columnName == table.childs[parseInt(colPos)].columnName)
			{
				colIdx = i;
				break;
			}
		}
		
		// Push position to array 
		var index = [];
		index.push(tableIdx); index.push(colIdx);
		relatedList.push(index);
		
		/** Find position of otm relation in refer table **/
		relatedList.push(TablesList.findOppositeRelationPos(table, colIdx));
	}
	else if(table.childs[parseInt(colPos)].json == "otm") {
		// Find mto column index
		relatedList.push(TablesList.findOppositeRelationPos(table, colPos));
		
		// Find foreign key of mto column
		tableIdx = parseInt(relatedList[0][0]);
		var rfTable = TablesList.instances[parseInt(relatedList[0][0])];
		var mtoColumn = rfTable.childs[parseInt(relatedList[0][1])];
		
		for(var i = 0; i < rfTable.childs.length; i++) {
			if(rfTable.childs[i].columnName == mtoColumn.foreignKey.columnName) {	
				colIdx = i;
				break;
			}
		}
		
		// Push position to array 
		var index = [];
		index.push(tableIdx); index.push(colIdx);
		relatedList.push(index);
	}
	else if(table.childs[parseInt(colPos)].json == "pk") {
		// Find all mto relations
		var mtoList = TablesList.findMTORelaPosRelatedToPK(table.childs[parseInt(colPos)]);
		
		/** With each mto relation, find all related elements and add them into array **/
		for(var i = 0; i < mtoList.length; i++) {
			// Add current mto into array
			relatedList.push(mtoList[i]);
			
			// Find related element
			var tempTable = TablesList.instances[parseInt(mtoList[i][0])];
			var list = TablesList.findRelatedElements(tempTable, tempTable.childs[parseInt(mtoList[i][1])].tempId);
			
			for(var j = 0; j < list.length; j++) {
				relatedList.push(list[j]);
			}
		}
	}
	return relatedList;
}