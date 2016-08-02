function Relationship() {};

Relationship.mto = {};
Relationship.mto.instances = [];
Relationship.otm = {};
Relationship.otm.instances = [];

Relationship.loadAll = function() {
	if(TablesList.instances == null) {
		console.log("No data");
	}
	else {
		for(var i = 0; i < TablesList.instances.length; i++) {
			for(var j = 0; j < TablesList.instances[i].childs.length; j++) {
				if(TablesList.instances[i].childs[j].json == "mto") {
					var mtoInstance = TablesList.instances[i].childs[j];
					mtoInstance.tableName = TablesList.instances[i].tableName;
					
					Relationship.mto.instances.push(mtoInstance);
					console.log("Added one Many-to-One relationship to array");
				}
				else if(TablesList.instances[i].childs[j].json == "otm") {
					var otmInstance = TablesList.instances[i].childs[j];
					mtoInstance.tableName = TablesList.instances[i].tableName;
					
					Relationship.otm.instances.push(mtoInstance);
					console.log("Added one One-to-Many relationship to array");
				}
			}
		}
	}
};