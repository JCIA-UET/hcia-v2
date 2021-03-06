/**
 * Đối tượng quản lý phần hiển thị cho Panel hiện thông tin của đối tượng Table.
 * Panel có 2 tab: Table và Relation.
 */
function InfoPanel() {};

/**
 * Hiện bảng thông tin của đối tượng Table đang được chọn
 * 
 * @param Không có tham số.
 * @return Không có giá trị trả về.
 */
InfoPanel.displayCurrentTable = function() {
	// Show table and detail area
	$(".hcia-contentpanel").children("ul").show();
	$(".tab-content").show();
	$(".detail-wrap").hide();
	
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

/**
 * Xóa một cột khỏi bảng
 * 
 * @param tableObj			Đối tượng chứa bảng được chọn
 * @param iColumnId			Vị trí của cột muốn xóa trong bảng
 * @param arrRelatedList	Mảng chứa những cột hoặc quan hệ có phụ thuộc cột muốn xóa
 * 
 * @return 					Không có giá trị trả về
 */
InfoPanel.deleteColumn = function(table, iColumnId, relatedList) {
	Table.deleteColumn(table, iColumnId, relatedList);
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
InfoPanel.deleteRela = function(tableObj, iRelaId, arrRelatedList) {
	Table.deleteRela(tableObj, iRelaId, arrRelatedList);
}

/**
 * Hiện thông tin chi tiết về một cột tại phần Detail ở tab Table
 * 
 * @param szColName			Tên cột muốn xem thông tin
 * 
 * @return 					Không có giá trị trả về
 */
InfoPanel.showColDetail = function(szColName) {
	for(var i = 0; i < Table.instance.childs.length; i++) {
		var col = Table.instance.childs[i];
		if(szColName == col.columnName) {
		  
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

/**
 * Thêm 1 cột vào bảng
 * 
 * @param tableObj			Đối tượng Table muốn thêm cột
 * @param simpleColObj		Dạng đơn giản của cột muốn thêm
 * 
 * @return 					Không có giá trị trả về
 */
InfoPanel.addColumn = function(tableObj, simpleColObj) {
	var result = Table.addColumn(tableObj, simpleCol);
	if(result == true) $('#addColModal').modal("hide");
}

/**
 * Hiện thông tin chi tiết về một Quan Hệ tại phần Detail ở tab Relation
 * 
 * @param szRelaName		Tên Quan Hệ muốn xem thông tin
 * 
 * @return 					Không có giá trị trả về
 */
InfoPanel.showRelaDetail = function(szRelaName) {
	for(var i = 0; i < Table.instance.childs.length; i++) {
		if(szRelaName == Table.instance.childs[i].foreignKey.columnName) {
			$("#btn-save").text("Update");
		  
			var element = Table.instance.childs[i];
			var fkColName = (element.foreignKey != null) ? element.foreignKey.columnName : "";
			var rfTableName = (element.referTable != null) ? element.referTable.tableName : "";
			var rfColName = (element.referColumn != null) ? element.referColumn.columnName : "";

			$("#rela-tempid-detail").val(element.tempId);
			$("#rela-tableid-detail").val(Table.instance.tempId);
			$("#rela-type-detail").val(element.type);
			$("#rela-col-detail").val(fkColName);
			
			var refTblEl = document.getElementById('rela-rftable-detail');
			refTblEl.options.length = 0;
			console.log(FakeTable.list.length);
			for (var i = 0; i < FakeTable.list.length; i++) {
				var tbl = FakeTable.list[i];
				refTblEl.options[refTblEl.options.length] = new Option(tbl.tblName, tbl.tblName);
	      
				if (tbl.tblName == rfTableName) {
					refTblEl.selectedIndex = i;
					document.getElementById('rela-rfcol-detail').options[0] = new Option(tbl.pkName, tbl.pkName);
				}
			}
	    
			break;
		}
	}
}

/**
 * Xóa hết thông tin hiển thị trên panel
 * 
 * @param 	Không có tham số
 * 
 * @return 	Không có giá trị trả về
 */
InfoPanel.clearData = function() {
	$(".table-info").html("");
	$(".relationship-info").html("");
}