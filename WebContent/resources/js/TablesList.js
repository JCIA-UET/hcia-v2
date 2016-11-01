/**
 * Object TableList chứa danh sách tất cả các Table
 */
function TablesList() {};

TablesList.instances = null;

/**
 * Chuyển data từ String sang đối tượng TableList
 * 
 * @param rawData	Danh sách bảng dưới dạng String
 * 
 * @return 			Không có giá trị trả về
 */
TablesList.convertStringToObject = function(szRawData) {
	if(szRawData != null && szRawData != "") {
		RootNode.instance = JSON.parse(szRawData);
		TablesList.instances = RootNode.instance.childs;		
	}
};

/**
 * Tìm chỉ số lớn nhất trong danh sách
 * 
 * @param 	Không có tham số truyền vào
 * 
 * @return 	Chỉ số lớn nhất
 */
TablesList.findMaximumTempId = function() {
	var iMax = -1;
	var iTempId = -1;
	
	for (var i = 0; i < TablesList.instances.length; i++) {
		var tempTable = TablesList.instances[i];
		iTempId = parseInt(tempTable.tempId);
		
		if (iTempId >= iMax) {
			iMax = iTempId;
		}
		
		for (var j = 0; j < TablesList.instances[i].childs.length; j++) {
			var tempCol = TablesList.instances[i].childs[j];
			iTempId = parseInt(tempCol.tempId);
			if (iTempId >= iMax) {
				iMax = iTempId;
			}
		}
	}
	return iMax;
}

/**
 * Tìm bảng trong danh sách theo tên
 * 
 * @param szTableName	Tên của bảng cần tìm
 * 
 * @return 				Đối tượng Table tìm đc hoặc null nếu không tìm thấy
 */
TablesList.findTableByName = function(szTableName) {
	if(szTableName == null || szTableName == "") {
		console.log("Can't find table with name: " + szTableName);
	}
	else {
		for(var i = 0; i < TablesList.instances.length; i++) {
			if(TablesList.instances[i].tableName.toLowerCase() == szTableName.toLowerCase())
				return TablesList.instances[i];
		}
		return null;
	}
};

/**
 * Tìm bảng trong danh sách theo chỉ số
 * 
 * @param szTableName	Tên của bảng cần tìm
 * 
 * @return 				Đối tượng Table tìm đc hoặc null nếu không tìm thấy
 */
TablesList.getTableById = function (iTableId) {
	for (var i = 0; i < TablesList.instances.length; i++) {
	    if (TablesList.instances[i].tempId == iTableId) {
	    	return table;
	    }
	}
	return null;
};


/**
 * Cập nhật bảng trong danh sách
 * 
 * @param table			Đối tượng Table cần cập nhật
 * 
 * @return 				Không có giá trị trả về
 */
TablesList.updateTable = function(table) {
	for(var i = 0; i < TablesList.instances.length; i++) {
		if(TablesList.instances[i].tempIdiTableIdble.tempId) {
			TablesList.instances[i] = table;
			break;
		}
	}
};


/**
 * Xóa bảng trong danh sách
 * 
 * @param table			Đối tượng Table cần xóa
 * 
 * @return 				Không có giá trị trả về
 */
TablesList.deleteTable = function(table) {
	for(var i = 0; i < TablesList.instances.length; i++) {
		if(TablesList.instances[i].tempId == table.tempId) {
			TablesList.instances.splice(i, 1);
			console.log("Delete successfully");
			break;
		}
	}
}

/**
 * Tìm một cột của một bảng trong danh sách
 * 
 * @param table			Đối tượng Table chứa cột cần tìm
 * @param iColumnTempId	Chỉ số của cột muốn tìm
 * 
 * @return 				Cột tìm được hoặc null nếu không tìm thấy
 */
TablesList.findColumnById = function(table, iColumnTempId) {
	for(var i = 0; i < table.childs.length; i++) {
		if(table.childs[i].json == "column" || table.childs[i].json == "pk") {
			if(table.childs[i].tempId == iColumnTempId) {
				return table.childs[i];
			}
		}
	}
	return null;
}


/**
 * Xóa một cột của một bảng trong danh sách
 * 
 * @param iColumnTempId		Chỉ số của cột muốn xóa
 * 
 * @return 				Không có giá trị trả về
 */
TablesList.deleteColumnById = function(iColumnTempId) {
	for(var i = 0; i < TablesList.instances.length; i++) {
		for(var j = 0; j < TablesList.instances[i].childs.length; j++) {
			if(TablesList.instances[i].childs[j].tempId == iColumnTempId) {
				TablesList.instances[i].childs.splice(j, 1);
			}
		}
	}
}

/**
 * Tìm các quan hệ Many-to-One của Khóa Chính
 * 
 * @param colPrimaryKey		Cột là Khóa Chính muốn tìm
 * 
 * @return 				Mảng vị trí các quan hệ Many-to-One. Mỗi phần tử của mảng có dang [i,j]
 * 						Với i: vị trí của bảng chứa quan hệ MTO trong TableList
 * 							j: vị trí của quan hệ trong bảng tìm đc
 */
TablesList.findMTORelaPosRelatedToPK = function(colPrimaryKey) {
	var affectedList = [];
	for(var i = 0; i < TablesList.instances.length; i++) {
		for(var j = 0; j < TablesList.instances[i].childs.length; j++) {
			var tempTable = TablesList.instances[i];
			if(tempTable.childs[j].json == "mto") {
				if(tempTable.childs[j].referColumn.columnName == colPrimaryKey.columnName) {
					var index = [];
					index.push(i); index.push(j);
					affectedList.push(index);
				}
			}
		}
	}
	return affectedList;
}

/**
 * Tìm quan hệ "đôi xứng" của một quan hệ
 * 
 * @param table			Bảng chứa quan hệ muốn tìm
 * @param iRelationId	Vị trí của quan hệ muốn tìm trong bảng
 * 
 * @return 				Vị trí của quan hệ tìm được dưới dạng [i, j]. Trong đó:
 * 						i: vị trí bảng của quan hệ tìm được trong TableList
 * 						j: vị trí của quan hệ tìm được trong bảng đó
 */
TablesList.findOppositeRelationPos = function(table, iRelationId) {
	var rfTable = {};
	var posCouple = [];
	var tableIdx, colIdx;
	
	// Finf refer table
	for(var i = 0; i < TablesList.instances.length; i++) {
		if(table.childs[parseInt(iRelationId)].referTable.tableName == TablesList.instances[i].tableName) {
			rfTable = TablesList.instances[i];
			tableIdx = i;
			break;
		}
	}
	
	if(table.childs[parseInt(iRelationId)].json == "mto") {
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
	else if(table.childs[parseInt(iRelationId)].json == "otm") {
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

/**
 * Tìm tất cả các thuộc tính hoặc quan hệ có phụ thuộc vào một cột cho trước
 * 
 * @param table			Bảng chứa cột muốn tìm
 * @param iColumnTempId	Chỉ số của cột muốn tìm trong TableList
 * 
 * @return 				Mảng vị trí các thuộc tính và quan hệ tìm được.
 * 						Mỗi phần tử của mảng có dang [i,j], trong đó:
 * 						i: vị trí của bảng bảng chứa thuộc tính hoặc quan hệ trong TableList
 * 						j: vị trí của thuộc tính hoặc quan hệ trong bảng tìm đc
 */
TablesList.findRelatedElements = function(table, iColumnTempId) {
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
		if(table.childs[i].tempId == iColumnTempId) {
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