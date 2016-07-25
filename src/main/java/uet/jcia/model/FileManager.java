package uet.jcia.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.w3c.dom.Document;

import uet.jcia.entities.Table;
import uet.jcia.utils.Constants;
import uet.jcia.utils.Helper;

public class FileManager {
    
    public String saveTables(List<Table> tableList) {
        Date d = new Date();
        String filePath = Constants.TEMP_SOURCE_FOLDER + File.separator
                + "temp-data-" + Helper.DATE_FORMATER.format(d);
        
        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(tableList);
            
            return filePath;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String saveDocumentsHash(HashMap<String, Document> documents) {
        Date d = new Date();
        String filePath = Constants.TEMP_SOURCE_FOLDER + File.separator
                + "document-data-" + Helper.DATE_FORMATER.format(d);
        
        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(documents);
            
            return filePath;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public List<Table> readTables(String filePath) {
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

    public HashMap<String, Document> readDocumentsHash(String filePath) {
        try {
            FileInputStream fis = new FileInputStream(filePath);
            ObjectInputStream ois = new ObjectInputStream(fis);
            HashMap<String, Document> hash = (HashMap<String, Document>) ois.readObject();
            return hash;
            
        } catch (IOException e) {
            e.printStackTrace();
            
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public void findFiles(
            String srcFolder, String pattern, List<String> resultList) {
        File node = new File(srcFolder);
        if (!node.exists()) return;

        if (node.isFile() && node.getName().matches(pattern)) {
            String absPath = node.getAbsolutePath();
            System.out.println("[Scanner] found [" + absPath + "]");
            resultList.add(absPath);
            
        } else if (node.isDirectory()) {
            for (File child : node.listFiles()) {
                findFiles(child.getAbsolutePath(), pattern, resultList);
            }
        }
    }
    
}
