$(document).ready(function() {
	$(".nav>li ul").hide();
	
	// Handle click event of label-tag (tree-root)
	$('.tree-toggle').click(function() {
		
		// refresh view
		$(".nav>li ul").hide();
		$(".table-info").empty();
		
		var childComp = $(this).parent().children('ul');
		
		// Toggles tree
		childComp.slideToggle(200);
		
		var g_tableInfo = new Array();
		console.log("Before: " + g_tableInfo);
		var colInfo = new Object();
		
		// Retrieves data
		childComp.children('input').each(function(){
			
			var className = $(this).attr('class');
			if(className == "col-name") {
				//console.log($(this).val());
				colInfo.name = $(this).val();
			}
			else if(className == "col-type") {
				//console.log($(this).val());
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
				
				// add object into array
				g_tableInfo.push(colInfo);
				colInfo = new Object();
			}
			
		});

		for(var i = 0; i < g_tableInfo.length; i++) {
			//console.log(g_tableInfo.length);
			var col = g_tableInfo[i];
			$(".table-info").append("<tr>" +
					"<td>" + col.name + "</td>" +
					"<td>" + col.type + "</td>" +
					"<td>" + "<input type='checkbox' value='" + col.nn + "'/></td>" +
					"<td>" + "<input type='checkbox' value='" + col.pk + "'/></td>" +
					"<td>" + "<input type='checkbox' value='" + col.fk + "'/></td>" +
					"<td>" + "<input type='checkbox' value='" + col.ai + "'/></td>" +
					"</tr>"
					);
		};
		
		console.log("After: " + g_tableInfo);
		
	});

	$('#xml-trigger').click(function() {
		$('#xmlModal').modal('show');
		return false;
	});

	$('#zip-trigger').click(function() {
		$('#zipModal').modal('show');
		return false;
	});

	$(".dropdown").hover(function() {
		$('.dropdown-menu', this).stop(true, true).fadeIn("fast");
	}, function() {
		$('.dropdown-menu', this).stop(true, true).fadeOut("fast");
	});
});
