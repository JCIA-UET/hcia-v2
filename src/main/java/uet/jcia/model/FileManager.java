package uet.jcia.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import uet.jcia.entities.Table;
import uet.jcia.utils.Constants;

public class FileManager {
    
    private static DateFormat dateFormat =
            new SimpleDateFormat("yyyyMMdd-HHmmss");
    
    public static void saveTables(List<Table> tableList) {
        Date d = new Date();
        String filePath = Constants.TEMP_SOURCE_FOLDER 
                + "temp-data-" + dateFormat.format(d);
        
        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(tableList);
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public static List<Table> readTables(String filePath) {
        try {
            FileInputStream fis = new FileInputStream(filePath);
            ObjectInputStream ois = new ObjectInputStream(fis);
            
            List<Table> list = (List<Table>) ois.readObject();
            
            return list;
            
        } catch (IOException e) {
            e.printStackTrace();
            
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
}
