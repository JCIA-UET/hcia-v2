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
	var json = RootNode.instance;
	var d3Obj = convert(RootNode.instance);
    listRelationship = d3Obj.relationships;
    listTable = d3Obj.tables;
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
				         var dates = d3.selectAll(".line").each(
				           function() {
				            var attrTableOne = d3.select(this).attr("tableone");
				            var attrTableTwo = d3.select(this).attr("tabletwo");
				
				            var table1 = d3.select("#" + attrTableOne).data()[0];
				            var table2 = d3.select("#" + attrTableTwo).data()[0];
				
				            var x1 = parseInt(d3.select(this).attr("x1"));
				            var y1 = parseInt(d3.select(this).attr("y1"));
				            var x2 = parseInt(d3.select(this).attr("x2"));
				            var y2 = parseInt(d3.select(this).attr("y2"));
				
				            if (d.name == attrTableOne) {
					             x1 = table1.x + parseInt(d3.select("#rect2" + attrTableOne).attr("width")) 
					             				+ parseInt(d3.select("#rect2" + attrTableOne).attr("x"));
					             y1 = table1.y + parseInt(d3.select("#rect2" + attrTableOne).attr("height"))/2 
					             				+ parseInt(d3.select("#rect2" + attrTableOne).attr("y"));
					             x2 = table2.x + parseInt(d3.select("#rect2" + attrTableTwo).attr("x"));
				
				             if (x1 > x2) {
					              x1 = x1 - parseInt(d3.select("#rect2" + attrTableOne).attr("width")) ;
					              x2 = x2 + parseInt(d3.select("#rect2" + attrTableTwo).attr("width")) ;
				             }
//				             if (y1 < y2 && x1 + 100 > x2) {
//				            	  x1 = table1.x + 50 + parseInt(d3.select("#rect" + attrTableOne).attr("x"));
//				            	  y1 = table1.y + 100 + parseInt(d3.select("#rect" + attrTableOne).attr("y"));
//					              x2 = table2.x + parseInt(d3.select("#rect" + attrTableTwo).attr("x")) + 50;
//					              y2 = table2.y + parseInt(d3.select("#rect" + attrTableTwo).attr("y"));
//				             }
				
				            } else if (d.name == attrTableTwo) {
				            	x2 = table2.x + parseInt(d3.select("#rect2" + attrTableTwo).attr("x"));
				            	y2 = table2.y + parseInt(d3.select("#rect2" + attrTableTwo).attr("y")) 
				            				  + parseInt(d3.select("#rect2" + attrTableTwo).attr("height"))/2 ;
				            	x1 = table1.x + parseInt(d3.select("#rect" + attrTableOne).attr("width")) 
				            				+ parseInt(d3.select("#rect" + attrTableOne).attr("x"));
					             if (x2 < x1) {
					              x2 = x2 + parseInt(d3.select("#rect2" + attrTableTwo).attr("width")) ;
					              x1 = x1 - parseInt(d3.select("#rect2" + attrTableOne).attr("width")) ;
					             }
				
				            }
				            d3.select(this).attr("x1",x1)
				            			   .attr("y1",y1)
				            			   .attr("x2",x2)
				            			   .attr("y2",y2);
				            })
				
				         return "translate(" + [d.x,d.y] + ")";
				         });
				     });
	   var zoom = d3.behavior.zoom().scaleExtent([0.4, 3]).on("zoom", zoomed);

	   var svg = d3.select("#ERD").append("svg")
	   							  .attr("width",width)
	   							  .attr("height", height)
	   							  .attr("transform","translate(" + 0 + "," + 0 + ")")
	   							  .style("background-color", "#F0F8FF");
	   var gFirst = svg.append("g").attr("transform","translate(" + margin.left + "," + margin.right + ")").call(zoom);
		var marker =   gFirst.append("defs").append("marker")
		   .attr("id", "arrowhead")
		   .attr("refX",   3) /*must be smarter way to calculate shift*/
		   .attr("refY", 2)
		   .attr("markerWidth", 20)
		   .attr("markerHeight", 20)
		   .attr("orient", "auto")
		   .append("path")
		   .attr("d", "M 0,0 V 4 L6,2 Z");
	   
		var rect2 = gFirst.append("rect")
	   					 .attr("width", width)
	   					 .attr("height", height)
	   					 .style("fill", "#FFFFFF");
	   var gSecond = gFirst.append("g");
	 
	   function zoomed() {
	    gSecond.attr("transform", "translate(" + d3.event.translate + ")scale(" + d3.event.scale + ")");
	   }

	   var i = 0;
	   var face = gSecond.selectAll("g")
	    				 .data(listTable)
	    				 .enter()
	    				 .append("g")
	    				 .each(function(d) {
						      var name = d.name;
						      var listColumn = d.listColumn;
						      var gCurrent = d3.select(this).attr("class", "jifj")
						      								.attr("transform","translate(" + d.x + "," + d.y + ")")
						      								.attr("id", name)
						      								.call(drag);
						      var table = gCurrent.append("rect")
						       .attr("id", 'rect' + name)
						       .attr("class", 'rectxxx')
						       .attr("x", i * 200)
						       .attr("y","0")
						       .attr("width",maxHeight(d))
						       .attr("height",25 )
						       .attr("fill","#98BFDA").attr("fill-opacity", 1)
						       .attr("stroke", "#87CEFA").attr("stroke-width", 3);
						      var table2 = gCurrent.append("rect")
						      .attr("id", 'rect2' + name)
						      .attr("class", 'rectxxx')
						      .attr("x", i * 200)
						      .attr("y","25")
						      .attr("width",maxHeight(d))
						      .attr("height", d.listColumn.length*20)
						      .attr("fill","white").attr("fill-opacity", .5)
						      .attr("stroke", "#87CEFA").attr("stroke-width", 3)
						      .on('mouseover', moveover_table)
						      .on('mouseout', moveout_table)
						      .on('click',mouse_onclick_table);
						      var texts = gCurrent
						       .append("text")
						       .text(d.name)
						       .attr("y", "0.5em")
						       .attr("transform","translate(" + [50 + i * 200,10] + ")")
						       .attr("text-anchor", "middle")
						       .attr("font-weight", 700).attr("font-family","Helvetica")
						       .attr("font-size","15px")
						       .attr("fill", "#000")
						       .attr("stroke", "none")
						       .attr("pointer-events","none");
						      for(var j = 0 ; j < d.listColumn.length;j++){
						    	  var textCol = gCurrent.append("text")
						    	  						.text(d.listColumn[j].name )
						    	  						 .attr("y", "0.5em")
						    	  						 .attr("class", "text"+name)
						    	  						 .attr("id",d.name+d.listColumn[j].name)
						    						       .attr("transform","translate(" + [5 + i * 200,31+j*20] + ")")
						    						       .attr("text-anchor", "start")
						    						       .attr("font-weight", 500).attr("font-family","Helvetica")
						    						       .attr( "fill", "#000")
						    						        .attr("font-size","12px")
						    						       .attr("stroke", "none")
						    						       .attr("pointer-events","none");
						      }
						      i++;
						     })
		   var line = gSecond.selectAll("line")
		    .data(listRelationship)
		    .enter()
		    .append("line")
		    .each(function(d) {
		      var data = d ;
		      
		      var pointOne = d3.select("#" + data.table).data()[0];
		      var pointTwo = d3.select("#" + data.referTbl).data()[0];
		      
		      var lCurrent = d3.select(this).attr('x1',pointOne.x +	parseInt( d3.select("#rect2" + data.table).attr("width")))
									        .attr("y1",pointOne.y + d3.select("#rect2" + data.table).attr("height") / 2)
									        .attr( "x2",pointTwo.x + parseInt(d3.select("#rect" + data.referTbl).attr("x")))
									        .attr("y2",pointTwo.y + d3.select("#rect2" + data.referTbl).attr("height") / 2)
									       .attr("class", "line")
									       .attr('id',"line"+d.table+d.referTbl)
									       .attr("marker-end", "url(#arrowhead)")
									       .attr("tableone", data.table)
									       .attr("tabletwo", data.referTbl)
									       .attr("stroke-width", 4)
									       .attr("stroke", "#808080")
									       .on("mouseover",mouse_over_line)
									       .on("mouseout",mouse_out_line)
									       .on("click",mouse_onclick_line)
									       ;
		      
		     });
	   
	   		
	  };

	/*function: maxHeight
	 * details: to return width of a rect2
	 * param : d - data of gCurrent
	 * */
	function maxHeight(d){
		var max = 0;
		for(var i = 0 ; i < d.listColumn.length ; i++){
			if(d.listColumn[i].name.length  > max){
				max = d.listColumn[i].name.length ;
			}
		}
		return max * 8 + 15;
	}
	
	/* function : convert
	 * details : to convert object Json from server to object json ERD
	 * param : datajson - object json from server
	 * */
	function convert(datajson){
		var result = {"tables":[], "relationships":[]};
		for(var i = 0 ; i<datajson.childs.length; i++ ){
			var currentTable = datajson.childs[i] ;
			var table = {"x":50, "y":50 , "listColumn":[]};
			table.name = currentTable.tableName;
			for(var j = 0 ; j < currentTable.childs.length ; j++){
				var currentElement = currentTable.childs[j];
				if ((currentElement.json == "pk" || currentElement.json == "column") && currentElement.foreignKey == false ){
					var column = {};
					column.name = currentElement.columnName;
					column.type = currentElement.dataType;
					column.length = currentElement.length;
					column.primaryKey = currentElement.primaryKey;
					column.notNull = currentElement.notNull;
					column.unique = currentElement.unique;
					if(currentElement.json == "pk"){
						column.autoIncrement = currentElement.autoIncrement;
					}
					table.listColumn.push(column);
				}
				if(currentElement.json == "otm"){
					var relationship = {};
					relationship.table = currentTable.tableName;
					relationship.column = currentElement.foreignKey.columnName;
					relationship.referTbl = currentElement.referTable.tableName;
					relationship.referColumn = currentElement.foreignKey.columnName;
					result.relationships.push(relationship);
				}
			}
			result.tables.push(table);
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
			if(value != null)
			$("#btn-delete-pro").prop("disabled",false);
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
		d3.select("#rect2"+tableName).attr("height",data.listColumn.length*20);
		for(var i = 0 ; i < data.listColumn.length ; i++){
			d3.select("#"+tableName).append("text")
				.text(data.listColumn[i].name )
					 .attr("y", "0.5em")
					 .attr("class", "text"+tableName)
					 .attr("id",data.name+data.listColumn[i].name)
			       .attr("transform","translate(" + [5 + parseInt(d3.select("#rect2"+tableName).attr("x")),31+i*20] + ")")
			       .attr("text-anchor", "start")
			       .attr("font-weight", 500).attr("font-family","Helvetica")
			       .attr( "fill", "#000")
			        .attr("font-size","12px")
			       .attr("stroke", "none")
			       .attr("pointer-events","none");
		}
	}
	
	
	/* function : onclick_btn_delete_rela
	 * details: to handle event click to  button id="btn_delete_rela" in ERD mode
	 * */
	function onclick_btn_delete_rela(){
		var d = d3.select("#line"+$('#fk-table-erd-current').val()+$('#fk-refertable-erd-current').val()).data()[0];
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
			console.log(RootNode.instance);
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
		
	}
	
	
	/* function : onclick_btn_cancel_rela
	 * details: to handle event click to  button id="btn_cancel_rela" in ERD mode
	 * */
	function onclick_btn_cancel_rela(){
		$('#new-fk').hide();
		$('#default-fk').show();
	}
	
	