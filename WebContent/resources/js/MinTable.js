/**
 * Đối tượng lưu phiên bản đơn giản của bảng, chỉ gồm tên bảng và class đc map từ java 
 */
function MinTable(table) {
  this.tableName = table.tableName;
  this.className = table.className;
  this.columns = [];
  for (var i = 0; i < table.childs.length; i++) {
    var child = table.childs[i];
    if (child.json == "column") {
      var col = new MinColumn(child.columnName);
      this.columns.push(col);
    }
  }
}

// Mảng lưu các đối tượng MinTable
MinTable.list = [];

/**
 * Thêm một đối tượng vào trong danh sách
 * 
 * @param minTableObj Đối tượng cần lưu
 * @return Không có giá trị trả về
 */
MinTable.add = function (minTableObj) {
  var minTbl = new MinTable(minTableObj);
  MinTable.list.push(minTbl);
}

/**
 * Lấy đối tượng MinTable trong danh sách theo tên
 * 
 * @param szMinTableName 	Tên của MinTable cần lấy
 * @return 					Đối tượng MinTable tìm thấy hoặc null nếu không tìm đc
 */
MinTable.getTableByName = function (szMinTableName) {
  for (var i = 0; i < MinTable.list.length; i++) {
    if (MinTable.list[i].tableName == requestedName) {
      return MinTable.list[i]; 
    }
  }
  return null;
}
