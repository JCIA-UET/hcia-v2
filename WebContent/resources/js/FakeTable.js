/**
 * Đối tượng chứa tạm thời chứa tên bảng và khóa chính của bảng
 */
function FakeTable(tblName, pkName) {
  this.tblName = tblName;
  this.pkName = pkName;
}

// Mảng lưu các đối tượng FakeTable
FakeTable.list = [];

/**
 * Thêm 1 đối tượng FakeTable vào mảng
 * 
 * @param szTableName 		Tên của bảng
 * @param szPrimaryKeyName	Tên của khóa chính
 * 
 * @return Không có giá trị trả về
 */
FakeTable.add = function (szTableName, szPrimaryKeyName) {
  var tbl = new FakeTable(szTableName, szPrimaryKeyName);
  FakeTable.list.push(tbl);
}

/**
 * Lấy tên cột khóa chính của một bảng
 * 
 * @param szTableName 		Tên của bảng muốn lấy khóa chính
 * 
 * @return Tên khóa chính nếu tìm thấy hoặc null nếu không tìm được
 */
FakeTable.getPkByTbl = function (szTableName) {
  for (var i = 0; i < FakeTable.list.length; i++) {
    if (FakeTable.list[i].tblName == szTableName) {
      return FakeTable.list[i].pkName;
    }
  }
  return null;
}

/**
 * Lấy danh sách các khóa chính của tất cả các bảng trong TablesList
 * 
 * @param Không có tham số
 * 
 * @return Danh sách khóa chính
 */
FakeTable.updateColList = function() {
  var refTblEl = document.getElementById('rela-rftable-detail');
  var refColEl = document.getElementById('rela-rfcol-detail');
  var selectedTable = refTblEl.options[refTblEl.selectedIndex].value;
  var pkName = FakeTable.getPkByTbl(selectedTable);
  refColEl.options[0] = new Option(pkName, pkName);
}