
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
  $("#rela-type-detail").val("");
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
  
  var tableId = $("#rela-tableid-detail").val();
  var colName = $("#rela-col-detail").val();
  var rfTableName = $("#rela-rftable-detail").val();
  var rfColName = $("#rela-rfcol-detail").val();
  
  for(var i = 0; i < Table.instance.childs.length; i++) {
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
          rela.foreignKey.columnName = colName;
          rela.referColumn.columnName = rfColName;
          
        } else { // refer to other table
          var oldRefTable = TablesList.getTableByName(rela.referTable.tableName);
          var newRefTable = TablesList.getTableByName(rfTableName);
          
          // update relationship on this table
          rela.referTable.tableName = rfTableName;
          rela.referTable.className = minRefTable.className;
          rela.foreignKey.columnName = colName;
          rela.referColumn.columnName = rfColName;
          
          // delete set on old referential table
          var newOtm;
          for (var j = 0; j < oldRefTable.childs.length; j++) {
            var child = oldRefTable.childs[j];
            if (child.json == "otm" && child.referTable.tempId == tableId) {
              oldRefTable.childs.splice(j, 1);
              newOtm = child;
              console.log(oldRefTable.childs[j]);
              break;
            }
          }
          newOtm.foreignKey.columnName = rfColName;
          newRefTable.childs.push(newOtm);
        }
        
        alert("updated relationship " + rela.tempId);
      
      } else {
        alert("not found relationship type");
      }
      
      
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