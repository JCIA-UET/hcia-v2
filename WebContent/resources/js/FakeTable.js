FakeTable.list = [];

function FakeTable(tblName, pkName) {
  this.tblName = tblName;
  this.pkName = pkName;
}

FakeTable.add = function (tblName, pkName) {
  var tbl = new FakeTable(tblName, pkName);
  FakeTable.list.push(tbl);
}

FakeTable.getFkByTbl = function (szName) {
  for (var i = 0; i < FakeTable.list.length; i++) {
    if (FakeTable.list[i].tblName == szName) {
      return FakeTable.list[i].pkName;
    }
  }
  return null;
}

FakeTable.updateColList = function() {
  var refTblEl = document.getElementById('rela-rftable-detail');
  var refColEl = document.getElementById('rela-rfcol-detail');
  var selectedTable = refTblEl.options[refTblEl.selectedIndex].value;
  var pkName = FakeTable.getFkByTbl(selectedTable);
  refColEl.options[0] = new Option(pkName, pkName);
}