$(document).ready(function(){
	draw();
	//jquery begin
	$("#btn-delete-pro").click(onclick_btn_delete_pro);
	$("#btn-delete-rela").click(onclick_btn_delete_rela);
	$("#btn-add-pro").click(onclick_btn_add_pro);
	$("#btn-add-rela").click(onclick_btn_add_rela);
	$("#btn-ok-pro").click(onclick_btn_ok_pro);
	$("#btn-cancel-pro").click(onclick_btn_cancel_pro);
	$("#btn-ok-rela").click(onclick_btn_ok_rela);
	$("#btn-cancel-rela").click(onclick_btn_cancel_rela);
	
});
    var listRelationship;
    var listTable ;
    /* function : draw
     * details: to draw ERD
     * */
  	function draw() {
  	if(RootNode.instance == null)
  		return;
  	
	var json = RootNode.instance;
	var d3Obj = convert(RootNode.instance);
    listRelationship = d3Obj.relationships;
    listTable = d3Obj.tables;
    //console.log(listRelationship);
	$("#btn-add-rela").prop('disabled',false);
	var margin = {
			top: -5,
			right: -40,
			bottom: -5,
			left: -5
	    },
	    
	    width = 960 - margin.left - margin.right,
	    height = 610 - margin.top - margin.bottom;
	   
		var drag = d3.behavior
				    .drag()
				    .on('dragstart', function() {
				     d3.event.sourceEvent.stopPropagation();
				    })
				    .on("drag",function(d, i) {
				      d.x += d3.event.dx;
				      d.y += d3.event.dy;
				      
				      d3.select(this).attr("transform",function(d, i) {
				    	  d3.selectAll('.jifj').each(function(d){
						   		d.calculateAllRelative();
						   		
						   	});
				    	  
				         var dates = d3.selectAll(".line").each(
				           function(dataLine) {
				        	   dataLine.draw(d3.select("#line"+dataLine.table+dataLine.referTbl));
				        	   console.log(dataLine.checkps('customer','order'));
				            })
				          
				         return "translate(" + [d.x,d.y] + ")";
				         });
				     });
	   var zoom = d3.behavior.zoom().scaleExtent([0.2, 3]).on("zoom", zoomed);

	   var svg = d3.select("#ERD").append("svg")
	   							  .attr("width",width)
	   							  .attr("height", height)
	   							  .attr("transform","translate(" + 0 + "," + 0 + ")")
	   							  .style("background-color", "white");
	   var gFirst = svg.append("g").attr("transform","translate(" + margin.left + "," + margin.right + ")").call(zoom);
		var marker1 =   gFirst.append("defs").append("marker")
		   .attr("id", "arrowhead")
		   .attr("refX",   8) /*must be smarter way to calculate shift*/
		   .attr("refY", 5)
		   .attr("markerWidth", 200)
		   .attr("markerHeight", 200)
		   .attr("orient", "auto")
		   .append("path")
		   .attr("d", "M3 5  L8 9 , M3 5  L8 1 , M0 1 L0 8")
		   .attr("stroke", "grey")
		   .attr("fill","none");
		var marker2 =   gFirst.append("defs").append("marker")
		.attr("id", "oneMar")
		.attr("refX", -5) /*must be smarter way to calculate shift*/
		.attr("refY", 4)
		.attr("markerWidth", 10)
		.attr("markerHeight", 10)
		.attr("orient", "auto")
		.append("path")
		.attr("d", "M2 1 L2 8 , M0 1 L0 8 ")
		   .attr("stroke", "grey")
		   
		   .attr("fill","none");
		
	   
		var rect2 = gFirst.append("rect")
	   					 .attr("width", width)
	   					 .attr("height", height)
	   					 .style("fill", "#FFF");
	   var gSecond = gFirst.append("g").attr("id","gSecond");
	 
	   function zoomed() {
		   gSecond.attr("transform", "translate(" + d3.event.translate + ")scale(" + d3.event.scale + ")");
	   }

	   var i = 0;
	   var k = 0;
	   var face = gSecond.selectAll("g")
	    				 .data(listTable)
	    				 .enter()
	    				 .append("g")
	    				 .each(function(d) {
						      var listColumn = d.listColumn;
						      var gCurrent = d3.select(this).attr("class", "jifj")
						      								.attr("transform","translate(" + d.x + "," + d.y + ")")
						      								.attr("id", d.tableName)
						      								.call(drag);
						      d.draw(gCurrent,i,k);
						      i++;
						      if(i == listTable.length/2){ 
						    	  i=0;
						    	  k++;
						      }
						     });
	   d3.selectAll('.jifj').each(function(d){
	   		d.calculateAllRelative();
	   	});
	 
	   	var line = gSecond.selectAll("polyline")
	    .data(listRelationship)
	    .enter()
	    .append("polyline")
	    .each(function(d) {
	    	d.draw(d3.select(this));
	    });
		 
	   		
	  };
	  

	/* function : convert
	 * details : to convert object Json from server to object json ERD
	 * param : datajson - object json from server
	 * */
	function convert(datajson){
		var result = {"tables":[], "relationships":[]};
		for(var i = 0 ; i<datajson.childs.length; i++ ){
			var currentTable = datajson.childs[i] ;

			var listColumn = [] ;
			for(var j = 0 ; j < currentTable.childs.length ; j++){
				var currentElement = currentTable.childs[j];
				if (currentElement.json == "pk" || currentElement.json == "column"){
					var name = currentElement.columnName;
					if(currentElement.dataType == null){
						currentElement.dataType = 0;
					}
					var type = currentElement.dataType;
					var length = currentElement.length;
					var primaryKey = currentElement.primaryKey;
					var notNull = currentElement.notNull;
					var unique = currentElement.unique;
					var foreignKey =currentElement.foreignKey;
					var autoIncrement = false;
					if(currentElement.json == "pk"){
						autoIncrement = currentElement.autoIncrement;
					}
					var column = new Columnd3(name, type , length , primaryKey, notNull,foreignKey, unique , autoIncrement);
					listColumn.push(column);
				}
				if(currentElement.json == "otm"){
					var type = 'otm';
				    var	table1 = currentTable.tableName.toUpperCase();
					var column1 = currentElement.foreignKey.columnName;
					var referTbl = currentElement.referTable.tableName.toUpperCase();
					var referColumn = currentElement.foreignKey.columnName;
					result.relationships.push(new Relationshipd3(type,table1,column1, referTbl, referColumn));
				}
				
			}
			var table = new Tabled3(currentTable.tableName.toUpperCase(), listColumn);
			result.tables.push(table);
		}
		/*
		 * thêm dữ liệu relative cho mỗi bảng:
		 * relative[] : chứa tên các bảng mà bảng này có quan hệ :)
		 * */
		for(var i = 0 ; i < result.relationships.length ; i++ ){
			for( var j = 0 ; j < result.tables.length ; j++){
				if(result.relationships[i].table == result.relationships[i].referTbl && 
						result.relationships[i].table == result.tables[j].tableName){
					result.tables[j].relative.push(result.tables[j].tableName);
					break;
				}
				if(result.relationships[i].table == result.tables[j].tableName){
					result.tables[j].relative.push(result.relationships[i].referTbl);
				}
				if(result.relationships[i].referTbl == result.tables[j].tableName){
					result.tables[j].relative.push(result.relationships[i].table);
				}
			}
		}
		return result;
	}
	
	/* function : moveover_table
	 * details: to handle event mouse over on the table in ERD
	 * */
	function moveover_table(d,i){
		  d3.select(this).attr("class"," mouseover-table");
		  d3.select("#rect"+d.name).attr("class"," mouseover-table");
	}
	
	/* function : moveout_table
	 * details: to handle event mouse out on the table in ERD
	 * */
	function moveout_table(d,i){
		 d3.select(this).attr("class"," mouseout-table");
		  d3.select("#rect"+d.name).attr("class"," mouseout-table");
	}
	

	/* function : mouse_onclick_table
	 * details: to handle event mouse click on the table in ERD
	 * */
	function mouse_onclick_table(d,i){
		$("#table-erd-current").val(d.name);
		$(".option-column-erd").remove();
		for(var i = 0 ; i < d.listColumn.length;i++){
			$("#column-erd-current").append($("<option></option>")
	                .attr("value",d.listColumn[i].name)
	                .attr("class","option-column-erd")
	                .text(d.listColumn[i].name));
		}
		$("#column-erd-current").change(function(){
			var value = $(this).val();
			if(value != null){
				$("#btn-delete-pro").prop("disabled",false);
				$('#form-new-property').hide();
				$('#form-btn-default').show();
			}
		}).change();
		$("#btn-add-pro").prop("disabled",false);
		d3.event.stopPropagation();
	
	}
	
	/* function : moveover_pp
	 * details: to handle event mouse over on the table in ERD
	 * */
	function moveover_pp(d,i){
		  d3.select(this).attr("class"," mouseover-table rectxxx"+d.name);
		  d3.select("#rect"+d.name).attr("class"," mouseover-table");
	}
	
	/* function : moveout_pp
	 * details: to handle event mouse out on the table in ERD
	 * */
	function moveout_pp(d,i){
		 d3.select(this).attr("class"," mouseout-table rectxxx"+d.name);
		  d3.select("#rect"+d.name).attr("class"," mouseout-table");
	}
	

	/* function : mouse_onclick_pp
	 * details: to handle event mouse click on the table in ERD
	 * */
	function mouse_onclick_pp(d,i){
		$("#table-erd-current").val(d.name);
		$(".option-column-erd").remove();
		for(var i = 0 ; i < d.listColumn.length;i++){
			$("#column-erd-current").append($("<option></option>")
	                .attr("value",d.listColumn[i].name)
	                .attr("class","option-column-erd")
	                .text(d.listColumn[i].name));
		}
		$("#column-erd-current").change(function(){
			var value = $(this).val();
			if(value != null){
				$("#btn-delete-pro").prop("disabled",false);
				$('#form-new-property').hide();
				$('#form-btn-default').show();
			}
		}).change();
		$("#btn-add-pro").prop("disabled",false);
		d3.event.stopPropagation();
	
	}

	/* function : mouse_over_line
	 * details: to handle event mouse over on the line in ERD
	 * */
	function mouse_over_line(d){
		d3.select(this).attr('stroke',"red");
		d3.select("#"+d.table+d.column).attr("fill","red");
		d3.select("#"+d.referTbl+d.referColumn).attr("fill","red");
	}
	

	/* function : mouse_out_line
	 * details: to handle event mouse out on the line in ERD
	 * */
	function mouse_out_line(d){
		d3.select(this).attr("stroke", "#808080");	
		d3.select("#"+d.table+d.column).attr("fill","#000");
		d3.select("#"+d.referTbl+d.referColumn).attr("fill","#000");
	}
	

	/* function : move_onclick
	 * details: to handle event mouse click on the line in ERD
	 * */
	function mouse_onclick_line(d){
		$('#new-fk').hide();
		$('#default-fk').show();
		$("#fk-table-erd-current").val(d.table);
		$("#fk-refertable-erd-current").val(d.referTbl);
		$('#btn-delete-rela').prop('disabled',false);
		d3.event.stopPropagation();
	}
	

	/* function : onclick_btn_delete_pro
	 * details: to handle event click to  button id="btn_delete_pro" in ERD mode
	 * */
	function onclick_btn_delete_pro(){
		var table = $("#table-erd-current").val();
		var columns = $('#column-erd-current').val();
		for(var temp = 0 ; temp < columns.length ; temp++){
			for(var i = 0 ; i < RootNode.instance.childs.length;i++){
				if(table == RootNode.instance.childs[i].tableName){
					var tableCurrent = RootNode.instance.childs[i];
					for(var j = 0 ; j < tableCurrent.childs.length;j++){
						if(tableCurrent.childs[j].json == "column")
							if(tableCurrent.childs[j].columnName == columns[temp]){
								 RootNode.instance.childs[i].childs.splice(j,1);
								delete_data_d3(table,columns[temp]);
								break;
							}
					}
					break;
				}
			}
		}
		$("#btn-delete-pro").prop("disabled",true);
		
	}
	// this function will be called on the below function ( onclick_btn_delete_pro )
	function delete_data_d3(tableName,column){
		var data = d3.select("#"+tableName).data()[0];
		for(var i = 0 ; i < data.listColumn.length ; i++ ){
			if(data.listColumn[i].name == column){
				data.listColumn.splice(i,1);
			}
		}

		redraw_rect(data,tableName);
	}
	
	/* function : redraw
	 * details : to redraw rect2 when we delete or add a property
	 * */
	function redraw_rect(data,tableName){
		
		$(".option-column-erd").remove();
		for(var i = 0 ; i < data.listColumn.length;i++){
			$("#column-erd-current").append($("<option></option>")
	                .attr("value",data.listColumn[i].name)
	                .attr("class","option-column-erd")
	                .text(data.listColumn[i].name));
		}
		d3.selectAll(".text"+tableName).remove();
		d3.select("#rect2"+tableName).attr("height",data.listColumn.length*30);
		
		
		var i = parseInt(d3.select("#lined"+tableName).attr("i"));
		var k = parseInt(d3.select("#lined"+tableName).attr("k"));
		var gCurrent = d3.select("#"+tableName);
		
		
		d3.select("#lined"+tableName).remove();
		d3.selectAll(".liner"+tableName).remove();
		d3.selectAll(".rectxxx"+tableName).remove();
		draw_rect2(gCurrent,i,k,data);
		
	}
	
	
	/* function : onclick_btn_delete_rela
	 * details: to handle event click to  button id="btn_delete_rela" in ERD mode
	 * */
	function onclick_btn_delete_rela(){
		var d = d3.select("#line"+$('#fk-table-erd-current').val()+$('#fk-refertable-erd-current').val()).data()[0];
		for(var i = 0 ; i < listRelationship.length ; i ++){
			if(listRelationship[i].table == d.table && listRelationship[i].referTbl){
				listRelationship.splice(i,1);
			}
		}
		d3.select('#line'+d.table+d.referTbl).remove();
		$("#fk-table-erd-current").val(null);
		$("#fk-refertable-erd-current").val(null);
		var colId ;
		var tableCurrent ;
		for(var i = 0 ; i < RootNode.instance.childs.length;i++){
			if(d.referTbl == RootNode.instance.childs[i].tableName){
			    tableCurrent = RootNode.instance.childs[i];
				for(var j = 0 ; j < tableCurrent.childs.length;j++){
					if(tableCurrent.childs[j].json == "column")
						if(tableCurrent.childs[j].foreignKey == true){
							colId =  tableCurrent.childs[j].tempId;
							break;
						}
				}
				break;
			}
		}
		var relatedList = TablesList.findRelatedElements(tableCurrent, colId);
		InfoPanel.deleteRela(tableCurrent, colId, relatedList);
		$('#btn-delete-rela').prop('disabled',true);
	}
	
	/* function : onclick_btn_add_pro
	 * details: to handle event click to  button id="btn_add_pro" in ERD mode
	 * */
	function onclick_btn_add_pro(){
		$('#form-new-property').show();
		$('#form-btn-default').hide();
	}
	
	/* function : onclick_btn_ok_pro
	 * details: to handle event click to  button id="btn_ok_pro" in ERD mode
	 * */
	function onclick_btn_ok_pro(){
		var new_pro = $("#new-property").val();
		if(new_pro == ""){
			$("#new-property").focus();
		}
		else{
			var tableName = $('#table-erd-current').val();
			var data = d3.select("#"+tableName).data()[0];
			var column = {};
			column.name = $("#new-property").val().toUpperCase();
			column.type = 'varchar()';
			column.length = 0;
			column.primaryKey = false;
			column.notNull = false;
			column.unique = false;
			data.listColumn.push(column);
			redraw_rect(data,tableName);
			
			var simpleCol = {
					type: 			'nm',
					columnName: 	$("#new-property").val().toUpperCase(),
					dataType:		'varchar()',
					notNull: 		false,
					autoIncrement:	false,
				};
			var tableCurrent;
			for(var i = 0 ; i < RootNode.instance.childs.length;i++){
				if(tableName == RootNode.instance.childs[i].tableName){
				    tableCurrent = RootNode.instance.childs[i];
					break;
				}
			}
			Table.addColumn(tableCurrent,simpleCol);
		}
	}
	
	/* function : onclick_btn_cancel_pro
	 * details: to handle event click to  button id="btn_cancel_pro" in ERD mode
	 * */
	function onclick_btn_cancel_pro(){
		$('#form-new-property').hide();
		$('#form-btn-default').show();
	}
	
	/* function : onclick_btn_add_rela
	 * details: to handle event click to  button id="btn_add_rela" in ERD mode
	 * */
	function onclick_btn_add_rela(){
		$('#default-fk').hide();
		$('#new-fk').show();
		if($('#fk-table-erd-new').val()==null){
			for(var i = 0; i < listTable.length;i++){
				$('#fk-table-erd-new').append($("<option></option>")
		                .attr("value",listTable[i].name)
		                .text(listTable[i].name));
				$('#fk-refertable-erd-new').append($("<option></option>")
		                .attr("value",listTable[i].name)
		                .text(listTable[i].name));
			}
		}
	}

	/* function : onclick_ok_cancel_rela
	 * details: to handle event click to  button id="btn_ok_rela" in ERD mode
	 * */
	function onclick_btn_ok_rela(){
		 var en1 = $("#fk-table-erd-new").val();
		 var en2 = $("#fk-refertable-erd-new").val();
		 if(en1 == en2){
		 }
		 else {
			 var i=0;
			 for( i = 0 ; i < listRelationship.length ; i++){
				 if((listRelationship[i].table == en1 && listRelationship[i].referTbl==en2)
						 || (listRelationship[i].table == en2 && listRelationship[i].referTbl==en1)) 
					 break;
			 }
			 if(i == listRelationship.length){
				 var relationship = {};
				 relationship.table = en1;
				 relationship.referTbl = en2;
				 relationship.column = en1+en2+"_ID";
				 relationship.referColumn = en1+"_"+en2+"_ID";
				 listRelationship.push(relationship);
				 d3.selectAll(".line").remove();
				 drawLine(listRelationship);
				 var simpleCol = {
							type: 			'fk',
							columnName: 	en1+"_"+en2+"_ID",
							dataType:		'varchar()',
							notNull: 		false,
							autoIncrement:	false,
							rfTableName:	en1,
							rfColName:		 en1+"_"+en2+"_ID"
						};
					var tableCurrent;
					for(var i = 0 ; i < RootNode.instance.childs.length;i++){
						if(en2 == RootNode.instance.childs[i].tableName){
						    tableCurrent = RootNode.instance.childs[i];
							break;
						}
					}
					Table.addColumn(tableCurrent,simpleCol);
					console.log(RootNode.instance);
			 }
		 }
	}
	
	
	/* function : onclick_btn_cancel_rela
	 * details: to handle event click to  button id="btn_cancel_rela" in ERD mode
	 * */
	function onclick_btn_cancel_rela(){
		$('#new-fk').hide();
		$('#default-fk').show();
	}
	
	