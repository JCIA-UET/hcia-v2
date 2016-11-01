/**
 * Đối tượng chứa danh sách các mối quan hệ
 * @returns
 */
function Relationship() {};

Relationship.mto = {};
Relationship.mto.instances = [];
Relationship.otm = {};
Relationship.otm.instances = [];


/**
 * Tách và lưu thông tin của các Quan Hệ từ TablesList
 * 
 * @param No param.
 * 
 * @return No return.
 */
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
				}
				else if(TablesList.instances[i].childs[j].json == "otm") {
					var otmInstance = TablesList.instances[i].childs[j];
					mtoInstance.tableName = TablesList.instances[i].tableName;
					
					Relationship.otm.instances.push(mtoInstance);
				}
			}
		}
	}
};