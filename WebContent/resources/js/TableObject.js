/****************************************
*********      Table Object     *********
*****************************************/
//Constructor
function Table() {};

/** Static property **/
// Static instance for retrieving data
Table.instance = null;

/** Method **/
Table.findColumnByName = function(table, szColumnName) {
	for(var i = 0; i < table.childs.length; i++) {
		if(table.childs[i].json == "column" || table.childs[i].json == "pk") {
			if(table.childs[i].columnName.toLowerCase() == szColumnName.toLowerCase()) {
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
			table.childs.splice(i, 1);
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
			break;
		}
	}
	
	InfoPanel.displayCurrentTable();
}

Table.addColumn = function(table, simpleCol) {
	var col = new Object();
	var generatedColTempId = TablesList.findMaximumTempId() + 1;
	
	col.tempId = generatedColTempId;
	col.childs = null;
	
	col.javaName = simpleCol.columnName;
	col.columnName = simpleCol.columnName;
	col.dataType = simpleCol.dataType;
	col.length = simpleCol.length == null ? 0 : simpleCol.length;
	col.notNull = simpleCol.notNull;
	
	col.unique = false;
	// What about this????
	col.hbmAttributes = {};
	
	if (simpleCol.type == "nm" || simpleCol.type == "fk") {
		col.json = "column";
		col.primaryKey = false;
		
		if(simpleCol.type == "nm") col.foreignKey = false;	
	}
	else if (simpleCol.type == "pk") {
		col.json = "pk";
		col.primaryKey = true;
		col.autoIncrement = simpleCol.autoIncrement;
	}
	
	// Add new column to database
	table.childs.push(col);
	
	if (simpleCol.type == "fk") {
		var rfTableName = simpleCol.rfTableName;
		var rfColName = simpleCol.rfColName;
		
		var rfTable = TablesList.findTableByName(rfTableName);
		var rfColumn = Table.findColumnByName(rfTable, rfColName);
		
		/** First, create new mto **/
		// Create refer table for MTO 
		var mtoRfTable = deepCopy(rfTable);
		mtoRfTable.childs = null;
		mtoRfTable.hbmAttributes = null;
		mtoRfTable.javaName = null;
		mtoRfTable.catalog = null;
		mtoRfTable.xmlPath = null;
		
		// Create MTO Relation
		var generatedMTOTempId = TablesList.findMaximumTempId() + 1;
		var mtoObj = {
			tempId: 		generatedMTOTempId,
			json: 			"mto",
			childs: 		null,
			// What can I do with this????
			hbmAttributes: 	{},
			javaName: 		rfColName,
			referTable: 	mtoRfTable,
			type: 			"Many-to-One",
			foreignKey: 	col,
			referColumn: 	rfColumn
		}
		
		/** Second, create new otm **/
		// Create refer Table for OTM
		var otmRfTable = deepCopy(table);
		otmRfTable.childs = null;
		otmRfTable.hbmAttributes = null;
		otmRfTable.javaName = null;
		otmRfTable.catalog = null;
		otmRfTable.xmlPath = null;
		
		// Create clone of primary key in OTM
		var otmRfColumn = deepCopy(col);
		otmRfColumn.hbmAttributes = {};
		otmRfColumn.tempId = 0;
		otmRfColumn.javaName = null;
		otmRfColumn.dataType = null;
		otmRfColumn.primaryKey = false;
		otmRfColumn.autoIncrement = undefined;
		
		// Create OTM Relation
		var generatedOTMTempId = TablesList.findMaximumTempId() + 1;
		var otmObj = {
			json: 			"otm",
			childs: 		null,
			hbmAttributes: 	{},
			tempId: 		generatedOTMTempId,
			javaName: 		"",
			referTable: 	otmRfTable,
			type: 			"One-to-Many",
			foreignKey:		otmRfColumn
		}
		
		/** Add mto and otm to database **/
		table.childs.push(mtoObj);
		rfTable.childs.push(otmObj);
	}
	return true;
};

function deepCopy(oldObj) {
    var newObj = oldObj;
    if (oldObj && typeof oldObj === 'object') {
        newObj = Object.prototype.toString.call(oldObj) === "[object Array]" ? [] : {};
        for (var i in oldObj) {
            newObj[i] = deepCopy(oldObj[i]);
        }
    }
    return newObj;
}