$(document).ready(
  function() {
   var margin = {
     top: -5,
     right: -40,
     bottom: -5,
     left: -5
    },
    width = 960 - margin.left - margin.right,
    height = 610 - margin.top - margin.bottom;
   
   var tbl1 = {
    "x": 50,
    "y": 50,
    "name": "table1",
    "listColumn": [{
     "name": "column1",
     "type": "int"
    }, {
     "name": "column2",
     "type": "varchar()"
    }]
   };
   var tbl2 = {
    "x": 50,
    "y": 50,
    "name": "table2",
    "listColumn": [{
     "name": "column3",
     "type": "int"
    }, {
     "name": "column4",
     "type": "varchar()"
    }]
   };
   var tbl3 = {
    "x": 50,
    "y": 50,
    "name": "table3",
    "listColumn": [{
     "name": "column3",
     "type": "int"
    },
    {
        "name": "column5555555555555",
        "type": "int"
       }, {
     "name": "column4",
     "type": "varchar()"
    }]
   };
   var listRelationship = [{
    "table": "table1",
    "column": "column1",
    "referTbl": "table2",
    "referColumn": "column3",
    "type": "onetomany"
   }, {
    "table": "table1",
    "column": "column1",
    "referTbl": "table3",
    "referColumn": "column3",
    "type": "onetomany"
   }];

   var listTable = [];
   listTable.push(tbl1, tbl2, tbl3);

   var drag = d3.behavior
			    .drag()
			    .on('dragstart', function() {
			     d3.event.sourceEvent.stopPropagation();
			     console.log("xxx")
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
//			             if (y1 < y2 && x1 + 100 > x2) {
//			            	  x1 = table1.x + 50 + parseInt(d3.select("#rect" + attrTableOne).attr("x"));
//			            	  y1 = table1.y + 100 + parseInt(d3.select("#rect" + attrTableOne).attr("y"));
//				              x2 = table2.x + parseInt(d3.select("#rect" + attrTableTwo).attr("x")) + 50;
//				              y2 = table2.y + parseInt(d3.select("#rect" + attrTableTwo).attr("y"));
//			             }
			
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
					      .attr("stroke", "#87CEFA").attr("stroke-width", 3);
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
					    	  						.text(d.listColumn[j].name+"("+d.listColumn[j].type+")")
					    	  						 .attr("y", "0.5em")
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
	   var line = gSecond
	    .selectAll("line")
	    .data(listRelationship)
	    .enter()
	    .append("line")
	    .each(function() {
	      var data = d3.select(this).data()[0];
	      var pointOne = d3.select("#" + data.table).data()[0];
	      var pointTwo = d3.select("#" + data.referTbl).data()[0];
	      
	      var lCurrent = d3.select(this).attr('x1',pointOne.x +	parseInt( d3.select("#rect2" + data.table).attr("width")))
								        .attr("y1",pointOne.y + d3.select("#rect2" + data.table).attr("height") / 2)
								        .attr( "x2",pointTwo.x + parseInt(d3.select("#rect" + data.referTbl).attr("x")))
								        .attr("y2",pointTwo.y + d3.select("#rect2" + data.referTbl).attr("height") / 2)
								       .attr("class", "line")
								       .attr("marker-end", "url(#arrowhead)")
								       .attr("tableone", data.table)
								       .attr("tabletwo", data.referTbl)
								       .attr("stroke-width", 2).attr("stroke", "#808080");
	      
	     });
  });

function maxHeight(d){
	var max = 0;
	for(var i = 0 ; i < d.listColumn.length ; i++){
		if(d.listColumn[i].name.length > max){
			max = d.listColumn[i].name.length;
		}
	}
	return max * 8 + 56;
}
