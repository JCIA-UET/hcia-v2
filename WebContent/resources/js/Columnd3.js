function Columnd3(name, type , length , primaryKey, notNull,foreignKey, unique , autoIncrement){
	this.name = name;
	this.typedata = type;
	this.length = length;
	this.primaryKey = primaryKey;
	this.notNull = notNull;
	this.unique = unique;
	this.foreignKey = foreignKey;
	this.autoIncrement = autoIncrement;
	
	this.draw = function(gCurrent , i , k ,j,tableName ,width){
		var col = gCurrent.append("text")
			.text(this.name + " " + this.typedata + "(" + this.length + ")")
			 .attr("y", "0.5em")
			 .attr("class", "text"+tableName)
			 .attr("id",tableName+this.name)
		       .attr("transform","translate(" + [22+ i * 200,35+j*22+k*250] + ")")
		       .attr("text-anchor", "left")
		       .attr("font-weight", 1000).attr("font-family","Helvetica")
		       .attr( "fill", "#000")
		        .attr("font-size","14px")
		       .attr("stroke", "none")
		       .attr("pointer-events","none");
	  if(this.primaryKey == true && this.foreignKey==true){
	  	  var symbol = gCurrent.append("text")
		     .attr("x",i*200 + 5)
		     .attr("y",35+j*22+k*250+6)
		     .attr("font-family","FontAwesome")
		     .attr('font-size', function(d) { return '12px';} )
		     .attr('fill','#EA4335')
		     .text(function(d) { return '\uf084'; }); 	
	  }
	  else if(this.primaryKey==true){
		  var symbol = gCurrent.append("text")
		     .attr("x",i*200 + 5)
		     .attr("y",35+j*22+k*250+6)
		     .attr("font-family","FontAwesome")
		     .attr('font-size', function(d) { return '12px';} )
		     .attr('fill','#FBBC05')
		     .text(function(d) { return '\uf084'; });   
	  }else if( this.foreignKey == true){
		  var symbol = gCurrent.append("text")
		     .attr("x",i*200 + 5)
		     .attr("y",35+j*22+k*250+6)
		     .attr("font-family","FontAwesome")
		     .attr('font-size', function(d) { return '12px';} )
		     .attr('fill','#EA4335')
		     .text(function(d) { return '\uf005'; });  
	  }
	  else if( this.notNull == true){
		  var symbol = gCurrent.append("text")
		     .attr("x",i*200 + 5)
		     .attr("y",35+j*22+k*250+6)
		     .attr("font-family","FontAwesome")
		     .attr('font-size', function(d) { return '12px';} )
		     .attr('fill','#2980B9')
		     .text(function(d) { return '\uf005'; });  
	  }
	  else if( this.notNull == false){
		  var symbol = gCurrent.append("text")
		     .attr("x",i*200 + 5)
		     .attr("y",35+j*22+k*250+6)
		     .attr("font-family","FontAwesome")
		     .attr('font-size', function(d) { return '12px';} )
		     .attr('fill','#2980B9')
		     .text(function(d) { return '\uf006'; });  
	  }

  	}
	
}