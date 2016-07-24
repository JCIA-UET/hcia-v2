var g_tableInfo = new Object();

$(document).ready(function() {
	$(".nav>li ul").hide();
	
	// Handle click event of label-tag (tree-root)
	$('.tree-toggle').click(function() {
		
		// refresh details view
		$(".col-name-detail").val("");
		$(".col-length-detail").val("");
		$(".col-nn-detail").val("");
		$(".col-pk-detail").val("");
		$(".col-fk-detail").val("");
		$(".col-ai-detail").val("");
		
		// refresh tree and table
		$(".nav>li ul").hide();
		$(".table-info").empty();
		renewColDetail();
		$(".relation-info").empty();
		renewRelaDetail();
		
		var childComp = $(this).parent().children('ul');
		
		// Toggles tree
		childComp.slideToggle(200);
		
		console.log("Before: " + g_tableInfo);
		
		var colInfo = new Object();
		var colArray = new Array();
		var relaInfo = new Object();
		var relaArray = new Array();
		
		// Retrieves data
		childComp.children('input').each(function(){
			
			var className = $(this).attr('class');
			
			// Rela
			if(className == "rela-table") {
				relaInfo.table = $(this).val();
			}
			else if(className == "rela-col") {
				relaInfo.col = $(this).val();
			}
			else if(className == "rela-type") {
				
				relaInfo.type = $(this).val();
			}
			else if(className == "rela-rftable") {
				relaInfo.rftable = $(this).val();
			}
			else if(className == "rela-rfcol") {
				relaInfo.rfcol = $(this).val();
				
				//add object to global var
				relaArray.push(relaInfo);
				relaInfo = new Object();
			}
			
			// Columns
			if(className == "col-id") {
				//console.log($(this).val());
				colInfo.id = $(this).val();
			}
			else if(className == "col-tableid") {
				//console.log("Table ID: " + $(this).val());
				colInfo.tableid = $(this).val();
			}
			else if(className == "col-name") {
				//console.log($(this).val());
				colInfo.name = $(this).val();
			}
			else if(className == "col-type") {
				console.log("Type: " + $(this).val());
				colInfo.type = $(this).val();
			}
			else if(className == "col-pk") {
				//console.log($(this).val());
				colInfo.pk = $(this).val();
			}
			else if(className == "col-ai") {
				//console.log($(this).val());
				colInfo.ai = $(this).val();
			}
			else if(className == "col-nn") {
				//console.log($(this).val());
				colInfo.nn = $(this).val();
			}
			else if(className == "col-fk") {
				//console.log($(this).val());
				colInfo.fk = $(this).val();
			}
			else if(className == "col-length") {
				//console.log($(this).val());
				colInfo.length = $(this).val();
				
				// add object to global var
				colArray.push(colInfo);
				colInfo = new Object();
			}
		});
		
		// Save data
		g_tableInfo.rela = relaArray;
		g_tableInfo.table = colArray;
		
		
		var colsList = g_tableInfo.table;
		console.log("Columns List: " + colsList);
		for(var i = 0; i < colsList.length; i++) {
			//console.log(g_tableInfo.length);
			var col = colsList[i];
			$(".table-info").append("<tr class='col-info'>" +
					"<td>" + col.name + "</td>" +
					"<td>" + col.type + "</td>" +
					"<td>" + "<input id='nn' type='checkbox'/></td>" +
					"<td>" + "<input id='pk' type='checkbox'/></td>" +
					"<td>" + "<input id='fk' type='checkbox'/></td>" +
					"<td>" + "<input id='ai' type='checkbox'/></td>" +
					"</tr>"
					);
			
			// Checked a checkbox if attribute of col is true
			if(col.nn == "true") $("#nn").prop('checked', true);
			if(col.pk == "true") $("#pk").prop('checked', true);
			if(col.fk == "true") $("#fk").prop('checked', true);
			if(col.ai == "true") $("#ai").prop('checked', true);
		};
		
		var relasList = g_tableInfo.rela;
		console.log("Rela List: " + relasList)
		for(var i = 0; i < relasList.length; i++) {
			//console.log(g_tableInfo.length);
			var rela = relasList[i];
			$(".relation-info").append("<tr class='rela-info'>" +
					"<td>" + rela.table + "</td>" +
					"<td>" + rela.col + "</td>" +
					"<td>" + rela.type + "</td>" +
					"<td>" + rela.rftable + "</td>" +
					"<td>" + rela.rfcol + "</td>" +
					"</tr>"
					);
		};
		
	});
	
	$(".relation-info").on("click", "tr", function(){
		var colName = $(this).children(":first").next().text();
		showRelaDetail(colName, g_tableInfo.rela);
	});
	
	$(".table-info").on("click", "tr", function(){
		var colName = $(this).children(":first").text();
		showColDetail(colName, g_tableInfo.table);
	});
	
	$(".tree-node").click(function(){
		// Get name of chosen column
		var crtColName = $(this).text();
		//console.log("Clicked Node: " + crtColName);
		
		// Check holding-data variable
		//console.log("Table Info: " + g_tableInfo);
		
		// Set info
		showColDetail(crtColName, g_tableInfo.table);
	});

	$('#xml-trigger').click(function() {
		$('#xmlModal').modal('show');
		return false;
	});

	$('#zip-trigger').click(function() {
		$('#zipModal').modal('show');
		return false;
	});
	
	$('#reset-data-trigger').click(function() {
		$('#noticeModal').modal('show');
		return false;
	});

	$(".dropdown").hover(function() {
		$('.dropdown-menu', this).stop(true, true).fadeIn("fast");
	}, function() {
		$('.dropdown-menu', this).stop(true, true).fadeOut("fast");
	});
});

function showColDetail(elementName, colArray) {
	for(var i = 0; i < colArray.length; i++) {
		var col = colArray[i];
		if(elementName == col.name) {
			
			// Hidden properties
			$(".col-id-pane input[type=hidden]").val(col.id);
			$(".col-tableid-pane input[type=hidden]").val(col.tableid);
			
			// Visible properties
			$(".col-name-detail").val(col.name);
			$(".col-type-detail").val(col.type);
			$(".col-length-detail").val(col.length);
			if(col.nn == "true") $(".col-nn-detail").prop('checked', true);
			else $(".col-nn-detail").prop('checked', false);
			if(col.pk == "true") $(".col-pk-detail").prop('checked', true);
			else $(".col-pk-detail").prop('checked', false);
			if(col.fk == "true") $(".col-fk-detail").prop('checked', true);
			else $(".col-fk-detail").prop('checked', false);
			if(col.ai == "true") $(".col-ai-detail").prop('checked', true);
			else $(".col-ai-detail").prop('checked', false);
			
			break;
		}
	}
};

function renewColDetail() {
	// Hidden properties
	$(".col-id-pane input[type=hidden]").val("");
	$(".col-tableid-pane input[type=hidden]").val("");
	
	// Visible properties
	$(".col-name-detail").val("");
	$(".col-type-detail").val("");
	$(".col-length-detail").val("");
	$(".col-nn-detail").prop('checked', false);
	$(".col-pk-detail").prop('checked', false);
	$(".col-fk-detail").prop('checked', false);
	$(".col-ai-detail").prop('checked', false);
}

function showRelaDetail(elementName, relaArray) {
	for(var i = 0; i < relaArray.length; i++) {
		var rela = relaArray[i];
		if(elementName == rela.col) {
			$(".rela-table-detail").val(rela.table);
			$(".rela-col-detail").val(rela.col);
			$(".rela-rftable-detail").val(rela.rftable);
			$(".rela-rfcol-detail").val(rela.rfcol);
			
			break;
		}
	}
}

function renewRelaDetail() {
	$(".rela-table-detail").val("");
	$(".rela-col-detail").val("");
	$(".rela-rftable-detail").val("");
	$(".rela-rfcol-detail").val("");
}