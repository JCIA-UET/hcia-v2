function TreeView() {};

TreeView.createTree = function() {
	if(TablesList.instances == null) {
		console.log("Can't create tree: No data");
	}
	
	var treeComponent = $("#tree-root");
	
	// empty element before create new tree
	treeComponent.empty();
	
	// Create Table List
	for(var i = 0; i < TablesList.instances.length; i++) {
		var Table = TablesList.instances[i];
		
		//add table to MinTable list
		MinTable.add(Table);
		
		treeComponent.append(
				"<li class='tree-root nav-header'>" +
					"<label class='tree-toggle'>" + Table.tableName + "</label>" +
					"<ul id='tree-child-" + Table.tempId + "' class='nav'>");
		
		var treeChildComponent = $("#tree-child-" + Table.tempId);
		
		// Create Column List
		for(var j = 0; j < Table.childs.length; j++) {
			var Column = Table.childs[j];
			if(Column.json == "column" || Column.json == "pk") {
				treeChildComponent.append(
						"<li><a class='tree-node' href='#' contenteditable='false'>" + Column.columnName + "</a></li>"
						);
				
				// add to FakeTable list
				if (Column.json == "pk") {
				  FakeTable.add(Table.tableName, Column.columnName);
				  console.log("Add to FakeTable: " + Table.tableName + " - " + Column.columnName);
				}
				
			}
		}
		
		treeComponent.append("</ul></li>");
	}
	
	// Tree toggle
	$(".nav>li ul").hide();
}

//TreeView.hideElementById = function(table, tempId) {
//	var treeElements = document.getElementsByClassName("tree-toggle");
//	var delCol = TablesList.findColumnById(table, tempId);
//	
//	if(delCol != null) {
//		for(var i = 0; i < treeElements.length; i++) {
//			if($(treeElements[i]).text() == table.tableName) {
//				$(treeElements[i]).parent().children("ul").children("li").each(function(){
//					if($(this).text() == delCol.columnName) {
//						$(this).remove();
//					}
//				});
//			}
//		}
//	}
//}

TreeView.recreateTree = function() {
	$("#tree-root").empty();
	TreeView.createTree();
}

TreeView.expanseElement = function(tableName) {
	var treeElements = document.getElementsByClassName("tree-toggle");
	
	for(var i = 0; i < treeElements.length; i++) {
		if($(treeElements[i]).text() == tableName) {
			$(treeElements[i]).parent().children("ul").show();
		}
	}
}