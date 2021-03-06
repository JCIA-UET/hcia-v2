package uet.jcia.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Helper {
    public static final DateFormat DATE_FORMATER =
            new SimpleDateFormat("yyyyMMdd-HHmmss");
    
    public static void copyFile(String srcPath, String dstPath) {
        try {
            File src = new File(srcPath);
            File dst = new File(dstPath);
            dst.getParentFile().mkdirs();
            
            Files.copy(src.toPath(), dst.toPath());
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static String prepareSessionFolder(String sessionid) throws Exception{
    	String sessionFolder = null;
    	
    	if (sessionid != null && !sessionid.equals("")) {
    		sessionFolder = Constants.TEMP_SOURCE_FOLDER + File.separator + sessionid;
        	File ctnerFile = new File(sessionFolder);
        	
        	if (!ctnerFile.isDirectory()) {
        		if (!ctnerFile.mkdirs()) {
        			throw new Exception("Error creating container folder");
        		}
        	}
        	
        	return sessionFolder;
        }
        else {
        	return Constants.TEMP_SOURCE_FOLDER;
        }
    }
    
    public static String findFile(String containerPath, String fileName) throws Exception {
    	File ctnerFile = new File(containerPath);
    	File[] listFile = ctnerFile.listFiles(new FilenameFilter() {
    		public boolean accept(File dir, String name) {
    			return name.startsWith(fileName);
    		}
    	});
    	
    	if (listFile.length > 1) {
    		throw new Exception("There are more than one folder with name: " + fileName);
    	}
    	else if (listFile.length == 0) {
    		return null;
    	}
    	else return listFile[0].getAbsolutePath();
    }
    
    public static String getFileName(String path) {
    	int index = path.lastIndexOf("\\");
    	return path.substring(index + 1);
    }
    
    public static Object deepClone(Object object) {
        try {
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          ObjectOutputStream oos = new ObjectOutputStream(baos);
          oos.writeObject(object);
          ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
          ObjectInputStream ois = new ObjectInputStream(bais);
          return ois.readObject();
        }
        catch (Exception e) {
          e.printStackTrace();
          return null;
        }
    }
    
    public static char[] file2CharArr(String path)
            throws IllegalArgumentException, IOException {
        
        File file = new File(path);
        if (!file.exists() || !file.isFile()) { 
            throw new IllegalArgumentException("Invalid input file");
        }
        
        BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), "UTF-8"));
        
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            content.append(line).append("\n");
        }
        br.close();
        return content.toString().toCharArray();
    }
}
