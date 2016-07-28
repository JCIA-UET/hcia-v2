package loc;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import uet.jcia.entities.TableNode;
import uet.jcia.model.CoreAPI;

public class JSONConvertTest {
	public static void main(String[] args){
		String sz = "{\"json\":\"table\",\"childs\":[{\"json\":\"pk\",\"childs\":null,\"tempId\":1,\"javaName\":\"addressId\",\"columnName\":\"ADDRESS_ID\",\"dataType\":\"INTEGER\",\"length\":0,\"primaryKey\":true,\"notNull\":true,\"foreignKey\":false,\"autoIncrement\":false},{\"json\":\"column\",\"childs\":null,\"tempId\":2,\"javaName\":\"firstName\",\"columnName\":\"ds\",\"dataType\":\"VARCHAR\",\"length\":\"0\",\"primaryKey\":false,\"notNull\":false,\"foreignKey\":false},{\"json\":\"column\",\"childs\":null,\"tempId\":3,\"javaName\":\"lastName\",\"columnName\":\"LASTNAME\",\"dataType\":\"VARCHAR\",\"length\":0,\"primaryKey\":false,\"notNull\":false,\"foreignKey\":false},{\"json\":\"column\",\"childs\":null,\"tempId\":4,\"javaName\":\"address\",\"columnName\":\"ADDRESS\",\"dataType\":\"VARCHAR\",\"length\":0,\"primaryKey\":false,\"notNull\":false,\"foreignKey\":false},{\"json\":\"mto\",\"childs\":null,\"tempId\":5,\"javaName\":\"customer\",\"referTable\":{\"json\":\"table\",\"childs\":null,\"tempId\":12,\"javaName\":null,\"className\":\"cuong.data.sample.Customer\",\"tableName\":\"CUSTOMER\",\"xmlPath\":\"C:/Users/dinht_000/workspace/hcia-v2/temp/upload-src-20160728-211152/vnu/jcia/Customer.hbm.xml\"},\"type\":\"Many-to-One\",\"foreignKey\":{\"json\":\"column\",\"childs\":null,\"tempId\":13,\"javaName\":null,\"columnName\":\"CUSTOMER_ID\",\"dataType\":\"INTEGER\",\"length\":0,\"primaryKey\":false,\"notNull\":true,\"foreignKey\":true},\"referColumn\":{\"json\":\"pk\",\"childs\":null,\"tempId\":13,\"javaName\":\"customerId\",\"columnName\":\"CUSTOMER_ID\",\"dataType\":\"INTEGER\",\"length\":0,\"primaryKey\":true,\"notNull\":true,\"foreignKey\":false,\"autoIncrement\":false}},{\"json\":\"column\",\"childs\":null,\"tempId\":13,\"javaName\":null,\"columnName\":\"CUSTOMER_ID\",\"dataType\":\"INTEGER\",\"length\":0,\"primaryKey\":false,\"notNull\":true,\"foreignKey\":true}],\"tempId\":6,\"javaName\":null,\"className\":\"cuong.data.sample.Address\",\"tableName\":\"ADDRESS\",\"xmlPath\":\"C:/Users/dinht_000/workspace/hcia-v2/temp/upload-src-20160728-211152/vnu/jcia/Address.hbm.xml\"}";
		try {
			ObjectMapper mapper = new ObjectMapper();
			//ColumnNode table = mapper.readValue(col, ColumnNode.class);
			TableNode table1 = mapper.readValue(sz, TableNode.class);
			System.out.println(table1);
			
			CoreAPI api = new CoreAPI();
			api.updateData(table1);
			System.out.println(table1.getXmlPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
