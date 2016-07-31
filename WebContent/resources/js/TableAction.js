
function TableAction() {
  
}

TableAction.resetDetails = function () {
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
  $("#rela-table-detail").val("");
  $("#rela-col-detail").val("");
  $("#rela-rftable-detail").val("");
  $("#rela-rfcol-detail").val("");
  
  // reset button save
  $("#btn-save").text("Save");
}

TableAction.save = function () {
  updateColumn();
  updateRelationship();
  addColumn();
  
  InfoPanel.displayCurrentTable();
}

function addColumn() {
  var colTempId = $("#col-tempid-detail").val();
  var colName = $("#col-name-detail").val();
  var colType = $("#col-type-detail").val();
  var colLength = $("#col-length-detail").val();
  var colNN = false, colPK = false, colFK = false, colAI = false;
  if ($('#col-nn-detail').is(":checked")) colNN = true;
  if ($('#col-pk-detail').is(":checked")) colPK = true;
  if ($('#col-fk-detail').is(":checked")) colFK = true;
  if ($('#col-ai-detail').is(":checked")) colAI = true;
  
  if (colTempId == "" && colName != "" && colType != "" && colLength != "") {
    var col = {};
    col['hbmAttributes'] = {};
    col['javaName'] = "";
    col['columnName'] = colName;
    col['dataType'] = colType;
    col['length'] = colLength;
    col['primaryKey'] = colPK;
    col['notNull'] = colNN;
    col['foreignKey'] = colFK;
    
    if (colPK) {
      col['autoIncrement'] = colAI;
      col['json'] = "pk";
    } else {
      col['json'] = "column";
    }
    
    Table.instance.childs.push(col);
    alert("add column successfully");
  }
  
}

function updateRelationship() {
  var relTempId = $("#rela-tempid-detail").val();
  if (relTempId == "") return;
  
  var colName = $("#rela-col-detail").val();
  var rfTableName = $("#rela-rftable-detail").val();
  var rfColName = $("#rela-rfcol-detail").val();
  
  for(var i = 0; i < Table.instance.childs.length; i++) {
    var rela = Table.instance.childs[i];
    if (rela.tempId == relTempId) {
      
      var minRefTable = MinTable.getTableByName(rfTableName);
      rela.referTable.tableName = rfTableName;
      rela.referTable.className = minRefTable.className;
      rela.foreignKey.columnName = colName;
      
      if (rela.json == "mto") {
        //cmto relationship
        rela.referColumn.columnName = rfColName;
      } else if (rela.json == "otm") {
        // do something with otm relationshipS
      }
      console.log(rela);
      alert("updated relationship " + rela.tempId);
    }
  }
}

function updateColumn() {
  var colTempId = $("#col-tempid-detail").val();
  if (colTempId == "") return;
  
  var colName = $("#col-name-detail").val();
  var colType = $("#col-type-detail").val();
  var colLength = $("#col-length-detail").val();
  var colNN = false, colPK = false, colFK = false, colAI = false;
  if ($('#col-nn-detail').is(":checked")) colNN = true;
  if ($('#col-pk-detail').is(":checked")) colPK = true;
  if ($('#col-fk-detail').is(":checked")) colFK = true;
  if ($('#col-ai-detail').is(":checked")) colAI = true;
  
  for(var i = 0; i < Table.instance.childs.length; i++) {
    var col = Table.instance.childs[i];
    if (col.tempId == colTempId) {
      if (col.foreignKey) {
        alert("cannot update foreign key from reference table");
        return ;
      }
      col.columnName = colName;
      col.dataType = colType;
      col.length = colLength;
      col.primaryKey = colPK;
      col.notNull = colNN;
      col.foreignKey = colFK;
      
      if (col.json == "pk") {
        // edit tables which have relationship with this pk here 
      }
      
      alert("updated column " + col.tempId);
    }
  }
}