
function TableAction() {
  
}

TableAction.resetDetails = function () {
  // reset column details
  $("#col-name-detail").val("");
  $("#col-type-detail").val("");
  $("#col-length-detail").val("");
  $("#col-nn-detail").prop('checked', false);
  $("#col-pk-detail").prop('checked', false);
  $("#col-fk-detail").prop('checked', false);
  $("#col-ai-detail").prop('checked', false);
  
  // reset relationship details
  $("#rela-table-detail").val("");
  $("#rela-col-detail").val("");
  $("#rela-rftable-detail").val("");
  $("#rela-rfcol-detail").val("");
}

TableAction.save = function () {
  updateColumn();
  updateRelationship();
  InfoPanel.displayCurrentTable();
}

function updateRelationship() {
  var relTempId = $("#rela-tempid-detail").val();
  var colName = $("#rela-col-detail").val();
  var rfTableName = $("#rela-rftable-detail").val();
  var rfColName = $("#rela-rfcol-detail").val();
  
  for(var i = 0; i < Table.instance.childs.length; i++) {
    var rela = Table.instance.childs[i];
    if (rela.tempId == relTempId) {
      console.log(rela);
      alert("updated relationship " + rela.tempId);
    }
  }
}

function updateColumn() {
  var colTempId = $("#col-tempid-detail").val();
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
    if (col.tempId == colTempId && colName != "" && colType != "" && colLength != "") {
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