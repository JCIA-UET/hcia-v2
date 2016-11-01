/**
 * Object Table chứa thông tin về Table đang được chọn tại tree-view
 */
function Table() {};

// Biến lưu thông tin bảng
Table.instance = null;

/**
 * Tìm một cột trong bảng
 * 
 * @param table			Đối tượng chứa bảng đang được chọn
 * @param szColumnName 	Tên của cột muốn tìm
 * @return Cột tìm thấy hoặc null nếu không tìm được
 */
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

/**
 * Xóa một cột khỏi bảng
 * 
 * @param tableObj			Đối tượng chứa bảng được chọn
 * @param iColumnId			Vị trí của cột muốn xóa trong bảng
 * @param arrRelatedList	Mảng chứa những cột hoặc quan hệ có phụ thuộc cột muốn xóa
 * 
 * @return 					Không có giá trị trả về
 */

Table.deleteColumn = function(tableObj, iColumnId, relatedList) {
	var arrColumnList = [];
	for(var i = 0; i < relatedList.length; i++) {
		arrColumnList.push(TablesList.instances[parseInt(relatedList[i][0])].childs[parseInt(relatedList[i][1])].tempId);
	}
	
	for(var i = 0; i < arrColumnList.length; i++) {
		TablesList.deleteColumnById(arrColumnList[i].toString());
	}
	
	for(var i = 0; i < tableObj.childs.length; i++) {
		if(tableObj.childs[i].tempId == iColumnId) {
			tableObj.childs.splice(i, 1);
			break;
		}
	}
	
	InfoPanel.displayCurrentTable();
};

/**
 * Xóa một quan hệ khỏi bảng
 * 
 * @param tableObj			Đối tượng chứa bảng được chọn
 * @param iRelaId			Chỉ số của quan hệ muốn xóa trong bảng
 * @param arrRelatedList	Mảng chứa những cột hoặc quan hệ có phụ thuộc quan hệ muốn xóa
 * 
 * @return 					Không có giá trị trả về
 */
Table.deleteRela = function(tableObj, iRelaId, arrRelatedList) {
	var relaIdList = [];
	for(var i = 0; i < arrRelatedList.length; i++) {
		arrRelatedList.push(TablesList.instances[parseInt(arrRelatedList[i][0])].childs[parseInt(arrRelatedList[i][1])].tempId);
	}
	
	for(var i = 0; i < relaIdList.length; i++) {
		TablesList.deleteColumnById(arrRelatedList[i]);
	}
	
	for(var i = 0; i < tableObj.childs.length; i++) {
		if(tableObj.childs[i].tempId == iRelaId) {
			tableObj.childs.splice(i, 1);	
			break;
		}
	}
	
	InfoPanel.displayCurrentTable();
}

/**
 * Thêm một cột vào bảng
 * 
 * @param table			Đối tượng Table cần thêm
 * @param simpleCol		Đối tượng chứa dạng đơn giản của cột muốn thêm
 * 
 * @return 				True nếu tạo thành công. Ngược lại, false nếu có lỗi
 */
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

/**
 * Copy object
 * 
 * @param oldObj		Đối tượng muốn copy
 * 
 * @return 				Đối tượng mới
 */
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