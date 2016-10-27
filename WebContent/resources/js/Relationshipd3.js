function Relationshipd3(type , table , column , referTbl, referColumn){
	this.type = type;
	this.table = table; 
	this.column = column; 
	this.referTbl = referTbl;
	this.referColumn = referColumn;
	
	this.draw = function(lCurrent){
		lCurrent.attr("points",this.calculate())
			.attr("class", "line")
		       .attr('id',"line"+this.table+this.referTbl)
		       .attr("marker-start", "url(#oneMar)")
		       .attr("marker-end", "url(#arrowhead)")
		       .attr("style", "fill:none;stroke-width:2;")
		       .attr("stroke-dasharray", "10,10")
		       .attr("stroke", "#808080")
		       .on("mouseover",mouse_over_line)
		       .on("mouseout",mouse_out_line)
		       .on("click",mouse_onclick_line)
		       ;
	}
	this.calculate = function(){
		var result = "";
		var point1 = this.findPointStick(table, referTbl);
		var point2 = this.findPointStick(referTbl, table);
		
		if(table == referTbl){
			var ps= this.checkps(table,table);
			var data = d3.select('#'+table).data()[0];
			
			var x = data.x + parseInt(d3.select('#rect'+table).attr('x'));
			var y = data.y + parseInt(d3.select('#rect'+table).attr('y'));
			
			var height = data.findheight();
			var width = data.findwidth()+10;
			
			result = (x+width)+"," + (y+height/ps.quantity*ps.position) + " " + (x+width+50)+"," + (y+height/ps.quantity*ps.position)
						+ " " + (x+width+50)+"," + (y+height/ps.quantity*(ps.position+1)) + " "+(x+width)+"," + (y+height/ps.quantity*(ps.position+1));
			
		}
		else if(this.checkps(table, referTbl).incence == 'top' || this.checkps(table, referTbl).incence == 'bottom'  ){
			result = point1.x +","+ point1.y + " " + point1.x+','+ (point1.y+point2.y)/2 +" " +point2.x + "," +(point1.y+ point2.y)/2 + " " + point2.x +","+point2.y; 
		}
		else{
			result = (point1.x) +","+ point1.y + " " + (point1.x+point2.x)/2 +","+ point1.y + " "+ (point1.x+point2.x)/2  +","+ point2.y + " "+ point2.x+','+point2.y;
		}
		
		return result;
	}
	this.findPointStick = function(table , tableStick){
		var ps = this.checkps(table , tableStick);
		var data = d3.select('#'+table).data()[0];
	
		var x = data.x + parseInt(d3.select('#rect'+table).attr('x'));
		var y = data.y + parseInt(d3.select('#rect'+table).attr('y'));
		
		var height = data.findheight();
		var width = data.findwidth()+10;		
		var result = {'x': 0 , 'y':0};
		
		if(ps.incence == 'top'){
			//console.log(width, ps.quantity,ps.position);
			result.x = x + width/ps.quantity*ps.position;
			result.y = y;
		}
		else if(ps.incence == 'bottom'){
			result.x = x + width/ps.quantity*ps.position;
			result.y = y + height;
		}
		else if(ps.incence == 'left'){
			result.x = x ;
			result.y = y + height/ps.quantity*ps.position;
		}
		else{
			result.x = x + width;
			result.y = y + height/ps.quantity*ps.position;
		}
		//console.log(result);
		return result;
	}
	
	this.checkps = function(table, findTable){
		var data = d3.select("#" + table.toUpperCase()).data()[0];
		
		
		for( var i = 0 ; i < data.bottom.length ; i++){
			if(findTable == data.bottom[i]){
				return {'incence':'bottom', 'position':i+1  , 'quantity': data.bottom.length+1};
			}
		}
		for( var i = 0 ; i < data.left.length ; i++){
			if(findTable == data.left[i]){
				return {'incence':'left', 'position':i+1,  'quantity': data.left.length+1};
			}
		}
		for( var i = 0 ; i < data.right.length ; i++){
			if(findTable == data.right[i]){
				return {'incence':'right', 'position':i+1  , 'quantity': data.right.length+1};
			}
		}
		for( var i = 0 ; i < data.top.length ; i++){
			if(findTable == data.top[i]){
				return {'incence':'top', 'position':i+1 , 'quantity': data.top.length+1};
			}
		}
	}
}