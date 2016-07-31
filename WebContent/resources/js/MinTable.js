// minimum version of table with necessary fields
// table name
// class name
// 
MinTable.list = [];

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

MinTable.add = function (table) {
  var minTbl = new MinTable(table);
  MinTable.list.push(minTbl);
}

MinTable.getTableByName = function (requestedName) {
  for (var i = 0; i < MinTable.list.length; i++) {
    if (MinTable.list[i].tableName == requestedName) {
      return MinTable.list[i]; 
    }
  }
  return null;
}

// column with only field is columnName
function MinColumn(columnName) {
  this.columnName = columnName;
}
