/**
 * Object quản lí các action làm thay đổi dữ liệu trong bảng hiện thông tin
 * của đối tượng Table
 * 
 * @returns
 */
function TableAction() {}

/**
 * Đặt lại giá trị mặc định cho các trường trong phần Detail
 * 
 * @param No param.
 * 
 * @return No return.
 */
TableAction.resetDetails = function() {
	// reset column details
	$("#col-tempid-detail").val("");
	$("#col-name-detail").val("");
	$("#col-type-detail").val("");
	$("#col-length-detail").val("");
	$("#col-nn-detail").prop('checked', false);
	$("#col-pk-detail").prop('checked', false);
	$("#col-fk-detail").prop('checked', false);
	$("#col-ai-detail").prop('checked', false);

	// reset relationship details
	$("#rela-tempid-detail").val("");
	$("#rela-type-detail").val("");
	$("#rela-col-detail").val("");
	$("#rela-rftable-detail").val("");
	$("#rela-rfcol-detail").val("");
}

/**
 * Lưu object Table đang hiện
 * 
 * @param No param.
 * 
 * @return No return.
 */
TableAction.save = function() {
	updateColumn();
	updateRelationship();

	InfoPanel.displayCurrentTable();
}

/**
 * Thay đổi hiển thị của cột khi chọn vào 1 dòng bất kì ở bảng thông tin
 * 
 * @param No param.
 * 
 * @return No return.
 */
TableAction.setFocus = function(parentElement, chosenElement) {
	var childrenElement = $(parentElement).children();
	console.log(childrenElement.length);
	for (var i = 0; i < childrenElement.length; i++) {
		$(childrenElement[i]).removeAttr("style");
		
		if (chosenElement == childrenElement[i]) {
			$(childrenElement[i]).css({"backgroundColor": "#949494"});
			$(childrenElement[i]).css({"color":"#fff"});
			$(childrenElement[i]).css({"cursor":"pointer"});
		}
	}
}

/**
 * Cập nhật những thông tin thay đổi từ phần Detail
 * trong tab Relation vào đối tượng RelationShip
 * 
 * @param No param.
 * 
 * @return No return.
 */
function updateRelationship() {
	var relTempId = $("#rela-tempid-detail").val();
	if (relTempId == "")
		return;

	var tableId = $("#rela-tableid-detail").val();
	var colName = $("#rela-col-detail").val();
	var rfTableName = $("#rela-rftable-detail").val();
	var rfColName = $("#rela-rfcol-detail").val();

	for (var i = 0; i < Table.instance.childs.length; i++) {
		var rela = Table.instance.childs[i];
		if (rela.tempId == relTempId) {
			if (rela.json == "otm") {
				alert("cannot update relationship from referenced table");
				return;
			}

			// mto relationship
			if (rela.json == "mto") {
				var minRefTable = MinTable.getTableByName(rfTableName);

				// only update referential column
				if (rela.referTable.tableName == rfTableName) {
					// Find foreign key's index
					for(var j = 0; j < Table.instance.childs.length; j++) {
						if(Table.instance.childs[j].json == "column" 
							&& Table.instance.childs[j].foreignKey == true
							&& Table.instance.childs[j].columnName == rela.foreignKey.columnName)
						{
							Table.instance.childs[j].columnName = colName;
							break;
						}	
					}
					rela.foreignKey.columnName = colName;
					rela.referColumn.columnName = rfColName;

				} else { // refer to other table
					var oldRefTable = TablesList
							.findTableByName(rela.referTable.tableName);
					var newRefTable = TablesList.findTableByName(rfTableName);

					// update relationship on this table
					rela.referTable.tableName = rfTableName;
					rela.referTable.className = minRefTable.className;
					rela.foreignKey.columnName = colName;
					rela.referColumn.columnName = rfColName;

					// delete set on old referential table
					var newOtm;
					for (var j = 0; j < oldRefTable.childs.length; j++) {
						var child = oldRefTable.childs[j];
						if (child.json == "otm"
							&& child.referTable.tempId == tableId) 
						{
							oldRefTable.childs.splice(j, 1);
							newOtm = child;
							break;
						}
					}
					newOtm.foreignKey.columnName = rfColName;
					newRefTable.childs.push(newOtm);
				}

				alert("updated relationship " + rela.tempId);
				
				TreeView.recreateTree();
				TreeView.expanseElement(Table.instance.tableName);

			} else {
				alert("not found relationship type");
			}

		}
	}
}

/**
 * Cập nhật những thông tin thay đổi từ phần Detail
 * trong tab Table vào đối tượng Table đang được chọn
 * 
 * @param No param.
 * 
 * @return No return.
 */
function updateColumn() {
	var colTempId = $("#col-tempid-detail").val();
	if (colTempId == "")
		return;

	var colName = $("#col-name-detail").val();
	var colType = $("#col-type-detail").val();
	var colLength = $("#col-length-detail").val();
	var colNN = false, colPK = false, colFK = false, colAI = false;
	if ($('#col-nn-detail').is(":checked"))
		colNN = true;
	if ($('#col-pk-detail').is(":checked"))
		colPK = true;
	if ($('#col-fk-detail').is(":checked"))
		colFK = true;
	if ($('#col-ai-detail').is(":checked"))
		colAI = true;

	for (var i = 0; i < Table.instance.childs.length; i++) {
		var col = Table.instance.childs[i];
		if (col.tempId == colTempId) {
			if (col.foreignKey) {
				alert("Can not update foreign key from reference table");
				return;
			}
			
			else if (col.json == "pk") {
				var cfDialog = confirm("You changed a PRIMARY KEY.\nThis action will affect other tables.\nDo you want to continue?");
				if(cfDialog == false) {
					return;
				}
				else {
					col.columnName = colName;
					col.dataType = colType;
					col.length = colLength;
					col.primaryKey = colPK;
					col.notNull = colNN;
					col.foreignKey = colFK;
					
					// edit tables which have relationship with this pk here
					var mtoList = TablesList.findMTORelaPosRelatedToPK(col);
					for (var i = 0; i < mtoList.length; i++) {
						var tempMTOTable = TablesList.instances[parseInt(mtoList[i][0])];
						tempMTOTable.childs[parseInt(mtoList[i][1])].referColumn.columnName = col;
	
						TablesList.updateTable(tempMTOTable);
					}
				}
			}
			else {
				col.columnName = colName;
				col.dataType = colType;
				col.length = colLength;
				col.primaryKey = colPK;
				col.notNull = colNN;
				col.foreignKey = colFK;
			}
			
			alert("Updated column " + col.tempId + " successfully!");
			break;
		}
	}
	
	TreeView.recreateTree();
	TreeView.expanseElement(Table.instance.tableName);
}