function Tabled3(tableName , listColumn){
	this.x = 50 ;
	this.y = 50 ;
	
	this.tableName = tableName;
	this.listColumn = listColumn;
	
	this.relative = [];
	this.top = [];
	this.bottom = [];
	this.right = [];
	this.left = [];
	
	this.draw = function(gCurrent, i ,k){

		var table = gCurrent.append("rect")
	       .attr("id", 'rect' + this.tableName)
	       .attr("class", 'rectxxx')
	       .attr("x", i * 200)
	       .attr("y",k*250-10)
	       .attr("width",this.findwidth()+10)
	       .attr("height",35 )
	       .attr("fill","#98BFDA").attr("fill-opacity", 1)
	       .attr("stroke", "#98BFDA").attr("stroke-width", 1)
	       .on('mouseover', moveover_table)
	      .on('mouseout', moveout_table)
	        .on('click',mouse_onclick_table);
	      var table2 = gCurrent.append("rect")
	      .attr("id", 'rect2' + this.tableName)
	      .attr("class", 'rectxxx')
	      .attr("x", i * 200)
	      .attr("y",25+k*250)
	      .attr("width", this.findwidth()+10)
	      .attr("height", this.listColumn.length*22)
	      .attr("fill","white").attr("fill-opacity", 1)
	      .attr("stroke", "#87CEFA").attr("stroke-width", 1)
	      .on('mouseover', moveover_table)
	      .on('mouseout', moveout_table)
	      .on('click',mouse_onclick_table);
	      var icontable = gCurrent.append("text")
		     .attr("x",i*200 + 5)
		     .attr("y",15+k*250)
		     .attr("font-family","FontAwesome")
		     .attr('font-size', function(d) { return '22px';} )
		     .attr('fill','#555')
		     .text(function(d) { return '\uf0ce'; }); 
	      var texts = gCurrent
	       .append("text")
	       .text(this.tableName)
	       .attr("y", "0.5em")
	       .attr("transform","translate(" + [30+ i * 200,5+k*250] + ")")
	       .attr("text-anchor", "left")
	       .attr("font-weight", 700).attr("font-family","Helvetica")
	       .attr("font-size","18px")
	       .attr("fill", "#000")
	       .attr("stroke", "none")
	       .attr("pointer-events","none");
	      this.drawColumn(gCurrent,i,k);
	};
	
	this.findwidth = function(){
		var list = this.listColumn; 
		var n = list.length;
		var max = this.tableName.length;
		for(var i = 0 ; i < n ; i++){
			if((list[i].name.length + list[i].typedata.length) > max){
				max = list[i].typedata.length + list[i].name.length;
			}
		}
		return max*9 +30 ;
	}
	this.findheight = function(){
		return 35 + this.listColumn.length*22;
	}
	
	this.drawColumn = function(gCurrent,i,k){
		for(var j = 0 ; j < this.listColumn.length ; j++){
			this.listColumn[j].draw(gCurrent,i,k, j,this.tableName,this.findwidth());
		}
	}
	
	this.calculateAllRelative = function(){
		this.top = [];
		this.bottom = [];
		this.left = [];
		this.right = [];
		for(var i = 0 ; i < this.relative.length ; i++){
			if(this.tableName == this.relative[i]){
				this.right.splice(i/2,0,this.tableName);
				this.right.splice(i/2,0,this.tableName);
			}
			else{
				this.calculateOneRelative(this.relative[i]);
			}
		}
	}
	
	this.calculateOneRelative = function(tblName){
		var x1 = this.x + parseInt(d3.select('#rect'+this.tableName).attr('x'));
		var y1 = this.y + parseInt(d3.select('#rect'+ this.tableName).attr('y'));
		var width1 = this.findwidth();
		var height1 = this.findheight();
		
		var dataTbl2 = d3.select('#'+tblName).data()[0];
		
		var x2 = dataTbl2.x + parseInt(d3.select('#rect'+tblName).attr('x'));
		var y2 = dataTbl2.y + parseInt(d3.select('#rect'+tblName).attr('y'));
		var width2 = dataTbl2.findwidth();
		var height2 = dataTbl2.findheight();
		
		if((y2+height2) < y1){
			if(this.top.length == 0){
				this.top.push(tblName);
			}
			else if(x2 <= (d3.select('#'+this.top[0]).data()[0].x 
					+ parseInt(d3.select('#rect'+this.top[0]).attr('x')))){
				this.top.splice(0,0,tblName);
			}
			else if(x2 > (d3.select('#'+this.top[0]).data()[0].x 
					+ parseInt(d3.select('#rect'+this.top[0]).attr('x'))) && this.top.length == 1){
				this.top.push(tblName);
			}
			else{
				var i = 0;
				for( i = 0 ; i < this.top.length-1; i++){
					if(x2 > (d3.select('#'+this.top[i]).data()[0].x + parseInt(d3.select('#rect'+this.top[i]).attr('x')))
					&& x2 < (d3.select('#'+this.top[i+1]).data()[0].x + parseInt(d3.select('#rect'+this.top[i+1]).attr('x')))){
						this.top.splice(i+1,0,tblName);
						break;
					}
					
				}
				if(i == this.top.length -1 ){
					this.top.push(tblName);
				}
			}
		}
		else if(y2 > (y1 +height1) ){
			if(this.bottom.length == 0){
				this.bottom.push(tblName);
			}
			else if(x2 <= (d3.select('#'+this.bottom[0]).data()[0].x 
					+ parseInt(d3.select('#rect'+this.bottom[0]).attr('x')))){
				this.bottom.splice(0,0,tblName);
			}
			else if(x2 > (d3.select('#'+this.bottom[0]).data()[0].x 
					+ parseInt(d3.select('#rect'+this.bottom[0]).attr('x'))) && this.bottom.length == 1){
				this.bottom.push(tblName);
			}
			else{
				var i = 0;
				for( i = 0 ; i < this.bottom.length-1; i++){
					if(x2 > (d3.select('#'+this.bottom[i]).data()[0].x + parseInt(d3.select('#rect'+this.bottom[i]).attr('x')))
					&& x2 < (d3.select('#'+this.bottom[i+1]).data()[0].x + parseInt(d3.select('#rect'+this.bottom[i+1]).attr('x')))){
						this.bottom.splice(i+1,0,tblName);
						break;
					}
					
				}
				if(i == this.bottom.length -1 ){
					this.bottom.push(tblName);
				}
			}
		}
		else{
			/*thao tác trên this.left*/
			if(x2 < x1 ){
				if(this.left.length == 0){
					this.left.push(tblName);
				}
				else if(y2 <= (d3.select('#'+this.left[0]).data()[0].y 
					+ parseInt(d3.select('#rect'+this.left[0]).attr('y')))){
					this.left.splice(0,0,tblName);
				}
				else if(y2 > (d3.select('#'+this.left[0]).data()[0].y
						+ parseInt(d3.select('#rect'+this.left[0]).attr('y'))) && this.left.length == 1){
					this.left.push(tblName);
				}
				else{
					var i = 0;
					for( i = 0 ; i < this.left.length-1; i++){
						if(y2 > (d3.select('#'+this.left[i]).data()[0].y + parseInt(d3.select('#rect'+this.left[i]).attr('y')))
						&& y2 < (d3.select('#'+this.left[i+1]).data()[0].y + parseInt(d3.select('#rect'+this.left[i+1]).attr('y')))){
							this.left.splice(i+1,0,tblName);
							break;
						}
						
					}
					if(i == this.left.length -1 ){
						this.left.push(tblName);
					}
				}
				
			}
			/*thao tác trên this.right*/
			else{
				if(this.right.length == 0){
					this.right.push(tblName);
				}
				else if(y2 <= (d3.select('#'+this.right[0]).data()[0].y 
					+ parseInt(d3.select('#rect'+this.right[0]).attr('y')))){
					this.right.splice(0,0,tblName);
				}
				else if(y2 > (d3.select('#'+this.right[0]).data()[0].y
						+ parseInt(d3.select('#rect'+this.right[0]).attr('y'))) && this.right.length == 1){
					this.right.push(tblName);
				}
				else{
					var i = 0;
					for( i = 0 ; i < this.right.length-1; i++){
						if(y2 > (d3.select('#'+this.right[i]).data()[0].y + parseInt(d3.select('#rect'+this.right[i]).attr('y')))
						&& y2 < (d3.select('#'+this.right[i+1]).data()[0].y + parseInt(d3.select('#rect'+this.right[i+1]).attr('y')))){
							this.right.splice(i+1,0,tblName);
							break;
						}
						
					}
					if(i == this.right.length -1 ){
						this.right.push(tblName);
					}
				}
			}
		}
		
		
	}
	
}