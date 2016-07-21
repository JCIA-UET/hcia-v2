package cuong;

import uet.jcia.entities.Column;
import uet.jcia.entities.Table;
import uet.jcia.model.Inverser;

public class TestInverser {
    
    public static void main(String argv[]) {
        Inverser inverser = new Inverser();
        
        /*Table tbl = new Table();
        tbl.setRefXml("src/main/resources/sample-data/Address.hbm.xml");
        tbl.setTempId("5");
        tbl.setTableName("ADDDRRRESS");
        inverser.updateHbmClass(tbl);
        
        /*Column col = new Column();
        col.setRefXml("src/main/resources/sample-data/Address.hbm.xml");
        col.setClassName("cuong.data.sample.Address");
        col.setMappingName("lastName");
        col.setLength("10");
        col.setName("last_name");
        col.setType("string");
        col.setNotNull(true);
        inverser.updateHbmProperty(col);*/
        
        /*col.setMappingName("addressId");
        col.setNotNull(true);
        col.setAutoIncrement(true);
        col.setName("address____id");
        col.setType("string123");
        col.setLength("100");
        inverser.updateHbmId(col);*/
    }
}
