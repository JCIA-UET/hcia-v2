package cuong;

import java.util.ArrayList;
import java.util.List;

import uet.jcia.model.FileManager;

public class TestFileManager {

    public static void main(String[] args) {
        FileManager fm = new FileManager();
        List<String> resultList = new ArrayList<>();

        fm.findFiles(
                "I:/Workspace/hcia-v2/temp/",
                ".*\\.xml", resultList);
        
        System.out.println(resultList);
        
        
        /*List<Table> tblList = FileManager
                .readTables("I:/Workspace/hcia-v2/temp/temp-data-20160719-141218");
        for (Table t : tblList){
            System.out.println(t);
        }*/
    }
}
